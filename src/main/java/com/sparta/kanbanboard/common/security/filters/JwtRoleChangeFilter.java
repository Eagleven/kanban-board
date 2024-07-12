package com.sparta.kanbanboard.common.security.filters;

import static com.sparta.kanbanboard.common.ResponseCodeEnum.SUCCESS_SUBSCRIPTION;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.kanbanboard.common.HttpResponseDto;
import com.sparta.kanbanboard.common.security.AuthEnum;
import com.sparta.kanbanboard.common.security.config.TokenProvider;
import com.sparta.kanbanboard.domain.user.utils.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtRoleChangeFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(request.getMethod()) && "/users".equalsIgnoreCase(
                request.getRequestURI())) {
            final String authHeader = request.getHeader("AccessToken");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String jwt = authHeader.substring(7);
                Claims claims = Jwts.parserBuilder().setSigningKey(tokenProvider.getKey()).build()
                        .parseClaimsJws(jwt).getBody();

                String username = claims.getSubject();
                Role currentRole = Role.valueOf(claims.get("auth").toString());
                Role newRole = (currentRole.equals(Role.USER)) ? Role.MANAGER : Role.USER;

                try {
                    String newAccessToken = tokenProvider.reissueAccessToken(username, newRole);
                    String newRefreshToken = tokenProvider.reissueRefreshToken(username);

                    response.addHeader(AuthEnum.ACCESS_TOKEN.getValue(), newAccessToken);
                    response.addHeader(AuthEnum.REFRESH_TOKEN.getValue(), newRefreshToken);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                // 로그인 성공 메시지
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter()
                        .write(new ObjectMapper().writeValueAsString(new HttpResponseDto(
                                SUCCESS_SUBSCRIPTION.getHttpStatus().value(),
                                SUCCESS_SUBSCRIPTION.getMessage())));
                response.getWriter().flush();
            }
        }
        chain.doFilter(request, response);
    }
}
