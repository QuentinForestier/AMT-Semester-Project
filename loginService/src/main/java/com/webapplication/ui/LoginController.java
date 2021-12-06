package com.webapplication.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @PostMapping("/auth/login")
    public ResponseEntity<Response> login(@RequestBody(required = false) LoginInformation loginInformation) {
        if (loginInformation == null || loginInformation.getUsername() == null || loginInformation.getPassword() == null) {
            return new ResponseEntity<>(new ErrorResponse("Invalid login information format"), HttpStatus.FORBIDDEN);
        }
        System.out.println("Try to log with : " + loginInformation.getUsername() + " - " + loginInformation.getPassword());
        return new ResponseEntity<>(new LoginResponse(new AccountResponse(0, "Name", "User"), ""), HttpStatus.OK);
    }

    @PostMapping("/accounts/register")
    public ResponseEntity<Response> register(@RequestBody(required = false) LoginInformation loginInformation) {
        if (loginInformation == null || loginInformation.getUsername() == null || loginInformation.getPassword() == null) {
            // TODO : Quel type utiliser
            return new ResponseEntity<>(new ErrorResponse("Invalid register information format"), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        System.out.println("Try to register with : " + loginInformation.getUsername() + " - " + loginInformation.getPassword());
        return new ResponseEntity<>(new AccountResponse(0, "Name", "User"), HttpStatus.CREATED);
    }
}