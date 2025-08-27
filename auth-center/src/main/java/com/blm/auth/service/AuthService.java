package com.blm.auth.service;

import com.blm.auth.vo.LoginVO;
import com.blm.common.dto.UserLoginDTO;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    LoginVO login(UserLoginDTO dto);

    /**
     * 刷新Token
     */
    LoginVO refreshToken(String refreshToken);

    /**
     * 验证Token
     */
    String validateToken(String token);

    /**
     * 用户登出
     */
    void logout(String token);
}
