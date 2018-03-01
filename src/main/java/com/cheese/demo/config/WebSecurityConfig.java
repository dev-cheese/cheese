package com.cheese.demo.config;

import com.cheese.demo.security.JwtAuthenticationEntryPoint;
import com.cheese.demo.security.JwtAuthenticationTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private UserDetailsService userDetailsService;

    private final String ADMIN = "ADMIN";
    private final String USER = "USER";
    private final String MEMBER_URL_PATH = "/members";


    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // allow anonymous resource requests
//                .antMatchers(
//                        HttpMethod.GET,
//                        "/",
//                        "/*.html",
//                        "/favicon.ico",
//                        "/**/*.html",
//                        "/**/*.css",
//                        "/**/*.js"
//                ).permitAll()

                // Un-secure H2 Database
                .antMatchers("/h2-console/**/**").permitAll()

                .antMatchers("/auth/**").permitAll()
                .antMatchers("/members/**").permitAll()
                .antMatchers(HttpMethod.POST, MEMBER_URL_PATH).permitAll()
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/exists**").permitAll()
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/{id}").authenticated()
                .antMatchers(HttpMethod.PUT, MEMBER_URL_PATH + "/{id}").authenticated()
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/**").authenticated()
//                .anyRequest().denyAll();
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity
                .headers()
                .frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
    }


//    private final PasswordEncoder passwordEncoder;
//    private final UserDetailsService userDetailsService;
//    private final String ADMIN = "ADMIN";
//    private final String USER = "USER";
//    private final String MEMBER_URL_PATH = "/members";
//
//    @Autowired
//    public WebSecurityConfig(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
//        this.passwordEncoder = passwordEncoder;
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.httpBasic();
//
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.POST, MEMBER_URL_PATH).permitAll()
//                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/exists**").permitAll()
//                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/{id}").hasRole(USER)
//                .antMatchers(HttpMethod.PUT, MEMBER_URL_PATH + "/{id}").hasRole(USER)
//                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/**").hasRole(ADMIN)
//                .anyRequest().denyAll();
//    }

}

