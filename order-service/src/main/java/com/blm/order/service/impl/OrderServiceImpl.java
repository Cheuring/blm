package com.blm.order.service.impl;

import com.blm.common.dto.OrderCreateDTO;
import com.blm.common.dto.PaymentDTO;
import com.blm.common.entity.Cart;
import com.blm.common.entity.Order;
import com.blm.common.entity.OrderDetail;
import com.blm.common.entity.Store;
import com.blm.common.exception.BusinessException;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.Result;
import com.blm.common.vo.*;
import com.blm.order.repository.CartRepository;
import com.blm.order.repository.OrderDetailRepository;
import com.blm.order.repository.OrderRepository;
import com.blm.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartRepository cartRepository;
    private final StoreServiceClient storeServiceClient;
    private final UserServiceClient userServiceClient;
    
    @Override
    @Transactional
    public OrderVO createOrder(Long userId, OrderCreateDTO dto) {
        // 获取购物车商品
        List<Cart> carts = cartRepository.findByUserIdAndStoreId(userId, dto.getStoreId());
        if (carts.isEmpty()) {
            throw new BusinessException("购物车为空");
        }
        
        // 验证店铺存在且营业
        Result<StoreVO> storeResult = storeServiceClient.getStoreById(dto.getStoreId());
        if (!storeResult.isSuccess() || storeResult.getData() == null) {
            throw new BusinessException("店铺不存在");
        }
        StoreVO store = storeResult.getData();
        if (!Store.StoreStatus.OPEN.equals(store.getStatus())) {
            throw new BusinessException("店铺暂停营业");
        }
        
        // 验证地址存在
        Result<UserAddressVO> addressResult = userServiceClient.getAddressById(dto.getAddressId());
        if (!addressResult.isSuccess() || addressResult.getData() == null) {
            throw new BusinessException("收货地址不存在");
        }
        
        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setStoreId(dto.getStoreId());
        order.setAddressId(dto.getAddressId());
        order.setRemark(dto.getRemark());
        order.setStatus(Order.OrderStatus.ORDER_CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setExpectedTime(LocalDateTime.now().plusMinutes(45)); // 预计45分钟送达
        
        // 计算金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal deliveryFee = store.getDeliveryFee() != null ? store.getDeliveryFee() : BigDecimal.ZERO;
        
        for (Cart cart : carts) {
            // 验证商品
            Result<FoodVO> foodResult = storeServiceClient.getFoodById(cart.getFoodId());
            if (!foodResult.isSuccess() || foodResult.getData() == null) {
                throw new BusinessException("商品 " + cart.getFoodId() + " 不存在");
            }
            
            FoodVO food = foodResult.getData();
            if (!food.getStatus().equals(com.blm.common.entity.Food.FoodStatus.ON_SHELF)) {
                throw new BusinessException("商品 " + food.getName() + " 已下架");
            }
            
            BigDecimal itemAmount = food.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            totalAmount = totalAmount.add(itemAmount);
        }
        
        // 检查起送金额
        if (store.getMinOrderAmount() != null && totalAmount.compareTo(store.getMinOrderAmount()) < 0) {
            throw new BusinessException("未达到起送金额 " + store.getMinOrderAmount() + " 元");
        }
        
        order.setTotalAmount(totalAmount);
        order.setDeliveryFee(deliveryFee);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setPaymentAmount(totalAmount.add(deliveryFee));
        
        orderRepository.insert(order);
        
        // 保存订单明细并清空购物车
        for (Cart cart : carts) {
            Result<FoodVO> foodResult = storeServiceClient.getFoodById(cart.getFoodId());
            FoodVO food = foodResult.getData();
            
            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getId());
            detail.setFoodId(cart.getFoodId());
            detail.setFoodName(food.getName());
            detail.setFoodImage(food.getImage());
            detail.setPrice(food.getPrice());
            detail.setQuantity(cart.getQuantity());
            detail.setAmount(food.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
            detail.setCreatedAt(LocalDateTime.now());
            
            orderDetailRepository.insert(detail);
            cartRepository.deleteById(cart.getId());
        }
        
        // 构建返回VO
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setStoreName(store.getName());
        
        return orderVO;
    }
    
    @Override
    public List<OrderVO> getUserOrders(Long userId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<Order> orders = orderRepository.findByUserIdWithPagination(userId, size, offset);
        
        return orders.stream().map(this::buildOrderVO).collect(Collectors.toList());
    }
    
    @Override
    public OrderDetailVO getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        return buildOrderDetailVO(order);
    }
    
    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        // 只有未支付的订单才能取消
        if (!Order.OrderStatus.ORDER_CREATED.equals(order.getStatus()) && 
            !Order.OrderStatus.PENDING_PAYMENT.equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许取消");
        }
        
        orderRepository.updateStatus(orderId, Order.OrderStatus.CANCELLED, LocalDateTime.now());
    }
    
    @Override
    @Transactional
    public void payOrder(Long userId, PaymentDTO dto) {
        Order order = orderRepository.findByIdAndUserId(dto.getOrderId(), userId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        if (!Order.OrderStatus.ORDER_CREATED.equals(order.getStatus()) && 
            !Order.OrderStatus.PENDING_PAYMENT.equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许支付");
        }
        
        orderRepository.updatePayment(dto.getOrderId(), userId, dto.getPaymentType(), 
                Order.OrderStatus.PAID, LocalDateTime.now());
    }
    
    @Override
    @Transactional
    public OrderDetailVO confirmReceipt(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        if (!Order.OrderStatus.DELIVERED.equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许确认收货");
        }
        
        orderRepository.updateStatusAndActualTime(orderId, Order.OrderStatus.COMPLETED, 
                LocalDateTime.now(), LocalDateTime.now());
        
        // 重新查询并返回
        Order updatedOrder = orderRepository.findById(orderId).orElseThrow();
        return buildOrderDetailVO(updatedOrder);
    }
    
    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Long storeId, String status) {
        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        Order.OrderStatus newStatus;
        try {
            newStatus = Order.OrderStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("无效的订单状态");
        }
        
        orderRepository.updateStatus(orderId, newStatus, LocalDateTime.now());
    }
    
    @Override
    public List<OrderVO> getStoreOrders(Long storeId, Integer page, Integer size, String status) {
        int offset = (page - 1) * size;
        List<Order> orders = orderRepository.findByStoreIdWithPagination(storeId, status, size, offset);
        
        return orders.stream().map(this::buildOrderVO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void acceptOrder(Long orderId, Long riderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        if (!Order.OrderStatus.READY_FOR_PICKUP.equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许接单");
        }
        
        orderRepository.updateRiderAndStatus(orderId, riderId, Order.OrderStatus.DISPATCHED, 
                LocalDateTime.now());
    }
    
    @Override
    @Transactional
    public void completeDelivery(Long orderId, Long riderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        if (!order.getRiderId().equals(riderId)) {
            throw new BusinessException("只能完成自己的配送订单");
        }
        
        if (!Order.OrderStatus.DELIVERING.equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许完成配送");
        }
        
        orderRepository.updateStatusAndActualTime(orderId, Order.OrderStatus.DELIVERED, 
                LocalDateTime.now(), LocalDateTime.now());
    }
    
    @Override
    @Transactional
    public void pickupOrder(Long orderId, Long riderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException("订单不存在"));
        
        if (!order.getRiderId().equals(riderId)) {
            throw new BusinessException("只能取自己接受的订单");
        }
        
        if (!Order.OrderStatus.DISPATCHED.equals(order.getStatus())) {
            throw new BusinessException("订单状态不允许取餐");
        }
        
        orderRepository.updateStatusAndActualTime(orderId, Order.OrderStatus.DELIVERING, 
                LocalDateTime.now(), LocalDateTime.now());
    }
    
    @Override
    public List<RiderOrderVO> getAvailableOrders() {
        // 获取状态为READY_FOR_PICKUP的订单
        List<Order> orders = orderRepository.findByStatus(Order.OrderStatus.READY_FOR_PICKUP);
        return orders.stream().map(this::buildRiderOrderVO).collect(Collectors.toList());
    }
    
    @Override
    public List<RiderOrderVO> getRiderOrders(Long riderId) {
        List<Order> orders = orderRepository.findByRiderId(riderId);
        return orders.stream().map(this::buildRiderOrderVO).collect(Collectors.toList());
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORDER_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * 构建OrderVO
     */
    private OrderVO buildOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        
        // 获取店铺信息
        try {
            Result<StoreVO> storeResult = storeServiceClient.getStoreById(order.getStoreId());
            if (storeResult.isSuccess() && storeResult.getData() != null) {
                StoreVO store = storeResult.getData();
                orderVO.setStoreName(store.getName());
                orderVO.setStoreImage(store.getLogo());
            }
        } catch (Exception e) {
            log.warn("获取店铺信息失败: {}", e.getMessage());
            orderVO.setStoreName("未知店铺");
        }
        
        return orderVO;
    }
    
    /**
     * 构建RiderOrderVO
     */
    private RiderOrderVO buildRiderOrderVO(Order order) {
        RiderOrderVO riderOrderVO = new RiderOrderVO();
        BeanUtils.copyProperties(order, riderOrderVO);
        riderOrderVO.setOrderStatus(order.getStatus());
        
        // 获取店铺信息
        try {
            Result<StoreVO> storeResult = storeServiceClient.getStoreById(order.getStoreId());
            if (storeResult.isSuccess() && storeResult.getData() != null) {
                StoreVO store = storeResult.getData();
                riderOrderVO.setStoreName(store.getName());
                riderOrderVO.setStoreImage(store.getLogo());
                riderOrderVO.setStoreAddress(store.getAddress());
            }
        } catch (Exception e) {
            log.error("获取店铺信息失败", e);
        }
        
        // 获取用户信息
        try {
            Result<UserVO> userResult = userServiceClient.getUserById(order.getUserId());
            if (userResult.isSuccess() && userResult.getData() != null) {
                UserVO user = userResult.getData();
                riderOrderVO.setUserName(user.getFullName());
            }
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
        }
        
        // 获取用户地址信息
        try {
            Result<UserAddressVO> addressResult = userServiceClient.getAddressById(order.getAddressId());
            if (addressResult.isSuccess() && addressResult.getData() != null) {
                UserAddressVO address = addressResult.getData();
                riderOrderVO.setUserAddress(address.getDetailAddress());
            }
        } catch (Exception e) {
            log.error("获取用户地址信息失败", e);
        }
        
        return riderOrderVO;
    }
    private OrderDetailVO buildOrderDetailVO(Order order) {
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtils.copyProperties(order, orderDetailVO);
        
        // 获取店铺信息
        try {
            Result<StoreVO> storeResult = storeServiceClient.getStoreById(order.getStoreId());
            if (storeResult.isSuccess() && storeResult.getData() != null) {
                orderDetailVO.setStoreName(storeResult.getData().getName());
            }
        } catch (Exception e) {
            log.warn("获取店铺信息失败: {}", e.getMessage());
        }
        
        // 获取地址信息
        try {
            Result<UserAddressVO> addressResult = userServiceClient.getAddressById(order.getAddressId());
            if (addressResult.isSuccess() && addressResult.getData() != null) {
                orderDetailVO.setDeliveryAddress(addressResult.getData());
            }
        } catch (Exception e) {
            log.warn("获取地址信息失败: {}", e.getMessage());
        }
        
        // 获取订单项
        List<OrderDetail> details = orderDetailRepository.findByOrderId(order.getId());
        List<OrderItemVO> items = details.stream().map(detail -> {
            OrderItemVO itemVO = new OrderItemVO();
            BeanUtils.copyProperties(detail, itemVO);
            return itemVO;
        }).collect(Collectors.toList());
        orderDetailVO.setItems(items);
        
        return orderDetailVO;
    }
}
