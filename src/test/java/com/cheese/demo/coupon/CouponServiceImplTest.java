package com.cheese.demo.coupon;

import com.cheese.demo.SpringServerApplication;
import com.cheese.demo.discount.Discount;
import com.cheese.demo.discount.DiscountDto;
import com.cheese.demo.discount.DiscountServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringServerApplication.class)
@Transactional
public class CouponServiceImplTest {

    @Autowired
    private CouponServiceImpl couponService;

    @Autowired
    private DiscountServiceImpl discountService;
    private Discount discount;

    @Before
    public void setUp() {
        DiscountDto.Creation discountDto = DiscountDto.Creation.builder()
                .amount(10)
                .description("테스트")
                .expiration(604800)
                .build();

        discount = discountService.create(discountDto);
    }

    @Test
    public void create() {
        CouponDto.Creation dto = CouponDto.Creation.builder()
                .discount(discount)
                .build();

        Coupon coupon = couponService.create(dto);
        assertThat(coupon.getDiscount(), is(dto.getDiscount()));
        assertThat(coupon.isExpiration()).isFalse();
        assertThat(coupon.getDiscount(), equalTo(dto.getDiscount()));
        assertThat(coupon.getExpirationDate().after(Date.from(Instant.now())));

    }

}