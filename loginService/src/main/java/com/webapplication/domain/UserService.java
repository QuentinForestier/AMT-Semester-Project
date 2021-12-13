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

    public static boolean checkBlankChar(String message) {
        if (message.length() == 0)
            return false;

        System.out.println(message);
        for (int i = 0; i < message.length(); i++) {
            System.out.println(message.charAt(i));
            if (Character.isSpaceChar(message.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPasswordValid(String password) {
        if (password.length() < 8)
            return false;

        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#&()–\\[{}\\]_:;',?\\/*~$^+=<>]).*$");
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