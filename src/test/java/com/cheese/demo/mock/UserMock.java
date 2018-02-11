package com.cheese.demo.mock;

import com.cheese.demo.member.MemberDto;

import java.sql.Date;

public class UserMock {

    public MemberDto.SignUpReq setSignUpDto(String email, String password, String rePassword) {
        MemberDto.SignUpReq signUpReqDto = new MemberDto.SignUpReq();
        signUpReqDto.setEmail(email);
        signUpReqDto.setPassword(password);
        signUpReqDto.setRePassword(rePassword);
        return signUpReqDto;
    }

    public MemberDto.MyAccountReq setMyAccountDto(String firstName, String lastName, String mobile, Date dob) {
        MemberDto.MyAccountReq dto = new MemberDto.MyAccountReq();

        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setMobile(mobile);
        dto.setDob(dob);
        return dto;
    }


}
