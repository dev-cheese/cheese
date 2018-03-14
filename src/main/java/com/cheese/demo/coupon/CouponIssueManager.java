package com.cheese.demo.coupon;

import com.cheese.demo.discount.Discount;
import com.cheese.demo.discount.DiscountIdEnum;
import com.cheese.demo.discount.DiscountService;
import com.cheese.demo.member.Member;
import com.cheese.demo.member.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public abstract class CouponIssueManager {

    protected final MemberServiceImpl memberService;
    private final DiscountService discountService;
    private final CouponRepository couponRepository;

    @Autowired
    protected CouponIssueManager(CouponRepository couponRepository, MemberServiceImpl memberService, DiscountService discountService) {
        this.couponRepository = couponRepository;
        this.memberService = memberService;
        this.discountService = discountService;
    }

    @Transactional
    public void create(Member member, Discount discount) {
        if (canIssued(member))
            couponRepository.save(buildCreationCoupon(member, discount).toEntity());
    }

    protected void issue(long memberId, DiscountIdEnum id) {
        Member member = memberService.findById(memberId);
        Discount discount = discountService.findById(id);

        if (canIssued(member))
            create(member, discount);

    }

    private CouponDto.Creation buildCreationCoupon(Member member, Discount discount) {
        return CouponDto.Creation.builder()
                .member(member)
                .discount(discount)
                .build();
    }

    abstract protected boolean canIssued(Member member);
}