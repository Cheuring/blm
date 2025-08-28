package com.blm.auth.service;


import com.blm.common.dto.LoginDTO;
import com.blm.common.dto.RegisterDTO;
import com.blm.common.vo.LoginVO;
import com.blm.common.vo.UserVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    LoginVO login(LoginDTO dto);

    UserVO register(RegisterDTO dto);
}
