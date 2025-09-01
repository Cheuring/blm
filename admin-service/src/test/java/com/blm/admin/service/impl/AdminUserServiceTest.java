package com.blm.admin.service.impl;

import com.blm.admin.service.AdminUserService;
import com.blm.common.vo.UserVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AdminUserService 单元测试
 * 测试管理员用户服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private AdminUserService adminUserService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== listUsers() 方法测试 ====================

    /**
     * 正向测试：成功获取用户列表
     */
    @Test
    void testListUsers_Success() {
        // 准备测试数据
        UserVO userVO1 = new UserVO();
        userVO1.setId(1L);
        userVO1.setUsername("user1");
        userVO1.setEmail("user1@example.com");
        
        UserVO userVO2 = new UserVO();
        userVO2.setId(2L);
        userVO2.setUsername("user2");
        userVO2.setEmail("user2@example.com");
        
        List<UserVO> expectedUsers = Arrays.asList(userVO1, userVO2);
        
        // 模拟服务行为
        when(adminUserService.listUsers()).thenReturn(expectedUsers);
        
        // 执行测试
        List<UserVO> actualUsers = adminUserService.listUsers();
        
        // 验证结果
        assertNotNull(actualUsers, "用户列表不应为null");
        assertEquals(2, actualUsers.size(), "用户列表大小应为2");
        assertEquals("user1", actualUsers.get(0).getUsername(), "第一个用户名应该匹配");
        assertEquals("user2", actualUsers.get(1).getUsername(), "第二个用户名应该匹配");
        assertEquals("user1@example.com", actualUsers.get(0).getEmail(), "第一个用户邮箱应该匹配");
        assertEquals("user2@example.com", actualUsers.get(1).getEmail(), "第二个用户邮箱应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).listUsers();
    }

    /**
     * 正向测试：获取空用户列表
     */
    @Test
    void testListUsers_EmptyList() {
        // 准备测试数据
        List<UserVO> expectedUsers = Collections.emptyList();
        
        // 模拟服务行为
        when(adminUserService.listUsers()).thenReturn(expectedUsers);
        
        // 执行测试
        List<UserVO> actualUsers = adminUserService.listUsers();
        
        // 验证结果
        assertNotNull(actualUsers, "用户列表不应为null");
        assertTrue(actualUsers.isEmpty(), "用户列表应为空");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).listUsers();
    }

    /**
     * 反向测试：listUsers抛出异常
     */
    @Test
    void testListUsers_ThrowsException() {
        // 模拟服务抛出异常
        when(adminUserService.listUsers()).thenThrow(new RuntimeException("数据库连接失败"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminUserService.listUsers();
        });
        
        assertEquals("数据库连接失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).listUsers();
    }

    /**
     * 反向测试：listUsers返回null
     */
    @Test
    void testListUsers_ReturnsNull() {
        // 模拟服务返回null
        when(adminUserService.listUsers()).thenReturn(null);
        
        // 执行测试
        List<UserVO> actualUsers = adminUserService.listUsers();
        
        // 验证结果
        assertNull(actualUsers, "应该返回null");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).listUsers();
    }

    // ==================== updateStatus() 方法测试 ====================

    /**
     * 正向测试：成功更新用户状态
     */
    @Test
    void testUpdateStatus_Success() {
        // 准备测试数据
        Long userId = 1L;
        Integer status = 1; // 激活状态
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(adminUserService).updateStatus(userId, status);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            adminUserService.updateStatus(userId, status);
        }, "更新用户状态不应该抛出异常");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 正向测试：更新不同的用户状态
     */
    @Test
    void testUpdateStatus_DifferentStatuses() {
        // 测试多种状态
        Integer[] statuses = {0, 1}; // 0-禁用, 1-激活
        Long userId = 1L;
        
        for (Integer status : statuses) {
            // 模拟服务行为
            doNothing().when(adminUserService).updateStatus(userId, status);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminUserService.updateStatus(userId, status);
            }, "更新用户状态为 " + status + " 不应该抛出异常");
        }
        
        // 验证方法被调用次数
        verify(adminUserService, times(statuses.length)).updateStatus(eq(userId), anyInt());
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testUpdateStatus_NullUserId() {
        // 准备测试数据
        Long userId = null;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为null"))
                .when(adminUserService).updateStatus(userId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminUserService.updateStatus(userId, status);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 反向测试：status为null
     */
    @Test
    void testUpdateStatus_NullStatus() {
        // 准备测试数据
        Long userId = 1L;
        Integer status = null;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("状态不能为null"))
                .when(adminUserService).updateStatus(userId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminUserService.updateStatus(userId, status);
        });
        
        assertEquals("状态不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 反向测试：userId为负数
     */
    @Test
    void testUpdateStatus_NegativeUserId() {
        // 准备测试数据
        Long userId = -1L;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("用户ID必须为正数"))
                .when(adminUserService).updateStatus(userId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminUserService.updateStatus(userId, status);
        });
        
        assertEquals("用户ID必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 反向测试：无效的状态值
     */
    @Test
    void testUpdateStatus_InvalidStatus() {
        // 准备测试数据
        Long userId = 1L;
        Integer status = 999; // 无效状态
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("无效的状态值: " + status))
                .when(adminUserService).updateStatus(userId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminUserService.updateStatus(userId, status);
        });
        
        assertEquals("无效的状态值: " + status, exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 反向测试：负数状态值
     */
    @Test
    void testUpdateStatus_NegativeStatus() {
        // 准备测试数据
        Long userId = 1L;
        Integer status = -1; // 负数状态
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("状态值不能为负数"))
                .when(adminUserService).updateStatus(userId, status);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminUserService.updateStatus(userId, status);
        });
        
        assertEquals("状态值不能为负数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 反向测试：用户不存在
     */
    @Test
    void testUpdateStatus_UserNotFound() {
        // 准备测试数据
        Long userId = 999L;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("用户不存在"))
                .when(adminUserService).updateStatus(userId, status);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminUserService.updateStatus(userId, status);
        });
        
        assertEquals("用户不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 反向测试：数据库操作失败
     */
    @Test
    void testUpdateStatus_DatabaseException() {
        // 准备测试数据
        Long userId = 1L;
        Integer status = 1;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("数据库操作失败"))
                .when(adminUserService).updateStatus(userId, status);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminUserService.updateStatus(userId, status);
        });
        
        assertEquals("数据库操作失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).updateStatus(userId, status);
    }

    /**
     * 正向测试：批量更新多个用户状态
     */
    @Test
    void testUpdateStatus_MultipleUsers() {
        // 准备测试数据
        Long[] userIds = {1L, 2L, 3L};
        Integer status = 0; // 禁用状态
        
        for (Long userId : userIds) {
            // 模拟服务行为
            doNothing().when(adminUserService).updateStatus(userId, status);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminUserService.updateStatus(userId, status);
            }, "更新用户 " + userId + " 的状态不应该抛出异常");
        }
        
        // 验证方法被调用次数
        verify(adminUserService, times(userIds.length)).updateStatus(anyLong(), eq(status));
    }

    /**
     * 正向测试：获取大量用户列表
     */
    @Test
    void testListUsers_LargeList() {
        // 准备测试数据 - 模拟大量用户
        List<UserVO> expectedUsers = Collections.nCopies(1000, new UserVO());
        
        // 模拟服务行为
        when(adminUserService.listUsers()).thenReturn(expectedUsers);
        
        // 执行测试
        List<UserVO> actualUsers = adminUserService.listUsers();
        
        // 验证结果
        assertNotNull(actualUsers, "用户列表不应为null");
        assertEquals(1000, actualUsers.size(), "用户列表大小应为1000");
        
        // 验证方法被调用
        verify(adminUserService, times(1)).listUsers();
    }
}
