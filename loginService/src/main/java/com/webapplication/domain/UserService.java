package com.webapplication.domain;

import com.webapplication.infra.User;
import com.webapplication.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Service
public class UserService {
    public static final String adminUsername = "crossport";
    private static final String adminPassword = "nXcuifw5y$zcsh8sjK@b";

    @Autowired
    private UserRepository userRepository;

    // Creation de l'utilisateur admin
    @PostConstruct
    public void init() {
        User u = userRepository.findFirstByUsername(adminUsername);
        if (u == null) {
            createUser(adminUsername, adminPassword);
        }
    }

    public User createUser(String userName, String password) {
        User u = new User(0L, userName, password);
        try {
            u = userRepository.save(u);
            return u;
        } catch (org.springframework.dao.DataIntegrityViolationException ex) {
            return null;
        }
    }

    public User login(String userName, String password) {
        User u = userRepository.findFirstByUsername(userName);

        if (u == null) {
            return null;
        }

        if (!Objects.equals(u.getPassword(), password)) {
            return null;
        }

        return u;
    }
}