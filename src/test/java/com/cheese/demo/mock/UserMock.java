package com.cheese.demo.mock;

import com.cheese.demo.user.UserDto;

import java.sql.Date;

public class UserMock {

    public UserDto.SignUpReq setSignUpDto(String email, String password, String rePassword) {
        UserDto.SignUpReq signUpReqDto = new UserDto.SignUpReq();
        signUpReqDto.setEmail(email);
        signUpReqDto.setPassword(password);
        signUpReqDto.setRePassword(rePassword);
        return signUpReqDto;
    }

    public UserDto.MyAccountReq setMyAccountDto(String firstName, String lastName, String mobile, Date dob) {
        UserDto.MyAccountReq dto = new UserDto.MyAccountReq();

        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setMobile(mobile);
        dto.setDob(dob);
        return dto;
    }


}
