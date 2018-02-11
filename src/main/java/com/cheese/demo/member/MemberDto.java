package com.cheese.demo.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.sql.Date;

public class MemberDto {

    @Getter
    @Setter
    public static class SignUpReq {
        private String email;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        private String password;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        private String rePassword;
    }

    @Getter
    @Setter
    public static class MyAccountReq {
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;
    }

    @Getter
    @Setter
    public static class Res {
        private Long id;
        private String email;
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;
    }

}
