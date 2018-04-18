package com.cheese.demo.discount.exception;

import com.cheese.demo.commons.ErrorCode;
import com.cheese.demo.discount.DiscountIdEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiscountNotFoundException extends RuntimeException {

    public DiscountNotFoundException(DiscountIdEnum id) {
        super(String.valueOf(ErrorCode.DISCOUNT_NOT_FOUND));
        log.error(ErrorCode.DISCOUNT_NOT_FOUND.getMessage(), id);
    }
}
