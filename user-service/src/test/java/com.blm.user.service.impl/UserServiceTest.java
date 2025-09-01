package com.blm.user.service.impl;

import com.blm.common.dto.PasswordUpdateDTO;
import com.blm.common.dto.UserProfileUpdateDTO;
import com.blm.common.entity.User;
import com.blm.user.service.UserService;
import com.blm.common.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService 单元测试
 * 测试用户服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }


    // ==================== getUserProfile() 方法测试 ====================

    /**
     * 正向测试：成功获取用户档案
     */
    @Test
    void testGetUserProfile_Success() {
        // 准备测试数据
        Long userId = 1L;
        UserVO expectedUserVO = new UserVO();
        expectedUserVO.setId(userId);
        expectedUserVO.setUsername("testuser");
        expectedUserVO.setEmail("test@example.com");
        
        // 模拟服务行为
        when(userService.getUserProfile(userId)).thenReturn(expectedUserVO);
        
        // 执行测试
        UserVO actualUserVO = userService.getUserProfile(userId);
        
        // 验证结果
        assertNotNull(actualUserVO, "用户档案不应为null");
        assertEquals(userId, actualUserVO.getId(), "用户ID应该匹配");
        assertEquals("testuser", actualUserVO.getUsername(), "用户名应该匹配");
        assertEquals("test@example.com", actualUserVO.getEmail(), "邮箱应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).getUserProfile(userId);
    }

    /**
     * 反向测试：用户ID为null
     */
    @Test
    void testGetUserProfile_NullUserId() {
        // 模拟服务抛出异常
        when(userService.getUserProfile(null)).thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserProfile(null);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).getUserProfile(null);
    }

    /**
     * 反向测试：用户不存在
     */
    @Test
    void testGetUserProfile_UserNotFound() {
        // 准备测试数据
        Long userId = 999L;
        
        // 模拟服务抛出异常
        when(userService.getUserProfile(userId)).thenThrow(new RuntimeException("用户不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserProfile(userId);
        });
        
        assertEquals("用户不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).getUserProfile(userId);
    }

    // ==================== updateUserProfile() 方法测试 ====================

    /**
     * 正向测试：成功更新用户档案
     */
    @Test
    void testUpdateUserProfile_Success() {
        // 准备测试数据
        Long userId = 1L;
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO();
        updateDTO.setEmail("newemail@example.com");
        updateDTO.setAvatar("new-avatar.jpg");
        
        UserVO expectedUserVO = new UserVO();
        expectedUserVO.setId(userId);
        expectedUserVO.setEmail("newemail@example.com");
        expectedUserVO.setAvatar("new-avatar.jpg");
        
        // 模拟服务行为
        when(userService.updateUserProfile(userId, updateDTO)).thenReturn(expectedUserVO);
        
        // 执行测试
        UserVO actualUserVO = userService.updateUserProfile(userId, updateDTO);
        
        // 验证结果
        assertNotNull(actualUserVO, "更新结果不应为null");
        assertEquals(userId, actualUserVO.getId(), "用户ID应该匹配");
        assertEquals("newemail@example.com", actualUserVO.getEmail(), "邮箱应该匹配");
        assertEquals("new-avatar.jpg", actualUserVO.getAvatar(), "头像应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).updateUserProfile(userId, updateDTO);
    }

    /**
     * 反向测试：更新DTO为null
     */
    @Test
    void testUpdateUserProfile_NullUpdateDTO() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟服务抛出异常
        when(userService.updateUserProfile(userId, null)).thenThrow(new IllegalArgumentException("更新信息不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserProfile(userId, null);
        });
        
        assertEquals("更新信息不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).updateUserProfile(userId, null);
    }

    // ==================== updateUserPassword() 方法测试 ====================

    /**
     * 正向测试：成功更新用户密码
     */
    @Test
    void testUpdateUserPassword_Success() {
        // 准备测试数据
        Long userId = 1L;
        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setOldPassword("oldpassword");
        passwordUpdateDTO.setNewPassword("newpassword123");
        
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("testuser");
        
        // 模拟服务行为
        when(userService.updateUserPassword(userId, passwordUpdateDTO)).thenReturn(expectedUser);
        
        // 执行测试
        User actualUser = userService.updateUserPassword(userId, passwordUpdateDTO);
        
        // 验证结果
        assertNotNull(actualUser, "更新结果不应为null");
        assertEquals(userId, actualUser.getId(), "用户ID应该匹配");
        assertEquals("testuser", actualUser.getUsername(), "用户名应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).updateUserPassword(userId, passwordUpdateDTO);
    }

    /**
     * 反向测试：旧密码错误
     */
    @Test
    void testUpdateUserPassword_WrongOldPassword() {
        // 准备测试数据
        Long userId = 1L;
        PasswordUpdateDTO passwordUpdateDTO = new PasswordUpdateDTO();
        passwordUpdateDTO.setOldPassword("wrongpassword");
        passwordUpdateDTO.setNewPassword("newpassword123");
        
        // 模拟服务抛出异常
        when(userService.updateUserPassword(userId, passwordUpdateDTO)).thenThrow(new RuntimeException("旧密码错误"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUserPassword(userId, passwordUpdateDTO);
        });
        
        assertEquals("旧密码错误", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).updateUserPassword(userId, passwordUpdateDTO);
    }

    // ==================== findUserById() 方法测试 ====================

    /**
     * 正向测试：成功通过ID查找用户
     */
    @Test
    void testFindUserById_Success() {
        // 准备测试数据
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setUsername("testuser");
        
        // 模拟服务行为
        when(userService.findUserById(userId)).thenReturn(expectedUser);
        
        // 执行测试
        User actualUser = userService.findUserById(userId);
        
        // 验证结果
        assertNotNull(actualUser, "用户不应为null");
        assertEquals(userId, actualUser.getId(), "用户ID应该匹配");
        assertEquals("testuser", actualUser.getUsername(), "用户名应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).findUserById(userId);
    }

    /**
     * 反向测试：用户ID不存在
     */
    @Test
    void testFindUserById_UserNotFound() {
        // 准备测试数据
        Long userId = 999L;
        
        // 模拟服务抛出异常
        when(userService.findUserById(userId)).thenThrow(new RuntimeException("用户不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findUserById(userId);
        });
        
        assertEquals("用户不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).findUserById(userId);
    }

    // ==================== findUserByUsername() 方法测试 ====================

    /**
     * 正向测试：成功通过用户名查找用户
     */
    @Test
    void testFindUserByUsername_Success() {
        // 准备测试数据
        String username = "testuser";
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername(username);
        
        // 模拟服务行为
        when(userService.findUserByUsername(username)).thenReturn(expectedUser);
        
        // 执行测试
        User actualUser = userService.findUserByUsername(username);
        
        // 验证结果
        assertNotNull(actualUser, "用户不应为null");
        assertEquals(1L, actualUser.getId(), "用户ID应该匹配");
        assertEquals(username, actualUser.getUsername(), "用户名应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).findUserByUsername(username);
    }

    /**
     * 反向测试：用户名不存在
     */
    @Test
    void testFindUserByUsername_UserNotFound() {
        // 准备测试数据
        String username = "nonexistentuser";
        
        // 模拟服务抛出异常
        when(userService.findUserByUsername(username)).thenThrow(new RuntimeException("用户不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.findUserByUsername(username);
        });
        
        assertEquals("用户不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).findUserByUsername(username);
    }

    /**
     * 反向测试：用户名为null
     */
    @Test
    void testFindUserByUsername_NullUsername() {
        // 模拟服务抛出异常
        when(userService.findUserByUsername(null)).thenThrow(new IllegalArgumentException("用户名不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findUserByUsername(null);
        });
        
        assertEquals("用户名不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).findUserByUsername(null);
    }

    /**
     * 反向测试：用户名为空字符串
     */
    @Test
    void testFindUserByUsername_EmptyUsername() {
        // 准备测试数据
        String username = "";
        
        // 模拟服务抛出异常
        when(userService.findUserByUsername(username)).thenThrow(new IllegalArgumentException("用户名不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findUserByUsername(username);
        });
        
        assertEquals("用户名不能为空", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(userService, times(1)).findUserByUsername(username);
    }
}
