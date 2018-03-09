package com.cheese.demo.coupon;

import com.cheese.demo.discount.DiscountDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    @Test
    public void create_AmountDiscountCoupon_ReturnCoupon() {
        //given
        final DiscountDto.Creation discountDto = buildAmountDiscountCreation();
        final CouponDto.Creation couponDto = buildCouponCreation(discountDto);
        final Coupon couponEntity = couponDto.toEntity();
        given(couponRepository.save(Matchers.any(Coupon.class))).willReturn(couponEntity);

        //when
        final Coupon coupon = couponService.create(couponDto);

        //then
        verify(couponRepository, atLeastOnce()).save(any(Coupon.class));
        assertThat(coupon, equalTo(couponEntity));
        assertThatProperty(couponDto, coupon);
    }

    @Test
    public void create_RateDiscountCoupon_ReturnCoupon() {
        //given
        final DiscountDto.Creation discountDto = buildRateDiscountCreation();
        final CouponDto.Creation couponDto = buildCouponCreation(discountDto);
        final Coupon couponEntity = couponDto.toEntity();
        given(couponRepository.save(Matchers.any(Coupon.class))).willReturn(couponEntity);

        //when
        final Coupon coupon = couponService.create(couponDto);

        //then
        verify(couponRepository, atLeastOnce()).save(any(Coupon.class));
        assertThat(coupon, equalTo(couponEntity));
        assertThatProperty(couponDto, coupon);
    }

    private void assertThatProperty(CouponDto.Creation couponDto, Coupon coupon) {
        //테스트 커버리지를 위한 검사
        assertThat(coupon.getDiscount(), is(couponDto.getDiscount()));
        assertThat(coupon.isExpiration(), is(false));
        assertThat(coupon.getId(), is(nullValue()));
        assertThat(coupon.getExpirationDate().getTime(), greaterThan(System.currentTimeMillis()));
    }

    private CouponDto.Creation buildCouponCreation(DiscountDto.Creation discountDto) {
        return CouponDto.Creation.builder()
                .discount(discountDto.toEntity())
                .build();
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