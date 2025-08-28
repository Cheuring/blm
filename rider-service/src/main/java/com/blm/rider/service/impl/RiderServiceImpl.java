package com.blm.rider.service.impl;

import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.OrderStatusUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.entity.*;
import com.blm.common.exception.BusinessException;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.OrderServiceClient;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.service.BaseService;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;
import com.blm.rider.repository.RiderRepository;
import com.blm.rider.repository.RiderStatsRepository;
import com.blm.rider.service.RiderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class RiderServiceImpl extends BaseService implements RiderService {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private RiderStatsRepository riderStatsRepository;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private StoreServiceClient storeService;

    @Autowired
    private OrderServiceClient orderService;

    @Override
    @Transactional
    public void registerRider(Long userId, RiderRegisterDTO dto) {
        if (riderRepository.findByUserId(userId).isPresent()) {
            throw new CommonException(ExceptionConstant.RIDER_ALREADY_EXISTS);
        }

        User user = userService.getUserById(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));

        Rider rider = new Rider();
        BeanUtils.copyProperties(dto, rider);
        rider.setUserId(userId);
        rider.setStatus(Rider.RiderStatus.OFFLINE); // 默认下线
        rider.setLongitude(null);
        rider.setLatitude(null);
        rider.setCreatedAt(LocalDateTime.now());
        rider.setUpdatedAt(LocalDateTime.now());
        riderRepository.insert(rider);

        // 更新用户角色
        userService.updateRole(userId, user.getRole() + "," + User.UserRole.RIDER);
    }

    @Override
    @Transactional
    public void updateWorkStatus(Long userId, WorkStatusUpdateDTO dto) {
        riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
        if (riderRepository.updateWorkStatus(userId, dto.getWorkStatus(), LocalDateTime.now()) != 1) {
            throw new CommonException(ExceptionConstant.SYS_DATABASE_ERROR);
        }
    }

    @Override
    public Rider.RiderStatus getWorkStatus(Long userId) {
        return riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND))
                .getStatus();
    }

    @Override
    @Transactional
    public Long updateLocation(Long userId, LocationUpdateDTO dto) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
        if (riderRepository.updateLocation(userId, dto.getLongitude(), dto.getLatitude(), LocalDateTime.now()) != 1) {
            throw new CommonException(ExceptionConstant.SYS_DATABASE_ERROR);
        }

        // todo: websocket通知用户其骑手位置已更新
//        List<Order> orders = orderRepository.findByRiderId(userId);
//        for (Order order : orders) {
//            Map<String, Object> content = Map.of(
//                    "orderId", order.getId(),
//                    "orderNo", order.getOrderNo(),
//                    "riderAddress", dto
//            );
//            WebSocketHandler.notify(order.getUserId(), Message.MessageType.USER_RIDER_ADDR_UPDATE, content);
//            redisUtil.clearCacheEx(CacheConstant.RIDER_LOCATION, order.getId());
//        }

        return rider.getId();
    }

    @Override
    public List<RiderOrderVO> listAvailableOrders() {
        List<Order> orders = orderService.getAvailableOrders()
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        return getRiderOrderVOS(orders);
    }

    @Override
    public List<RiderOrderVO> listMyOrders(Long userId) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
        List<Order> orders = orderService.getOrdersByRiderId(rider.getId())
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        return getRiderOrderVOS(orders);
    }

    private List<RiderOrderVO> getRiderOrderVOS(List<Order> orders) {
        List<RiderOrderVO> result = new ArrayList<>();
        if (orders == null || orders.isEmpty()) {
            return result;
        }

        for (Order order : orders) {
            Store store = storeService.getStoreById(order.getStoreId())
                    .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));
            UserAddress userAddress = userService.getAddressById(order.getAddressId())
                    .orElseThrow(() -> new CommonException(ExceptionConstant.USER_ADDRESS_NOT_FOUND));

            RiderOrderVO vo = RiderOrderVO.builder()
                    .id(order.getId())
                    .orderNo(order.getOrderNo())
                    .orderStatus(order.getStatus())
                    .storeId(order.getStoreId())
                    .storeName(store.getName())
                    .storeImage(store.getLogo())
                    .storeAddress(store.getAddress())
                    .userId(order.getUserId())
                    .userName(userAddress.getReceiver())
                    .userAddress(userAddress.toString())
                    .deliveryFee(order.getDeliveryFee())
                    .paymentAmount(order.getPaymentAmount())
                    .createdAt(order.getCreatedAt())
                    .build();

            result.add(vo);
        }
        return result;
    }

    @Override
    @Transactional
    public void acceptOrder(Long userId, Long orderId) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));
        User user = userService.getUserById(rider.getId())
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));
        // 检查骑手工作状态
        if (!rider.getStatus().equals(Rider.RiderStatus.ONLINE)) {
            throw new CommonException(ExceptionConstant.RIDER_STATUS_ERROR);
        }

        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        int updated = orderService.assignRiderToOrder(orderId, rider.getId());
        if (updated != 1) {
            throw new CommonException(ExceptionConstant.RIDER_ASSIGN_FAILED);
        }

        Map<String, Object> content = Map.of(
                "orderId", order.getId(),
                "orderNo", order.getOrderNo(),
                "rider", Map.of(
                        "id", rider.getId(),
                        "name", rider.getRealName(),
                        "phone", user.getPhone()
                )
        );
        // todo: websocket通知用户其订单已被骑手接单
//        WebSocketHandler.notify(order.getUserId(), Message.MessageType.USER_ORDER_ACCEPTED, content);
    }

    @Override
    @Transactional
    public void pickupOrder(Long userId, Long orderId) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));

        // 检查订单状态转换是否合法
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        if (order.getRiderId() == null || !order.getRiderId().equals(rider.getId())) {
            throw new CommonException(ExceptionConstant.ORDER_UNAUTHORIZED);
        }

        // 验证状态转换的合法性
        if (!Order.OrderStatus.RIDER_ASSIGNED.equals(order.getStatus())) {
            throw new CommonException(ExceptionConstant.ORDER_STATUS_ERROR);
        }

        // 更新订单状态为已取餐，开始配送
        int updated = orderService.updateOrderStatusByRider(orderId, rider.getId(), Order.OrderStatus.FOOD_PICKED);
        if (updated != 1) {
            throw new CommonException(ExceptionConstant.SYS_DATABASE_ERROR);
        }

        Map<String, Object> content = Map.of(
                "orderId", order.getId(),
                "orderNo", order.getOrderNo(),
                "riderAddress", Map.of(
                        "longitude", rider.getLongitude(),
                        "latitude", rider.getLatitude()
                )
        );

        // todo: websocket通知用户其订单已被骑手取餐
//        WebSocketHandler.notify(order.getUserId(), Message.MessageType.USER_ORDER_PICKED, content);
    }

    @Override
    @Transactional
    public void delivered(Long userId, Long orderId) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));

        // 检查订单状态转换是否合法
        Order order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));

        if (order.getRiderId() == null || !order.getRiderId().equals(rider.getId())) {
            throw new CommonException(ExceptionConstant.ORDER_UNAUTHORIZED);
        }

        // 确保订单当前状态是配送中
        if (!Order.OrderStatus.FOOD_PICKED.equals(order.getStatus())) {
            throw new CommonException(ExceptionConstant.ORDER_STATUS_ERROR);
        }

        int updated = orderService.updateOrderStatusByRider(orderId, rider.getId(), Order.OrderStatus.DELIVERED);
        if (updated != 1) {
            throw new CommonException(ExceptionConstant.SYS_DATABASE_ERROR);
        }

        Map<String, Object> content = Map.of(
                "orderId", order.getId(),
                "orderNo", order.getOrderNo(),
                "riderAddress", Map.of(
                        "longitude", rider.getLongitude(),
                        "latitude", rider.getLatitude()
                )
        );

        // todo: websocket通知用户其订单已被骑手送达
//        WebSocketHandler.notify(order.getUserId(), Message.MessageType.USER_ORDER_DELIVERED, content);

        // 更新当日统计数据
        updateRiderDailyStats(rider.getId());
    }

    /**
     * 更新骑手当日统计数据
     *
     * @param riderId 骑手ID
     */
    private void updateRiderDailyStats(Long riderId) {
        LocalDate today = LocalDate.now();

        // 获取今日已送达订单数量
        List<Order> completedOrders = orderService.getOrdersByRiderIdAndStatusByTime(riderId, Order.OrderStatus.COMPLETED, today)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        // 计算总收入和平均配送时间
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalMinutes = BigDecimal.ZERO;

        for (Order order : completedOrders) {
            // 假设骑手每单有固定收入
            totalIncome = totalIncome.add(new BigDecimal("5.00"));

            // 计算配送时间（从接单到送达）
            if (order.getActualTime() != null && order.getCreatedAt() != null) {
                long minutes = java.time.Duration.between(order.getCreatedAt(), order.getActualTime()).toMinutes();
                totalMinutes = totalMinutes.add(new BigDecimal(minutes));
            }
        }

        // 计算平均配送时间
        BigDecimal avgDeliveryTime = completedOrders.isEmpty() ? BigDecimal.ZERO :
                totalMinutes.divide(new BigDecimal(completedOrders.size()), 2, RoundingMode.HALF_UP);

        // 获取今日取消订单数量
        List<Order> canceledOrders = orderService.getOrdersByRiderIdAndStatusByTime(riderId, Order.OrderStatus.CANCELLED, today)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        // 更新骑手统计数据
        RiderStats stats = new RiderStats();
        stats.setRiderId(riderId);
        stats.setDate(today);
        stats.setOrdersCount(completedOrders.size() + canceledOrders.size());
        stats.setCompletedOrders(completedOrders.size());
        stats.setCanceledOrders(canceledOrders.size());
        stats.setTotalIncome(totalIncome);
//        stats.setAvgDeliveryTime(avgDeliveryTime);
        stats.setCreatedAt(LocalDateTime.now());
        stats.setUpdatedAt(LocalDateTime.now());

        riderStatsRepository.insertOrUpdate(stats);
    }

    @Override
    public RiderStatsVO getStats(Long userId, String period) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));

        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate = today;

        // 根据统计周期确定开始日期
        switch (period) {
            case "day":
                startDate = today;
                break;
            case "week":
                // 获取本周的第一天（星期一）
                startDate = today.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
                break;
            case "month":
                // 获取本月的第一天
                startDate = today.with(TemporalAdjusters.firstDayOfMonth());
                break;
            default:
                throw BusinessException.of(400, "无效的统计周期");
        }

        // 查询统计数据
        Integer ordersCount = riderStatsRepository.countOrdersByRiderIdAndDateRange(rider.getId(), startDate, endDate);
        Integer completedOrders = riderStatsRepository.countCompletedOrdersByRiderIdAndDateRange(rider.getId(), startDate, endDate);
        Integer canceledOrders = riderStatsRepository.countCanceledOrdersByRiderIdAndDateRange(rider.getId(), startDate, endDate);
        BigDecimal totalIncome = riderStatsRepository.sumIncomeByRiderIdAndDateRange(rider.getId(), startDate, endDate);

        // 计算完成率
        BigDecimal completionRate = BigDecimal.ZERO;
        if (ordersCount > 0) {
            completionRate = new BigDecimal(completedOrders)
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(ordersCount), 2, RoundingMode.HALF_UP);
        }

        // 构建返回对象
        RiderStatsVO statsVO = new RiderStatsVO();
        statsVO.setStartDate(startDate);
        statsVO.setEndDate(endDate);
        statsVO.setOrdersCount(ordersCount);
        statsVO.setCompletedOrders(completedOrders);
        statsVO.setCanceledOrders(canceledOrders);
        statsVO.setTotalIncome(totalIncome);
        statsVO.setCompletionRate(completionRate);

        return statsVO;
    }
}