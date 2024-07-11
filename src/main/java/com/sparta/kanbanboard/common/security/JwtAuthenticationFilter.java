package com.sparta.kanbanboard.common.security;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_LOGIN;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.NOT_FOUND_AUTHENTICATION_INFO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.dto.LoginRequestDto;
import com.sparta.kanbanboard.domain.user.utils.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        setFilterProcessesUrl("/users/login");
    }


    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        UserDetailsImpl userdetails = (UserDetailsImpl) authResult.getPrincipal();
        User user = userdetails.user();
        String username = user.getUsername();
        Role role = user.getUserRole();

        String accessToken = tokenProvider.createAccessToken(username, role);
        String refreshToken = tokenProvider.createRefreshToken(username, role);

        response.addHeader(AuthEnum.ACCESS_TOKEN.getValue(), accessToken);
        response.addHeader(AuthEnum.REFRESH_TOKEN.getValue(), refreshToken);

        // 로그인 성공 메시지
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(new HttpResponseDto(SUCCESS_LOGIN.getHttpStatus().value(), SUCCESS_LOGIN.getMessage())));
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException {
        log.error("Authentication failed: {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(
                new HttpResponseDto(NOT_FOUND_AUTHENTICATION_INFO.getHttpStatus().value(), NOT_FOUND_AUTHENTICATION_INFO.getMessage())));
    }
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) req;
//        HttpServletResponse response = (HttpServletResponse) res;
//
//        String accessToken = tokenProvider.extractToken(
//                request.getHeader(AuthEnum.ACCESS_TOKEN.getValue()));
//        String refreshToken = tokenProvider.extractToken(
//                request.getHeader(AuthEnum.REFRESH_TOKEN.getValue()));
//
//        try {
//            if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
//                setSecurityContext(accessToken);
//            } else if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(
//                    refreshToken)) {
//                accessToken = processRefreshToken(refreshToken, response);
//                setSecurityContext(accessToken);
//            }
//        } catch (ExpiredJwtException | UnsupportedJwtException | IllegalAccessException e) {
//            request.setAttribute("exception", e);
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    private void setSecurityContext(String token) {
//        Claims claims = tokenProvider.getUserInfoFromToken(token);
//        String username = claims.getSubject();
//        Role role = Role.valueOf(claims.get("auth", String.class));
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                username, null, List.of(new SimpleGrantedAuthority(role.name())));
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//    }
//
//    private String processRefreshToken(String refreshToken, HttpServletResponse response)
//            throws IllegalAccessException {
//        String username = tokenProvider.getUserInfoFromToken(refreshToken).getSubject();
//        Role role = Role.valueOf(
//                tokenProvider.getUserInfoFromToken(refreshToken).get("auth", String.class));
//        String newAccessToken = tokenProvider.reissueAccessToken(username, role);
//        response.addHeader(AuthEnum.ACCESS_TOKEN.getValue(), newAccessToken);
//        return newAccessToken;
//    }
}
