package com.webapplication.ui;

public class LoginResponse extends Response {
    private final AccountResponse account;
    private final String token;

    public LoginResponse(AccountResponse account, String token) {
        this.account = account;
        this.token = token;
    }

    public AccountResponse getAccount() {
        return account;
    }

    public String getToken() {
        return token;
    }
}