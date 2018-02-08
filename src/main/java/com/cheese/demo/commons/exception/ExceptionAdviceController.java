package com.cheese.demo.commons.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.commons.ErrorResponse;
import com.cheese.demo.user.exception.EmailDuplicationException;
import com.cheese.demo.user.exception.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionAdviceController {

    @Autowired
    private ModelMapper modelMapper;

    @ExceptionHandler(value = {
            EmailDuplicationException.class,
            UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorResponse handleBadRequestException(RuntimeException ex) {
        ErrorCodeEnum code = getErrorCodeEnum(ex.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), code.getCode(), code.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationError(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        ErrorCodeEnum code = getErrorCodeEnum("INVALID_INPUTS");
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), code.getCode(), code.getMessage(), null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        final int value = HttpStatus.BAD_REQUEST.value();
        final String code = ErrorCodeEnum.INVALID_DOMAIN.getCode();
        final String message = ErrorCodeEnum.INVALID_DOMAIN.getMessage();
        final List<ErrorResponse.FieldError> collect = ex.getConstraintViolations()
                .parallelStream()
                .map(error -> modelMapper.map(error, ErrorResponse.FieldError.class))
                .collect(Collectors.toList());

        return createErrorResponse(value, code, message, collect);
    }


    private ErrorResponse createErrorResponse(int status, String code, String message, List<ErrorResponse.FieldError> errors) {

        ErrorResponse response = new ErrorResponse();
        response.setStatus(status);
        response.setCode(code);
        response.setMessage(message);
        response.setErrors(errors);

        return response;
    }

    private ErrorCodeEnum getErrorCodeEnum(String code) {
        return ErrorCodeEnum.valueOf(code);
    }
}
