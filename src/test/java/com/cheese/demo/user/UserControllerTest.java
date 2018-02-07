package com.cheese.demo.user;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.commons.ErrorCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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


    private final String email = "cheese10yun@gmail.com";
    private final String password = "rePassword";
    private final String rePassword = "rePassword";
    private final String firstName = "길동";
    private final String lastName = "홍";
    private final String mobile = "01071333262";
    private final Date dob = Date.valueOf(LocalDate.now());

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
//    public void When_signUp_expect_succeed() throws Exception {
    public void When_signUp_expect_succeed() throws Exception {
        UserDto.SignUp dto = setSignUpDto(email, password, rePassword);
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(dto.getEmail())));
    }

    @Test
    public void When_duplicateEmail_expect_EMAIL_DUPLICATION() throws Exception {
        When_signUp_expect_succeed();

        UserDto.SignUp dto = setSignUpDto(email, password, rePassword);
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.EMAIL_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.EMAIL_DUPLICATION.getCode())));
    }

    @Test
    public void When_inputValueNotValidation_expect_INVALID_INPUTS() throws Exception {
        UserDto.SignUp email_type_validation = setSignUpDto("not_email_validate", "rePassword", "rePassword");
        requestSinUpNotValidate(email_type_validation);

        UserDto.SignUp password_length_validation = setSignUpDto("test@test.com", "123456", "123456");
        requestSinUpNotValidate(password_length_validation);
    }

    @Test
    public void When_myAccountUpdate_expect_succeed() throws Exception {
        User user = userService.create(setSignUpDto(email, password, rePassword));
        UserDto.MyAccount dto = setMyAccountDto(firstName, lastName, mobile, dob);

        requestMyAccount(dto, user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)))
                .andExpect(jsonPath("$.mobile", is(mobile)))
                .andExpect(jsonPath("$.dob", is(dob.toString())));
    }

    @Test
    public void When_notExistedUserUpdate_expect_USER_NOT_FOUND() throws Exception {
        UserDto.MyAccount dto = setMyAccountDto(firstName, lastName, mobile, dob);

        requestMyAccount(dto, 0L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    @Test
    public void When_getUser_expect_succeed() throws Exception {
        User user = userService.create(setSignUpDto(email, password, rePassword));
        RequestGetUser(user.getId())
                .andExpect(status().isOk());
    }

    @Test
    public void When_notExistedUserGet_expect_USER_NOT_FOUND() throws Exception {
        RequestGetUser(0L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    @Test
    public void When_getUsers_expect_succeed() throws Exception {
        eachCreateUser(20);
        requestGetUsers()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.last", is(instanceOf(Boolean.class))))
                .andExpect(jsonPath("$.totalPages", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.size", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.number", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.sort", is(instanceOf(List.class))))
                .andExpect(jsonPath("$.first", is(instanceOf(Boolean.class))))
                .andExpect(jsonPath("$.numberOfElements", is(instanceOf(Integer.class))));
    }

    @Test
    public void When_get2Page_expect_succeed() throws Exception {
        eachCreateUser(20);
        final int size = 10;
        final int page = 2;

        requestGetUsersInPage(page, size)
                .andExpect(jsonPath("$.totalElements", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.last", is(instanceOf(Boolean.class))))
                .andExpect(jsonPath("$.totalPages", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.size", is(size)))
                .andExpect(jsonPath("$.number", is(page - 1)))
                .andExpect(jsonPath("$.sort", is(instanceOf(List.class))))
                .andExpect(jsonPath("$.first", is(instanceOf(Boolean.class))))
                .andExpect(jsonPath("$.numberOfElements", is(instanceOf(Integer.class))));
    }

    @Test
    public void When_sizeOverThan50_expect_sizeSet10() throws Exception {
        eachCreateUser(20);
        final int size = 51;
        final int page = 2;

        requestGetUsersInPage(page, size)
                .andExpect(jsonPath("$.totalElements", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.last", is(instanceOf(Boolean.class))))
                .andExpect(jsonPath("$.totalPages", is(instanceOf(Integer.class))))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.number", is(page - 1)))
                .andExpect(jsonPath("$.sort", is(instanceOf(List.class))))
                .andExpect(jsonPath("$.first", is(instanceOf(Boolean.class))))
                .andExpect(jsonPath("$.numberOfElements", is(instanceOf(Integer.class))));
    }

    @Test
    public void When_emailNotExist_expect_false() throws Exception {
        requestExists("email", email)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existence", is(false)));
    }

    @Test
    public void When_emailExist_expect_true() throws Exception {
        userService.create(setSignUpDto(email, password, rePassword));
        requestExists("email", email)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existence", is(true)));
    }

    private ResultActions requestExists(String type, String value) throws Exception {
        return mockMvc.perform(get(getUrlExistsTemplate(type, value))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private String getUrlExistsTemplate(String type, String value) {
        return "/users/exists?" + type + "=" + value;
    }

    private ResultActions requestGetUsers() throws Exception {
        return mockMvc.perform(get("/users/"))
                .andDo(print());
    }

    private ResultActions requestGetUsersInPage(int page, int size) throws Exception {
        return mockMvc.perform(get(getUrlPageTemplate(page, size)))
                .andDo(print());
    }

    private String getUrlPageTemplate(int page, int size) {
        return "/users?page=" + page + "&size=" + size;
    }

    private ResultActions RequestGetUser(Long id) throws Exception {
        return mockMvc.perform(get("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestMyAccount(UserDto.MyAccount dto, Long id) throws Exception {
        return mockMvc.perform(put("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private void requestSinUpNotValidate(UserDto.SignUp dto) throws Exception {
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.INVALID_INPUTS.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.INVALID_INPUTS.getCode())));
    }

    private UserDto.SignUp setSignUpDto(String email, String password, String rePassword) {
        UserDto.SignUp signUpDto = new UserDto.SignUp();
        signUpDto.setEmail(email);
        signUpDto.setPassword(password);
        signUpDto.setRePassword(rePassword);
        return signUpDto;
    }

    private UserDto.MyAccount setMyAccountDto(String firstName, String lastName, String mobile, Date dob) {
        UserDto.MyAccount dto = new UserDto.MyAccount();

        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setMobile(mobile);
        dto.setDob(dob);
        return dto;
    }

    private ResultActions requestSignUp(UserDto.SignUp signUpDto) throws Exception {
        return mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpDto)));
    }

    private void eachCreateUser(final int endExclusive) {
        IntStream.range(0, endExclusive).forEach(i ->
                userService.create(setSignUpDto(i + email, password, rePassword)));
    }
}