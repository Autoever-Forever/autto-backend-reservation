package com.autto.autto_reservation.sercurity;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtTokenFilter extends OncePerRequestFilter{

    private final JwtTokenUtil jwtTokenUtil;

    public JwtTokenFilter(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/reservation/list") || path.startsWith("/api/v1/reservation/cancel")) {
            filterChain.doFilter(request, response); // permitAll 경로에서는 필터를 건너뛰고 계속 진행
            return;
        }

        String token = request.getHeader("Authorization");
        // Header의 Authorization의 값이 비어있거나 Bearer 로 시작하지 않으면 오류
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            return;
        }

        String jwtToken = token.replace("Bearer ", "");
        // 전송받은 Jwt Token이 만료되었으면 오류
        if (jwtTokenUtil.isExpired(jwtToken)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Token");
            return;
        }

        // Jwt Token에서 사용자 UUID 추출 - 현재 UUID로 변경되어야 함
        String userId = jwtTokenUtil.getLoginId(jwtToken);
        String userIdString = userId.substring(0, 8) + "-" +
                userId.substring(8, 12) + "-" +
                userId.substring(12, 16) + "-" +
                userId.substring(16, 20) + "-" +
                userId.substring(20);

        request.setAttribute("userId", userIdString);

        filterChain.doFilter(request, response);
    }

}