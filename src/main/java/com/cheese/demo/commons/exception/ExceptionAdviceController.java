package com.cheese.demo.commons.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.commons.ErrorResponse;
import com.cheese.demo.user.exception.EmailDuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(value = {
            EmailDuplicatedException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ErrorResponse handleConflict(RuntimeException ex) {
        ErrorResponse er = new ErrorResponse();
        ErrorCodeEnum code = getErrorCodeEnum(ex);
        er.setMessage(code.message());
        er.setCode(code);
        return er;
    }

    private ErrorCodeEnum getErrorCodeEnum(RuntimeException ex) {
        return ErrorCodeEnum.valueOf(ex.getMessage());
    }
}
