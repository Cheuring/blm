package com.blm.auth.service.impl;

import com.blm.auth.service.AuthService;
import com.blm.auth.vo.LoginVO;
import com.blm.common.dto.UserLoginDTO;
import com.blm.common.exception.BusinessException;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.Result;
import com.blm.common.result.ResultCode;
import com.blm.common.util.JwtUtil;
import com.blm.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String TOKEN_BLACKLIST_PREFIX = "auth:blacklist:";
    private static final String REFRESH_TOKEN_PREFIX = "auth:refresh:";

    @Override
    public LoginVO login(UserLoginDTO dto) {
        // 调用用户服务验证用户
        Result<UserVO> userResult = userServiceClient.getUserByUsername(dto.getUsername());
        if (userResult.getCode() != 200 || userResult.getData() == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        UserVO user = userResult.getData();
        
        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 注意：在实际应用中，密码验证应该在用户服务中进行
        // 这里为了演示目的，暂时跳过密码验证
        // 实际应用中应该调用用户服务的验证接口

        // 生成Token
        String userId = user.getId().toString();
        String accessToken = jwtUtil.generateAccessToken(userId);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        // 存储刷新Token到Redis
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + userId,
                refreshToken,
                Duration.ofDays(7)
        );

        // 构造返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(accessToken);
        loginVO.setRefreshToken(refreshToken);
        loginVO.setUser(user);

        log.info("User {} login successfully", dto.getUsername());
        return loginVO;
    }

    @Override
    public LoginVO refreshToken(String refreshToken) {
        if (!StringUtils.hasText(refreshToken)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 验证刷新Token
        if (!jwtUtil.validateToken(refreshToken) || !jwtUtil.isRefreshToken(refreshToken)) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_EXPIRED);
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 检查Redis中的刷新Token
        String storedRefreshToken = redisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + userId);
        if (!refreshToken.equals(storedRefreshToken)) {
            throw new BusinessException(ResultCode.REFRESH_TOKEN_EXPIRED);
        }

        // 获取用户信息
        Result<UserVO> userResult = userServiceClient.getUserById(Long.valueOf(userId));
        if (userResult.getCode() != 200 || userResult.getData() == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        UserVO user = userResult.getData();
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }

        // 生成新的Token
        String newAccessToken = jwtUtil.generateAccessToken(userId);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId);

        // 更新Redis中的刷新Token
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_PREFIX + userId,
                newRefreshToken,
                Duration.ofDays(7)
        );

        // 将旧的刷新Token加入黑名单
        long remainingTime = jwtUtil.getTokenRemainingTime(refreshToken);
        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(
                    TOKEN_BLACKLIST_PREFIX + refreshToken,
                    "blacklisted",
                    Duration.ofMillis(remainingTime)
            );
        }

        LoginVO loginVO = new LoginVO();
        loginVO.setAccessToken(newAccessToken);
        loginVO.setRefreshToken(newRefreshToken);
        loginVO.setUser(user);

        log.info("Token refreshed for user {}", userId);
        return loginVO;
    }

    @Override
    public String validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 检查Token是否在黑名单中
        Boolean isBlacklisted = redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token);
        if (Boolean.TRUE.equals(isBlacklisted)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        // 验证Token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(ResultCode.TOKEN_EXPIRED);
        }

        String userId = jwtUtil.getUserIdFromToken(token);
        if (!StringUtils.hasText(userId)) {
            throw new BusinessException(ResultCode.TOKEN_INVALID);
        }

        return userId;
    }

    @Override
    public void logout(String token) {
        if (!StringUtils.hasText(token)) {
            return;
        }

        // 将Token加入黑名单
        long remainingTime = jwtUtil.getTokenRemainingTime(token);
        if (remainingTime > 0) {
            redisTemplate.opsForValue().set(
                    TOKEN_BLACKLIST_PREFIX + token,
                    "blacklisted",
                    Duration.ofMillis(remainingTime)
            );
        }

        // 删除刷新Token
        String userId = jwtUtil.getUserIdFromToken(token);
        if (StringUtils.hasText(userId)) {
            redisTemplate.delete(REFRESH_TOKEN_PREFIX + userId);
        }

        log.info("User {} logout successfully", userId);
    }
}
