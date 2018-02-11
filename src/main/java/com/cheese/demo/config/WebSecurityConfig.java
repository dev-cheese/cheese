package com.cheese.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final String ADMIN = "ADMIN";
    private final String USER = "USER";
    private final String MEMBER_URL_PATH = "/members";

    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.httpBasic();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, MEMBER_URL_PATH).permitAll()
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/exists**").permitAll()
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/{id}").hasRole(USER)
                .antMatchers(HttpMethod.PUT, MEMBER_URL_PATH + "/{id}").hasRole(USER)
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/**").hasRole(ADMIN)
                .anyRequest().denyAll();
    }

}

