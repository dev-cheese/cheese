package com.cheese.demo.user;

import com.cheese.demo.commons.EntityBaseDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends EntityBaseDateTime {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "email", updatable = false, nullable = false, unique = true)
    private String email;

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

    @Builder
    public User(Long id, String email, String password, String lastName, String firstName, String mobile, Date dob) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.mobile = mobile;
        this.dob = dob;
    }
}
