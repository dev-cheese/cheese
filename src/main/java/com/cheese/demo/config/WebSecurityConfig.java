package com.cheese.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


//    private UserDetailsService userDetailsService;

    private final String ADMIN = "ADMIN";
    private final String USER = "USER";
    private final String MEMBER_URL_PATH = "/members";


//    @Autowired
//    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder
//                .userDetailsService(this.userDetailsService)
//                .passwordEncoder(passwordEncoder());
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    private final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .authorizeRequests()
                // browser
                .antMatchers("/browser/**").permitAll()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().permitAll();
//                .antMatchers(HttpMethod.POST, MEMBER_URL_PATH).permitAll()
//                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/exists**").permitAll()
//                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/{id}").authenticated()
//                .antMatchers(HttpMethod.PUT, MEMBER_URL_PATH + "/{id}").authenticated()
//                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/**").authenticated()
//                .anyRequest().authenticated();


    }
}

