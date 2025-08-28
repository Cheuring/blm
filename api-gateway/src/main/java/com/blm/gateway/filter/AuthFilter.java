package com.blm.gateway.filter;

import com.blm.common.entity.User;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * 认证过滤器
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private JwtUtil jwtUtil;

//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;

    @Autowired @Lazy
    private UserServiceClient userServiceClient;

    private static final String USER_ROLES_CACHE_PREFIX = "gateway:user:roles:";

    /**
     * 白名单路径，不需要认证
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/auth/login",
            "/api/auth/register",
//            "/api/auth/refresh",
            "/v3/api-docs",
            "/swagger-ui"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 检查是否在白名单中
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 获取Token
        String token = getTokenFromRequest(request);
        if (!StringUtils.hasText(token)) {
            return unauthorized(exchange.getResponse(), "缺少认证Token");
        }

        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            return unauthorized(exchange.getResponse(), "Token无效或已过期");
        }

        // 获取用户ID并添加到请求头
        String userId = jwtUtil.getUserIdFromToken(token);
        if (!StringUtils.hasText(userId)) {
            return unauthorized(exchange.getResponse(), "无法解析用户信息");
        }

        // 这里需要调用用户服务获取用户角色信息
        // 为了简化，暂时从Token中解析或使用默认值
        // 在实际项目中，可以考虑在JWT中包含角色信息或调用用户服务
        String userRoles = getUserRoles(userId); // 需要实现此方法

        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", userId)
                .header("X-User-Roles", userRoles)
                .build();
        exchange = exchange.mutate().request(mutatedRequest).build();

        return chain.filter(exchange);
    }

    /**
     * 检查是否在白名单中
     */
    private boolean isWhiteList(String path) {
        return WHITE_LIST.stream().anyMatch(path::startsWith);
    }

    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        log.warn("Authentication failed: {}", message);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        String body = "{\"code\":401,\"message\":\"" + message + "\",\"timestamp\":" + System.currentTimeMillis() + "}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    /**
     * 获取用户角色信息
     * 优先从Redis缓存获取，缓存未命中则返回默认角色
     */
    private String getUserRoles(String userId) {
        try {
            // 从Redis缓存获取用户角色信息
//            String cacheKey = USER_ROLES_CACHE_PREFIX + userId;
//            String roles = redisTemplate.opsForValue().get(cacheKey);
//
//            if (StringUtils.hasText(roles)) {
//                return roles;
//            }

            // 缓存未命中，返回默认角色
            // 在实际项目中，这里应该调用用户服务获取角色信息
            User user = userServiceClient.getUserById(Long.valueOf(userId))
                    .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));
            String userRoles = user.getRole();

            // 缓存用户角色信息，设置5分钟过期
//            redisTemplate.opsForValue().set(cacheKey, userRoles, Duration.ofMinutes(5));

            return userRoles;
        } catch (Exception e) {
            log.warn("Failed to get user roles for userId: {}, error: {}", userId, e.getMessage());
            return "USER"; // 默认角色
        }
    }

    @Override
    public int getOrder() {
        return -100; // 确保认证过滤器优先执行
    }
}
