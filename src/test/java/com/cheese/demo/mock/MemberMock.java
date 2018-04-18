package com.cheese.demo.mock;

import com.cheese.demo.commons.model.Address;
import com.cheese.demo.member.MemberDto;

import java.sql.Date;

public class MemberMock {

    public MemberDto.SignUpReq setSignUpDto(String email, String password, String rePassword) {
        return MemberDto.SignUpReq.builder()
                .email(email)
                .password(password)
                .rePassword(rePassword)
                .address(buildAdress())
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

    private Address buildAdress() {
        return Address.builder()
                .address1("서울특별시")
                .address2("신림동")
                .zipCode("645-223")
                .build();
    }
}
