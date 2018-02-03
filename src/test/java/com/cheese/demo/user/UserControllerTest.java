package com.cheese.demo.user;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.commons.ErrorCodeEnum;
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
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void test_sign_up() throws Exception {
        UserDto.SignUp dto = createSignUpReq("cheese10yun@gmail.com", "rePassword", "rePassword");
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(dto.getEmail())));
    }

    @Test
    public void sign_up_email_duplicate_assert_that_bad_request_and_code_001() throws Exception {
        test_sign_up();

        UserDto.SignUp dto = createSignUpReq("cheese10yun@gmail.com", "rePassword", "rePassword");
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.EMAIL_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.EMAIL_DUPLICATION.getCode())));
    }

    @Test
    public void sign_up_not_validate_assert_that_bad_request_and_code_XXX() throws Exception {
        UserDto.SignUp email_type_validation = createSignUpReq("not_email_validate", "rePassword", "rePassword");
        requestSinUpNotValidate(email_type_validation);

        UserDto.SignUp password_length_validation = createSignUpReq("test@test.com", "123456", "123456");
        requestSinUpNotValidate(password_length_validation);
    }

    private void requestSinUpNotValidate(UserDto.SignUp dto) throws Exception {
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.INVALID_INPUTS.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.INVALID_INPUTS.getCode())));
    }

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