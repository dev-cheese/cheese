package com.cheese.demo.member;

import com.cheese.demo.member.exception.EmailDuplicationException;
import com.cheese.demo.member.exception.MemberNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Member create(MemberDto.SignUpReq dto) {
        final String email = dto.getEmail();

        if (isExistedEmail(email))
            throw new EmailDuplicationException(email);

        final String encodePassword = encodePassword(dto.getPassword());
        MemberRoleEnum role = MemberRoleEnum.USER;

        return memberRepository.save(dto.toEntity(encodePassword, role));
    }

    @Transactional
    public Member update(Long id, MemberDto.MyAccountReq dto) {
        return dto.toEntity(findById(id));
    }

    public PageImpl<MemberDto.Res> findAll(Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        List<MemberDto.Res> content = convertResDto(page);
        return new PageImpl<>(content, pageable, page.getTotalElements());
    }

    public Member findById(Long id) {
        Member member = memberRepository.findOne(id);
        if (member != null)
            return member;
        else
            throw new MemberNotFoundException(id);
    }

    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null)
            return member;
        else
            throw new MemberNotFoundException(email);
    }

    public boolean isExistedEmail(String email) {
        return memberRepository.findByEmail(email) != null;
    }

    private List<MemberDto.Res> convertResDto(Page<Member> page) {
        return page.getContent()
                .parallelStream()
                .map(MemberDto.Res::new)
                .collect(Collectors.toList());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
