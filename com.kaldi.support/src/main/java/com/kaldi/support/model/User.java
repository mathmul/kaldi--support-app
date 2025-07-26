package com.kaldi.support.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.Username;
import io.quarkus.security.jpa.UserDefinition;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@UserDefinition
public class User extends PanacheEntity {

    @Username
    @Column(nullable = false, unique = true)
    public String username;

    @Password
    @Column(nullable = false)
    public String password;

    @Roles
    @Column(name = "role", nullable = false)
    public String roleString;

    @Transient
    public UserRole getRole() {
        return UserRole.valueOf(roleString);
    }

    public void setRole(UserRole role) {
        this.roleString = role.name();
    }
}
