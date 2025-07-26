package com.kaldi.support.model;

import java.time.Instant;
import java.util.List;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "chat")
public class Chat extends PanacheEntity {

    @ManyToOne(optional = false)
    public User user;

    @ManyToOne(optional = true)
    public User operator; // null if unclaimed

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("sentAt ASC")
    public List<Message> messages;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public ChatStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public ChatRoom type;

    @Column
    public Instant claimedAt;

    @Column
    public Instant closedAt;
}
