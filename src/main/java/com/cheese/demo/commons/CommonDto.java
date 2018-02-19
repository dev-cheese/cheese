package com.cheese.demo.commons;


import lombok.AllArgsConstructor;
import lombok.Getter;

public class CommonDto {

    @Getter
    @AllArgsConstructor
    public static class ExistenceRes {
        private boolean existence;
    }
}
