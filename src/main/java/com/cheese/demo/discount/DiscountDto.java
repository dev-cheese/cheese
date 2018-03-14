package com.cheese.demo.discount;

import lombok.Builder;
import lombok.Getter;

public class DiscountDto {

    @Getter
    public static class Creation {

        private DiscountIdEnum id;
        private String description;
        private int amount;
        private double rate;
        private long expiration;

        @Builder
        public Creation(DiscountIdEnum id, String description, int amount, double rate, long expiration) {
            this.id = id;
            this.description = description;
            this.amount = amount;
            this.rate = rate;
            this.expiration = expiration;
        }

        public Discount toEntity() {
            return Discount.builder()
                    .id(id)
                    .description(description)
                    .amount(amount)
                    .rate(rate)
                    .expiration(expiration)
                    .build();
        }
    }
}