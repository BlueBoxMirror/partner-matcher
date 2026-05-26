package com.partner.partnermatch.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.partner.partnermatch.annotation.LoginRequired;
import com.partner.partnermatch.common.Result;
import com.partner.partnermatch.context.UserContext;
import com.partner.partnermatch.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        boolean loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class) != null ||
                handlerMethod.getBeanType().getAnnotation(LoginRequired.class) != null;

        if (!loginRequired) {
            return true;   // 不需要登录的接口直接放行
        }


        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return sendUnauthorized(response, "未提供认证令牌");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return sendUnauthorized(response, "令牌无效或已过期");
        }


        Long userId = jwtUtil.getUserIdFromToken(token);
        UserContext.setUserId(userId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {

        UserContext.clear();
    }

    private boolean sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(HttpStatus.UNAUTHORIZED.value(), message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
        return false;
    }
}