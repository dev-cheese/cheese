package com.cheese.demo.user.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {

    private Long id;

    public UserNotFoundException(Long id) {
        super(String.valueOf(ErrorCodeEnum.USER_NOT_FOIND));
        log.error(ErrorCodeEnum.USER_NOT_FOIND.getMessage(), id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
