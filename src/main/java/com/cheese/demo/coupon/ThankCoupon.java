package com.cheese.demo.coupon;

import com.cheese.demo.discount.DiscountService;
import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberServiceImpl;
import org.springframework.stereotype.Component;


/**
 * 발급 조건이 없는 쿠폰
 */
@Component
public class ThankCoupon extends CouponIssueManager {

    public ThankCoupon(CouponRepository couponRepository, MemberServiceImpl memberService, DiscountService discountService) {
        super(couponRepository, memberService, discountService);
    }

    @Override
    public boolean canIssued(Member member) {
        return true;
    }
}
