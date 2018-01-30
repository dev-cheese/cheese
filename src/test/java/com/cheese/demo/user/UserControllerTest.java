package com.cheese.demo.user;

import com.cheese.demo.SpringServerApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Date;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
@WebAppConfiguration
@Transactional
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;
    private String email;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();

        email = "test@naver.com";
    }

    @After
    public void tearDown() {

    }

    // TODO: 2018. 1. 31. 이메일 중복 validation -yun
    // TODO: 2018. 1. 31. 이메일 형식 validation -yun
    // TODO: 2018. 1. 31. 이메일 길이 validation -yun
    // TODO: 2018. 1. 31. 비밀번호 길이 validation -yun

    @Test
    public void test_sign_up() throws Exception {
        UserDto.SignUpReq signUpReqDto = createSignUpReq();

        ResultActions result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpReqDto)));

        result.andDo(print());
        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.email", is(email)));
    }

    private UserDto.SignUpReq createSignUpReq() {
        UserDto.SignUpReq signUpReqDto = new UserDto.SignUpReq();
        signUpReqDto.setEmail(email);
        signUpReqDto.setPassword("test");
        return signUpReqDto;
    }

    @Test
    public void test_update() throws Exception {
        UserDto.SignUpReq signUpReqDto = createSignUpReq();
        User user = userService.create(signUpReqDto);

        UserDto.UpdateReq updateDto = new UserDto.UpdateReq();
        updateDto.setDob(Date.valueOf(LocalDate.now()));
        updateDto.setFirstName("firs");
        updateDto.setLastName("last");
        updateDto.setMobile("010-7133-3262");

        ResultActions result = mockMvc.perform(put("/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)));

        result.andDo(print());

    }
}