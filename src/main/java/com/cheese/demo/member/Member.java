package com.cheese.demo.member;

import com.cheese.demo.commons.EntityBaseDateTime;
import com.cheese.demo.commons.model.Address;
import com.cheese.demo.coupon.Coupon;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends EntityBaseDateTime {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Email
    @Column(name = "email", updatable = false, nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "dob", columnDefinition = "DATE")
    private Date dob;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(20) default 'USER'")
    private MemberRoleEnum role;

    @OneToMany(mappedBy = "member")
    private List<Coupon> coupons = new ArrayList<>();

    @Builder
    public Member(long id, String email, String password, String lastName, String firstName, String mobile, Date dob, MemberRoleEnum role, Address address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.mobile = mobile;
        this.dob = dob;
        this.role = role;
        this.address = address;
    }

    public void update(MemberDto.MyAccountReq dto) {
        this.lastName = dto.getLastName();
        this.firstName = dto.getFirstName();
        this.mobile = dto.getMobile();
        this.dob = dto.getDob();

    }

}
