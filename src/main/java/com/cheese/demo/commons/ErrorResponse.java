package com.cheese.demo.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private String code;
    private int status;
    private List<FieldError> errors;

//    @Getter
//    @Setter
//    public static class FieldError {
//        private String field;
//        private String rejectedValue;
//        private String defaultMessage;
//    }
}
