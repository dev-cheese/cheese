package com.cheese.demo.member.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(Long id) {
        super(String.valueOf(ErrorCodeEnum.USER_NOT_FOUND));
        log.error(ErrorCodeEnum.USER_NOT_FOUND.getMessage(), id);
    }

    public MemberNotFoundException(String email) {
        super(String.valueOf(ErrorCodeEnum.USER_NOT_FOUND));
        log.error(ErrorCodeEnum.USER_NOT_FOUND.getMessage(), email);
    }

}

