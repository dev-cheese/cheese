package com.cheese.demo.security.controller;

import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberServiceImpl;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationControllerTest2 {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private String CONTENT_TYPE = "application/json;charset=UTF-8";
    private String SCOPE = "read";
    private String CLIENT_ID = "foo";
    private String CLIENT_SECRET = "bar";
    private String SECURITY_USERNAME = "admin001@gmail.com";
    private String SECURITY_PASSWORD = "password001";
    private String ADMIN_EMAIL = "admin001@gmail.com";
    private String AUTHORIZATION = "Authorization";
    private String BEARER = "Bearer ";

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void when_callApi_expect_unauthorized() throws Exception {
        requestMembers("")
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void when_callUsers_expect_success() throws Exception {
        final String token = obtainAccessToken(SECURITY_USERNAME, SECURITY_PASSWORD);

        requestMembers(token)
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));
    }

    @Test
    public void when_ckeck2User_expect_success() throws Exception {
        final Member member = memberService.findByEmail(ADMIN_EMAIL);
        final String accessToken = obtainAccessToken(SECURITY_USERNAME, SECURITY_PASSWORD);
        final String email = member.getEmail();

        mockMvc.perform(get("/members/" + member.getId())
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(CONTENT_TYPE))
                .andDo(print())
                .andExpect(jsonPath("$.email", is(email)));
    }

    private ResultActions requestMembers(final String token) throws Exception {
        return mockMvc.perform(get("/members")
                .header(AUTHORIZATION,  BEARER + token)
                .accept(CONTENT_TYPE))
                .andDo(print());
    }

    private String obtainAccessToken(String username, String password) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", CLIENT_ID);
        params.add("username", username);
        params.add("password", password);
        params.add("scope", SCOPE);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .params(params)
                .with(httpBasic(CLIENT_ID, CLIENT_SECRET))
                .accept(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }
}
