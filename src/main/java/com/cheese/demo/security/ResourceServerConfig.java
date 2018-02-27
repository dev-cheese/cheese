package com.cheese.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final String ADMIN = "ADMIN";
    private final String USER = "USER";
    private final String MEMBER_URL_PATH = "/members";
    private final ResourceServerTokenServices tokenServices;
    //    @Value("${security.jwt.resource-ids}")
    private String resourceIds = "testjwtresourceid";

    @Autowired
    public ResourceServerConfig(ResourceServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(resourceIds).tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http
                .requestMatchers()
                .and()
                .authorizeRequests()
                .antMatchers("/browser/**").permitAll() // browser 허용
                .antMatchers(HttpMethod.POST, MEMBER_URL_PATH).permitAll()
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/exists**").permitAll()
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/{id}").hasRole(USER)
                .antMatchers(HttpMethod.PUT, MEMBER_URL_PATH + "/{id}").hasRole(USER)
                .antMatchers(HttpMethod.GET, MEMBER_URL_PATH + "/**").hasRole(ADMIN)
                .anyRequest().denyAll();
    }


}
