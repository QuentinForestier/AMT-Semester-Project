package com.webapplication.ui;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/auth/login")
    public String login() {
        return "test";
    }

    @GetMapping("/accounts/register")
    public String register() {
        return "register";
    }
}