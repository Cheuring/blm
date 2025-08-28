package com.blm.common.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * 认证中心Feign客户端
 */
@FeignClient(name = "auth-center", path = "/internal/auth")
public interface AuthServiceClient {
}
