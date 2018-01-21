package com.cheese.demo.user.dto;

import com.cheese.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class UserSignUpReqDto {

    private String email;
    private String password;
    private String lastName;
    private String firstName;
    private String mobile;
    private LocalDateTime dob;

    @Builder
    public UserSignUpReqDto(String email, String password, String lastName, String firstName, String mobile, LocalDateTime dob) {
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.mobile = mobile;
        this.dob = dob;
    }

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .lastName(lastName)
                .firstName(firstName)
                .mobile(mobile)
                .dob(dob)
                .build();
    }
}
