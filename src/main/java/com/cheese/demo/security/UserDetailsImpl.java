package com.cheese.demo.security;

import com.cheese.demo.member.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl extends User {

    public UserDetailsImpl(Member member) {
        super(member.getEmail(), member.getPassword(), authorities(member));
    }

    private static Collection<? extends GrantedAuthority> authorities(Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (member.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }
}
