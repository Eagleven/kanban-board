package com.sparta.kanbanboard.common.security.filters;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_SUBSCRIPTION;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.NOT_FOUND_AUTHENTICATION_INFO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.common.security.AuthEnum;
import com.sparta.kanbanboard.common.security.config.TokenProvider;
import com.sparta.kanbanboard.common.security.details.UserDetailsServiceImpl;
import com.sparta.kanbanboard.domain.user.utils.Role;
import com.sparta.kanbanboard.exception.user.UserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String accessToken = tokenProvider.getAccessTokenFromHeader(request);

        if (StringUtils.hasText(accessToken)) {

            if (tokenProvider.validateToken(accessToken)) {
                validToken(accessToken);
            } else {
                invalidToken(request, response);
            }
        }

        filterChain.doFilter(request, response);
    }


    private void validToken(String token) throws UserException {
        Claims info = tokenProvider.getUserInfoFromToken(token);

        try {
            setAuthentication(info.getSubject());
        } catch (RuntimeException e) {
            log.error("username = {}, message = {}", info.getSubject(), "인증 정보를 찾을 수 없습니다.");
            throw new UserException(NOT_FOUND_AUTHENTICATION_INFO);
        }
    }

    private void invalidToken(HttpServletRequest request, HttpServletResponse response)
            throws IllegalAccessException {
        String refreshToken = tokenProvider.getRefreshTokenFromHeader(request);

        if (StringUtils.hasText(refreshToken)) {
            if (tokenProvider.validateToken(refreshToken) && tokenProvider.validateToken(refreshToken)) {
                Claims info = tokenProvider.getUserInfoFromToken(refreshToken);
                Role role = Role.valueOf(info.get("auth").toString());

                String newAccessToken = tokenProvider.reissueAccessToken(info.getSubject(), role);

                tokenProvider.setHeaderAccessToken(response, newAccessToken);

                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error("username = {}, message = {}", info.getSubject(), "인증 정보를 찾을 수 없습니다.");
                    throw new UserException(NOT_FOUND_AUTHENTICATION_INFO);
                }
            } else {
                throw new UserException(ResponseExceptionEnum.INVALID_REFRESHTOKEN);
            }
        }
    }

    // 인증 처리
    private void setAuthentication(String accountId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(accountId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String accountId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(accountId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}