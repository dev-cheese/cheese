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
    private final String password = "passwordIsValidated001";
    private final String rePassword = "passwordIsValidated001";
    private final String firstName = "길동";
    private final String lastName = "홍";
    private final String mobile = "01071333262";
    private final Date dob = Date.valueOf(LocalDate.now());

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    //    회원가입
    @Test
    public void When_signUp_expect_succeed() throws Exception {
        UserDto.SignUpReq dto = setSignUpDto(email, password, rePassword);
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(dto.getEmail())));
    }

    //    이메일 중복 예외
    @Test
    public void When_emailIsDuplicated_expect_EMAIL_DUPLICATION() throws Exception {
        UserDto.SignUpReq dto = setSignUpDto(email, password, rePassword);
        userService.create(dto);

        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.EMAIL_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.EMAIL_DUPLICATION.getCode())));
    }

    //    이메일 유효성 예외
    @Test
    public void When_emailIsNotValidated_expect_INVALID_DOMAIN() throws Exception {
        UserDto.SignUpReq email_type_validation = setSignUpDto("not_email_validate", password, rePassword);
        requestSinUpNotValidate(email_type_validation, ErrorCodeEnum.INVALID_DOMAIN);
    }

    //    비밀번호 유호성 예외
    @Test
    public void When_passwordIsNotValidated_expect_INVALID_DOMAIN() throws Exception {
        UserDto.SignUpReq password_length_validation = setSignUpDto(email, "123456", "123456");
        requestSinUpNotValidate(password_length_validation, ErrorCodeEnum.INVALID_DOMAIN);
    }

    //    회원 정보 수정
    @Test
    public void When_myAccountUpdate_expect_succeed() throws Exception {
        User user = userService.create(setSignUpDto(email, password, rePassword));
        UserDto.MyAccountReq dto = setMyAccountDto(firstName, lastName, mobile, dob);

        requestMyAccount(dto, user.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.lastName", is(lastName)))
                .andExpect(jsonPath("$.mobile", is(mobile)))
                .andExpect(jsonPath("$.dob", is(dob.toString())));
    }

    //    없는 유저 업데이트시 404
    @Test
    public void When_notExistedUser_expect_USER_NOT_FOUND() throws Exception {
        UserDto.MyAccountReq dto = setMyAccountDto(firstName, lastName, mobile, dob);

        requestMyAccount(dto, 0L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    //    특정 유저 조회
    @Test
    public void When_getUser_expect_succeed() throws Exception {
        User user = userService.create(setSignUpDto(email, password, rePassword));
        RequestGetUser(user.getId())
                .andExpect(status().isOk());
    }

    //   없는 유저 조회시 404
    @Test
    public void When_getUserNotExisted_expect_USER_NOT_FOUND() throws Exception {

        RequestGetUser(0L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    //    유저 페이지 조회
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

    //    유저 2 페이지 조회
    @Test
    public void When_getUserPage2_expect_succeed() throws Exception {
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

    //    페이지 사이즈 50 이상일 경우 10으로 강제 지정
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

    private ResultActions requestMyAccount(UserDto.MyAccountReq dto, Long id) throws Exception {
        return mockMvc.perform(put("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private void requestSinUpNotValidate(UserDto.SignUpReq dto, ErrorCodeEnum invalidInputs) throws Exception {
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(invalidInputs.getMessage())))
                .andExpect(jsonPath("$.code", is(invalidInputs.getCode())));
    }

    private UserDto.SignUpReq setSignUpDto(String email, String password, String rePassword) {
        UserDto.SignUpReq signUpReqDto = new UserDto.SignUpReq();
        signUpReqDto.setEmail(email);
        signUpReqDto.setPassword(password);
        signUpReqDto.setRePassword(rePassword);
        return signUpReqDto;
    }

    private UserDto.MyAccountReq setMyAccountDto(String firstName, String lastName, String mobile, Date dob) {
        UserDto.MyAccountReq dto = new UserDto.MyAccountReq();

        dto.setFirstName(firstName);
        dto.setLastName(lastName);
        dto.setMobile(mobile);
        dto.setDob(dob);
        return dto;
    }

    private ResultActions requestSignUp(UserDto.SignUpReq signUpReqDto) throws Exception {
        return mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpReqDto)));
    }

    private void eachCreateUser(final int endExclusive) {
        IntStream.range(0, endExclusive).forEach(i ->
                userService.create(setSignUpDto(i + email, password, rePassword)));
    }
}