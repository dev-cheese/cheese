package com.cheese.demo.member;

import com.cheese.demo.commons.model.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.sql.Date;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    public static class SignUpReq {
        private String email;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        private String password;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        private String rePassword;
        private Address address;

        @Builder
        public SignUpReq(String email, String password, String rePassword, Address address) {
            this.email = email;
            this.password = password;
            this.rePassword = rePassword;
            this.address = address;
        }

        public Member toEntity(String encodePassword, MemberRoleEnum role) {
            return Member.builder()
                    .email(email)
                    .password(encodePassword)
                    .role(role)
                    .address(address)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MyAccountReq {
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;

        @Builder
        public MyAccountReq(String lastName, String firstName, String mobile, Date dob) {
            this.lastName = lastName;
            this.firstName = firstName;
            this.mobile = mobile;
            this.dob = dob;
        }

    }

    @Getter
    public static class Res {
        private Long id;
        private String email;
        private String lastName;
        private String firstName;
        private String mobile;
        private Date dob;
        private Address address;

        public Res(Member entity) {
            this.id = entity.getId();
            this.email = entity.getEmail();
            this.lastName = entity.getLastName();
            this.firstName = entity.getFirstName();
            this.mobile = entity.getMobile();
            this.dob = entity.getDob();
            this.address = entity.getAddress();
        }

    }

}
