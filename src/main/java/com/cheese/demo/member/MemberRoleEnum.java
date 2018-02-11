package com.cheese.demo.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRoleEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String name;
}
