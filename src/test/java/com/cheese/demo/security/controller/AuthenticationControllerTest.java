package com.cheese.demo.security.controller;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberService;
import com.cheese.demo.mock.DeviceDummy;
import com.cheese.demo.security.JwtTokenUtil;
import com.cheese.demo.security.JwtUser;
import com.cheese.demo.security.JwtUserFactory;
import com.cheese.demo.security.dto.JwtAuthenticationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
@Transactional
public class AuthenticationControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithAnonymousUser
    //인증이 유효한 경우 토큰을 리턴합니다.
    public void When_authenticationIsValid_expect_returnToken() throws Exception {
        JwtAuthenticationDto.Request dto = buildDto("wan@gmail.com", "password001");

        requestAuth(dto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(notNullValue())));
    }


    @Test
    @WithAnonymousUser
    public void When_authenticationIsNotValid_Expect_401() throws Exception {
        JwtAuthenticationDto.Request dto = buildDto("wan@gmail.com", "notMatchedPassword");
        requestAuth(dto)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void When_getRefreshTokenIfValidTokenWithUserRole_Except_returnRefreshToken() throws Exception {
        Member member = memberService.findByEmail("wan@gmail.com");
        String token = generateToken(member);

        requestRefresh(token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(notNullValue())));
    }

    @Test
    public void When_getRefreshTokenIfInValidTokenWithUserRole_Except_returnRefreshToken() throws Exception {
        requestRefresh("inValidToken")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.UNAUTHORIZED.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.UNAUTHORIZED.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCodeEnum.UNAUTHORIZED.getStatus())));
    }

    @Test
    public void When_getRefreshTokenIfValidTokenWithAdminRole_Except_returnRefreshToken() throws Exception {
        Member member = memberService.findByEmail("admin001@gmail.com");
        String token = generateToken(member);

        requestRefresh(token)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(notNullValue())));
    }

    @Test
    public void When_getRefreshTokenIfInValidTokenWithAdminRole_Except_returnRefreshToken() throws Exception {
        requestRefresh("inValidToken")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message", is(ErrorCodeEnum.UNAUTHORIZED.getMessage())))
                .andExpect(jsonPath("$.code", is(ErrorCodeEnum.UNAUTHORIZED.getCode())))
                .andExpect(jsonPath("$.status", is(ErrorCodeEnum.UNAUTHORIZED.getStatus())));
    }

    private JwtAuthenticationDto.Request buildDto(String email, String password) {
        return JwtAuthenticationDto
                .Request.builder()
                .email(email)
                .password(password)
                .build();
    }

    private ResultActions requestAuth(JwtAuthenticationDto.Request dto) throws Exception {
        return mockMvc.perform(post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print());
    }

    private ResultActions requestRefresh(String token) throws Exception {
        return mockMvc.perform(get("/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token))
                .andDo(print());
    }

    private String generateToken(Member member) {
        final DeviceDummy device = new DeviceDummy();
        device.setNormal(true);
        final JwtUser jwtUser = JwtUserFactory.buildJwtUser(member);

        return jwtTokenUtil.generateToken(jwtUser, device);
    }
}