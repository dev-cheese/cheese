package com.cheese.demo.user;

import com.cheese.demo.SpringServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;


    @Test
    public void create() {
    }

    @Test
    public void update() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findById() {
    }

    @Test
    public void name() {


//        userService.
    }
}