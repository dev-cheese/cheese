package com.cheese.demo.coupon;


import com.cheese.demo.discount.DiscountService;
import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberServiceImpl;
import org.springframework.stereotype.Component;

@Component
public class InviteeCouponService extends CouponServiceAbs {

    public InviteeCouponService(MemberServiceImpl memberService, DiscountService discountService, CouponRepository couponRepository) {
        super(couponRepository, memberService, discountService);
    }

    //임시 로직
    @Override
    public boolean canIssued(Member member) {
        return member.getEmail().startsWith("ch");
    }
}
