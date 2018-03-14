package com.cheese.demo.coupon;

import com.cheese.demo.discount.Discount;
import com.cheese.demo.member.Member;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;


public class CouponDto {

    @Getter
    public static class Creation {
        private Discount discount;
        private Member member;

        @Builder
        public Creation(Discount discount, Member member) {
            this.discount = discount;
            this.member = member;
        }

        public Coupon toEntity() {
            final Date expirationDate = calculateExpirationDate();

            return Coupon.builder()
                    .discount(this.discount)
                    .member(this.member)
                    .code(makeCode())
                    .expirationDate(expirationDate)
                    .build();
        }

        private Date calculateExpirationDate() {
            return new Date(System.currentTimeMillis() + this.discount.getExpiration() * 1000);
        }

        private String makeCode() {
            return "쿠폰만들자";
        }
    }
}
