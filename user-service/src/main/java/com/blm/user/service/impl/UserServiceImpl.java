package com.blm.user.service.impl;

import com.blm.common.dto.UserRegisterDTO;
import com.blm.common.entity.User;
import com.blm.common.exception.BusinessException;
import com.blm.common.result.ResultCode;
import com.blm.common.vo.UserVO;
import com.blm.user.repository.UserRepository;
import com.blm.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserVO register(UserRegisterDTO dto) {
        // 检查用户名是否已存在
        if (existsByUsername(dto.getUsername())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "用户名已存在");
        }

        // 检查邮箱是否已存在
        if (dto.getEmail() != null && existsByEmail(dto.getEmail())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "邮箱已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setRole("USER");
        user.setStatus(User.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        log.info("User registered successfully: {}", dto.getUsername());
        return userVO;
    }

    @Override
    public UserVO getUserById(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    @Transactional
    public UserVO updateUser(Long userId, UserRegisterDTO dto) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // 检查用户名是否已被其他用户使用
        if (!user.getUsername().equals(dto.getUsername()) && existsByUsername(dto.getUsername())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "用户名已存在");
        }

        // 检查邮箱是否已被其他用户使用
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail()) && existsByEmail(dto.getEmail())) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXISTS, "邮箱已存在");
        }

        // 更新用户信息
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.update(user);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        log.info("User updated successfully: {}", userId);
        return userVO;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email) != null;
    }
}
