package com.cheese.demo.security;

import com.cheese.demo.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUserFactory {

    public static JwtUser buildJwtUser(Member member) {

        return JwtUser.builder()
                .id(member.getId())
                .email(member.getEmail())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .password(member.getPassword())
                .username(member.getEmail())
                .lastPasswordResetDate(member.getCreatedDt())
                .enabled(true)
                .authorities(authorities(member))
                .build();

    }

    private static Collection<? extends GrantedAuthority> authorities(final Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().getName()));
        return authorities;
    }
}