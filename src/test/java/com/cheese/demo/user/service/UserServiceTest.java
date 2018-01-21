package com.cheese.demo.user.service;

import com.cheese.demo.user.domain.User;
import com.cheese.demo.user.domain.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void setUp() {
//        user = buildUser();
    }

    @After
    public void cleanUp() {
//        userRepository.deleteAll();
    }

    @Test
    public void test_create() {
//        userService.signUp(user);


    }

    private User buildUser() {
        return User.builder()
                .mobile("111")
                .build();
    }
}