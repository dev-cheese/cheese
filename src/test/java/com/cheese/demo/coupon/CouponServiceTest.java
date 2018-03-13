//package com.cheese.demo.coupon;
//
//import com.cheese.demo.discount.DiscountDto;
//import com.cheese.demo.member.MemberDto;
//import com.cheese.demo.member.MemberRoleEnum;
//import com.cheese.demo.mock.MemberMock;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Matchers;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.hamcrest.CoreMatchers.*;
//import static org.hamcrest.Matchers.greaterThan;
//import static org.junit.Assert.assertThat;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Matchers.any;
//import static org.mockito.Mockito.verify;
//
//@RunWith(MockitoJUnitRunner.class)
//public class CouponServiceTest {
//
//    private final String email = "cheese10yun@gmail.com";
//    private final String password = "password001";
//    @Mock
//    private CouponRepository couponRepository;
//
//    @InjectMocks
//    private CouponService couponService;
//
//    private MemberMock memberMock = new MemberMock();
//
//    @Test
//    public void create_AmountDiscountCoupon_ReturnCoupon() {
//        //given
//        final DiscountDto.Creation discountDto = buildAmountDiscountCreation();
//        final MemberDto.SignUpReq memberDto = buildSignUp();
//        final CouponDto.Creation couponDto = buildCouponCreation(discountDto, memberDto);
//        final Coupon couponEntity = couponDto.toEntity();
//        given(couponRepository.save(Matchers.any(Coupon.class))).willReturn(couponEntity);
//
//
//        //when
////        final Coupon coupon = couponService.create(couponDto, );
////
////        //then
////        verify(couponRepository, atLeastOnce()).save(any(Coupon.class));
////        assertThat(coupon, equalTo(couponEntity));
////        assertThatProperty(couponDto, coupon);
//    }
//
//    @Test
//    public void create_RateDiscountCoupon_ReturnCoupon() {
//        //given
//        final DiscountDto.Creation discountDto = buildRateDiscountCreation();
//        final MemberDto.SignUpReq memberDto = buildSignUp();
//        final CouponDto.Creation couponDto = buildCouponCreation(discountDto, memberDto);
//        final Coupon couponEntity = couponDto.toEntity();
//        given(couponRepository.save(Matchers.any(Coupon.class))).willReturn(couponEntity);
//
////        //when
////        final Coupon coupon = couponService.create(1L);
////
////        //then
////        verify(couponRepository, atLeastOnce()).save(any(Coupon.class));
////        assertThat(coupon, equalTo(couponEntity));
////        assertThatProperty(couponDto, coupon);
//    }
//
//    private void assertThatProperty(CouponDto.Creation couponDto, Coupon coupon) {
//        //테스트 커버리지를 위한 검사
//        assertThat(coupon.getDiscount(), is(couponDto.getDiscount()));
//        assertThat(coupon.isExpiration(), is(false));
//        assertThat(coupon.getId(), is(nullValue()));
//        assertThat(coupon.isUsed(), is(false));
//        assertThat(coupon.isUseAvailable(), is(true));
//        assertThat(coupon.getMember().getEmail(), is(email));
//        assertThat(couponDto.getMember().getEmail(), is(email));
//        assertThat(coupon.getExpirationDate().getTime(), greaterThan(System.currentTimeMillis()));
//
//    }
//
//    private CouponDto.Creation buildCouponCreation(DiscountDto.Creation discountDto, MemberDto.SignUpReq memberDto) {
//        return CouponDto.Creation.builder()
//                .discount(discountDto.toEntity())
//                .member(memberDto.toEntity(password, MemberRoleEnum.USER))
//                .build();
//    }
//
//    private MemberDto.SignUpReq buildSignUp() {
//        return memberMock.setSignUpDto(email, password, password);
//    }
//
//    private DiscountDto.Creation buildAmountDiscountCreation() {
//        return DiscountDto.Creation.builder()
//                .amount(10)
//                .description("10 discount amount coupon")
//                .expiration(604800)
//                .build();
//    }
//
//    private DiscountDto.Creation buildRateDiscountCreation() {
//        return DiscountDto.Creation.builder()
//                .rate(0.4)
//                .description("0.4 rate amount coupon")
//                .expiration(604800)
//                .build();
//    }
//}