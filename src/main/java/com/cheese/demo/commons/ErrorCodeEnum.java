package com.cheese.demo.commons;

public enum ErrorCodeEnum {


    U001("중복된 이메일입니다.");

    private final String message;

    ErrorCodeEnum(String message) {
        this.message = message;
    }

    public String message() {
        return this.message;
    }
}
