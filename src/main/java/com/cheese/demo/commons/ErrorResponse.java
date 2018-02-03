package com.cheese.demo.commons;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorResponse {

    private String message;
    private ErrorCodeEnum code;
    private List<FieldError> errors;

    @Getter
    @Setter
    public static class FieldError {
        private String field;
        private String rejectedValue;
        private String defaultMessage;
    }
}
