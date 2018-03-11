package com.cheese.demo.coupon;

import com.cheese.demo.discount.Discount;
import com.cheese.demo.member.Member;
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
    private Long id;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @Lob
    @Column(name = "used", nullable = false)
    private boolean used;

    @Transient
    private boolean expiration;

    @Transient
    private boolean useAvailable;

    @ManyToOne(optional = false)
    @JoinColumn(name = "discount_id", nullable = false, updatable = false)
    private Discount discount;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    private Member member;


    @Builder
    public Coupon(Date expirationDate, Discount discount, Member member, boolean used) {
        this.expirationDate = expirationDate;
        this.discount = discount;
        this.member = member;
        this.used = used;
    }

    public boolean isExpiration() {
        return System.currentTimeMillis() > expirationDate.getTime();
    }

    public boolean isUseAvailable() {
        return !isExpiration() && !isUsed();
    }


}
