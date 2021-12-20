package com.webapplication.ui.DTO;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorsResponse extends Response {
    private final List<Error> errors;

    @Data
    public static class Error {
        private final String property;
        private final String message;
    }
}