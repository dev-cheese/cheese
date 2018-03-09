package com.cheese.demo.member;

import com.cheese.demo.member.exception.EmailDuplicationException;
import com.cheese.demo.member.exception.MemberNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = SpringServerApplication.class)
//@Transactional
@RunWith(MockitoJUnitRunner.class)
public class MemberServiceImplTest {


    @Mock
    private MemberRepository memberRepository;


    private MemberServiceImpl memberService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() {
        memberService = new MemberServiceImpl(memberRepository, passwordEncoder);
    }

    @Test
    public void create_Succeed_ReturnMember() {
        //given
        final MemberDto.SignUpReq dto = buildSignUp();
        final Member memberEntity = toEntity(dto);
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
        final MemberDto.SignUpReq dto = buildSignUp();
        final Member memberEntity = toEntity(dto);
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
        MemberDto.SignUpReq dto = buildSignUp();
        final Member memberEntity = toEntity(dto);
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
        MemberDto.SignUpReq dto = buildSignUp();
        final Member memberEntity = toEntity(dto);
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
        MemberDto.SignUpReq dto = buildSignUp();
        final Member memberEntity = toEntity(dto);
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


    private Member toEntity(MemberDto.SignUpReq dto) {
        return dto.toEntity(passwordEncoder.encode("password001"), MemberRoleEnum.USER);
    }

    private MemberDto.SignUpReq buildSignUp() {
        return MemberDto.SignUpReq.builder()
                .email("cheese10yun@gmail.com")
                .password("password001")
                .rePassword("password001")
                .build();
    }

    private void assertThatProperty(MemberDto.SignUpReq dto, Member memberEntity, Member member) {
        assertThat(member.getId(), is(nullValue()));
        assertThat(member.getEmail(), is(dto.getEmail()));
        assertThat(member.getFirstName(), is(memberEntity.getFirstName()));
        assertThat(member.getLastName(), is(memberEntity.getLastName()));
        assertThat(member.getDob(), is(memberEntity.getDob()));
        assertThat(member.getMobile(), is(memberEntity.getMobile()));
        assertThat(member.getRole(), is(memberEntity.getRole()));
        assertThat(member.getCreatedDt(), is(memberEntity.getCreatedDt()));
        assertThat(member.getUpdatedDt(), is(memberEntity.getUpdatedDt()));
        assertThat(isMatchedPassword(dto.getPassword(), member.getPassword()), is(true));
    }

    private boolean isMatchedPassword(String rewPassword, String encodePassword) {
        return passwordEncoder.matches(rewPassword, encodePassword);
    }

    //    private final String EMAIL = "cheese10yun@gmail.com";
//    private final String PASSWORD = "password001";
//    private final String RE_PASSWORD = "password001";
//    @Autowired
//    private MemberServiceImpl memberService;
//    private MemberMock memberMock;
//
//    @Before
//    public void setUp() {
//        memberMock = new MemberMock();
//    }
//
//    @Test
//    public void When_emailIsExisted_expect_userReturn() {
//        memberService.create(memberMock.setSignUpDto(EMAIL, PASSWORD, RE_PASSWORD));
//        Member member = memberService.findByEmail(EMAIL);
//        assertThat(member, is(notNullValue()));
//        assertThat(member.getEmail(), is(EMAIL));
//    }
//
//    @Test(expected = MemberNotFoundException.class)
//    public void When_emailIsNotExisted_expect_UserNotFoundException() {
//        memberService.findByEmail("notExistedEmail@test.com");
//    }
//
//    @Test
//    public void name() {
//        final DiscountDto.Creation dto = DiscountDto.Creation.builder()
//                .description(null)
//                .amount(10)
//                .build();
//    }
}