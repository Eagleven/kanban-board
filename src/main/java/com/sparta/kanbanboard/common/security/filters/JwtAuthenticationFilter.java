//package com.sparta.kanbanboard.common.security.filters;
//
//import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_LOGIN;
//import static com.sparta.kanbanboard.common.ResponseExceptionEnum.NOT_FOUND_AUTHENTICATION_INFO;
//import static com.sparta.kanbanboard.common.security.AuthEnum.ACCESS_TOKEN;
//import static com.sparta.kanbanboard.common.security.AuthEnum.REFRESH_TOKEN;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.kanbanboard.common.HttpResponseDto;
//import com.sparta.kanbanboard.common.security.config.TokenProvider;
//import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
//import com.sparta.kanbanboard.domain.user.dto.LoginRequestDto;
//import com.sparta.kanbanboard.domain.user.utils.Role;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
// *****임의 변형파일 ( 백단 테스트용 )*****
//@Slf4j
//@Order(1)
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final TokenProvider tokenProvider;
//    private final ObjectMapper objectMapper;
//
//    public JwtAuthenticationFilter(TokenProvider tokenProvider, ObjectMapper objectMapper) {
//        this.tokenProvider = tokenProvider;
//        this.objectMapper = objectMapper;
//        setFilterProcessesUrl("/users/login");
//    }
//
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request,
//            HttpServletResponse response) throws AuthenticationException {
//        try {
//            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
//                    LoginRequestDto.class);
//
//            return getAuthenticationManager().authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            requestDto.getUsername(),
//                            requestDto.getPassword(),
//                            null
//                    )
//            );
//        } catch (IOException e) {
//            log.error(e.getMessage());
//            throw new RuntimeException(e.getMessage());
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//            HttpServletResponse response, FilterChain chain,
//            Authentication authResult) throws IOException {
//        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
//        Role role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUserRole();
//
//        String accessToken = tokenProvider.createAccessToken(username, role);
//        String refreshToken = tokenProvider.createRefreshToken(username, role);
//
//        response.addHeader(ACCESS_TOKEN.getValue(), accessToken);
//        response.addHeader(REFRESH_TOKEN.getValue(), refreshToken);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        response.getWriter().write(objectMapper.writeValueAsString(new HttpResponseDto(
//                SUCCESS_LOGIN.getHttpStatus().value(), SUCCESS_LOGIN.getMessage())));
//
//        response.getWriter().flush();
//        response.getWriter().close();
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request,
//            HttpServletResponse response,
//            AuthenticationException failed) throws IOException {
//        log.error("Authentication failed: {}", failed.getMessage());
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        response.getWriter().write(objectMapper.writeValueAsString(new HttpResponseDto(
//                NOT_FOUND_AUTHENTICATION_INFO.getHttpStatus().value(),
//                NOT_FOUND_AUTHENTICATION_INFO.getMessage())));
//
//        response.getWriter().flush();
//        response.getWriter().close();
//    }
//}