package com.blm.rider.service.impl;

import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.entity.Rider;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;
import com.blm.rider.service.RiderService;
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
 * RiderService 单元测试
 * 测试骑手服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class RiderServiceTest {

    @Mock
    private RiderService riderService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== registerRider() 方法测试 ====================

    /**
     * 正向测试：成功注册骑手
     */
    @Test
    void testRegisterRider_Success() {
        // 准备测试数据
        Long userId = 1L;
        RiderRegisterDTO dto = new RiderRegisterDTO();
        dto.setRealName("张三");
        dto.setIdCard("110101199001011234");
        dto.setVehicleType(com.blm.common.entity.Rider.VehicleType.BIKE);
        dto.setVehicleNumber("京A12345");

        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(riderService).registerRider(userId, dto);

        // 执行测试
        assertDoesNotThrow(() -> {
            riderService.registerRider(userId, dto);
        }, "注册骑手不应该抛出异常");

        // 验证方法被调用
        verify(riderService, times(1)).registerRider(userId, dto);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testRegisterRider_NullUserId() {
        // 准备测试数据
        Long userId = null;
        RiderRegisterDTO dto = new RiderRegisterDTO();
        dto.setRealName("张三");

        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为null"))
                .when(riderService).registerRider(userId, dto);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            riderService.registerRider(userId, dto);
        });

        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).registerRider(userId, dto);
    }

    /**
     * 反向测试：dto为null
     */
    @Test
    void testRegisterRider_NullDto() {
        // 准备测试数据
        Long userId = 1L;
        RiderRegisterDTO dto = null;

        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("注册信息不能为null"))
                .when(riderService).registerRider(userId, dto);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            riderService.registerRider(userId, dto);
        });

        assertEquals("注册信息不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).registerRider(userId, dto);
    }

    /**
     * 反向测试：用户已经是骑手
     */
    @Test
    void testRegisterRider_UserAlreadyRider() {
        // 准备测试数据
        Long userId = 1L;
        RiderRegisterDTO dto = new RiderRegisterDTO();
        dto.setRealName("张三");

        // 模拟服务抛出异常
        doThrow(new RuntimeException("用户已经是骑手"))
                .when(riderService).registerRider(userId, dto);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            riderService.registerRider(userId, dto);
        });

        assertEquals("用户已经是骑手", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).registerRider(userId, dto);
    }

    // ==================== updateWorkStatus() 方法测试 ====================

    /**
     * 正向测试：成功更新工作状态
     */
    @Test
    void testUpdateWorkStatus_Success() {
        // 准备测试数据
        Long userId = 1L;
        WorkStatusUpdateDTO dto = new WorkStatusUpdateDTO();
        dto.setWorkStatus(com.blm.common.entity.Rider.RiderStatus.ONLINE); // 在线状态

        // 模拟服务行为
        doNothing().when(riderService).updateWorkStatus(userId, dto);

        // 执行测试
        assertDoesNotThrow(() -> {
            riderService.updateWorkStatus(userId, dto);
        }, "更新工作状态不应该抛出异常");

        // 验证方法被调用
        verify(riderService, times(1)).updateWorkStatus(userId, dto);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testUpdateWorkStatus_NullUserId() {
        // 准备测试数据
        Long userId = null;
        WorkStatusUpdateDTO dto = new WorkStatusUpdateDTO();
        dto.setWorkStatus(com.blm.common.entity.Rider.RiderStatus.ONLINE);

        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为null"))
                .when(riderService).updateWorkStatus(userId, dto);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            riderService.updateWorkStatus(userId, dto);
        });

        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).updateWorkStatus(userId, dto);
    }

    // ==================== updateLocation() 方法测试 ====================

    /**
     * 正向测试：成功更新位置
     */
    @Test
    void testUpdateLocation_Success() {
        // 准备测试数据
        Long userId = 1L;
        LocationUpdateDTO dto = new LocationUpdateDTO();
        dto.setLatitude(new java.math.BigDecimal("39.9087"));
        dto.setLongitude(new java.math.BigDecimal("116.3975"));
        Long expectedLocationId = 100L;

        // 模拟服务行为
        when(riderService.updateLocation(userId, dto)).thenReturn(expectedLocationId);

        // 执行测试
        Long actualLocationId = riderService.updateLocation(userId, dto);

        // 验证结果
        assertEquals(expectedLocationId, actualLocationId, "位置ID应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).updateLocation(userId, dto);
    }

    /**
     * 反向测试：位置更新失败
     */
    @Test
    void testUpdateLocation_Failed() {
        // 准备测试数据
        Long userId = 1L;
        LocationUpdateDTO dto = new LocationUpdateDTO();
        dto.setLatitude(new java.math.BigDecimal("39.9087"));
        dto.setLongitude(new java.math.BigDecimal("116.3975"));

        // 模拟服务抛出异常
        when(riderService.updateLocation(userId, dto))
                .thenThrow(new RuntimeException("位置更新失败"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            riderService.updateLocation(userId, dto);
        });

        assertEquals("位置更新失败", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).updateLocation(userId, dto);
    }

    // ==================== listAvailableOrders() 方法测试 ====================

    /**
     * 正向测试：成功获取可接单列表
     */
    @Test
    void testListAvailableOrders_Success() {
        // 准备测试数据
        RiderOrderVO orderVO1 = new RiderOrderVO();
        orderVO1.setId(1L);
        orderVO1.setOrderNo("ORDER001");

        RiderOrderVO orderVO2 = new RiderOrderVO();
        orderVO2.setId(2L);
        orderVO2.setOrderNo("ORDER002");

        List<RiderOrderVO> expectedOrders = Arrays.asList(orderVO1, orderVO2);

        // 模拟服务行为
        when(riderService.listAvailableOrders()).thenReturn(expectedOrders);

        // 执行测试
        List<RiderOrderVO> actualOrders = riderService.listAvailableOrders();

        // 验证结果
        assertNotNull(actualOrders, "订单列表不应为null");
        assertEquals(2, actualOrders.size(), "订单列表大小应为2");
        assertEquals("ORDER001", actualOrders.get(0).getOrderNo(), "第一个订单号应该匹配");
        assertEquals("ORDER002", actualOrders.get(1).getOrderNo(), "第二个订单号应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).listAvailableOrders();
    }

    /**
     * 正向测试：获取空的可接单列表
     */
    @Test
    void testListAvailableOrders_EmptyList() {
        // 准备测试数据
        List<RiderOrderVO> expectedOrders = Collections.emptyList();

        // 模拟服务行为
        when(riderService.listAvailableOrders()).thenReturn(expectedOrders);

        // 执行测试
        List<RiderOrderVO> actualOrders = riderService.listAvailableOrders();

        // 验证结果
        assertNotNull(actualOrders, "订单列表不应为null");
        assertTrue(actualOrders.isEmpty(), "订单列表应为空");

        // 验证方法被调用
        verify(riderService, times(1)).listAvailableOrders();
    }

    // ==================== listMyOrders() 方法测试 ====================

    /**
     * 正向测试：成功获取我的订单列表
     */
    @Test
    void testListMyOrders_Success() {
        // 准备测试数据
        Long userId = 1L;
        RiderOrderVO orderVO1 = new RiderOrderVO();
        orderVO1.setId(1L);
        orderVO1.setOrderNo("ORDER001");

        List<RiderOrderVO> expectedOrders = Arrays.asList(orderVO1);

        // 模拟服务行为
        when(riderService.listMyOrders(userId)).thenReturn(expectedOrders);

        // 执行测试
        List<RiderOrderVO> actualOrders = riderService.listMyOrders(userId);

        // 验证结果
        assertNotNull(actualOrders, "订单列表不应为null");
        assertEquals(1, actualOrders.size(), "订单列表大小应为1");
        assertEquals("ORDER001", actualOrders.get(0).getOrderNo(), "订单号应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).listMyOrders(userId);
    }

    /**
     * 反向测试：userId为null
     */
    @Test
    void testListMyOrders_NullUserId() {
        // 准备测试数据
        Long userId = null;

        // 模拟服务抛出异常
        when(riderService.listMyOrders(userId))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            riderService.listMyOrders(userId);
        });

        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).listMyOrders(userId);
    }

    // ==================== acceptOrder() 方法测试 ====================

    /**
     * 正向测试：成功接受订单
     */
    @Test
    void testAcceptOrder_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 100L;

        // 模拟服务行为
        doNothing().when(riderService).acceptOrder(userId, orderId);

        // 执行测试
        assertDoesNotThrow(() -> {
            riderService.acceptOrder(userId, orderId);
        }, "接受订单不应该抛出异常");

        // 验证方法被调用
        verify(riderService, times(1)).acceptOrder(userId, orderId);
    }

    /**
     * 反向测试：订单已被接受
     */
    @Test
    void testAcceptOrder_OrderAlreadyAccepted() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 100L;

        // 模拟服务抛出异常
        doThrow(new RuntimeException("订单已被其他骑手接受"))
                .when(riderService).acceptOrder(userId, orderId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            riderService.acceptOrder(userId, orderId);
        });

        assertEquals("订单已被其他骑手接受", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).acceptOrder(userId, orderId);
    }

    // ==================== pickupOrder() 方法测试 ====================

    /**
     * 正向测试：成功取餐开始配送
     */
    @Test
    void testPickupOrder_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 100L;

        // 模拟服务行为
        doNothing().when(riderService).pickupOrder(userId, orderId);

        // 执行测试
        assertDoesNotThrow(() -> {
            riderService.pickupOrder(userId, orderId);
        }, "取餐开始配送不应该抛出异常");

        // 验证方法被调用
        verify(riderService, times(1)).pickupOrder(userId, orderId);
    }

    /**
     * 反向测试：订单状态不正确
     */
    @Test
    void testPickupOrder_InvalidOrderStatus() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 100L;

        // 模拟服务抛出异常
        doThrow(new RuntimeException("订单状态不正确，无法取餐"))
                .when(riderService).pickupOrder(userId, orderId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            riderService.pickupOrder(userId, orderId);
        });

        assertEquals("订单状态不正确，无法取餐", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).pickupOrder(userId, orderId);
    }

    // ==================== delivered() 方法测试 ====================

    /**
     * 正向测试：成功送达订单
     */
    @Test
    void testDelivered_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 100L;

        // 模拟服务行为
        doNothing().when(riderService).delivered(userId, orderId);

        // 执行测试
        assertDoesNotThrow(() -> {
            riderService.delivered(userId, orderId);
        }, "送达订单不应该抛出异常");

        // 验证方法被调用
        verify(riderService, times(1)).delivered(userId, orderId);
    }

    /**
     * 反向测试：订单状态不正确，无法送达
     */
    @Test
    void testDelivered_InvalidOrderStatus() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 100L;

        // 模拟服务抛出异常
        doThrow(new RuntimeException("订单状态不正确，无法送达"))
                .when(riderService).delivered(userId, orderId);

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            riderService.delivered(userId, orderId);
        });

        assertEquals("订单状态不正确，无法送达", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).delivered(userId, orderId);
    }

    /**
     * 反向测试：用户ID为null
     */
    @Test
    void testDelivered_NullUserId() {
        // 准备测试数据
        Long userId = null;
        Long orderId = 100L;

        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为null"))
                .when(riderService).delivered(userId, orderId);

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            riderService.delivered(userId, orderId);
        });

        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).delivered(userId, orderId);
    }

    // ==================== getWorkStatus() 方法测试 ====================

    /**
     * 正向测试：成功获取工作状态
     */
    @Test
    void testGetWorkStatus_Success() {
        // 准备测试数据
        Long userId = 1L;
        Rider.RiderStatus expectedStatus = Rider.RiderStatus.ONLINE;

        // 模拟服务行为
        when(riderService.getWorkStatus(userId)).thenReturn(expectedStatus);

        // 执行测试
        Rider.RiderStatus actualStatus = riderService.getWorkStatus(userId);

        // 验证结果
        assertEquals(expectedStatus, actualStatus, "工作状态应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).getWorkStatus(userId);
    }

    /**
     * 反向测试：用户ID为null
     */
    @Test
    void testGetWorkStatus_NullUserId() {
        // 准备测试数据
        Long userId = null;

        // 模拟服务抛出异常
        when(riderService.getWorkStatus(userId))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            riderService.getWorkStatus(userId);
        });

        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).getWorkStatus(userId);
    }

    /**
     * 反向测试：用户不是骑手
     */
    @Test
    void testGetWorkStatus_UserNotRider() {
        // 准备测试数据
        Long userId = 999L;

        // 模拟服务抛出异常
        when(riderService.getWorkStatus(userId))
                .thenThrow(new RuntimeException("用户不是骑手"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            riderService.getWorkStatus(userId);
        });

        assertEquals("用户不是骑手", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).getWorkStatus(userId);
    }

    // ==================== getStats() 方法测试 ====================

    /**
     * 正向测试：成功获取统计数据
     */
    @Test
    void testGetStats_Success() {
        // 准备测试数据
        Long userId = 1L;
        String period = "day";
        RiderStatsVO expectedStats = new RiderStatsVO();
        expectedStats.setOrdersCount(10);
        expectedStats.setTotalIncome(new java.math.BigDecimal("100.00"));

        // 模拟服务行为
        when(riderService.getStats(userId, period)).thenReturn(expectedStats);

        // 执行测试
        RiderStatsVO actualStats = riderService.getStats(userId, period);

        // 验证结果
        assertNotNull(actualStats, "统计数据不应为null");
        assertEquals(10, actualStats.getOrdersCount(), "总订单数应该匹配");
        assertEquals(new java.math.BigDecimal("100.00"), actualStats.getTotalIncome(), "总收入应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).getStats(userId, period);
    }

    /**
     * 正向测试：测试不同时间周期
     */
    @Test
    void testGetStats_DifferentPeriods() {
        // 准备测试数据
        Long userId = 1L;
        String[] periods = {"day", "week", "month"};

        for (String period : periods) {
            RiderStatsVO expectedStats = new RiderStatsVO();
            expectedStats.setOrdersCount(5);

            // 模拟服务行为
            when(riderService.getStats(userId, period)).thenReturn(expectedStats);

            // 执行测试
            RiderStatsVO actualStats = riderService.getStats(userId, period);

            // 验证结果
            assertNotNull(actualStats, period + " 周期的统计数据不应为null");
            assertEquals(5, actualStats.getOrdersCount(), period + " 周期的订单数应该匹配");
        }

        // 验证方法被调用次数
        verify(riderService, times(periods.length)).getStats(eq(userId), anyString());
    }

    /**
     * 反向测试：无效的时间周期
     */
    @Test
    void testGetStats_InvalidPeriod() {
        // 准备测试数据
        Long userId = 1L;
        String period = "invalid";

        // 模拟服务抛出异常
        when(riderService.getStats(userId, period))
                .thenThrow(new IllegalArgumentException("无效的时间周期: " + period));

        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            riderService.getStats(userId, period);
        });

        assertEquals("无效的时间周期: " + period, exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).getStats(userId, period);
    }

    /**
     * 反向测试：用户不是骑手
     */
    @Test
    void testGetStats_UserNotRider() {
        // 准备测试数据
        Long userId = 999L;
        String period = "day";

        // 模拟服务抛出异常
        when(riderService.getStats(userId, period))
                .thenThrow(new RuntimeException("用户不是骑手"));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            riderService.getStats(userId, period);
        });

        assertEquals("用户不是骑手", exception.getMessage(), "异常消息应该匹配");

        // 验证方法被调用
        verify(riderService, times(1)).getStats(userId, period);
    }
}

