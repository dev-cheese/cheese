package com.cheese.demo.commons;

public enum ErrorCodeEnum {


    U001("회원 가입 유효성 검사 실패");

    private final String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
