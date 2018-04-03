package com.cheese.demo.coupon;

import com.cheese.demo.coupon.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    @Autowired
    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }


    @Transactional(readOnly = true)
    public Coupon findById(long id) {
        final Coupon coupon = couponRepository.findOne(id);
        if (coupon == null)
            throw new CouponNotFoundException(id);
        return coupon;
    }

}