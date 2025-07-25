package com.kaldi.support.resource;

import java.util.List;

import com.kaldi.support.model.User;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;


@Path("/users")
public class UserTestResource {

    @GET
    public List<User> list() {
        return User.listAll();
    }
}