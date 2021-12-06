package com.webapplication.ui;

public class ErrorResponse extends Response {
    private final String error;

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}