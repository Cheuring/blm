package com.blm.auth.service.impl;

import com.blm.auth.service.AuthService;
import com.blm.common.dto.LoginDTO;
import com.blm.common.dto.RegisterDTO;
import com.blm.common.vo.LoginVO;
import com.blm.common.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试
 * 测试用户服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }


    // ==================== register() 方法测试 ====================

    /**
     * 正向测试：成功注册用户
     */
    @Test
    void testRegister_Success() {
        // 准备测试数据
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("test@example.com");
        registerDTO.setPhone("13800138001");
        
        UserVO expectedUserVO = new UserVO();
        expectedUserVO.setId(1L);
        expectedUserVO.setUsername("testuser");
        expectedUserVO.setEmail("test@example.com");
        expectedUserVO.setPhone("13800138001");
        
        // 模拟服务行为
        when(authService.register(registerDTO)).thenReturn(expectedUserVO);
        
        // 执行测试
        UserVO actualUserVO = authService.register(registerDTO);
        
        // 验证结果
        assertNotNull(actualUserVO, "注册结果不应为null");
        assertEquals(1L, actualUserVO.getId(), "用户ID应该匹配");
        assertEquals("testuser", actualUserVO.getUsername(), "用户名应该匹配");
        assertEquals("test@example.com", actualUserVO.getEmail(), "邮箱应该匹配");
        assertEquals("13800138001", actualUserVO.getPhone(), "电话应该匹配");
        
        // 验证方法被调用
        verify(authService, times(1)).register(registerDTO);
    }

    /**
     * 反向测试：注册DTO为null
     */
    @Test
    void testRegister_NullRegisterDTO() {
        // 模拟服务抛出异常
        when(authService.register(null)).thenThrow(new IllegalArgumentException("注册信息不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.register(null);
        });
        
        assertEquals("注册信息不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(authService, times(1)).register(null);
    }

    /**
     * 反向测试：用户名已存在
     */
    @Test
    void testRegister_UsernameExists() {
        // 准备测试数据
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("existinguser");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("test@example.com");
        
        // 模拟服务抛出异常
        when(authService.register(registerDTO)).thenThrow(new RuntimeException("用户名已存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerDTO);
        });
        
        assertEquals("用户名已存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(authService, times(1)).register(registerDTO);
    }

    /**
     * 反向测试：邮箱已存在
     */
    @Test
    void testRegister_EmailExists() {
        // 准备测试数据
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newuser");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("existing@example.com");
        
        // 模拟服务抛出异常
        when(authService.register(registerDTO)).thenThrow(new RuntimeException("邮箱已存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(registerDTO);
        });
        
        assertEquals("邮箱已存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(authService, times(1)).register(registerDTO);
    }

    // ==================== getUserDetails() 方法测试 ====================

    // ==================== login() 方法测试 ====================

    /**
     * 正向测试：成功登录
     */
    @Test
    void testLogin_Success() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("password123");

        UserVO userVO = new UserVO();
        userVO.setId(1L);
        userVO.setUsername("testuser");
        userVO.setEmail("test@example.com");
        userVO.setPhone("13800138001");

        LoginVO expectedLoginVO = new LoginVO();
        expectedLoginVO.setToken("jwt.token.here");
        expectedLoginVO.setUserInfo(userVO);

        // 模拟服务行为
        when(authService.login(loginDTO)).thenReturn(expectedLoginVO);

        // 执行测试
        LoginVO actualLoginVO = authService.login(loginDTO);

        // 验证结果
        assertNotNull(actualLoginVO, "登录结果不应为null");
        assertNotNull(actualLoginVO.getToken(), "token不应为null");
        assertEquals("jwt.token.here", actualLoginVO.getToken(), "token应该匹配");
        assertNotNull(actualLoginVO.getUserInfo(), "用户信息不应为null");
        assertEquals(1L, actualLoginVO.getUserInfo().getId(), "用户ID应该匹配");
        assertEquals("testuser", actualLoginVO.getUserInfo().getUsername(), "用户名应该匹配");
        assertEquals("test@example.com", actualLoginVO.getUserInfo().getEmail(), "邮箱应该匹配");
        assertEquals("13800138001", actualLoginVO.getUserInfo().getPhone(), "电话应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

    /**
     * 反向测试：登录DTO为null
     */
    @Test
    void testLogin_NullLoginDTO() {
        // 模拟服务抛出异常
        when(authService.login(null)).thenThrow(new IllegalArgumentException("登录信息不能为null"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(null);
        });

        assertEquals("登录信息不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(null);
    }

    /**
     * 反向测试：用户名不存在
     */
    @Test
    void testLogin_UserNotFound() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("nonexistentuser");
        loginDTO.setPassword("password123");

        // 模拟服务抛出异常
        when(authService.login(loginDTO)).thenThrow(new RuntimeException("用户不存在"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("用户不存在", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

    /**
     * 反向测试：密码错误
     */
    @Test
    void testLogin_InvalidPassword() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("wrongpassword");

        // 模拟服务抛出异常
        when(authService.login(loginDTO)).thenThrow(new RuntimeException("密码错误"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("密码错误", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

    /**
     * 反向测试：用户账户被禁用
     */
    @Test
    void testLogin_UserAccountDisabled() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("disableduser");
        loginDTO.setPassword("password123");

        // 模拟服务抛出异常
        when(authService.login(loginDTO)).thenThrow(new RuntimeException("用户账户已被禁用"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("用户账户已被禁用", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

    /**
     * 反向测试：用户名为空字符串
     */
    @Test
    void testLogin_EmptyUsername() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("");
        loginDTO.setPassword("password123");

        // 模拟服务抛出异常
        when(authService.login(loginDTO)).thenThrow(new IllegalArgumentException("用户名不能为空"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("用户名不能为空", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

    /**
     * 反向测试：密码为空字符串
     */
    @Test
    void testLogin_EmptyPassword() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword("");

        // 模拟服务抛出异常
        when(authService.login(loginDTO)).thenThrow(new IllegalArgumentException("密码不能为空"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("密码不能为空", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

    /**
     * 反向测试：用户名为null
     */
    @Test
    void testLogin_NullUsername() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername(null);
        loginDTO.setPassword("password123");

        // 模拟服务抛出异常
        when(authService.login(loginDTO)).thenThrow(new IllegalArgumentException("用户名不能为null"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("用户名不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

    /**
     * 反向测试：密码为null
     */
    @Test
    void testLogin_NullPassword() {
        // 准备测试数据
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("testuser");
        loginDTO.setPassword(null);

        // 模拟服务抛出异常
        when(authService.login(loginDTO)).thenThrow(new IllegalArgumentException("密码不能为null"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.login(loginDTO);
        });

        assertEquals("密码不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(authService, times(1)).login(loginDTO);
    }

}
