package com.blm.order.service.impl;


import com.blm.common.dto.*;
import com.blm.common.entity.*;
import com.blm.common.exception.BusinessException;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.RiderServiceClient;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.vo.*;
import com.blm.order.repository.CartRepository;
import com.blm.order.repository.OrderDetailRepository;
import com.blm.order.repository.OrderRepository;
import com.blm.order.repository.ReviewRepository;
import com.blm.order.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderDetailRepository detailRepository;

    @Autowired
    private StoreServiceClient storeService;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private RiderServiceClient riderService;

    @Override
    @Transactional
    public OrderVO createOrder(Long userId, OrderCreateDTO dto) {
        // 获取购物车
        List<Cart> carts = cartRepository.findByUserStore(userId, dto.getStoreId());
        if (carts.isEmpty()) {
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }

        // 检查地址是否存在
        userService.getAddressByUserIdAndId(userId, dto.getAddressId())
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_ADDRESS_NOT_FOUND));

        // 检查店铺是否营业
        Store store = storeService.getStoreByIdAndStatus(dto.getStoreId(), Store.StoreStatus.OPEN)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_CLOSED));

        // 生成订单
        Order order = new Order();
        String orderNo = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStoreId(dto.getStoreId());
        order.setAddressId(dto.getAddressId());
        order.setRemark(dto.getRemark());
        order.setStatus(Order.OrderStatus.ORDER_CREATED);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        // 计算金额
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal deliveryFee = store.getDeliveryFee();

        for (Cart c : carts) {
            Food food = storeService.getFoodByIdAndStatus(c.getFoodId(), Food.FoodStatus.ON_SHELF)
                    .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_STATUS_ERROR));

            BigDecimal price = food.getPrice();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(c.getQuantity()));
            total = total.add(amount);
        }

        // 检查是否达到最低订单金额
        if (total.compareTo(store.getMinOrderAmount()) < 0) {
            throw BusinessException.of(ExceptionConstant.ORDER_AMOUNT_BELOW.getCode(), "未达到店铺最低订单金额 " + store.getMinOrderAmount() + "元");
        }

        order.setTotalAmount(total);
        order.setDeliveryFee(deliveryFee);
        order.setDiscountAmount(BigDecimal.ZERO);  // todo:此处可以添加优惠逻辑
        order.setPaymentAmount(total.add(deliveryFee));
        orderRepository.insert(order);

        // 保存明细并清空对应购物车项
        for (Cart c : carts) {
            Food food = storeService.getFoodById(c.getFoodId()).orElse(new Food());

            OrderDetail detail = new OrderDetail();
            detail.setOrderId(order.getId());
            detail.setFoodId(c.getFoodId());
            detail.setFoodName(food.getName());
            detail.setFoodImage(food.getImage());
            detail.setPrice(food.getPrice());
            detail.setQuantity(c.getQuantity());
            detail.setAmount(food.getPrice().multiply(BigDecimal.valueOf(c.getQuantity())));
            detail.setCreatedAt(LocalDateTime.now());
            detailRepository.insert(detail);

            cartRepository.deleteByIdAndUserId(c.getId(), userId);
        }

        // 返回VO
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStoreName(store.getName());
        return vo;
    }

    private List<OrderVO> order2vo(List<Order> orders) {
        return orders.stream().map(order -> {
            Store store = storeService.getStoreById(order.getStoreId()).orElse(new Store());
            OrderVO vo = new OrderVO();
            BeanUtils.copyProperties(order, vo);
            vo.setStoreName(store.getName());
            vo.setStoreImage(store.getLogo());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public PageVO<OrderVO> listStoreOrders(Long storeId, Order.OrderStatus status, int page, int size) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(page, size);

        List<Order> orders;
        if (status != null) {
            orders = orderRepository.findByStoreIdAndStatus(storeId, status);
        } else {
            orders = orderRepository.findAllByStoreId(storeId);
        }

        return getPageVO(page, size, orders);
    }

    @Override
    public PageVO<OrderVO> listUserOrders(Long userId, Order.OrderStatus status, int page, int size) {
        // 使用PageHelper进行分页查询
        PageHelper.startPage(page, size);

        List<Order> orders;
        if (status != null) {
            orders = orderRepository.findByUserIdAndStatus(userId, status);
        } else {
            orders = orderRepository.findAllByUserId(userId);
        }

        return getPageVO(page, size, orders);
    }

    private PageVO<OrderVO> getPageVO(int page, int size, List<Order> orders) {
        // 获取分页信息
        Page<Order> pageInfo = (Page<Order>) orders;
        // 转换为VO列表
        List<OrderVO> orderVOs = order2vo(orders);
        // 创建Spring分页对象
        return new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), orderVOs);
    }

    @Override
    public OrderDetailVO getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        return getOrderDetailVOFromOrder(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        if (order.getStatus().compareTo(Order.OrderStatus.PENDING) > 0) {
            throw new CommonException(ExceptionConstant.ORDER_STATUS_ERROR);
        }

        int updated = orderRepository.updateStatus(orderId, userId, Order.OrderStatus.CANCELLED, LocalDateTime.now());
        // todo: 退款逻辑
        if (updated != 1) {
            throw new CommonException(ExceptionConstant.SYS_DATABASE_ERROR);
        }
    }

    @Override
    @Transactional
    public PaymentResultVO payOrder(Long userId, Long orderId, PaymentDTO.PaymentType paymentType) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));
        Long merchantId = storeService.getStoreOwnerIdByStoreId(order.getStoreId())
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));

        if (!order.getStatus().equals(Order.OrderStatus.ORDER_CREATED)) {
            throw new CommonException(ExceptionConstant.ORDER_STATUS_ERROR);
        }

        // 这里应该调用实际的支付网关
        // 支付成功后更新订单状态
        int insertRes = orderRepository.updatePaymentType(orderId, userId, paymentType, LocalDateTime.now());
        int updateRes = orderRepository.updateStatus(orderId, userId, Order.OrderStatus.PENDING, LocalDateTime.now());

        if (updateRes + insertRes != 2) {
            throw new CommonException(ExceptionConstant.SYS_DATABASE_ERROR);
        }

        // 通知商家 todo: websocket

        PaymentResultVO res = new PaymentResultVO();
        res.setOrderId(orderId);
        res.setOrderNo(order.getOrderNo());
        res.setPaymentStatus(Order.PAID);
        res.setPaymentMessage("支付成功");
        res.setPaymentAmount(order.getPaymentAmount());
        res.setPaymentTime(LocalDateTime.now());

        return res;
    }

    @Override
    @Transactional
    public PaymentResultVO simulatePayment(Long userId, PaymentDTO dto) {
        return payOrder(userId, dto.getOrderId(), dto.getPaymentType());
    }

    @Override
    @Transactional
    public void urgeOrder(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        Long merchantId = storeService.getStoreOwnerIdByStoreId(order.getStoreId())
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));
        Long riderId = order.getRiderId();
        // 如果已经完成或取消
        if (order.getStatus().compareTo(Order.OrderStatus.COMPLETED) >= 0) {
            throw new CommonException(ExceptionConstant.ORDER_STATUS_ERROR);
        }

        // 催单
        // 实际逻辑：记录催单记录，通知商家或骑手
        // 这里简化为检查状态即可
        Map<String, Object> urgeData = Map.of(
                "orderId", orderId,
                "orderNo", order.getOrderNo(),
                "userId", userId
        );
        // todo: websocket notify rider and merchant
//        WebSocketHandler.notify(merchantId, Message.MessageType.USER_ORDER_URGENT, urgeData);
//        if(riderId != null) {
//            Long riderUserid = riderRepository.findById(riderId).get().getUserId();
//            WebSocketHandler.notify(riderUserid, Message.MessageType.USER_ORDER_URGENT, urgeData);
//        }
    }

    @Override
    @Transactional
    public OrderDetailVO confirmReceipt(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        if (!order.getStatus().equals(Order.OrderStatus.DELIVERING) &&
                !order.getStatus().equals(Order.OrderStatus.DELIVERED)) {
            throw new CommonException(ExceptionConstant.ORDER_STATUS_ERROR);
        }

        int updated = orderRepository.updateStatus(orderId, userId, Order.OrderStatus.COMPLETED, LocalDateTime.now());
        if (updated != 1) {
            throw new CommonException(ExceptionConstant.SYS_DATABASE_ERROR);
        }

        return getOrderDetail(userId, orderId);
    }

    @Override
    @Transactional
    public ReviewVO addReview(Long userId, Long orderId, ReviewDTO reviewDTO) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        if (!order.getStatus().equals(Order.OrderStatus.COMPLETED)) {
            throw new CommonException(ExceptionConstant.ORDER_STATUS_ERROR);
        }

        // 检查是否已评价
        if (reviewRepository.existsByOrderId(orderId)) {
            throw new CommonException(ExceptionConstant.REVIEW_ALREADY_EXISTS);
        }

        Review review = new Review();
        BeanUtils.copyProperties(reviewDTO, review);
        BeanUtils.copyProperties(order, review);
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());
        review.setId(null);

        reviewRepository.save(review);

        // todo: 更新店铺评分（实际应该有更复杂的计算逻辑）
        // 此处简化处理

        ReviewVO vo = new ReviewVO();
        BeanUtils.copyProperties(review, vo);
        return vo;
    }

    /**
     * 商家用于更新订单状态 (例如: `CONFIRMED` - 已接单, `PREPARING` - 备餐中, `READY_FOR_PICKUP` - 备餐完成)
     */
    @Override
    @Transactional
    public void updateOrderStatusByStore(Long orderId, Long storeId, OrderStatusUpdateDTO dto, User user) {
        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));
        OrderTracking orderTracking = new OrderTracking();
        orderTracking.setOrderId(orderId);
        orderTracking.setStatus(dto.getOrderStatus());
        orderTracking.setCreatedAt(LocalDateTime.now());
        orderTracking.setOperatorId(user.getId());
        orderTracking.setOperatorType(user.getRole());
        orderTracking.setStatus(dto.getOrderStatus());

        orderRepository.insertOrderTraceByMerchant(orderTracking);
        orderRepository.updateStatusByStore(orderId, storeId, orderTracking.getStatus(), LocalDateTime.now());
    }

    @Override
    public OrderDetailVO getOrderDetailwithStore(Long orderId, Long storeId) {
        Order order = orderRepository.findByIdAndStoreId(orderId, storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        return getOrderDetailVOFromOrder(order);
    }

    @Override
    public OrderDetailVO getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        return getOrderDetailVOFromOrder(order);
    }

    public OrderDetailVO getOrderDetailVOFromOrder(Order order) {
        Store store = storeService.getStoreById(order.getStoreId()).orElse(new Store());
        UserAddress address = userService.getAddressById(order.getAddressId()).orElse(null);

        OrderDetailVO vo = new OrderDetailVO();
        BeanUtils.copyProperties(order, vo);
        vo.setStoreName(store.getName());

        if (address != null) {
            UserAddressVO addressVO = new UserAddressVO();
            BeanUtils.copyProperties(address, addressVO);
            vo.setDeliveryAddress(addressVO);
        }

        if (order.getRiderId() != null) {
            Rider rider = riderService.getRiderById(order.getRiderId())
                    .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
            vo.setRiderName(rider.getRealName());
            User rider_user = userService.getUserById(rider.getUserId())
                    .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
            vo.setRiderPhone(rider_user.getPhone());
        }

        List<OrderItemVO> items = detailRepository.findByOrderId(order.getId()).stream().map(d -> {
            OrderItemVO ivo = new OrderItemVO();
            BeanUtils.copyProperties(d, ivo);
            return ivo;
        }).collect(Collectors.toList());

        vo.setItems(items);
        return vo;
    }

    @Override
    public LocationUpdateDTO getRiderLocation(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));
        if (order.getRiderId() == null) {
            throw new CommonException(ExceptionConstant.RIDER_NOT_FOUND);
        }
        Rider rider = riderService.getRiderById(order.getRiderId())
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
        LocationUpdateDTO dto = new LocationUpdateDTO();
        dto.setLatitude(rider.getLatitude());
        dto.setLongitude(rider.getLongitude());

        return dto;
    }

    @Override
    public PageVO<OrderVO> findByConditions(Order.OrderStatus status, Long userId, Long storeId, Long riderId, int page, int size) {
        PageHelper.startPage(page, size);
        List<Order> byConditions = orderRepository.findByConditions(status, userId, storeId, riderId);
        return getPageVO(page, size, byConditions);
    }
}