package com.webapplication.ui;

import com.webapplication.domain.User;
import com.webapplication.domain.UserService;
import com.webapplication.ui.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
                                user.getRole().toString()),
                        userService.CreateJWT(user.getRole().toString())),
                HttpStatus.OK);
    }

    @PostMapping("/accounts/register")
    public ResponseEntity<Response> register(@RequestBody(required = false) LoginInformation loginInformation) {
        if (loginInformation == null ||
                loginInformation.getUsername() == null ||
                loginInformation.getPassword() == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        if (UserService.detectBlankInString(loginInformation.getUsername())) {
            return new ResponseEntity<>(
                    new ErrorsResponse(
                            List.of(
                                    new ErrorsResponse.Error(
                                            "username",
                                            "The username cannot contain blank characters"
                                    )
                            )
                    ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (UserService.detectBlankInString(loginInformation.getPassword())) {
            return new ResponseEntity<>(
                    new ErrorsResponse(
                            List.of(
                                    new ErrorsResponse.Error(
                                            "password",
                                            "The password cannot contain blank characters"
                                    )
                            )
                    ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!UserService.isPasswordValid(loginInformation.getPassword())) {
            return new ResponseEntity<>(
                    new ErrorsResponse(
                            List.of(
                                    new ErrorsResponse.Error(
                                            "password",
                                            "The password does not match the security politics, it should be at least 8 char long, should contain at least one uppercase char, one lowercase char, one digit and one special character"
                                    )
                            )
                    ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        User user = userService.createUser(loginInformation.getUsername(), loginInformation.getPassword());
        if (user == null) {
            return new ResponseEntity<>(
                    new ErrorResponse("The username already exist"),
                    HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(
                new AccountResponse(user.getId(), user.getUsername(), user.getRole().toString()),
                HttpStatus.CREATED);
    }
}