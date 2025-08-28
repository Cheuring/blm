package com.blm.auth.controller;

import com.blm.auth.service.AuthService;
import com.blm.common.dto.LoginDTO;
import com.blm.common.dto.RegisterDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.LoginVO;
import com.blm.common.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Operation(summary = "用户登录", description = "使用用户名和密码进行登录，成功后返回Token和用户信息")
    @ApiResponse(responseCode = "200", description = "登录成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginVO.class)))
    @PostMapping("/login")
    public Result<LoginVO> login(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用户登录凭证", required = true, content = @Content(schema = @Schema(implementation = LoginDTO.class))) @org.springframework.web.bind.annotation.RequestBody LoginDTO dto) {
        LoginVO vo = authService.login(dto);
        return Result.success(vo);
    }

    @Operation(summary = "用户注册", description = "根据提供的用户信息创建新用户账号")
    @ApiResponse(responseCode = "200", description = "注册成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserVO.class)))
    @PostMapping("/register")
    public Result<UserVO> register(@RequestBody(description = "用户注册信息", required = true, content = @Content(schema = @Schema(implementation = RegisterDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody RegisterDTO dto) {
        UserVO userVO = authService.register(dto);
        return Result.success(userVO);
    }

    @Operation(summary = "用户登出", description = "用户登出系统 (对于无状态JWT，主要由客户端清除Token)")
    @ApiResponse(responseCode = "200", description = "登出成功")
    @GetMapping("/logout")
    public Result<Void> logout() {
        // In a stateless JWT setup, server-side logout might be minimal.
        // Invalidate token if using a blacklist/refresh token strategy.
        return Result.success(null);
    }
}
