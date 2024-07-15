package com.sparta.kanbanboard.common.security.filters;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_LOGOUT;
import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_TO_SINGOUT;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.INVALID_REFRESHTOKEN;
import static com.sparta.kanbanboard.common.ResponseExceptionEnum.NOT_FOUND_AUTHENTICATION_INFO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.security.config.TokenProvider;
import com.sparta.kanbanboard.common.security.config.TokenService;
import com.sparta.kanbanboard.common.security.details.UserDetailsServiceImpl;
import com.sparta.kanbanboard.domain.user.utils.Role;
import com.sparta.kanbanboard.exception.user.UserException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
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
    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    private final List<String> WHITE_LIST = List.of(
            "/trello",
            "/trello/signupPage",
            "/static",
            "/image",
            "/css",
            "/js"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        log.info("Requested URI: {}", requestUri);

        // 보호된 경로에 대한 요청 확인
        if (!isProtectedPath(requestUri, method) || requestUri.equals("/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // AccessToken 가져오기
        String accessToken = tokenProvider.getAccessTokenFromHeader(request);

        try {
            // AccessToken 검증
            if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
                processValidToken(accessToken, request, response, filterChain);
            } else {
                // 유효하지 않은 토큰 처리
                processInvalidToken(request, response);
            }
        } catch (ExpiredJwtException e) {
            // AccessToken이 만료된 경우
            log.info("Access token expired. Handling expired access token.");
            processInvalidToken(request, response);
        } catch (JwtException | IllegalArgumentException e) {
            // 다른 JWT 예외 처리
            log.error("JWT validation failed: {}", e.getMessage());
            setErrorResponse(response);
        }
    }

    // 유효한 Access Token 처리
    private void handleValidAccessToken(String accessToken) {
        // 액세스 토큰에서 클레임(사용자 정보)을 추출
        Claims accessTokenClaims = tokenProvider.getUserInfoFromToken(accessToken);
        String username = accessTokenClaims.getSubject();

        // 사용자 인증 설정
        setAuthentication(username);
    }


    // isProtectedPath 메서드 구현 업데이트
    private boolean isProtectedPath(String requestUri, String method) {
        return WHITE_LIST.stream().noneMatch(requestUri::startsWith) &&
                !("/users".equals(requestUri) && "POST".equalsIgnoreCase(method));
    }

    // 유효한 토큰 처리
    private void processValidToken(String token, HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException {
        Claims info = tokenProvider.getUserInfoFromToken(token);

        try {
            setAuthentication(info.getSubject());
            handleLogoutIfNeeded(request, response, info.getSubject());
            filterChain.doFilter(request, response);
        } catch (RuntimeException | ServletException e) {
            log.error("username = {}, message = {}", info.getSubject(), "인증 정보를 찾을 수 없습니다.");
            throw new UserException(NOT_FOUND_AUTHENTICATION_INFO);
        }
    }

    // 유효하지 않은 토큰 처리
    private void processInvalidToken(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String refreshToken = tokenProvider.getRefreshTokenFromHeader(request);

        if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
            reissueAccessToken(request, response, refreshToken);
        } else {
            throw new UserException(INVALID_REFRESHTOKEN);
        }
    }

    // 새로운 액세스 토큰 발급
    private void reissueAccessToken(HttpServletRequest request, HttpServletResponse response,
            String refreshToken)
            throws IOException {

        Claims info = tokenProvider.getUserInfoFromToken(refreshToken);
        Role role = Role.valueOf(info.get("auth").toString());

        log.info("새로운 토큰 발급 중 ~");
        String refreshTokenFromRedis = tokenService.getRefreshToken(info.getSubject());

        if (!Objects.equals(refreshToken, refreshTokenFromRedis)) {
            throw new UserException(INVALID_REFRESHTOKEN);
        }

        String newAccessToken = tokenProvider.createAccessToken(info.getSubject(), role);
        tokenProvider.setHeaderAccessToken(response, newAccessToken);

        try {
            setAuthentication(info.getSubject());
        } catch (Exception e) {
            log.error("username = {}, message = {}", info.getSubject(), "인증 정보를 찾을 수 없습니다.");
            throw new UserException(NOT_FOUND_AUTHENTICATION_INFO);
        }
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
    }

    // 인증 처리
    private void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // 로그아웃 필요 시 처리
    private void handleLogoutIfNeeded(HttpServletRequest request, HttpServletResponse response,
            String username)
            throws IOException {
        String requestUri = request.getRequestURI();
        if ("/users/logout".equals(requestUri) || ("/users".equals(requestUri)
                && "PATCH".equalsIgnoreCase(request.getMethod()))) {
            handleLogout(request, response, username);
        }
    }

    // 로그아웃 처리
    private void handleLogout(HttpServletRequest request, HttpServletResponse response,
            String username)
            throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            invalidateTokens(request, username);
        }

        new SecurityContextLogoutHandler().logout(request, response, authentication);
        writeLogoutResponse(request, response);
        log.info("User logged out successfully");
    }

    // 토큰 무효화
    private void invalidateTokens(HttpServletRequest request, String username) {
        String accessToken = request.getHeader("AccessToken");
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            String token = accessToken.substring(7);
            tokenProvider.invalidateTokens(username, token);
        }
    }

    // 로그아웃 및 회원 탈퇴 응답 작성
    private void writeLogoutResponse(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if ("/users".equals(request.getRequestURI())) {
            response.getWriter().write(objectMapper.writeValueAsString(new HttpResponseDto(
                    SUCCESS_TO_SINGOUT.getHttpStatus().value(), SUCCESS_TO_SINGOUT.getMessage())));
        } else {
            response.getWriter().write(objectMapper.writeValueAsString(new HttpResponseDto(
                    SUCCESS_LOGOUT.getHttpStatus().value(), SUCCESS_LOGOUT.getMessage())));
        }

        response.getWriter().flush();
        response.getWriter().close();
    }

    private void setErrorResponse(HttpServletResponse res) throws IOException {
        res.setStatus(INVALID_REFRESHTOKEN.getHttpStatus().value());
        res.setContentType("application/json;charset=UTF-8");
        HttpResponseDto responseDto = new HttpResponseDto(
                INVALID_REFRESHTOKEN.getHttpStatus().value(),
                INVALID_REFRESHTOKEN.getMessage()
        );
        res.getWriter().write(objectMapper.writeValueAsString(responseDto));
    }
}
