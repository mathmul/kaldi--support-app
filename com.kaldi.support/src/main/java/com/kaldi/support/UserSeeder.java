package com.kaldi.support;

import com.kaldi.support.model.User;
import com.kaldi.support.model.UserRole;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@Startup
@IfBuildProfile("dev")
@ApplicationScoped
public class UserSeeder {

    @PostConstruct
    void init() {
        seed();
    }

    @Transactional
    void seed() {
        System.out.println("Seeding users...");
        if (User.count() == 0) {
            User u = new User();
            u.setRole(UserRole.USER);
            u.username = "alice";
            u.password = BcryptUtil.bcryptHash("secret");
            u.persist();
            System.out.println("Added user 'alice' with password 'secret' and role 'USER' to database.");

            User o = new User();
            o.setRole(UserRole.OPERATOR);
            o.username = "admin";
            o.password = BcryptUtil.bcryptHash("admin123");
            o.persist();
            System.out.println("Added user 'admin' with password 'admin123' and role 'OPERATOR' to database.");
        } else {
            System.out.println("Users already exist.");
        }
    }
}
