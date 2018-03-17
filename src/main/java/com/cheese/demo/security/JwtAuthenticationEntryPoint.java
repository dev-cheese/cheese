//package com.cheese.demo.security;
//
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
//        // This is invoked when user tries to access a secured REST resource without supplying any credentials
//        // We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
//        // 사용자가 자격 증명을 제공하지 않고 보안 REST 리소스에 액세스하려고 할 때 호출됩니다.        
//        // 리디렉션 할 '로그인 페이지'가 없기 때문에 401 Unauthorized 응답 만 보내야합니다.
//
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
////        throw new JwtAuthenticationException("Unauthorized");
//    }
//}
