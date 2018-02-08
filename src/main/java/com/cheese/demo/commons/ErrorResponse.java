package com.cheese.demo.commons;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {

    private String message;
    private String code;
    private int status;
    private List<FieldError> errors;

    @Data
    public static class FieldError {
        private String propertyPath;
        private String inValidValue;
        private String message;
    }
}
