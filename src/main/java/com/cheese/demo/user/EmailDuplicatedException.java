package com.cheese.demo.user;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailDuplicatedException extends RuntimeException {

    private String email;

    public EmailDuplicatedException(String email) {
        log.error("user duplicated exception. {}", email);
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
