package com.cheese.demo.mock;

import com.cheese.demo.member.MemberDto;

import java.sql.Date;

public class MemberMock {

    public MemberDto.SignUpReq setSignUpDto(String email, String password, String rePassword) {
        return MemberDto.SignUpReq.builder()
                .email(email)
                .password(password)
                .rePassword(rePassword)
                .build();
    }

    public MemberDto.MyAccountReq setMyAccountDto(String firstName, String lastName, String mobile, Date dob) {
        return MemberDto.MyAccountReq.builder()
                .firstName(firstName)
                .lastName(lastName)
                .mobile(mobile)
                .dob(dob)
                .build();
    }
}
