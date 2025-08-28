package com.blm.user.service.impl;


import com.blm.common.dto.PasswordUpdateDTO;
import com.blm.common.dto.RegisterDTO;
import com.blm.common.dto.UserProfileUpdateDTO;
import com.blm.common.entity.User;
import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.vo.UserVO;
import com.blm.user.repository.UserRepository;
import com.blm.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserVO register(RegisterDTO registerDTO) {
        // Check if username or phone already exists
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new CommonException(ExceptionConstant.USER_ALREADY_EXISTS);
        }
//        if (userRepository.existsByPhone(registerDTO.getPhone())) {
//            throw new CommonException(ExceptionConstant.USER_PHONE_ALREADY_REGISTERED);
//        }

        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setRole(registerDTO.getRole() == null ? User.UserRole.USER.toString() : registerDTO.getRole().toString());
        user.setStatus(User.ACTIVE); // Default status active
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public UserVO getUserProfile(Long userId) {
        User user = findUserById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    @Transactional
    public UserVO updateUserProfile(Long userId, UserProfileUpdateDTO profileUpdateDTO) {
        User user = findUserById(userId);

        // Check if new username conflicts with others
        if (StringUtils.hasText(profileUpdateDTO.getUsername()) && !user.getUsername().equals(profileUpdateDTO.getUsername())) {
            if (userRepository.existsByUsername(profileUpdateDTO.getUsername())) {
                throw new CommonException(ExceptionConstant.USER_ALREADY_EXISTS);
            }
            user.setUsername(profileUpdateDTO.getUsername());
        }

        if (StringUtils.hasText(profileUpdateDTO.getEmail())) {
            user.setEmail(profileUpdateDTO.getEmail());
        }
        if (StringUtils.hasText(profileUpdateDTO.getAvatar())) {
            user.setAvatar(profileUpdateDTO.getAvatar());
        }
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    @Transactional
    public User updateUserPassword(Long userId, PasswordUpdateDTO passwordUpdateDTO) {
        User user = findUserById(userId);

        // Verify old password
        if (!passwordEncoder.matches(passwordUpdateDTO.getOldPassword(), user.getPassword())) {
            throw new CommonException(ExceptionConstant.USER_INVALID_CREDENTIALS);
        }

        // Update with new password
        LocalDateTime now = LocalDateTime.now();
        String newEncodedPassword = passwordEncoder.encode(passwordUpdateDTO.getNewPassword());
        user.setPassword(newEncodedPassword);
        userRepository.updatePassword(userId, newEncodedPassword, now);
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));
    }
}