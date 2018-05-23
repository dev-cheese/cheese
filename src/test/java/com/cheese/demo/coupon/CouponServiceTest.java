package com.cheese.demo.coupon;

import com.cheese.demo.coupon.exception.CouponNotFoundException;
import com.cheese.demo.discount.Discount;
import com.cheese.demo.discount.DiscountDto;
import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberDto;
import com.cheese.demo.member.MemberRoleEnum;
import com.cheese.demo.mock.MemberMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyLong;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceTest {

    private final String email = "cheese10yun@gmail.com";
    private final String password = "password001";
    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    private MemberMock memberMock = new MemberMock();

    private Discount amountDiscount;
    private Discount rateDiscount;
    private Member member;
    private CouponDto.Creation amountCoupon;
    private CouponDto.Creation discountCoupon;


    @Before
    public void setUp() {
        amountDiscount = buildAmountDiscountCreation().toEntity();
        rateDiscount = buildRateDiscountCreation().toEntity();
        member = toEntityMember(buildSignUp());
        amountCoupon = buildCouponCreation(buildAmountDiscountCreation(), buildSignUp());
        discountCoupon = buildCouponCreation(buildRateDiscountCreation(), buildSignUp());
    }

    @Test
    public void findById_Existed_ReturnCoupon() {
        //given
        given(couponRepository.findOne(anyLong())).willReturn(amountCoupon.toEntity());

        //when
        Coupon coupon = couponService.findById(anyLong());

        //then
        assertThatProperty(amountCoupon, coupon);
    }

    @Test(expected = CouponNotFoundException.class)
    public void findById_NotExisted_NotFoundCouponException() {
        //given
        given(couponRepository.findOne(anyLong())).willReturn(null);

        //when
        couponService.findById(anyLong());
    }

    @Test
    public void doUse_Succeed_UsedIsTrue() {
        //given
        final Coupon coupon = amountCoupon.toEntity();
        given(couponRepository.findOne(anyLong())).willReturn(coupon);

        //when
        coupon.doUse();

        //then
        assertThat(coupon.isUsed(), is(true));
        assertThat(coupon.isUseAvailable(), is(false));
    }

    @Test
    public void cancelUse_Succeed_UsedIsFalse() {
        //given
        final Coupon coupon = amountCoupon.toEntity();
        given(couponRepository.findOne(anyLong())).willReturn(coupon);

        //when
        coupon.cancelUse();

        //then
        assertThat(coupon.isUsed(), is(false));
        assertThat(coupon.isUseAvailable(), is(true));

    }

    private Member toEntityMember(MemberDto.SignUpReq signUpReq) {
        return signUpReq.toEntity(password, MemberRoleEnum.USER);
    }

    private void assertThatProperty(CouponDto.Creation couponDto, Coupon coupon) {
        //테스트 커버리지를 위한 검사
        assertThat(coupon.getDiscount(), is(couponDto.getDiscount()));
        assertThat(coupon.getCode(), is(notNullValue()));
        assertThat(coupon.isExpiration(), is(false));
        assertThat(coupon.getId(), is(nullValue()));
        assertThat(coupon.isUsed(), is(false));
        assertThat(coupon.isUseAvailable(), is(true));
        assertThat(coupon.getMember().getEmail(), is(email));
        assertThat(couponDto.getMember().getEmail(), is(email));
        assertThat(coupon.getExpirationDate().getTime(), greaterThan(System.currentTimeMillis()));
    }

    private CouponDto.Creation buildCouponCreation(DiscountDto.Creation discountDto, MemberDto.SignUpReq memberDto) {
        return CouponDto.Creation.builder()
                .discount(discountDto.toEntity())
                .member(toEntityMember(memberDto))
                .build();
    }

    private MemberDto.SignUpReq buildSignUp() {
        return memberMock.setSignUpDto(email, password, password);
    }

    private DiscountDto.Creation buildAmountDiscountCreation() {
        return DiscountDto.Creation.builder()
                .amount(10)
                .description("10 discount amount coupon")
                .expiration(604800)
                .build();
    }

    private DiscountDto.Creation buildRateDiscountCreation() {
        return DiscountDto.Creation.builder()
                .rate(0.4)
                .description("0.4 rate amount coupon")
                .expiration(604800)
                .build();
    }
}