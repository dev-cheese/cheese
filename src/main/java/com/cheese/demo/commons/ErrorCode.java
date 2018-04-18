package com.cheese.demo.commons;

import lombok.Getter;

@Getter
public enum ErrorCode {

    EMAIL_DUPLICATION("U001", "중복된 이메일입니다.", 400),
    MEMBER_NOT_FOUND("U002", "해당 회원을 찾을 수 없습니다.", 404),
    DISCOUNT_NOT_FOUND("XXX", "해당 할인 정보를 찾을 수 없습니다.", 404),
    COUPON_NOT_FOUND("XXX", "해당 할인 정보를 찾을 수 없습니다.", 404),
    INVALID_INPUTS("XXX", "유효하지 않은 값입니다. 입력값을 확인하세요", 400),
    INVALID_DOMAIN("D001", "유효하지 않은 값입니다. 입력값을 확인하세요", 400),
    UNAUTHORIZED("XXX", "Unauthorized", 401),
    EXPIRATION_TOKEN("XXX", "토큰이 만료되었습니다.", 401);


    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}