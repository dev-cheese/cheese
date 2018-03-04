package com.cheese.demo.security;

import com.cheese.demo.commons.ErrorCodeEnum;
import com.cheese.demo.commons.ErrorResponse;
import com.cheese.demo.security.exception.JwtTokenMalformedException;
import com.cheese.demo.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {


    private final Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String requestHeader = request.getHeader(this.tokenHeader);

        try {
            JwtAuthTokenDto jwtAuthTokenDto = new JwtAuthTokenDto(requestHeader).invoke();
            logger.info("checking authentication for user " + jwtAuthTokenDto.getEmail());
            setAuthentication(request, jwtAuthTokenDto);
            chain.doFilter(request, response);
        } catch (JwtTokenMalformedException e) {
            doResponseError(response, getErrorResponse(getFieldErrors(e), ErrorCodeEnum.UNAUTHORIZED));
        } catch (ExpiredJwtException e) {
            doResponseError(response, getErrorResponse(getFieldErrors(e), ErrorCodeEnum.EXPIRATION_TOKEN));
        }
    }

    private ErrorResponse getErrorResponse(List<ErrorResponse.FieldError> fieldErrors, ErrorCodeEnum expirationToken) {
        List<ErrorResponse.FieldError> error = fieldErrors;
        return buildErrorResponse(expirationToken, error);
    }

    private void setAuthentication(HttpServletRequest request, JwtAuthTokenDto jwtAuthTokenDto) {
        final String email = jwtAuthTokenDto.getEmail();
        final String authToken = jwtAuthTokenDto.getAuthToken();
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // It is not compelling necessary to load the use details from the database. You could also store the information
            // in the token and read it from it. It's up to you ;)
            // 데이터베이스에서 사용 세부 정보를로드 할 필요가 없습니다. 정보를 저장할 수도 있습니다.
            // 토큰에 넣고 읽습니다. 그것은 당신에게 달렸습니다;)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);


            // For simple validation it is completely sufficient to just check the token integrity.
            // You don't have to call the database compellingly. Again it's up to you ;)
            // 단순한 검증을 위해서 토큰 무결성을 검사하는 것만으로도 충분하다.
            // 당신은 데이터베이스를 강력하게 호출 할 필요가 없습니다. 다시 그것은 당신에게 달렸습니다;)

            if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info("authenticated user " + email + ", setting security context");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }

    private List<ErrorResponse.FieldError> getFieldErrors(RuntimeException e) {
        List<ErrorResponse.FieldError> error = new ArrayList<>();
        ErrorResponse.FieldError fieldError = new ErrorResponse.FieldError();

        fieldError.setMessage(e.getMessage());
        fieldError.setInValidValue(e.getLocalizedMessage());
        error.add(fieldError);
        return error;
    }

    private void doResponseError(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }

    private ErrorResponse buildErrorResponse(ErrorCodeEnum errorCode, List<ErrorResponse.FieldError> errors) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .errors(errors)
                .build();
    }

    private class JwtAuthTokenDto {
        private String requestHeader;
        private String email;
        private String authToken;

        public JwtAuthTokenDto(String requestHeader) {
            this.requestHeader = requestHeader;
            this.email = email;
            this.authToken = authToken;
        }

        public String getEmail() {
            return email;
        }

        public String getAuthToken() {
            return authToken;
        }

        public JwtAuthTokenDto invoke() {
            if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
                authToken = requestHeader.substring(7);
                try {
                    email = jwtTokenUtil.getUsernameFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    logger.error("an error occured during getting username from token", e);
                } catch (ExpiredJwtException e) {
                    logger.warn("the token is expired and not valid anymore", e);
                } catch (MalformedJwtException e) {
                    throw new JwtTokenMalformedException("111");
                }
            } else {
                logger.warn("couldn't find bearer string, will ignore the header");
            }
            return this;
        }
    }
}
