package com.cheese.demo.commons.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.commons.ErrorResponse;
import com.cheese.demo.member.exception.EmailDuplicationException;
import com.cheese.demo.member.exception.MemberNotFoundException;
//import com.cheese.demo.security.exception.JwtTokenMalformedException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionAdviceController {

    @Autowired
    private ModelMapper modelMapper;

    @ExceptionHandler(value = {
            EmailDuplicationException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorResponse handleBadRequestException(RuntimeException ex) {
        ErrorCodeEnum code = getErrorCodeEnum(ex.getMessage());
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), code.getCode(), code.getMessage(), null);
    }

    @ExceptionHandler(value = {
            MemberNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    protected ErrorResponse handleNotFoundException(RuntimeException ex) {
        ErrorCodeEnum code = getErrorCodeEnum(ex.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND.value(), code.getCode(), code.getMessage(), null);
    }

//    @ExceptionHandler(value = {
//            JwtTokenMalformedException.class
//    })
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    protected ErrorResponse handleTest(JwtTokenMalformedException ex) {
//        ErrorCodeEnum code = getErrorCodeEnum(ex.getMessage());
//        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), code.getCode(), code.getMessage(), null);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleValidationError(MethodArgumentNotValidException ex) {
        ErrorCodeEnum code = getErrorCodeEnum("INVALID_INPUTS");
        return createErrorResponse(HttpStatus.BAD_REQUEST.value(), code.getCode(), code.getMessage(), null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        final int status = HttpStatus.BAD_REQUEST.value();
        final String code = ErrorCodeEnum.INVALID_DOMAIN.getCode();
        final String message = ErrorCodeEnum.INVALID_DOMAIN.getMessage();
        final List<ErrorResponse.FieldError> collect = ex.getConstraintViolations()
                .parallelStream()
                .map(error -> modelMapper.map(error, ErrorResponse.FieldError.class))
                .collect(Collectors.toList());

        return createErrorResponse(status, code, message, collect);
    }


    private ErrorResponse createErrorResponse(int status, String code, String message, List<ErrorResponse.FieldError> errors) {
        return ErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .errors(errors)
                .build();
    }

    private ErrorCodeEnum getErrorCodeEnum(String code) {
        return ErrorCodeEnum.valueOf(code);
    }
}
