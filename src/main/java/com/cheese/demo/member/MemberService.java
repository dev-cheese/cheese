package com.cheese.demo.member;


import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    Member create(MemberDto.SignUpReq dto);

    Member update(long id, MemberDto.MyAccountReq dto);

    PageImpl<MemberDto.Res> findAll(Pageable pageable);

    Member findById(Long id);

    Member findByEmail(String email);

    boolean isExistedEmail(String email);

}
