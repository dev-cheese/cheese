package com.cheese.demo.discount;

import lombok.Builder;
import lombok.Getter;

public class DiscountDto {

    @Getter
    public static class Creation {

        private String description;
        private int amount;
        private double rate;
        private long expiration;

        @Builder
        public Creation(String description, int amount, double rate, long expiration) {
            this.description = description;
            this.amount = amount;
            this.rate = rate;
            this.expiration = expiration;
        }

        public Discount toEntity() {
            return Discount.builder()
                    .description(description)
                    .amount(amount)
                    .rate(rate)
                    .expiration(expiration)
                    .build();
        }
    }
}