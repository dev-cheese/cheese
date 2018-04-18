package com.cheese.demo.security.exception;

import com.cheese.demo.commons.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j

/**
 * MalformedJwtException 발생시 발생
 * JWT가 올바르게 건설되지 않았고 거절되어야한다는 예외.
 *
 */
public class JwtTokenMalformedException extends RuntimeException {

    public JwtTokenMalformedException(String message) {
        super(String.valueOf(ErrorCode.UNAUTHORIZED.getMessage()));
        log.error(message);
    }
}
