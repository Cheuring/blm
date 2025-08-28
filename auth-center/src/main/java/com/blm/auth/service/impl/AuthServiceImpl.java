package com.blm.auth.service.impl;

import com.blm.auth.service.AuthService;
import com.blm.common.dto.LoginDTO;
import com.blm.common.dto.RegisterDTO;
import com.blm.common.entity.User;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.util.JwtUtil;
import com.blm.common.vo.LoginVO;
import com.blm.common.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public LoginVO login(LoginDTO dto) {
        // 调用用户服务验证用户
        User user = userServiceClient.getUserByUsername(dto.getUsername())
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));
        // 检查用户状态
        if (user.getStatus() == User.INACTIVE) {
            throw new CommonException(ExceptionConstant.USER_ACCOUNT_DISABLED);
        }

        // check password
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CommonException(ExceptionConstant.USER_INVALID_CREDENTIALS);
        }

        // 生成Token
        String userId = user.getId().toString();
        String token = jwtUtil.generateToken(userId);

        // 构造返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        loginVO.setUserInfo(userVO);

        log.info("User {} login successfully", dto.getUsername());
        return loginVO;
    }

    @Override
    public UserVO register(RegisterDTO dto) {
        return userServiceClient.register(dto)
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_ALREADY_EXISTS));
    }

}

