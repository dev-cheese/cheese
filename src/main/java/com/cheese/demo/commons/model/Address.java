package com.cheese.demo.commons.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Builder
    public Address(String zipCode, String address1, String address2) {
        this.zipCode = zipCode;
        this.address1 = address1;
        this.address2 = address2;
    }
}
