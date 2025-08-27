package com.blm.user.service;

import com.blm.common.dto.UserRegisterDTO;
import com.blm.common.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    UserVO register(UserRegisterDTO dto);

    /**
     * 根据用户ID获取用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 根据用户名获取用户信息
     */
    UserVO getUserByUsername(String username);

    /**
     * 更新用户信息
     */
    UserVO updateUser(Long userId, UserRegisterDTO dto);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);
}
