//package com.cheese.demo.security.service;
//
//import com.cheese.demo.member.Member;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class UserDetailsImpl extends User {
//
//    public UserDetailsImpl(Member member) {
//        super(member.getEmail(), member.getPassword(), authorities(member));
//    }
//
//    private static Collection<? extends GrantedAuthority> authorities(final Member member) {
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority(member.getRole().getName()));
//        return authorities;
//    }
//}