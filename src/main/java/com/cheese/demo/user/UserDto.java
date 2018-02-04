package com.cheese.demo.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.sql.Date;

public class UserDto {

    @Getter
    @Setter
    static class SignUp {
        @Email
        @NotEmpty
        private String email;
        @Size(min = 8)
        @NotEmpty
        private String password;
        @Size(min = 8)
        @NotEmpty
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
}
