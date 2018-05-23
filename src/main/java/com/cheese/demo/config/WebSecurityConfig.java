package com.cheese.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableResourceServer
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //
//    @Autowired
//    private UserDetailsServiceImpl userDetailsServiceImple;
//
//    private final String ADMIN = "ADMIN";
//    private final String USER = "USER";
//    private final String MEMBER_URL_PATH = "/members";
//
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.parentAuthenticationManager(authenticationManager).userDetailsService(userDetailsServiceImple);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .anonymous().disable()
//                .authorizeRequests()
//                .antMatchers("/api-docs/**").permitAll();
//    }
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Constructor disables the default security settings
     */
    public WebSecurityConfig() {
        super(true);
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/login");
//    }

    //    private final String[] AUTH_WHITELIST = {
//            // -- swagger ui
//            "/swagger-resources/**",
//            "/swagger-ui.html",
//            "/v2/api-docs",
//            "/webjars/**"
//    };

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/gigy/**")
                .authorizeRequests().anyRequest().authenticated();
    }

    @Autowired
    private UserDetailsService userDetailsService;

    //    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public TokenStore JdbcTokenStore(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcTokenStore(dataSource);
    }

}