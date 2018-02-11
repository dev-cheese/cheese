package com.cheese.demo.member;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.member.exception.UserNotFoundException;
import com.cheese.demo.mock.UserMock;
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
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

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
        memberService.create(userMock.setSignUpDto(email, password, rePassword));
        Member member = memberService.findByEmail(email);
        assertThat(member, is(notNullValue()));
        assertThat(member.getEmail(), is(email));
    }

    @Test(expected = UserNotFoundException.class)
    public void When_emailIsNotExisted_expect_UserNotFoundException() {
        memberService.findByEmail("notExistedEmail@test.com");
    }
}