package com.webapplication.ui;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponse extends Response {
    private final AccountResponse account;
    private final String token;
}