package com.blm.common.feign;

import com.blm.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 认证中心Feign客户端
 */
@FeignClient(name = "auth-center", path = "/api/auth")
public interface AuthServiceClient {

    /**
     * 验证Token
     */
    @PostMapping("/validate")
    Result<String> validateToken(@RequestParam("token") String token);

    /**
     * 刷新Token
     */
    @PostMapping("/refresh")
    Result<String> refreshToken(@RequestParam("refreshToken") String refreshToken);

    /**
     * 注销Token
     */
    @PostMapping("/logout")
    Result<Void> logout(@RequestParam("token") String token);
}
