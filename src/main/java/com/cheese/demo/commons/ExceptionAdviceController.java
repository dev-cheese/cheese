package com.cheese.demo.commons;

import com.cheese.demo.user.exception.EmailDuplicationException;
import com.cheese.demo.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(value = {
            EmailDuplicationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorResponse handleBadRequestException(RuntimeException ex) {
        ErrorCodeEnum code = getErrorCodeEnum(ex.getMessage());
        return getErrorResponse(HttpStatus.BAD_REQUEST.value(), code.getCode(), code.getMessage(), null);
    }

    @ExceptionHandler(value = {
            UserNotFoundException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponse handleFoundException(RuntimeException ex) {
        ErrorCodeEnum code = getErrorCodeEnum(ex.getMessage());
        return getErrorResponse(HttpStatus.NOT_FOUND.value(), code.getCode(), code.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorCodeEnum code = getErrorCodeEnum("INVALID_INPUTS");
        return getErrorResponse(HttpStatus.BAD_REQUEST.value(), code.getCode(), code.getMessage(), fieldErrors);
    }

    private ErrorResponse getErrorResponse(int status, String code, String message, List<FieldError> errors) {
        return new ErrorResponse(message, code, status, errors);
    }

    private ErrorCodeEnum getErrorCodeEnum(String code) {
        return ErrorCodeEnum.valueOf(code);
    }
}
