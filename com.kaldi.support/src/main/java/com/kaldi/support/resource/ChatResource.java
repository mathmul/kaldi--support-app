package com.kaldi.support.resource;

import com.kaldi.support.model.*;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Path("/chat")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChatResource {

    @Inject
    SecurityIdentity identity;

    // DTOs
    public record ChatRoomDto(String name) {}
    public record NewChatRequest(ChatRoom room, String message) {}
    public record ChatSummary(Long id, ChatRoom room, ChatStatus status, Instant claimedAt, Instant closedAt) {}
    public record MessageRequest(String content) {}
    public record MessageDto(String sender, String content, Instant sentAt) {}

    @GET
    @Path("/rooms")
    public List<ChatRoomDto> listRooms() {
        return Arrays.stream(ChatRoom.values()).map(r -> new ChatRoomDto(r.name())).toList();
    }

    @POST
    @Transactional
    @RolesAllowed("USER")
    public Response startChat(NewChatRequest req) {
        User user = User.find("username", identity.getPrincipal().getName()).firstResult();
        if (user == null) return Response.status(401).build();

        Chat chat = new Chat();
        chat.user = user;
        chat.type = req.room();
        chat.status = ChatStatus.WAITING;
        chat.persist();

        Message m = new Message();
        m.chat = chat;
        m.sender = user;
        m.content = req.message();
        m.sentAt = Instant.now();
        m.persist();

        return Response.status(201).entity(chat.id).build();
    }

    @GET
    @Path("/{id}")
    public Response getMessages(@PathParam("id") Long chatId) {
        Chat chat = Chat.findById(chatId);
        if (chat == null) return Response.status(404).build();

        boolean isUser = identity.hasRole("USER") && chat.user.username.equals(identity.getPrincipal().getName());
        boolean isOperator = identity.hasRole("OPERATOR") && (chat.operator == null || chat.operator.username.equals(identity.getPrincipal().getName()));

        if (!isUser && !isOperator) return Response.status(403).build();

        List<MessageDto> result = chat.messages.stream()
            .map(m -> new MessageDto(m.sender.username, m.content, m.sentAt))
            .collect(Collectors.toList());

        return Response.ok(result).build();
    }

    @POST
    @Path("/{id}")
    @Transactional
    public Response sendMessage(@PathParam("id") Long chatId, MessageRequest req) {
        Chat chat = Chat.findById(chatId);
        if (chat == null || chat.status == ChatStatus.CLOSED) return Response.status(400).build();

        String username = identity.getPrincipal().getName();
        boolean isUser = identity.hasRole("USER") && chat.user.username.equals(username);
        boolean isOperator = identity.hasRole("OPERATOR") && chat.operator != null && chat.operator.username.equals(username);

        if (!isUser && !isOperator) return Response.status(403).build();

        User sender = User.find("username", username).firstResult();
        Message msg = new Message();
        msg.chat = chat;
        msg.sender = sender;
        msg.content = req.content();
        msg.sentAt = Instant.now();
        msg.persist();

        return Response.status(201).build();
    }

    @GET
    @RolesAllowed("OPERATOR")
    public List<ChatSummary> listChats(@QueryParam("status") ChatStatus status) {
        String username = identity.getPrincipal().getName();

        if (status == ChatStatus.WAITING) {
            return Chat.<Chat>list("status", ChatStatus.WAITING).stream()
                .map(c -> new ChatSummary(c.id, c.type, c.status, c.claimedAt, c.closedAt))
                .toList();
        } else {
            return Chat.<Chat>list("operator.username = ?1 and status = ?2", username, status).stream()
                .map(c -> new ChatSummary(c.id, c.type, c.status, c.claimedAt, c.closedAt))
                .toList();
        }
    }

    @POST
    @Path("/{id}/claim")
    @RolesAllowed("OPERATOR")
    @Transactional
    public Response claimChat(@PathParam("id") Long chatId) {
        Chat chat = Chat.findById(chatId);
        if (chat == null || chat.status != ChatStatus.WAITING) return Response.status(400).build();

        User op = User.find("username", identity.getPrincipal().getName()).firstResult();
        chat.operator = op;
        chat.claimedAt = Instant.now();
        chat.status = ChatStatus.CLAIMED;
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/close")
    @RolesAllowed("OPERATOR")
    @Transactional
    public Response closeChat(@PathParam("id") Long chatId) {
        Chat chat = Chat.findById(chatId);
        String username = identity.getPrincipal().getName();

        if (chat == null || chat.operator == null || !chat.operator.username.equals(username)) {
            return Response.status(403).build();
        }

        chat.closedAt = Instant.now();
        chat.status = ChatStatus.CLOSED;
        return Response.ok().build();
    }
}
