package com.cheese.demo.coupon;

import com.cheese.demo.discount.DiscountDto;
import com.cheese.demo.discount.DiscountIdEnum;
import com.cheese.demo.discount.DiscountService;
import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberDto;
import com.cheese.demo.member.MemberRoleEnum;
import com.cheese.demo.member.MemberServiceImpl;
import com.cheese.demo.mock.MemberMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class ThankCouponTest {

    private final String email = "cheese10yun@gmail.com";
    private final String password = "password001";

    private MemberMock memberMock = new MemberMock();

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private MemberServiceImpl memberService;

    @Mock
    private DiscountService discountService;

    @InjectMocks
    private ThankCoupon thankCoupon;

    @Test
    public void issue() {
        //given
        final DiscountDto.Creation discountDto = buildAmountDiscount();
        final MemberDto.SignUpReq memberDto = buildSignUp(email);
        final CouponDto.Creation couponDto = buildCouponCreation(discountDto, memberDto);
        final Coupon couponEntity = couponDto.toEntity();
        final Member member = toMemberEntity(memberDto);

        given(memberService.findById(anyLong())).willReturn(member);
        given(discountService.findById(DiscountIdEnum.T001)).willReturn(discountDto.toEntity());
        given(couponRepository.save(Matchers.any(Coupon.class))).willReturn(couponEntity);

        //when
        thankCoupon.issue(member.getId(), DiscountIdEnum.T001);


    }

    @Test
    public void canIssued() {
        final MemberDto.SignUpReq memberDto = buildSignUp("admin001@test.com");
        //given
        Member member = toMemberEntity(memberDto);

        //when
        boolean canIssued = thankCoupon.canIssued(member);

        //then
        assertThat(canIssued, is(true));
    }


    private CouponDto.Creation buildCouponCreation(DiscountDto.Creation discountDto, MemberDto.SignUpReq memberDto) {
        return CouponDto.Creation.builder()
                .discount(discountDto.toEntity())
                .member(toMemberEntity(memberDto))
                .build();
    }

    private MemberDto.SignUpReq buildSignUp(String email) {
        return memberMock.setSignUpDto(email, password, password);
    }

    private DiscountDto.Creation buildAmountDiscount() {
        return DiscountDto.Creation.builder()
                .id(DiscountIdEnum.T001)
                .rate(0.4)
                .description("0.4 rate amount coupon")
                .expiration(604800)
                .build();
    }

    private Member toMemberEntity(MemberDto.SignUpReq memberDto) {
        return memberDto.toEntity(password, MemberRoleEnum.USER);
    }
}