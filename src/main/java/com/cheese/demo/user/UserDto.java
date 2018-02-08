package com.cheese.demo.user;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

public class UserDto {

    @Getter
    @Setter
    static class SignUp {
        private String email;
        private String password;
        private String rePassword;
    }

    @Getter
    @Setter
    static class MyAccount {
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;
    }

    @Getter
    @Setter
    static class Res {
        private Long id;
        private String email;
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;
    }

    @Getter
    @Setter
    static class Existence {
        private boolean existence;
    }
}
