package com.cheese.demo.user.dto;

import com.cheese.demo.user.domain.User;
import lombok.Builder;

public class UserUpdateReqDto {

    private String lastName;
    private String firstName;
    private String mobile;

    @Builder
    public UserUpdateReqDto(String lastName, String firstName, String mobile) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.mobile = mobile;
    }

    public User toEntity() {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .mobile(mobile)
                .build();
    }
}
