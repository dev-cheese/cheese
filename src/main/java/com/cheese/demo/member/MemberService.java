package com.cheese.demo.member;

import com.cheese.demo.commons.CommonDto;
import com.cheese.demo.member.exception.EmailDuplicationException;
import com.cheese.demo.member.exception.MemberNotFoundException;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public Member create(MemberDto.SignUpReq dto) {
        final String email = dto.getEmail();

        if (isDuplicatedEmail(email))
            throw new EmailDuplicationException(email);

        dto.setPassword(encodePassword(dto.getPassword()));
        return memberRepository.save(modelMapper.map(dto, Member.class));
    }

    @Transactional
    public Member update(Long id, MemberDto.MyAccountReq dto) {
        Member member = findById(id);
        member.setLastName(dto.getLastName());
        member.setFirstName(dto.getFirstName());
        member.setMobile(dto.getMobile());
        member.setDob(dto.getDob());
        return member;
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

    public CommonDto.ExistenceRes isExist(String email) {
        CommonDto.ExistenceRes existence = new CommonDto.ExistenceRes();
        existence.setExistence(isDuplicatedEmail(email));
        return existence;
    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email) != null;
    }

    private List<MemberDto.Res> convertResDto(Page<Member> page) {
        return page.getContent()
                .parallelStream()
                .map(member -> modelMapper.map(member, MemberDto.Res.class))
                .collect(Collectors.toList());
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
