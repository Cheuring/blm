package com.blm.auth.controller;

import com.blm.auth.service.AuthService;
import com.blm.auth.vo.LoginVO;
import com.blm.common.dto.UserLoginDTO;
import com.blm.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户认证相关接口")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     */
    @Operation(summary = "用户登录", description = "用户登录获取访问令牌和刷新令牌")
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody UserLoginDTO dto) {
        log.info("User login attempt: {}", dto.getUsername());
        LoginVO loginVO = authService.login(dto);
        return Result.success("登录成功", loginVO);
    }

    /**
     * 刷新Token
     */
    @Operation(summary = "刷新Token", description = "使用刷新令牌获取新的访问令牌")
    @PostMapping("/refresh")
    public Result<LoginVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        log.info("Token refresh attempt");
        LoginVO loginVO = authService.refreshToken(refreshToken);
        return Result.success("Token刷新成功", loginVO);
    }

    /**
     * 验证Token
     */
    @Operation(summary = "验证Token", description = "验证访问令牌是否有效")
    @PostMapping("/validate")
    public Result<String> validateToken(@RequestParam("token") String token) {
        String userId = authService.validateToken(token);
        return Result.success("Token有效", userId);
    }

    /**
     * 用户登出
     */
    @Operation(summary = "用户登出", description = "注销用户令牌")
    @PostMapping("/logout")
    public Result<Void> logout(@RequestParam("token") String token) {
        log.info("User logout attempt");
        authService.logout(token);
        return Result.success(null);
    }
}
