package com.cheese.demo.security.service;

import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberService;
import com.cheese.demo.security.JwtUserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Member member = memberService.findByEmail(email);
        return JwtUserFactory.create(member);
//        return new UserDetailsImpl(member);
    }
}
