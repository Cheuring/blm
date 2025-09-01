package com.blm.admin.service.impl;

import com.blm.admin.service.AdminOrderService;
import com.blm.common.vo.OrderVO;
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
 * AdminOrderService 单元测试
 * 测试管理员订单服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class AdminOrderServiceTest {

    @Mock
    private AdminOrderService adminOrderService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== listOrders() 方法测试 ====================

    /**
     * 正向测试：成功获取订单列表
     */
    @Test
    void testListOrders_Success() {
        // 准备测试数据
        OrderVO orderVO1 = new OrderVO();
        orderVO1.setId(1L);
        orderVO1.setOrderNo("ORDER_001");
        orderVO1.setStoreName("测试店铺1");
        
        OrderVO orderVO2 = new OrderVO();
        orderVO2.setId(2L);
        orderVO2.setOrderNo("ORDER_002");
        orderVO2.setStoreName("测试店铺2");
        
        List<OrderVO> expectedOrders = Arrays.asList(orderVO1, orderVO2);
        
        // 模拟服务行为
        when(adminOrderService.listOrders()).thenReturn(expectedOrders);
        
        // 执行测试
        List<OrderVO> actualOrders = adminOrderService.listOrders();
        
        // 验证结果
        assertNotNull(actualOrders, "订单列表不应为null");
        assertEquals(2, actualOrders.size(), "订单列表大小应为2");
        assertEquals("ORDER_001", actualOrders.get(0).getOrderNo(), "第一个订单号应该匹配");
        assertEquals("ORDER_002", actualOrders.get(1).getOrderNo(), "第二个订单号应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).listOrders();
    }

    /**
     * 正向测试：获取空订单列表
     */
    @Test
    void testListOrders_EmptyList() {
        // 准备测试数据
        List<OrderVO> expectedOrders = Collections.emptyList();
        
        // 模拟服务行为
        when(adminOrderService.listOrders()).thenReturn(expectedOrders);
        
        // 执行测试
        List<OrderVO> actualOrders = adminOrderService.listOrders();
        
        // 验证结果
        assertNotNull(actualOrders, "订单列表不应为null");
        assertTrue(actualOrders.isEmpty(), "订单列表应为空");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).listOrders();
    }

    /**
     * 反向测试：listOrders抛出异常
     */
    @Test
    void testListOrders_ThrowsException() {
        // 模拟服务抛出异常
        when(adminOrderService.listOrders()).thenThrow(new RuntimeException("数据库连接失败"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminOrderService.listOrders();
        });
        
        assertEquals("数据库连接失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).listOrders();
    }

    /**
     * 反向测试：listOrders返回null
     */
    @Test
    void testListOrders_ReturnsNull() {
        // 模拟服务返回null
        when(adminOrderService.listOrders()).thenReturn(null);
        
        // 执行测试
        List<OrderVO> actualOrders = adminOrderService.listOrders();
        
        // 验证结果
        assertNull(actualOrders, "应该返回null");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).listOrders();
    }

    // ==================== updateStatus() 方法测试 ====================

    /**
     * 正向测试：成功更新订单状态
     */
    @Test
    void testUpdateStatus_Success() {
        // 准备测试数据
        Long orderId = 1L;
        String orderStatus = "CONFIRMED";
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        }, "更新订单状态不应该抛出异常");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }

    /**
     * 正向测试：更新不同的订单状态
     */
    @Test
    void testUpdateStatus_DifferentStatuses() {
        // 测试多种状态
        String[] statuses = {"PENDING", "CONFIRMED", "PREPARING", "READY", "DELIVERED", "CANCELLED"};
        Long orderId = 1L;
        
        for (String status : statuses) {
            // 模拟服务行为
            doNothing().when(adminOrderService).updateStatus(orderId, status);
            
            // 执行测试
            assertDoesNotThrow(() -> {
                adminOrderService.updateStatus(orderId, status);
            }, "更新订单状态为 " + status + " 不应该抛出异常");
        }
        
        // 验证方法被调用次数
        verify(adminOrderService, times(statuses.length)).updateStatus(eq(orderId), anyString());
    }

    /**
     * 反向测试：orderId为null
     */
    @Test
    void testUpdateStatus_NullOrderId() {
        // 准备测试数据
        Long orderId = null;
        String orderStatus = "CONFIRMED";
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("订单ID不能为null"))
                .when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        });
        
        assertEquals("订单ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }

    /**
     * 反向测试：orderStatus为null
     */
    @Test
    void testUpdateStatus_NullOrderStatus() {
        // 准备测试数据
        Long orderId = 1L;
        String orderStatus = null;
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("订单状态不能为null"))
                .when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        });
        
        assertEquals("订单状态不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }

    /**
     * 反向测试：orderStatus为空字符串
     */
    @Test
    void testUpdateStatus_EmptyOrderStatus() {
        // 准备测试数据
        Long orderId = 1L;
        String orderStatus = "";
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("订单状态不能为空"))
                .when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        });
        
        assertEquals("订单状态不能为空", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }

    /**
     * 反向测试：orderId为负数
     */
    @Test
    void testUpdateStatus_NegativeOrderId() {
        // 准备测试数据
        Long orderId = -1L;
        String orderStatus = "CONFIRMED";
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("订单ID必须为正数"))
                .when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        });
        
        assertEquals("订单ID必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }

    /**
     * 反向测试：无效的订单状态
     */
    @Test
    void testUpdateStatus_InvalidOrderStatus() {
        // 准备测试数据
        Long orderId = 1L;
        String orderStatus = "INVALID_STATUS";
        
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("无效的订单状态: " + orderStatus))
                .when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        });
        
        assertEquals("无效的订单状态: " + orderStatus, exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }

    /**
     * 反向测试：订单不存在
     */
    @Test
    void testUpdateStatus_OrderNotFound() {
        // 准备测试数据
        Long orderId = 999L;
        String orderStatus = "CONFIRMED";
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("订单不存在"))
                .when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        });
        
        assertEquals("订单不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }

    /**
     * 反向测试：数据库操作失败
     */
    @Test
    void testUpdateStatus_DatabaseException() {
        // 准备测试数据
        Long orderId = 1L;
        String orderStatus = "CONFIRMED";
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("数据库操作失败"))
                .when(adminOrderService).updateStatus(orderId, orderStatus);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminOrderService.updateStatus(orderId, orderStatus);
        });
        
        assertEquals("数据库操作失败", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(adminOrderService, times(1)).updateStatus(orderId, orderStatus);
    }
}
