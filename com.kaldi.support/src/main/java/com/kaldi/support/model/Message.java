package com.kaldi.support.model;

import java.time.Instant;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message extends PanacheEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    public Chat chat;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false)
    public User sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String content;

    @Column(nullable = false, updatable = false)
    public Instant sentAt;

    @PrePersist
    public void prePersist() {
        if (sentAt == null) sentAt = Instant.now();
    }
}
