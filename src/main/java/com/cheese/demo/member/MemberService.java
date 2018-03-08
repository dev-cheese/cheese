package com.cheese.demo.member;

public interface MemberService {

    Member create(MemberDto.SignUpReq dto);

    Member update(Long id, MemberDto.MyAccountReq dto);

    Member findById(Long id);

    Member findByEmail(String email);

    boolean isExistedEmail(String email);

}
