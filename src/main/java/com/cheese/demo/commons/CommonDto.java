package com.cheese.demo.commons;


import lombok.Getter;
import lombok.Setter;

public class CommonDto {

    @Getter
    @Setter
    public static class ExistenceRes {
        private boolean existence;
    }
}
