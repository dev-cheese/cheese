package com.cheese.user.domain;

import com.cheese.common.domain.BaseDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseDateTime {

    @Id
    @GeneratedValue
    private Long id;

    // TODO: 2018. 1. 20. 라스트 네임 퍼스트 네임 분리할것 -yun
    // TODO: 2018. 1. 20. 이메일 관리하는 클래스 분리할것 -yun
    // TODO: 2018. 1. 20. ?? -yun
}
