package com.webapplication.ui;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountResponse extends Response {
    private final long id;
    private final String username;
    private final String role;
}