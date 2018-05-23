package com.cheese.demo.config.oauth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;

public class OauthUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;
    private final Timestamp lastPasswordResetDate;

    @Builder
    public OauthUser(
            Long id,
            String username,
            String firstName,
            String lastName,
            String email,
            String password,
            Collection<? extends GrantedAuthority> authorities,
            boolean enabled,
            Timestamp lastPasswordResetDate
    ) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

//    @JsonIgnore
    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

//    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

//    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

//    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @JsonIgnore
    public Date getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
