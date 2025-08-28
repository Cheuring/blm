package com.blm.gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * API 网关 Swagger 配置
 * 聚合各个微服务的 OpenAPI 文档
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public List<GroupedOpenApi> apis() {
        List<GroupedOpenApi> groups = new ArrayList<>();
        
        // 网关自身的 API 文档
        groups.add(GroupedOpenApi.builder()
                .group("API网关")
                .displayName("API 网关服务")
                .pathsToMatch("/gateway/**")
                .build());
        
        // 认证中心 API 文档
        groups.add(GroupedOpenApi.builder()
                .group("认证中心")
                .displayName("认证中心 API")
                .pathsToMatch("/api/auth/**")
                .build());
        
        // 用户服务 API 文档
        groups.add(GroupedOpenApi.builder()
                .group("用户服务")
                .displayName("用户服务 API")
                .pathsToMatch("/api/users/**", "/api/user/**")
                .build());
        
        // 订单服务 API 文档
        groups.add(GroupedOpenApi.builder()
                .group("订单服务")
                .displayName("订单服务 API")
                .pathsToMatch("/api/orders/**", "/api/order/**")
                .build());
        
        // 商店服务 API 文档
        groups.add(GroupedOpenApi.builder()
                .group("商店服务")
                .displayName("商店服务 API")
                .pathsToMatch("/api/stores/**", "/api/store/**")
                .build());
        
        // 骑手服务 API 文档
        groups.add(GroupedOpenApi.builder()
                .group("骑手服务")
                .displayName("骑手服务 API")
                .pathsToMatch("/api/riders/**", "/api/rider/**")
                .build());
        
        return groups;
    }
}
