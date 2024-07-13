package com.sparta.kanbanboard.domain.userandboard.repository.filters;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_LOGOUT;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.NOT_FOUND_AUTHENTICATION_INFO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.ResponseExceptionEnum;
import com.sparta.kanbanboard.common.security.config.TokenProvider;
import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.common.security.details.UserDetailsServiceImpl;
import com.sparta.kanbanboard.domain.user.User;
import com.sparta.kanbanboard.domain.user.utils.Role;
import com.sparta.kanbanboard.exception.user.UserException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) {
        if (!(request.getRequestURI().equals("/users") && request.getMethod().equals("POST"))) {
            String accessToken = tokenProvider.getAccessTokenFromHeader(request);
            String requestUri = request.getRequestURI();

            if (StringUtils.hasText(accessToken)) {
                if (tokenProvider.validateToken(accessToken)) {
                    validToken(accessToken);
                    if ("/users/logout".equals(requestUri)) {
                        handleLogout(request, response);
                        return;
                    }
                } else {
                    invalidToken(request, response);
                }
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
            if (tokenProvider.validateToken(refreshToken) && tokenProvider.validateToken(
                    refreshToken)) {
                Claims info = tokenProvider.getUserInfoFromToken(refreshToken);
                Role role = Role.valueOf(info.get("auth").toString());

                String newAccessToken = tokenProvider.reissueAccessToken(info.getSubject(), role);

                tokenProvider.setHeaderAccessToken(response, newAccessToken);

                try {
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error("username = {}, message = {}", info.getSubject(),
                            "인증 정보를 찾을 수 없습니다.");
                    throw new UserException(NOT_FOUND_AUTHENTICATION_INFO);
                }
            } else {
                throw new UserException(ResponseExceptionEnum.INVALID_REFRESHTOKEN);
            }
        }
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
    }

    // 인증 처리
    private void setAuthentication(String accountId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(accountId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) {
        try {
            processLogout(request, response);
        } catch (IOException e) {
            log.error("Logout failed", e);
        }
    }

    private void processLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetails.getUser();

            String accessToken = request.getHeader("Authorization");
            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                String token = accessToken.substring(7);
                tokenProvider.invalidateTokens(user.getUsername(), token);
            }
        }

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new HttpResponseDto(
                SUCCESS_LOGOUT.getHttpStatus().value(), SUCCESS_LOGOUT.getMessage())));
        response.getWriter().flush();
        response.getWriter().close();

        // Log logout action
        log.info("User logged out successfully");
    }
}