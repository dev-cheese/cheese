package com.cheese.demo.member;

import com.cheese.demo.member.exception.EmailDuplicationException;
import com.cheese.demo.member.exception.MemberNotFoundException;
import com.cheese.demo.mock.MemberMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {


    @Mock
    private MemberRepository memberRepository;


    private MemberService memberService;
    private MemberMock memberMock = new MemberMock();
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private MemberDto.SignUpReq dto;
    private Member memberEntity;

    @Before
    public void setUp() {
        memberService = new MemberServiceImpl(memberRepository, passwordEncoder);
        dto = buildSignUp();
        memberEntity = toEntity(dto);
    }

    @Test
    public void create_Succeed_ReturnMember() {
        //given
        given(memberRepository.save(any(Member.class))).willReturn(memberEntity);

        //when
        final Member member = memberService.create(dto);

        //then
        verify(memberRepository, atLeastOnce()).save(any(Member.class));
        assertThat(member, equalTo(memberEntity));
        assertThatProperty(dto, memberEntity, member);
    }

    @Test(expected = EmailDuplicationException.class)
    public void create_ExistedEmail_EmailDuplicationException() {
        //given
        given(memberRepository.findByEmail(anyString())).willReturn(memberEntity);

        //when
        memberService.create(dto);
    }

    @Test
    public void isExistedEmail_NotExistedEmail_ReturnFalse() {
        //given
        given(memberRepository.findByEmail(anyString())).willReturn(null);

        //when
        boolean isExistedEmail = memberService.isExistedEmail(anyString());

        //then
        verify(memberRepository, atLeastOnce()).findByEmail(anyString());
        assertThat(isExistedEmail, is(false));
    }

    @Test
    public void isExistedEmail_ExistedEmail_ReturnTrue() {
        //given
        given(memberRepository.findByEmail(anyString())).willReturn(memberEntity);

        //when
        boolean isExistedEmail = memberService.isExistedEmail(anyString());

        //then
        verify(memberRepository, atLeastOnce()).findByEmail(anyString());
        assertThat(isExistedEmail, is(true));
    }


    @Test
    public void findById_ExistedId_ReturnMember() {
        //given
        given(memberRepository.findOne(anyLong())).willReturn(memberEntity);

        //when
        Member member = memberService.findById(anyLong());

        //then
        verify(memberRepository, atLeastOnce()).findOne(anyLong());
        assertThat(member, equalTo(memberEntity));
        assertThatProperty(dto, memberEntity, member);
    }


    @Test(expected = MemberNotFoundException.class)
    public void findById_NotExistedId_MemberNotFoundException() {
        //given
        given(memberRepository.findOne(anyLong())).willReturn(null);

        //when
        memberService.findById(anyLong());
    }

    @Test
    public void findByEmail_ExistedEmail_ReturnMember() {
        //given
        given(memberRepository.findByEmail(anyString())).willReturn(memberEntity);

        //when
        Member member = memberService.findByEmail(anyString());

        //then
        verify(memberRepository, atLeastOnce()).findByEmail(anyString());
        assertThat(member, equalTo(memberEntity));
        assertThatProperty(dto, memberEntity, member);
    }


    @Test(expected = MemberNotFoundException.class)
    public void findByEmail_NotExistedEmail_MemberNotFoundException() {
        //given
        given(memberRepository.findByEmail(anyString())).willReturn(null);

        //when
        memberService.findByEmail(anyString());
    }


    @Test
    public void update_Succeed_ReturnMember() {
        //given
        final MemberDto.MyAccountReq myAccountReq = buildMyAccountReq();
        given(memberRepository.findOne(anyLong())).willReturn(memberEntity);

        //when
        Member member = memberService.update(anyLong(), myAccountReq);

        //then
        verify(memberRepository, atLeastOnce()).findOne(anyLong());
        assertThat(member.getLastName(), is(myAccountReq.getLastName()));
        assertThat(member.getFirstName(), is(myAccountReq.getFirstName()));
        assertThat(member.getMobile(), is(myAccountReq.getMobile()));
        assertThat(member.getDob(), is(nullValue()));
    }

    @Test
    public void findAll() {
        //given
        final int endExclusive = 20;
        final List<Member> members = eachCreateMember(endExclusive);
        given(memberRepository.findAll(new PageRequest(0, endExclusive))).willReturn(new PageImpl<>(members));

        //when
        final PageImpl<MemberDto.Res> page = memberService.findAll(new PageRequest(0, endExclusive));
        final List<MemberDto.Res> content = page.getContent();

        //then
        assertThat(content, hasSize(endExclusive));
    }

    private List<Member> eachCreateMember(final int endExclusive) {

        final String EMAIL = "test@email.com";
        final String PASSWORD = "qwer1234";

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < endExclusive; i++) {
            final MemberDto.SignUpReq member = memberMock.setSignUpDto(i + EMAIL, PASSWORD, PASSWORD);
            given(memberRepository.save(any(Member.class))).willReturn(toEntity(member));
            members.add(memberService.create(member));
        }
        return members;
    }

    private MemberDto.MyAccountReq buildMyAccountReq() {
        return MemberDto.MyAccountReq.builder()
                .lastName("테")
                .firstName("스트")
                .mobile("01071333262")
                .build();
    }

    private Member toEntity(MemberDto.SignUpReq dto) {
        return dto.toEntity(passwordEncoder.encode("password001"), MemberRoleEnum.USER);
    }

    private MemberDto.SignUpReq buildSignUp() {
        return memberMock.setSignUpDto("cheese10yun@gmail.com", "password001", "password001");
    }

    private MemberDto.SignUpReq buildSignUp(String email) {
        return memberMock.setSignUpDto(email, "password001", "password001");
    }

    private void assertThatProperty(MemberDto.SignUpReq dto, Member memberEntity, Member member) {
        assertThat(member.getEmail(), is(dto.getEmail()));
        assertThat(member.getFirstName(), is(memberEntity.getFirstName()));
        assertThat(member.getLastName(), is(memberEntity.getLastName()));
        assertThat(member.getDob(), is(memberEntity.getDob()));
        assertThat(member.getMobile(), is(memberEntity.getMobile()));
        assertThat(member.getRole(), is(memberEntity.getRole()));
        assertThat(member.getCreatedAt(), is(memberEntity.getCreatedAt()));
        assertThat(member.getUpdatedAt(), is(memberEntity.getUpdatedAt()));
        assertThat(isMatchedPassword(dto.getPassword(), member.getPassword()), is(true));
    }

    private boolean isMatchedPassword(String rewPassword, String encodePassword) {
        return passwordEncoder.matches(rewPassword, encodePassword);
    }

}