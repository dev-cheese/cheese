package com.cheese.demo.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl extends User {

    public UserDetailsImpl(com.cheese.demo.user.User user) {
        super(user.getEmail(), user.getPassword(), authorities(user));
    }

    private static Collection<? extends GrantedAuthority> authorities(com.cheese.demo.user.User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }
}
