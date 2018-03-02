package com.cheese.demo.member;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.mock.DeviceDummy;
import com.cheese.demo.mock.MemberMock;
import com.cheese.demo.security.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
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
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
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

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private MockMvc mockMvc;
    private MemberMock memberMock;

    @Before
    public void setUp() {
        memberMock = new MemberMock();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void When_SignUpValidMember_Expect_ReturnMember() throws Exception {
        MemberDto.SignUpReq dto = memberMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD);
        requestSignUp(dto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(dto.getEmail())));
    }

    @Test
    public void When_SignUpEmailIsDuplicated_Expect_EmailDuplicationException() throws Exception {
        MemberDto.SignUpReq dto = memberMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD);
        memberService.create(memberMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));

        requestSignUp(dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.EMAIL_DUPLICATION.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.EMAIL_DUPLICATION.getCode())));
    }

    @Test
    public void When_SignUpEmailIsInValid_Expect_ConstraintViolationException() throws Exception {
        MemberDto.SignUpReq email_type_validation = memberMock.setSignUpDto("not_email_validate", PASSWORD, RE_PASSWORD);
        requestSinUpNotValidate(email_type_validation, ErrorCodeEnum.INVALID_DOMAIN);
    }

    @Test
    public void When_SignUpPasswordIsOnlyString_Expect_MethodArgumentNotValidException() throws Exception {
        //password is must to include number
        MemberDto.SignUpReq password_length_validation = memberMock.setSignUpDto(EMAIL, "123456", "123456");
        requestSinUpNotValidate(password_length_validation, ErrorCodeEnum.INVALID_INPUTS);
    }

    @Test
    public void When_MyAccountUpdateValid_Expect_returnMember() throws Exception {
        Member member = createUser();
        MemberDto.MyAccountReq dto = CreateMyAccountReq();
        String token = generateToken(member);

        requestMyAccount(dto, member.getId(), token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.mobile", is(MOBILE)))
                .andExpect(jsonPath("$.dob", is(DOB.toString())));
    }

    @Test
    public void When_MyAccountUpdateNotExistedMember_Expect_MemberNotFoundException() throws Exception {
        Member member = createUser();
        MemberDto.MyAccountReq dto = CreateMyAccountReq();
        String token = generateToken(member);

        requestMyAccount(dto, 0L, token)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    // TODO: 2018. 3. 2. 비정상 토큰시 401에러를 못돌려주고 있음 리팩토리할것
    @Ignore
    @Test
    public void When_updateUserWithUnauthorized_expect_401() throws Exception {
        Member member = createUser();
        MemberDto.MyAccountReq dto = CreateMyAccountReq();

        requestMyAccount(dto, member.getId(), "1111")
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void When_getMember_expect_succeed() throws Exception {
        Member member = createUser();
        String token = generateToken(member);
        RequestGetUser(member.getId(), token)
                .andExpect(status().isOk());
    }

    @Test
    public void When_getMemberIfNotExisted_Expect_MemberNotFoundException() throws Exception {
        Member member = memberService.create(memberMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
        String token = generateToken(member);

        RequestGetUser(0L, token)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.USER_NOT_FOUND.getCode())))
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.USER_NOT_FOUND.getMessage())));
    }

    @Test
    public void When_getMembers_Expect_ReturnMembers() throws Exception {
        eachCreateUser(20);
        String token = generateToken(memberService.findByEmail(ADMIN_EMAIL));
        requestGetUsers(token)
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
    public void When_getUserPage2_expect_succeed() throws Exception {
        eachCreateUser(20);
        String token = generateToken(memberService.findByEmail(ADMIN_EMAIL));
        final int size = 10;
        final int page = 2;

        requestGetUsersInPage(page, size, token)
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
        String token = generateToken(memberService.findByEmail(ADMIN_EMAIL));
        final int size = 51;
        final int page = 2;

        requestGetUsersInPage(page, size, token)
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
    public void When_emailIsNotExist_expect_false() throws Exception {
        requestExists(EMAIL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existence", is(false)));
    }

    @Test
    public void When_emailIsExist_expect_true() throws Exception {
        memberService.create(memberMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
        requestExists(EMAIL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.existence", is(true)));
    }


    // TODO: 2018. 3. 2. 비정상 토큰시 401에러를 못돌려주고 있음 리팩토리할것
    @Ignore
    @Test
    public void When_getUserWithUnauthorized_expect_401() throws Exception {
        RequestGetUser(0L, "111")
                .andExpect(status().isUnauthorized());
    }


    //ACL 검사
    private ResultActions requestExists(String value) throws Exception {
        return mockMvc.perform(get(getUrlExistsTemplate(value))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private String getUrlExistsTemplate(String value) {
        return "/members" + "/exists?email=" + value;
    }

    private ResultActions requestGetUsers(String token) throws Exception {
        return mockMvc.perform(get("/members" + "/")
                .header("Authorization", "Bearer " + token))
                .andDo(print());
    }

    private ResultActions requestGetUsersInPage(int page, int size, String token) throws Exception {
        return mockMvc.perform(get(getUrlPageTemplate(page, size))
                .header("Authorization", "Bearer " + token))
                .andDo(print());
    }

    private String getUrlPageTemplate(int page, int size) {
        return "/members" + "?page=" + page + "&size=" + size;
    }

    private ResultActions RequestGetUser(Long id, String token) throws Exception {
        return mockMvc.perform(get("/members" + "/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestMyAccount(MemberDto.MyAccountReq dto, Long id, String token) throws Exception {
        return mockMvc.perform(put("/members" + "/" + id)
                .header("Authorization", "Bearer " + token)
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
                .content(objectMapper.writeValueAsString(signUpReqDto)))
                .andDo(print());
    }

    private void eachCreateUser(final int endExclusive) {
        IntStream.range(0, endExclusive).forEach(i ->
                memberService.create(memberMock.setSignUpDto(i + EMAIL, PASSWORD, RE_PASSWORD)));
    }


    private Member createUser() {
        return memberService.create(memberMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
    }

    private String generateToken(Member member) {
        final DeviceDummy device = new DeviceDummy();
        device.setNormal(true);
        return jwtTokenUtil.generateToken(member, device);
    }

    private MemberDto.MyAccountReq CreateMyAccountReq() {
        return memberMock.setMyAccountDto(FIRST_NAME, LAST_NAME, MOBILE, DOB);
    }
}