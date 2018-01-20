package com.cheese.common.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
public abstract class BaseDateTime {

    @CreatedDate
    private LocalDateTime createdDt;

    @LastModifiedDate
    private LocalDateTime updatedDt;
}
