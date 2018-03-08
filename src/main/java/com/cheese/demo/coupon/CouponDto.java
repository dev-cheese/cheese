package com.cheese.demo.coupon;

import com.cheese.demo.discount.Discount;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;


public class CouponDto {

    @Getter
    public static class Creation {
        private Discount discount;

        @Builder
        public Creation(Discount discount) {
            this.discount = discount;
        }

        public Coupon toEntity() {
            final Date expirationDate = calculateExpirationDate();

            return Coupon.builder()
                    .discount(discount)
                    .expirationDate(expirationDate)
                    .build();
        }

        private Date calculateExpirationDate() {
            return new Date(System.currentTimeMillis() + discount.getExpiration() * 1000);
        }
    }
}
