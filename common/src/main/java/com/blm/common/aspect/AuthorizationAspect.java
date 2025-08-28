package com.blm.common.aspect;

import com.blm.common.annotation.RequireRole;
import com.blm.common.exception.BusinessException;
import com.blm.common.result.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 权限检查切面
 */
@Slf4j
@Aspect
@Component
public class AuthorizationAspect {

    @Around("@annotation(requireRole) || @within(requireRole)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {
        // 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new BusinessException(ExceptionConstant.REQ_UNAUTHORIZED.getCode(), "无法获取请求上下文");
        }

        HttpServletRequest request = attributes.getRequest();
        
        // 从请求头获取用户ID和角色信息
        String userId = request.getHeader("X-User-Id");
        String userRoles = request.getHeader("X-User-Roles");

        if (userId == null || userRoles == null) {
            throw new BusinessException(ExceptionConstant.REQ_UNAUTHORIZED.getCode(), "缺少用户认证信息");
        }

        // 检查角色权限
        List<String> currentRoles = Arrays.asList(userRoles.split(","));
        List<String> requiredRoles = Arrays.asList(requireRole.value());

        boolean hasPermission;
        if (requireRole.requireAll()) {
            // 需要拥有所有角色
            hasPermission = currentRoles.containsAll(requiredRoles);
        } else {
            // 拥有任一角色即可
            hasPermission = requiredRoles.stream().anyMatch(currentRoles::contains);
        }

        if (!hasPermission) {
            log.warn("用户 {} 尝试访问需要角色 {} 的资源，当前角色: {}", 
                    userId, requiredRoles, currentRoles);
            throw new BusinessException(ExceptionConstant.REQ_FORBIDDEN.getCode(), requireRole.message());
        }

        log.debug("用户 {} 权限检查通过，角色: {}", userId, currentRoles);
        return joinPoint.proceed();
    }
}
