package com.blm.order.service;

import com.blm.common.dto.*;
import com.blm.common.entity.Order;
import com.blm.common.entity.User;
import com.blm.common.vo.*;

public interface OrderService {
    /**
     * 创建新订单
     *
     * @param userId 用户ID
     * @param dto    订单创建DTO
     * @return 创建的订单VO
     */
    OrderVO createOrder(Long userId, OrderCreateDTO dto);


    PageVO<OrderVO> listStoreOrders(Long storeId, Order.OrderStatus status, int page, int size);


    /**
     * 分页获取用户订单
     *
     * @param userId 用户ID
     * @param status 订单状态筛选
     * @param page   页码
     * @param size   每页大小
     * @return 分页订单VO列表
     */
    PageVO<OrderVO> listUserOrders(Long userId, Order.OrderStatus status, int page, int size);

    /**
     * 获取订单详情
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情VO
     */
    OrderDetailVO getOrderDetail(Long userId, Long orderId);

    /**
     * 取消订单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    void cancelOrder(Long userId, Long orderId);

    /**
     * 订单支付
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 支付结果VO
     */
    PaymentResultVO payOrder(Long userId, Long orderId, PaymentDTO.PaymentType paymentType);

    /**
     * 模拟支付(简化版)
     *
     * @param userId 用户ID
     * @param dto    支付DTO
     * @return 支付结果VO
     */
    PaymentResultVO simulatePayment(Long userId, PaymentDTO dto);

    /**
     * 催单
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     */
    void urgeOrder(Long userId, Long orderId);

    /**
     * 确认收货
     *
     * @param userId  用户ID
     * @param orderId 订单ID
     * @return 订单详情VO
     */
    OrderDetailVO confirmReceipt(Long userId, Long orderId);

    /**
     * 评价订单
     *
     * @param userId    用户ID
     * @param orderId   订单ID
     * @param reviewDTO 评价DTO
     * @return 评价VO
     */
    ReviewVO addReview(Long userId, Long orderId, ReviewDTO reviewDTO);

    /**
     * 商家用于更新订单状态 (例如: `CONFIRMED` - 已接单, `PREPARING` - 备餐中, `READY_FOR_PICKUP` - 备餐完成)
     */
    void updateOrderStatusByStore(Long orderId, Long storeId, OrderStatusUpdateDTO dto, User user);


    OrderDetailVO getOrderDetailwithStore(Long orderId, Long storeId);

    OrderDetailVO getOrderDetail(Long orderId);

    LocationUpdateDTO getRiderLocation(Long userId, Long orderId);
}