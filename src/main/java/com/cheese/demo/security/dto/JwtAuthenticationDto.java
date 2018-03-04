package com.cheese.demo.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

public class JwtAuthenticationDto implements Serializable {

    private static final long serialVersionUID = -8445943548965154778L;

    @Getter
    @NoArgsConstructor
    public static class Request {
        private String email;
        private String password;

        @Builder
        public Request(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    public static class Response {
        private String token;

        public Response(String token) {
            this.token = token;
        }
    }

}
