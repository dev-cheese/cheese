package com.cheese.demo.discount.exception;

import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.discount.DiscountIdEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundDiscountException extends RuntimeException {

    public NotFoundDiscountException(DiscountIdEnum id) {
        super(String.valueOf(ErrorCodeEnum.DISCOUNT_NOT_FOUND));
        log.error(ErrorCodeEnum.DISCOUNT_NOT_FOUND.getMessage(), id);
    }
}
