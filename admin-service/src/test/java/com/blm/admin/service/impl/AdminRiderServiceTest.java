package com.blm.admin.service.impl;

import com.blm.admin.service.AdminRiderService;
import com.blm.common.vo.RiderVO;
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
 * AdminRiderService 单元测试
 * 测试管理员骑手服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class AdminRiderServiceTest {

    @Mock
    private AdminRiderService adminRiderService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== listRiders() 方法测试 ====================

    /**
     * 正向测试：成功获取骑手列表
     */
    @Test
    void testListRiders_Success() {
        // 准备测试数据
        RiderVO riderVO1 = new RiderVO();
        riderVO1.setId(1L);
        riderVO1.setRealName("张三");
        riderVO1.setVehicleNumber("京A12345");
        
        RiderVO riderVO2 = new RiderVO();
        riderVO2.setId(2L);
        riderVO2.setRealName("李四");
        riderVO2.setVehicleNumber("京B67890");
        
        List<RiderVO> expectedRiders = Arrays.asList(riderVO1, riderVO2);
        
        // 模拟服务行为
        when(adminRiderService.listRiders()).thenReturn(expectedRiders);
        
        // 执行测试
        List<RiderVO> actualRiders = adminRiderService.listRiders();
        
        // 验证结果
        assertNotNull(actualRiders, "骑手列表不应为null");
        assertEquals(2, actualRiders.size(), "骑手列表大小应为2");
        assertEquals("张三", actualRiders.get(0).getRealName(), "第一个骑手姓名应该匹配");
        assertEquals("李四", actualRiders.get(1).getRealName(), "第二个骑手姓名应该匹配");
        assertEquals("京A12345", actualRiders.get(0).getVehicleNumber(), "第一个骑手车牌应该匹配");
        assertEquals("京B67890", actualRiders.get(1).getVehicleNumber(), "第二个骑手车牌应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).listRiders();
    }

    /**
     * 正向测试：获取空骑手列表
     */
    @Test
    void testListRiders_EmptyList() {
        // 准备测试数据
        List<RiderVO> expectedRiders = Collections.emptyList();
        
        // 模拟服务行为
        when(adminRiderService.listRiders()).thenReturn(expectedRiders);
        
        // 执行测试
        List<RiderVO> actualRiders = adminRiderService.listRiders();
        
        // 验证结果
        assertNotNull(actualRiders, "骑手列表不应为null");
        assertTrue(actualRiders.isEmpty(), "骑手列表应为空");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).listRiders();
    }

    /**
     * 反向测试：listRiders抛出异常
     */
    @Test
    void testListRiders_ThrowsException() {
        // 模拟服务抛出异常
        when(adminRiderService.listRiders()).thenThrow(new RuntimeException("数据库连接失败"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminRiderService.listRiders();
        });
        
        assertEquals("数据库连接失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).listRiders();
    }

    /**
     * 反向测试：listRiders返回null
     */
    @Test
    void testListRiders_ReturnsNull() {
        // 模拟服务返回null
        when(adminRiderService.listRiders()).thenReturn(null);
        
        // 执行测试
        List<RiderVO> actualRiders = adminRiderService.listRiders();
        
        // 验证结果
        assertNull(actualRiders, "应该返回null");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).listRiders();
    }

    // ==================== updateWorkStatus() 方法测试 ====================

    /**
     * 正向测试：成功更新骑手工作状态
     */
    @Test
    void testUpdateWorkStatus_Success() {
        // 准备测试数据
        Long riderId = 1L;
        Integer workStatus = 1; // 工作中
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        }, "更新骑手工作状态不应该抛出异常");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 正向测试：更新不同的工作状态
     */
    @Test
    void testUpdateWorkStatus_DifferentStatuses() {
        // 测试多种状态
        Integer[] statuses = {0, 1, 2}; // 0-休息中, 1-工作中, 2-暂停接单
        Long riderId = 1L;
        
        for (Integer status : statuses) {
            // 模拟服务行为
            doNothing().when(adminRiderService).updateWorkStatus(riderId, status);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminRiderService.updateWorkStatus(riderId, status);
            }, "更新骑手工作状态为 " + status + " 不应该抛出异常");
        }
        
        // 验证方法被调用次数
        verify(adminRiderService, times(statuses.length)).updateWorkStatus(eq(riderId), anyInt());
    }

    /**
     * 反向测试：riderId为null
     */
    @Test
    void testUpdateWorkStatus_NullRiderId() {
        // 准备测试数据
        Long riderId = null;
        Integer workStatus = 1;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("骑手ID不能为null"))
                .when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        });
        
        assertEquals("骑手ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 反向测试：workStatus为null
     */
    @Test
    void testUpdateWorkStatus_NullWorkStatus() {
        // 准备测试数据
        Long riderId = 1L;
        Integer workStatus = null;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("工作状态不能为null"))
                .when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        });
        
        assertEquals("工作状态不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 反向测试：riderId为负数
     */
    @Test
    void testUpdateWorkStatus_NegativeRiderId() {
        // 准备测试数据
        Long riderId = -1L;
        Integer workStatus = 1;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("骑手ID必须为正数"))
                .when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        });
        
        assertEquals("骑手ID必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 反向测试：无效的工作状态
     */
    @Test
    void testUpdateWorkStatus_InvalidWorkStatus() {
        // 准备测试数据
        Long riderId = 1L;
        Integer workStatus = 999; // 无效状态
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("无效的工作状态: " + workStatus))
                .when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        });
        
        assertEquals("无效的工作状态: " + workStatus, exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 反向测试：负数工作状态
     */
    @Test
    void testUpdateWorkStatus_NegativeWorkStatus() {
        // 准备测试数据
        Long riderId = 1L;
        Integer workStatus = -1; // 负数状态
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("工作状态不能为负数"))
                .when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        });
        
        assertEquals("工作状态不能为负数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 反向测试：骑手不存在
     */
    @Test
    void testUpdateWorkStatus_RiderNotFound() {
        // 准备测试数据
        Long riderId = 999L;
        Integer workStatus = 1;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("骑手不存在"))
                .when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        });
        
        assertEquals("骑手不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 反向测试：数据库操作失败
     */
    @Test
    void testUpdateWorkStatus_DatabaseException() {
        // 准备测试数据
        Long riderId = 1L;
        Integer workStatus = 1;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("数据库操作失败"))
                .when(adminRiderService).updateWorkStatus(riderId, workStatus);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminRiderService.updateWorkStatus(riderId, workStatus);
        });
        
        assertEquals("数据库操作失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminRiderService, times(1)).updateWorkStatus(riderId, workStatus);
    }

    /**
     * 正向测试：更新多个骑手状态
     */
    @Test
    void testUpdateWorkStatus_MultipleRiders() {
        // 准备测试数据
        Long[] riderIds = {1L, 2L, 3L};
        Integer workStatus = 1;
        
        for (Long riderId : riderIds) {
            // 模拟服务行为
            doNothing().when(adminRiderService).updateWorkStatus(riderId, workStatus);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminRiderService.updateWorkStatus(riderId, workStatus);
            }, "更新骑手 " + riderId + " 的工作状态不应该抛出异常");
        }
        
        // 验证方法被调用次数
        verify(adminRiderService, times(riderIds.length)).updateWorkStatus(anyLong(), eq(workStatus));
    }
}
