package com.sparta.kanbanboard.common.security.filters;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_LOGOUT;
import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_TO_SINGOUT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.security.config.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenInvalidationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        if ("/users/logout".equals(path) || "/users/signout".equals(path)) {
            String accessToken = tokenProvider.getAccessTokenFromHeader(request);
            String refreshToken = tokenProvider.getRefreshTokenFromHeader(request);

            if (accessToken != null) {
                String username = tokenProvider.getUserInfoFromToken(accessToken).getSubject();
                accessToken = tokenProvider.invalidateAccessTokens(username, accessToken);
                response.setHeader("AccessToken", null); // 응답 헤더에서 액세스 토큰 제거
            }

            if (refreshToken != null) {
                String username = tokenProvider.getUserInfoFromToken(refreshToken).getSubject();
                refreshToken = tokenProvider.invalidateRefreshTokens(username, refreshToken);
                response.setHeader("RefreshToken", null); // 응답 헤더에서 리프레시 토큰 제거
                // HTTP 응답 헤더에 토큰을 null로 설정
                response.setStatus(HttpStatus.NO_CONTENT.value());
            }

            SecurityContextHolder.clearContext(); // Security Context 초기화
            writeLogoutResponse(request, response, path);
            return; // 필터 체인 진행 중단
        }

        filterChain.doFilter(request, response);
    }

    // 로그아웃 및 회원 탈퇴 응답 작성
    private void writeLogoutResponse(HttpServletRequest request, HttpServletResponse response, String path)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if ("/users/signout".equals(path)) {
            response.getWriter().write(objectMapper.writeValueAsString(new HttpResponseDto(
                    SUCCESS_TO_SINGOUT.getHttpStatus().value(), SUCCESS_TO_SINGOUT.getMessage())));
        } else {
            response.getWriter().write(objectMapper.writeValueAsString(new HttpResponseDto(
                    SUCCESS_LOGOUT.getHttpStatus().value(), SUCCESS_LOGOUT.getMessage())));
        }

        response.getWriter().flush();
        response.getWriter().close();
    }
}
