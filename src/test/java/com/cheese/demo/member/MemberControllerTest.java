package com.cheese.demo.member;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.mock.UserMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
@WebAppConfiguration
@Transactional
public class MemberControllerTest {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private final String PASSWORD = "password001";
    private final String RE_PASSWORD = "password001";
    private final String EMAIL = "cheese10yun@gmail.com";

    private final String FIRST_NAME = "길동";
    private final String LAST_NAME = "홍";
    private final String MOBILE = "01071333262";
    private final Date DOB = Date.valueOf(LocalDate.now());
    private final String ADMIN_EMAIL = "admin001@gmail.com";
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    private MockMvc mockMvc;
    private UserMock userMock;

    @Before
    public void setUp() {
        userMock = new UserMock();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .build();
    }

    //    회원가입
    @Test
    public void When_signUp_expect_succeed() throws Exception {
        MemberDto.SignUpReq dto = userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD);
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(dto.getEmail())));
    }

    //    이메일 중복 예외
    @Test
    public void When_emailIsDuplicated_expect_EMAIL_DUPLICATION() throws Exception {
        MemberDto.SignUpReq dto = userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD);
        memberService.create(userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));

        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.EMAIL_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.EMAIL_DUPLICATION.getCode())));
    }

    //    이메일 유효성 예외
    @Test
    public void When_emailIsNotValidated_expect_INVALID_DOMAIN() throws Exception {
        MemberDto.SignUpReq email_type_validation = userMock.setSignUpDto("not_email_validate", PASSWORD, RE_PASSWORD);
        requestSinUpNotValidate(email_type_validation, ErrorCodeEnum.INVALID_DOMAIN);
    }

    //    비밀번호 유호성 예외
    @Test
    public void When_passwordIsNotValidated_expect_INVALID_INPUTS() throws Exception {
        MemberDto.SignUpReq password_length_validation = userMock.setSignUpDto(EMAIL, "123456", "123456");
        requestSinUpNotValidate(password_length_validation, ErrorCodeEnum.INVALID_INPUTS);
    }

    //    회원 정보 수정
    @Test
    public void When_myAccountUpdate_expect_succeed() throws Exception {
        Member member = memberService.create(userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
        MemberDto.MyAccountReq dto = userMock.setMyAccountDto(FIRST_NAME, LAST_NAME, MOBILE, DOB);

        requestMyAccount(dto, member.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.mobile", is(MOBILE)))
                .andExpect(jsonPath("$.dob", is(DOB.toString())));
    }

    //    없는 유저 업데이트시 404
    @Test
    public void When_notExistedUser_expect_USER_NOT_FOUND() throws Exception {
        memberService.create(userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
        MemberDto.MyAccountReq dto = userMock.setMyAccountDto(FIRST_NAME, LAST_NAME, MOBILE, DOB);

        requestMyAccount(dto, 0L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    //    특정 유저 조회
    @Test
    public void When_getUser_expect_succeed() throws Exception {
        Member member = memberService.create(userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
        RequestGetUser(member.getId())
                .andExpect(status().isOk());
    }

    //   없는 유저 조회시 404
    @Test
    public void When_getUserNotExisted_expect_USER_NOT_FOUND() throws Exception {
        memberService.create(userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
        RequestGetUser(0L)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    //    유저 페이지 조회
    @Test
    public void When_getUsers_expect_succeed() throws Exception {
        createAdmin(); // 유저 조회를 위해 관리자 계정 생성
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
        createAdmin(); // 유저 조회를 위해 관리자 계정 생성
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
        createAdmin(); // 유저 조회를 위해 관리자 계정 생성
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

    // 이메일 존재 유무 검사
    @Test
    public void When_emailIsNotExist_expect_false() throws Exception {
        requestExists(EMAIL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existence", is(false)));
    }

    // 이메일 존재 유무 검사
    @Test
    public void When_emailIsExist_expect_true() throws Exception {
        memberService.create(userMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
        requestExists(EMAIL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existence", is(true)));
    }

    private ResultActions requestExists(String value) throws Exception {
        return mockMvc.perform(get(getUrlExistsTemplate(value))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private String getUrlExistsTemplate(String value) {
        return "/members" + "/exists?email=" + value;
    }

    private ResultActions requestGetUsers() throws Exception {
        return mockMvc.perform(get("/members" + "/")
                .with(httpBasic(ADMIN_EMAIL, PASSWORD)))
                .andDo(print());
    }

    private ResultActions requestGetUsersInPage(int page, int size) throws Exception {
        return mockMvc.perform(get(getUrlPageTemplate(page, size))
                .with(httpBasic(ADMIN_EMAIL, PASSWORD)))
                .andDo(print());
    }

    private String getUrlPageTemplate(int page, int size) {
        return "/members" + "?page=" + page + "&size=" + size;
    }

    private ResultActions RequestGetUser(Long id) throws Exception {
        return mockMvc.perform(get("/members" + "/" + id)
                .with(httpBasic(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestMyAccount(MemberDto.MyAccountReq dto, Long id) throws Exception {
        return mockMvc.perform(put("/members" + "/" + id)
                .with(httpBasic(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private void requestSinUpNotValidate(MemberDto.SignUpReq dto, ErrorCodeEnum invalidInputs) throws Exception {
        requestSignUp(dto)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(invalidInputs.getMessage())))
                .andExpect(jsonPath("$.code", is(invalidInputs.getCode())));
    }

    private ResultActions requestSignUp(MemberDto.SignUpReq signUpReqDto) throws Exception {
        return mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpReqDto)));
    }

    private void eachCreateUser(final int endExclusive) {
        IntStream.range(0, endExclusive).forEach(i ->
                memberService.create(userMock.setSignUpDto(i + EMAIL, PASSWORD, RE_PASSWORD)));
    }

    private Member createAdmin() {
        Member admin = memberService.create(userMock.setSignUpDto(ADMIN_EMAIL, PASSWORD, RE_PASSWORD));
        admin.setRole(MemberRoleEnum.ADMIN);
        return memberRepository.save(admin);
    }
}