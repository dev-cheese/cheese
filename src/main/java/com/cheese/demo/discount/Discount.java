package com.cheese.demo.discount;

import com.cheese.demo.coupon.Coupon;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount")
@Getter
public class Discount {

    @Id
    @GeneratedValue
    private DiscountIdEnum id;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "amount")
    private int amount;

    @Column(name = "rate")
    private double rate;

    @Column(name = "expiration", nullable = false)
    private long expiration;

    @Column(name = "created_dt", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdDt;

    @Column(name = "updated_dt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedDt;

    @OneToMany(mappedBy = "discount")
    private List<Coupon> coupons = new ArrayList<>();

    @Builder
    public Discount(String description, int amount, double rate, long expiration, DiscountIdEnum id) {
        this.description = description;
        this.amount = amount;
        this.rate = rate;
        this.expiration = expiration;
        this.id = id;
    }
}