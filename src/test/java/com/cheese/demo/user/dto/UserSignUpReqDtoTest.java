package com.cheese.demo.user.dto;

import com.cheese.demo.user.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserSignUpReqDtoTest {


    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UserSignUpReqDto userSignUpReqDto;


    @Test
    public void name() {


    }
}