package com.cheese.demo.commons;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ErrorResponse {

    private String message;
    private String code;
    private int status;
    private List<FieldError> errors;

    @Builder
    public ErrorResponse(String message, String code, int status, List<FieldError> errors) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.errors = errors;
    }

    @Getter
    @Setter
    public static class FieldError {
        private String propertyPath;
        private String inValidValue;
        private String message;
    }
}
