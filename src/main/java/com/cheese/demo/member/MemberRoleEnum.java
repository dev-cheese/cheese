package com.cheese.demo.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRoleEnum {

    read("read");
//    ADMIN("ROLE_ADMIN");

    private String name;
}
