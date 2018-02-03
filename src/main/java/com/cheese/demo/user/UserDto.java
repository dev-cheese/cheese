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

    // TODO: 2018. 2. 2. 기본 리폰슨스 만들어서 리턴해줄것 -yun
//    @Getter
//    @Setter
//    static class SignUpRes {
//        private String email;
//        private String mobile;
//    }

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
    static class Update {
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;
    }
}
