package com.webapplication.domain;

import com.webapplication.infra.User;
import com.webapplication.infra.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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