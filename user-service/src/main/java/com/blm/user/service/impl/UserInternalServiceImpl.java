package com.blm.user.service.impl;

import com.blm.common.entity.User;
import com.blm.common.entity.UserAddress;
import com.blm.common.service.BaseService;
import com.blm.common.vo.UserVO;
import com.blm.common.vo.PageVO;
import com.blm.user.repository.UserAddressRepository;
import com.blm.user.repository.UserRepository;
import com.blm.user.service.UserInternalService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserInternalServiceImpl extends BaseService implements UserInternalService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAddressRepository userAddressRepository;

    @Override
    public UserAddress findAddressByUserIdAndId(Long userId, Long id) {
        return userAddressRepository.findByIdAndUserId(id, userId).orElse(null);
    }

    @Override
    public UserAddress findAddressById(Long id) {
        return userAddressRepository.findById(id).orElse(null);
    }

    @Override
    public int updateRole(Long userId, String role) {
        return userRepository.updateRole(userId, role, LocalDateTime.now());
    }

    @Override
    public int updateStatus(Long userId, Integer status) {
        return userRepository.updateStatus(userId, status, LocalDateTime.now());
    }

    @Override
    public PageVO<UserVO> getByConditions(User.UserRole role, Integer status, String keyword, int page, int size) {
//         这里只做简单实现，实际应结合分页插件

        // 使用PageHelper进行分页
        PageHelper.startPage(page, size);

        // 根据条件查询用户列表
        List<User> users = userRepository.findByConditions(role, status, keyword);

        // 获取PageHelper分页信息并自动关闭资源
        Page<User> pageInfo = (Page<User>) users;
        // 转换为VO列表
        List<UserVO> userVOs = entity2VO(users, UserVO.class);

        // 构造分页结果
        return new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), userVOs);
    }

    @Override
    public Long countUser(User.UserRole role, LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null) {
            return userRepository.countByCreatedAtBetween(start, end);
        } else if (role != null) {
            return userRepository.countByRole(role);
        } else {
            return userRepository.count();
        }
    }
}
