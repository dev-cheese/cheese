package com.cheese.demo.coupon.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(Long id) {
        super(String.valueOf(ErrorCodeEnum.COUPON_NOT_FOUND));
        log.error(ErrorCodeEnum.COUPON_NOT_FOUND.getMessage(), id);
    }
}
