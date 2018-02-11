package com.cheese.demo.security;

import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberService;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Member member = memberService.findByEmail(email);
        return new UserDetailsImpl(member);
    }
}
