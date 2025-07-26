package com.kaldi.support;

import com.kaldi.support.model.User;
import com.kaldi.support.model.UserRole;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;


@Startup
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

            User o = new User();
            o.setRole(UserRole.OPERATOR);
            o.username = "admin";
            o.password = BcryptUtil.bcryptHash("admin123");
            o.persist();
        }
    }
}
