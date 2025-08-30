package com.blm.user.service;

import com.blm.common.entity.User;
import com.blm.common.entity.UserAddress;
import com.blm.common.vo.UserVO;
import com.blm.common.vo.PageVO;

import java.time.LocalDateTime;

public interface UserInternalService {
    // 贴合UserInternal控制器的接口定义
   
    UserAddress findAddressByUserIdAndId(Long userId, Long id);

    UserAddress findAddressById(Long id);

    int updateRole(Long userId, User.UserRole role);

    int updateStatus(Long userId, Integer status);

    PageVO<UserVO> getByConditions(User.UserRole role, Integer status, String keyword, int page, int size);
    
    Long countUser(User.UserRole role, LocalDateTime start, LocalDateTime end);

    
}
