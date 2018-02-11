package com.cheese.demo.user;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.mock.UserMock;
import com.cheese.demo.user.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    private final String email = "cheese10yun@gmail.com";
    private final String password = "password001";
    private final String rePassword = "password001";
    private UserMock userMock;

    @Before
    public void setUp() {
        userMock = new UserMock();
    }

    @Test
    public void When_emailIsExisted_expect_userReturn() {
        userService.create(userMock.setSignUpDto(email, password, rePassword));
        User user = userService.findByEmail(email);
        assertThat(user, is(notNullValue()));
        assertThat(user.getEmail(), is(email));
    }

    @Test(expected = UserNotFoundException.class)
    public void When_emailIsNotExisted_expect_UserNotFoundException() {
        userService.findByEmail("notExistedEmail@test.com");
    }
}