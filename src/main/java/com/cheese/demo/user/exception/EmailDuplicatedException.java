package com.cheese.demo.user.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailDuplicatedException extends RuntimeException {

    private String email;

    public EmailDuplicatedException(String email) {
        super(String.valueOf(ErrorCodeEnum.U001));
        log.error(ErrorCodeEnum.U001.message(), email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
