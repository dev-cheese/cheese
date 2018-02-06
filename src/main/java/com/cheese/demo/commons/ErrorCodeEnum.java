package com.cheese.demo.commons;

public enum ErrorCodeEnum {


    EMAIL_DUPLICATION("U001", "중복된 이메일입니다."),
    USER_NOT_FOUND("U002", "해당 회원을 찾을 수 없습니다."),
    INVALID_INPUTS("XXX", "유효하지 않은 값입니다. 입력값을 확인하세요");

    private final String code;
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}