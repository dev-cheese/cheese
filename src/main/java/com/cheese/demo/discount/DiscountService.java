package com.cheese.demo.discount;

public interface DiscountService {
    Discount create(DiscountDto.Creation dto);
}