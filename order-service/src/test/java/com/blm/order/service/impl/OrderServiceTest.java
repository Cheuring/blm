package com.blm.order.service.impl;

import com.blm.common.dto.*;
import com.blm.common.entity.Order;
import com.blm.common.vo.*;
import com.blm.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * OrderService 单元测试
 * 测试订单服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== createOrder() 方法测试 ====================

    /**
     * 正向测试：成功创建订单
     */
    @Test
    void testCreateOrder_Success() {
        // 准备测试数据
        Long userId = 1L;
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setStoreId(1L);
        orderCreateDTO.setAddressId(1L);
        orderCreateDTO.setRemark("请尽快配送");
        
        OrderVO expectedOrder = new OrderVO();
        expectedOrder.setId(1L);
        expectedOrder.setOrderNo("ORDER_20250826_001");
        expectedOrder.setStoreId(1L);
        expectedOrder.setStoreName("测试餐厅");
        expectedOrder.setTotalAmount(new BigDecimal("45.50"));
        expectedOrder.setStatus(Order.OrderStatus.ORDER_CREATED);
        expectedOrder.setCreatedAt(LocalDateTime.now());
        
        // 模拟服务行为
        when(orderService.createOrder(userId, orderCreateDTO)).thenReturn(expectedOrder);
        
        // 执行测试
        OrderVO actualOrder = orderService.createOrder(userId, orderCreateDTO);
        
        // 验证结果
        assertNotNull(actualOrder, "创建的订单不应为null");
        assertEquals(1L, actualOrder.getId(), "订单ID应该匹配");
        assertEquals("ORDER_20250826_001", actualOrder.getOrderNo(), "订单号应该匹配");
        assertEquals(1L, actualOrder.getStoreId(), "店铺ID应该匹配");
        assertEquals("测试餐厅", actualOrder.getStoreName(), "店铺名称应该匹配");
        assertEquals(new BigDecimal("45.50"), actualOrder.getTotalAmount(), "订单总金额应该匹配");
        assertEquals(Order.OrderStatus.ORDER_CREATED, actualOrder.getStatus(), "订单状态应该为已创建");
        
        // 验证方法被调用
        verify(orderService, times(1)).createOrder(userId, orderCreateDTO);
    }

    /**
     * 反向测试：用户ID为null
     */
    @Test
    void testCreateOrder_NullUserId() {
        // 准备测试数据
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setStoreId(1L);
        
        // 模拟服务抛出异常
        when(orderService.createOrder(null, orderCreateDTO))
                .thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(null, orderCreateDTO);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).createOrder(null, orderCreateDTO);
    }

    /**
     * 反向测试：订单创建DTO为null
     */
    @Test
    void testCreateOrder_NullOrderCreateDTO() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟服务抛出异常
        when(orderService.createOrder(userId, null))
                .thenThrow(new IllegalArgumentException("订单创建信息不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(userId, null);
        });
        
        assertEquals("订单创建信息不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).createOrder(userId, null);
    }

    /**
     * 反向测试：购物车为空
     */
    @Test
    void testCreateOrder_EmptyCart() {
        // 准备测试数据
        Long userId = 1L;
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO();
        orderCreateDTO.setStoreId(1L);
        
        // 模拟服务抛出异常
        when(orderService.createOrder(userId, orderCreateDTO))
                .thenThrow(new RuntimeException("购物车为空，无法创建订单"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(userId, orderCreateDTO);
        });
        
        assertEquals("购物车为空，无法创建订单", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).createOrder(userId, orderCreateDTO);
    }

    // ==================== getOrderDetail() 方法测试 ====================

    /**
     * 正向测试：成功获取订单详情
     */
    @Test
    void testGetOrderDetail_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        OrderDetailVO expectedOrderDetail = new OrderDetailVO();
        expectedOrderDetail.setId(orderId);
        expectedOrderDetail.setOrderNo("ORDER_20250826_001");
        expectedOrderDetail.setStoreName("测试餐厅");
        expectedOrderDetail.setTotalAmount(new BigDecimal("45.50"));
        expectedOrderDetail.setStatus(Order.OrderStatus.MERCHANT_CONFIRMED);
        
        // 模拟服务行为
        when(orderService.getOrderDetail(userId, orderId)).thenReturn(expectedOrderDetail);
        
        // 执行测试
        OrderDetailVO actualOrderDetail = orderService.getOrderDetail(userId, orderId);
        
        // 验证结果
        assertNotNull(actualOrderDetail, "订单详情不应为null");
        assertEquals(orderId, actualOrderDetail.getId(), "订单ID应该匹配");
        assertEquals("ORDER_20250826_001", actualOrderDetail.getOrderNo(), "订单号应该匹配");
        assertEquals("测试餐厅", actualOrderDetail.getStoreName(), "店铺名称应该匹配");
        assertEquals(new BigDecimal("45.50"), actualOrderDetail.getTotalAmount(), "总金额应该匹配");
        assertEquals(Order.OrderStatus.MERCHANT_CONFIRMED, actualOrderDetail.getStatus(), "订单状态应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).getOrderDetail(userId, orderId);
    }

    /**
     * 反向测试：订单不存在
     */
    @Test
    void testGetOrderDetail_OrderNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 999L;
        
        // 模拟服务抛出异常
        when(orderService.getOrderDetail(userId, orderId))
                .thenThrow(new RuntimeException("订单不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.getOrderDetail(userId, orderId);
        });
        
        assertEquals("订单不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).getOrderDetail(userId, orderId);
    }

    /**
     * 反向测试：无权访问他人订单
     */
    @Test
    void testGetOrderDetail_UnauthorizedAccess() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L; // 属于其他用户的订单
        
        // 模拟服务抛出异常
        when(orderService.getOrderDetail(userId, orderId))
                .thenThrow(new RuntimeException("无权访问此订单"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.getOrderDetail(userId, orderId);
        });
        
        assertEquals("无权访问此订单", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).getOrderDetail(userId, orderId);
    }

    // ==================== cancelOrder() 方法测试 ====================

    /**
     * 正向测试：成功取消订单
     */
    @Test
    void testCancelOrder_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(orderService).cancelOrder(userId, orderId);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            orderService.cancelOrder(userId, orderId);
        }, "取消订单不应该抛出异常");
        
        // 验证方法被调用
        verify(orderService, times(1)).cancelOrder(userId, orderId);
    }

    /**
     * 反向测试：订单状态不允许取消
     */
    @Test
    void testCancelOrder_InvalidStatus() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("订单状态不允许取消"))
                .when(orderService).cancelOrder(userId, orderId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(userId, orderId);
        });
        
        assertEquals("订单状态不允许取消", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).cancelOrder(userId, orderId);
    }

    /**
     * 反向测试：取消不存在的订单
     */
    @Test
    void testCancelOrder_OrderNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 999L;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("订单不存在"))
                .when(orderService).cancelOrder(userId, orderId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.cancelOrder(userId, orderId);
        });
        
        assertEquals("订单不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).cancelOrder(userId, orderId);
    }

    // ==================== payOrder() 方法测试 ====================

    /**
     * 正向测试：成功支付订单
     */
    @Test
    void testPayOrder_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        PaymentDTO.PaymentType paymentType = PaymentDTO.PaymentType.WECHAT;
        
        PaymentResultVO expectedPaymentResult = new PaymentResultVO();
        expectedPaymentResult.setOrderId(orderId);
        expectedPaymentResult.setOrderNo("ORDER_20250826_001");
        expectedPaymentResult.setPaymentStatus(1); // 支付成功
        expectedPaymentResult.setPaymentMessage("支付成功");
        expectedPaymentResult.setPaymentAmount(new BigDecimal("45.50"));
        
        // 模拟服务行为
        when(orderService.payOrder(userId, orderId, paymentType)).thenReturn(expectedPaymentResult);
        
        // 执行测试
        PaymentResultVO actualPaymentResult = orderService.payOrder(userId, orderId, paymentType);
        
        // 验证结果
        assertNotNull(actualPaymentResult, "支付结果不应为null");
        assertEquals(1, actualPaymentResult.getPaymentStatus(), "支付状态应该为成功");
        assertEquals("ORDER_20250826_001", actualPaymentResult.getOrderNo(), "订单号应该匹配");
        assertEquals(new BigDecimal("45.50"), actualPaymentResult.getPaymentAmount(), "支付金额应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).payOrder(userId, orderId, paymentType);
    }

    /**
     * 反向测试：订单已支付
     */
    @Test
    void testPayOrder_AlreadyPaid() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        PaymentDTO.PaymentType paymentType = PaymentDTO.PaymentType.ALIPAY;
        
        // 模拟服务抛出异常
        when(orderService.payOrder(userId, orderId, paymentType))
                .thenThrow(new RuntimeException("订单已支付"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.payOrder(userId, orderId, paymentType);
        });
        
        assertEquals("订单已支付", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).payOrder(userId, orderId, paymentType);
    }

    /**
     * 反向测试：支付类型为null
     */
    @Test
    void testPayOrder_NullPaymentType() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        // 模拟服务抛出异常
        when(orderService.payOrder(userId, orderId, null))
                .thenThrow(new IllegalArgumentException("支付类型不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.payOrder(userId, orderId, null);
        });
        
        assertEquals("支付类型不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).payOrder(userId, orderId, null);
    }

    // ==================== confirmReceipt() 方法测试 ====================

    /**
     * 正向测试：成功确认收货
     */
    @Test
    void testConfirmReceipt_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        OrderDetailVO expectedOrderDetail = new OrderDetailVO();
        expectedOrderDetail.setId(orderId);
        expectedOrderDetail.setOrderNo("ORDER_20250826_001");
        expectedOrderDetail.setStatus(Order.OrderStatus.DELIVERED);
        
        // 模拟服务行为
        when(orderService.confirmReceipt(userId, orderId)).thenReturn(expectedOrderDetail);
        
        // 执行测试
        OrderDetailVO actualOrderDetail = orderService.confirmReceipt(userId, orderId);
        
        // 验证结果
        assertNotNull(actualOrderDetail, "确认收货后的订单详情不应为null");
        assertEquals(orderId, actualOrderDetail.getId(), "订单ID应该匹配");
        assertEquals(Order.OrderStatus.DELIVERED, actualOrderDetail.getStatus(), "订单状态应该为已送达");
        
        // 验证方法被调用
        verify(orderService, times(1)).confirmReceipt(userId, orderId);
    }

    /**
     * 反向测试：订单状态不允许确认收货
     */
    @Test
    void testConfirmReceipt_InvalidStatus() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        // 模拟服务抛出异常
        when(orderService.confirmReceipt(userId, orderId))
                .thenThrow(new RuntimeException("订单状态不允许确认收货"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.confirmReceipt(userId, orderId);
        });
        
        assertEquals("订单状态不允许确认收货", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).confirmReceipt(userId, orderId);
    }

    // ==================== urgeOrder() 方法测试 ====================

    /**
     * 正向测试：成功催单
     */
    @Test
    void testUrgeOrder_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(orderService).urgeOrder(userId, orderId);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            orderService.urgeOrder(userId, orderId);
        }, "催单不应该抛出异常");
        
        // 验证方法被调用
        verify(orderService, times(1)).urgeOrder(userId, orderId);
    }

    /**
     * 反向测试：催单过于频繁
     */
    @Test
    void testUrgeOrder_TooFrequent() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("催单过于频繁，请稍后再试"))
                .when(orderService).urgeOrder(userId, orderId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.urgeOrder(userId, orderId);
        });
        
        assertEquals("催单过于频繁，请稍后再试", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).urgeOrder(userId, orderId);
    }

    // ==================== addReview() 方法测试 ====================

    /**
     * 正向测试：成功添加订单评价
     */
    @Test
    void testAddReview_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setStoreRating(5);
        reviewDTO.setRiderRating(5);
        reviewDTO.setContent("非常满意");
        
        ReviewVO expectedReview = new ReviewVO();
        expectedReview.setId(1L);
        expectedReview.setOrderId(orderId);
        expectedReview.setStoreRating(5);
        expectedReview.setRiderRating(5);
        expectedReview.setContent("非常满意");
        
        // 模拟服务行为
        when(orderService.addReview(userId, orderId, reviewDTO)).thenReturn(expectedReview);
        
        // 执行测试
        ReviewVO actualReview = orderService.addReview(userId, orderId, reviewDTO);
        
        // 验证结果
        assertNotNull(actualReview, "评价结果不应为null");
        assertEquals(1L, actualReview.getId(), "评价ID应该匹配");
        assertEquals(orderId, actualReview.getOrderId(), "订单ID应该匹配");
        assertEquals(5, actualReview.getStoreRating(), "店铺评分应该匹配");
        assertEquals(5, actualReview.getRiderRating(), "骑手评分应该匹配");
        assertEquals("非常满意", actualReview.getContent(), "评价内容应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).addReview(userId, orderId, reviewDTO);
    }

    /**
     * 反向测试：订单已评价
     */
    @Test
    void testAddReview_AlreadyReviewed() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setStoreRating(5);
        
        // 模拟服务抛出异常
        when(orderService.addReview(userId, orderId, reviewDTO))
                .thenThrow(new RuntimeException("订单已评价"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.addReview(userId, orderId, reviewDTO);
        });
        
        assertEquals("订单已评价", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).addReview(userId, orderId, reviewDTO);
    }

    /**
     * 反向测试：评价DTO为null
     */
    @Test
    void testAddReview_NullReviewDTO() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        
        // 模拟服务抛出异常
        when(orderService.addReview(userId, orderId, null))
                .thenThrow(new IllegalArgumentException("评价信息不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.addReview(userId, orderId, null);
        });
        
        assertEquals("评价信息不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).addReview(userId, orderId, null);
    }

    /**
     * 反向测试：评分超出范围
     */
    @Test
    void testAddReview_InvalidRating() {
        // 准备测试数据
        Long userId = 1L;
        Long orderId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setStoreRating(6); // 超出1-5范围
        
        // 模拟服务抛出异常
        when(orderService.addReview(userId, orderId, reviewDTO))
                .thenThrow(new IllegalArgumentException("评分必须在1-5之间"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.addReview(userId, orderId, reviewDTO);
        });
        
        assertEquals("评分必须在1-5之间", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(orderService, times(1)).addReview(userId, orderId, reviewDTO);
    }
}
