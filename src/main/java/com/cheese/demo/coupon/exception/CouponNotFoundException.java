package com.cheese.demo.coupon.exception;

import com.cheese.demo.commons.ErrorCode;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(Long id) {
        super(String.valueOf(ErrorCode.COUPON_NOT_FOUND));
        log.error(ErrorCode.COUPON_NOT_FOUND.getMessage(), id);
    }
}
