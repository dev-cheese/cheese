package com.cheese.demo.config.oauth.service;

import com.cheese.demo.config.oauth.OauthUserFactory;
import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberServiceImpl memberService;

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Member member = memberService.findByEmail(email);
        return OauthUserFactory.buildOauthUser(member);
    }
}
