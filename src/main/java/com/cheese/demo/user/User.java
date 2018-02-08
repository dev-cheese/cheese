package com.cheese.demo.user;

import com.cheese.demo.commons.EntityBaseDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class User extends EntityBaseDateTime {
    @Id
    @GeneratedValue
    private Long id;

    @Email
    @Column(name = "email", updatable = false, nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "비밀번호는 문자, 숫자 포함 8자리 이상")
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
}
