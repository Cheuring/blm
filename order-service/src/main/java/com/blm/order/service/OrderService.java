package com.blm.order.service;

import com.blm.common.dto.OrderCreateDTO;
import com.blm.common.dto.PaymentDTO;
import com.blm.common.vo.OrderDetailVO;
import com.blm.common.vo.OrderVO;

import java.util.List;

public interface OrderService {
    
    /**
     * 创建订单
     */
    OrderVO createOrder(Long userId, OrderCreateDTO dto);
    
    /**
     * 获取用户订单列表
     */
    List<OrderVO> getUserOrders(Long userId, Integer page, Integer size);
    
    /**
     * 获取订单详情
     */
    OrderDetailVO getOrderDetail(Long userId, Long orderId);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long userId, Long orderId);
    
    /**
     * 支付订单
     */
    void payOrder(Long userId, PaymentDTO dto);
    
    /**
     * 确认收货
     */
    OrderDetailVO confirmReceipt(Long userId, Long orderId);
    
    /**
     * 更新订单状态 (商家调用)
     */
    void updateOrderStatus(Long orderId, Long storeId, String status);
    
    /**
     * 获取商家订单列表
     */
    List<OrderVO> getStoreOrders(Long storeId, Integer page, Integer size, String status);
    
    /**
     * 骑手接受订单
     */
    void acceptOrder(Long orderId, Long riderId);
    
    /**
     * 完成配送
     */
    void completeDelivery(Long orderId, Long riderId);
}
