package com.webapplication.ui;

import lombok.Data;

import java.util.List;

@Data
public class ErrorsResponse extends Response {
    private final List<Error> errors;

    @Data
    public static class Error {
        private final String property;
        private final String message;
    }
}