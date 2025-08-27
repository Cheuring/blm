package com.blm.user.controller;

import com.blm.common.dto.UserRegisterDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.UserVO;
import com.blm.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户信息控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "新用户注册")
    @PostMapping("/register")
    public Result<UserVO> register(@Validated @RequestBody UserRegisterDTO dto) {
        log.info("User registration attempt: {}", dto.getUsername());
        UserVO userVO = userService.register(dto);
        return Result.success("注册成功", userVO);
    }

    /**
     * 根据用户ID获取用户信息
     */
    @Operation(summary = "根据ID获取用户信息", description = "根据用户ID获取用户详细信息")
    @GetMapping("/{userId}")
    public Result<UserVO> getUserById(@PathVariable("userId") Long userId) {
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    /**
     * 根据用户名获取用户信息
     */
    @Operation(summary = "根据用户名获取用户信息", description = "根据用户名获取用户详细信息")
    @GetMapping("/username/{username}")
    public Result<UserVO> getUserByUsername(@PathVariable("username") String username) {
        UserVO userVO = userService.getUserByUsername(username);
        return Result.success(userVO);
    }

    /**
     * 获取当前用户个人资料
     */
    @Operation(summary = "获取当前用户资料", description = "获取当前登录用户的详细信息")
    @GetMapping("/profile")
    public Result<UserVO> getProfile(@RequestHeader("X-User-Id") String userId) {
        UserVO userVO = userService.getUserById(Long.valueOf(userId));
        return Result.success(userVO);
    }

    /**
     * 更新当前用户个人资料
     */
    @Operation(summary = "更新用户资料", description = "更新当前登录用户的个人信息")
    @PutMapping("/profile")
    public Result<UserVO> updateProfile(@RequestHeader("X-User-Id") String userId,
                                        @Validated @RequestBody UserRegisterDTO dto) {
        UserVO userVO = userService.updateUser(Long.valueOf(userId), dto);
        return Result.success("更新成功", userVO);
    }
}
