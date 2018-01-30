package com.cheese.demo.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.sql.Date;

public class UserDto {

    @Getter
    @Setter
    static class SignUpReq {
        @NotEmpty
        private String email;
        @Size(min = 5)
        @NotEmpty
        private String password;
    }

    @Getter
    @Setter
    static class SignUpRes {
        private String email;
        private String mobile;
    }

    @Getter
    @Setter
    static class UpdateReq {
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;
    }

    @Getter
    @Setter
    static class UpdateRes {
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;

    }
}
