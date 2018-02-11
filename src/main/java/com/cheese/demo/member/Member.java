package com.cheese.demo.member;

import com.cheese.demo.commons.EntityBaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Member extends EntityBaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

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

    @Column(name = "admin")
    private boolean admin;
}
