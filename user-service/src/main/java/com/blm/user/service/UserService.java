package com.blm.user.service;

import com.blm.common.dto.PasswordUpdateDTO;
import com.blm.common.dto.RegisterDTO;
import com.blm.common.dto.UserProfileUpdateDTO;
import com.blm.common.entity.User;
import com.blm.common.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    UserVO register(RegisterDTO registerDTO);

    UserVO getUserProfile(Long userId);

    UserVO updateUserProfile(Long userId, UserProfileUpdateDTO profileUpdateDTO);

    User updateUserPassword(Long userId, PasswordUpdateDTO passwordUpdateDTO);

    User findUserById(Long userId); // Helper method to get User entity

    User findUserByUsername(String username);
}
