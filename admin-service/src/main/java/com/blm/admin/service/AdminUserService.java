package com.blm.admin.service;

import com.blm.common.vo.UserVO;

import java.util.List;

public interface AdminUserService {
    List<UserVO> listUsers();
    void updateStatus(Long userId, Integer status);
}