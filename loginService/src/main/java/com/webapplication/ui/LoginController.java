package com.webapplication.ui;

import com.webapplication.domain.UserService;
import com.webapplication.infra.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth/login")
    public ResponseEntity<Response> login(@RequestBody(required = false) LoginInformation loginInformation) {
        if (loginInformation == null ||
                loginInformation.getUsername() == null ||
                loginInformation.getPassword() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        User user = userService.login(loginInformation.getUsername(), loginInformation.getPassword());

        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse("The credentials are incorrect"), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(
                new LoginResponse(
                        new AccountResponse(
                                user.getId(),
                                user.getUsername(),
                                Objects.equals(user.getUsername(), UserService.adminUsername) ? "Admin" : "User"),
                        ""),
                HttpStatus.OK);
    }

    @PostMapping("/accounts/register")
    public ResponseEntity<Response> register(@RequestBody(required = false) LoginInformation loginInformation) {
        if (loginInformation == null ||
                loginInformation.getUsername() == null ||
                loginInformation.getPassword() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

            return new ResponseEntity<>(
                    new ErrorResponse("Invalid register information format"),
                    HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = userService.createUser(loginInformation.getUsername(), loginInformation.getPassword());
        if (user == null) {
            return new ResponseEntity<>(
                    new ErrorResponse("The username already exist"),
                    HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                new AccountResponse(user.getId(), user.getUsername(), "User"),
                HttpStatus.CREATED);
    }
}