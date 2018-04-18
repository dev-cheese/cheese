package com.cheese.demo.member.exception;

import com.cheese.demo.commons.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailDuplicationException extends RuntimeException {

    public EmailDuplicationException(String email) {
        super(String.valueOf(ErrorCode.EMAIL_DUPLICATION));
        log.error(ErrorCode.EMAIL_DUPLICATION.getMessage(), email);
    }
}
