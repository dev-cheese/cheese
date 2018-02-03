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

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        UserDto.SignUp dto = createSignUpReq("cheese10yun@gmail.com", "rePassword", "rePassword");
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(dto.getEmail())));
    }

    @Test
    public void name() throws Exception {
        test_sign_up();

        UserDto.SignUp dto = createSignUpReq("cheese10yun@gmail.com", "rePassword", "rePassword");

        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest());

//                .andExpect(jsonPath("$.email", is(dto.getEmail())));


    }

    //    @Test
//    public void test_update() throws Exception {
//        UserDto.SignUp signUpDto = createSignUpReq(email, "test", "rePassword");
//        User user = userService.create(signUpDto);
//
//        UserDto.MyAccount updateDto = new UserDto.MyAccount();
//        updateDto.setDob(Date.valueOf(LocalDate.now()));
//        updateDto.setFirstName("firs");
//        updateDto.setLastName("last");
//        updateDto.setMobile("010-7133-3262");
//
//        ResultActions result = mockMvc.perform(put("/users/" + user.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updateDto)));
//
//        result.andDo(print());
//
//    }

    private UserDto.SignUp createSignUpReq(String email, String password, String rePassword) {
        UserDto.SignUp signUpDto = new UserDto.SignUp();
        signUpDto.setEmail(email);
        signUpDto.setPassword(password);
        signUpDto.setRePassword(rePassword);
        return signUpDto;
    }

    private ResultActions requestSignUp(UserDto.SignUp signUpDto) throws Exception {
        return mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDto)));
    }
}