package com.cheese.demo.coupon;

import com.cheese.demo.discount.Discount;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "coupon")
@Getter
public class Coupon {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Transient
    private boolean expiration;

    @ManyToOne(optional = false)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;


    @Builder
    public Coupon(Date expirationDate, Discount discount) {
        this.expirationDate = expirationDate;
        this.discount = discount;
    }

    public boolean isExpiration() {
        return System.currentTimeMillis() > expirationDate.getTime();
    }


}
