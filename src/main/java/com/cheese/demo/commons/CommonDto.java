package com.cheese.demo.commons;


import lombok.Builder;
import lombok.Getter;

public class CommonDto {

    @Getter
    public static class ExistenceRes {
        private boolean existence;

        @Builder
        public ExistenceRes(boolean existence) {
            this.existence = existence;
        }
    }
}
