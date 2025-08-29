package com.blm.admin.service.impl;

import com.blm.admin.service.AdminService;
import com.blm.common.dto.AuditDTO;
import com.blm.common.dto.StoreCategoryDTO;
import com.blm.common.entity.*;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.OrderServiceClient;
import com.blm.common.feign.RiderServiceClient;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.service.BaseService;
import com.blm.common.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.blm.common.dto.AuditDTO.AuditStatus.APPROVED;
import static com.blm.common.dto.AuditDTO.AuditStatus.SUSPENDED;

/**
 * 管理员服务实现类
 */
@Slf4j
@Service
public class AdminServiceImpl extends BaseService implements AdminService {

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private StoreServiceClient storeService;

    @Autowired
    private RiderServiceClient riderService;

    @Autowired
    private OrderServiceClient orderService;

    /**
     * 获取用户列表
     */
    @Override
    public PageVO<UserVO> listUsers(User.UserRole role, Integer status, String keyword, int page, int size) {
        // 验证状态值是否合法
        if (status != null) {
            if (status != 0 && status != 1) {
                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
            }
        }

        // 根据条件查询用户列表
        PageVO<UserVO> users = userService.getByConditions(role, status, keyword, page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        // 构造分页结果
        return users;
    }

    /**
     * 更新用户状态
     */
    @Override
    @Transactional
    public void updateUserStatus(Long userId, AuditDTO dto) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));

        AuditDTO.AuditStatus status = dto.getStatus();
        // 验证状态值是否合法
        if (APPROVED.equals(status)) {
            // 重复激活
            if (User.ACTIVE == user.getStatus()) {
                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
            }

            user.setStatus(User.ACTIVE);
        } else if (SUSPENDED.equals(status)) {
            // 重复禁用
            if (User.INACTIVE == user.getStatus()) {
                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
            }

            user.setStatus(User.INACTIVE);
        } else {
            // 如果状态不合法，抛出异常
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }

        user.setUpdatedAt(LocalDateTime.now());
        userService.updateStatus(userId, user.getStatus());
    }

    /**
     * 获取商家店铺列表
     */
    @Override
    public PageVO<StoreVO> listStores(Store.StoreStatus status, String keyword, int page, int size) {
        // 根据条件查询店铺列表
        PageVO<StoreVO> stores = storeService.getStoreByConditions(status, keyword, page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        return stores;
    }

    /**
     * 审核店铺
     */
    @Override
    @Transactional
    public void auditStore(Long storeId, AuditDTO dto) {
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));

        // 验证状态值是否合法
        AuditDTO.AuditStatus status = dto.getStatus();
        if (APPROVED.equals(status)) {
            // 只有PENDING的店铺可以被审核通过(APPROVED)
//            if(!Store.StoreStatus.PENDING.equals(store.getStatus())) {
//                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
//            }

            // 默认为营业状态
            store.setStatus(Store.StoreStatus.CLOSED);
        } else if (SUSPENDED.equals(status)) {
            // 不能suspend 已经suspended的店铺
            if (Store.StoreStatus.SUSPENDED.equals(store.getStatus())) {
                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
            }

            store.setStatus(Store.StoreStatus.SUSPENDED);
            store.setRejectReason(dto.getReason());
        } else {
            // 如果状态不合法，抛出异常
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }

        storeService.auditStore(storeId, store.getStatus(), store.getRejectReason(), LocalDateTime.now());
    }

    /**
     * 获取商品列表
     */
    @Override
    public PageVO<FoodVO> listFoods(Food.FoodStatus status, String keyword, Long storeId, int page, int size) {
        // 根据条件查询商品列表
        PageVO<FoodVO> foods = storeService.getFoodByConditions(storeId, status, keyword, page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        return foods;
    }

    /**
     * 审核商品
     */
    @Override
    @Transactional
    public void auditFood(Long foodId, AuditDTO dto) {
        Food food = storeService.getFoodById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));

        AuditDTO.AuditStatus status = dto.getStatus();
        if (APPROVED.equals(status)) {
            // 只有suspended的商品可以被审核通过(APPROVED)
//            if(!Food.FoodStatus.SUSPENDED.equals(food.getStatus())) {
//                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
//            }

            // 默认状态为下架
            food.setStatus(Food.FoodStatus.OFF_SHELF);
        } else if (SUSPENDED.equals(status)) {
            // 不能suspend 已经suspended的商品
            if (Food.FoodStatus.SUSPENDED.equals(food.getStatus())) {
                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
            }

            food.setStatus(Food.FoodStatus.SUSPENDED);
            food.setRejectReason(dto.getReason());
            // todo: 发送通知给商家
        } else {
            // 如果状态不合法，抛出异常
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }

        storeService.auditFood(foodId, food.getStatus(), food.getRejectReason(), LocalDateTime.now());
    }

    /**
     * 获取骑手列表
     */
    @Override
    public PageVO<RiderVO> listRiders(Rider.RiderStatus status, String keyword, int page, int size) {
        // 根据条件查询骑手列表
        PageVO<RiderVO> riders = riderService.getRiderByConditions(status, keyword, page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        return riders;
    }

    /**
     * 更新骑手状态
     */
    @Override
    @Transactional
    public void updateRiderStatus(Long riderId, AuditDTO dto) {
        Rider rider = riderService.getRiderById(riderId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.RIDER_NOT_FOUND));

        AuditDTO.AuditStatus status = dto.getStatus();
        if (APPROVED.equals(status)) {
//            if(!Rider.RiderStatus.SUSPENDED.equals(rider.getStatus())) {
//                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
//            }

            // 默认为离线状态
            rider.setStatus(Rider.RiderStatus.OFFLINE);
        } else if (SUSPENDED.equals(dto.getStatus())) {
            // 不能suspend 已经suspended的骑手
            if (Rider.RiderStatus.SUSPENDED.equals(rider.getStatus())) {
                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
            }

            rider.setStatus(Rider.RiderStatus.SUSPENDED);
            // todo: set reason
        } else {
            // 如果状态不合法，抛出异常
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }

        riderService.auditRider(riderId, rider.getStatus(), LocalDateTime.now());
    }

    /**
     * 获取所有订单
     */
    @Override
    public PageVO<OrderVO> listAllOrders(Order.OrderStatus status, Long userId, Long storeId, Long riderId, int page, int size) {
        // 根据条件查询订单列表
        PageVO<OrderVO> orders = orderService.getOrderByConditions(status, userId, storeId, riderId, page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        return orders;
    }

    /**
     * 获取评价列表
     */
    @Override
    public PageVO<ReviewVO> listReviews(Long userId, Long storeId, Long foodId, Integer rating, int page, int size) {
        // 根据条件查询评价列表
        PageVO<ReviewVO> reviews = orderService.getReviewByConditions(userId, storeId, foodId, rating, page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        return reviews;
    }

    /**
     * 删除评价
     */
    @Override
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = orderService.getReviewsById(reviewId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.REVIEW_NOT_FOUND));

        // 删除评价
        orderService.deleteReviewById(reviewId);

        // 更新商家评分
        updateStoreRating(review.getStoreId());
    }

    /**
     * 更新店铺评分 todo: 性能太低
     */
    private void updateStoreRating(Long storeId) {
        List<Review> reviews = orderService.getReviewsByStoreId(storeId, null, null)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        if (reviews == null || reviews.isEmpty()) {
            return;
        }

        double avgRating = reviews.stream().mapToInt(Review::getStoreRating).average().orElse(0);
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));

        storeService.updateStoreRating(storeId, avgRating, LocalDateTime.now());
    }

    /**
     * 获取平台统计数据
     */
    @Override
    public PlatformStatsVO getPlatformStatistics(LocalDate startDate, LocalDate endDate) {
        // 基础统计数据
        long totalUsers = 0;
        long totalMerchants = 0;
        long totalRiders = 0;
        long totalOrders = 0;
        try {
            totalUsers = userService.countUser(null, null, null).get();
            totalMerchants = userService.countUser(User.UserRole.MERCHANT, null, null).get();
            totalRiders = riderService.countRider().get();
            totalOrders = orderService.countOrder(null, null, null).get();
        } catch (Exception e) {
            log.error("Failed to fetch basic statistics", e);
            throw new RuntimeException(e);
        }

        // 获取今天的开始和结束时间
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // 今日订单数和交易额
        long todayOrders = orderService.countOrder(Order.OrderStatus.COMPLETED, todayStart, todayEnd).orElse(0L);
        BigDecimal todayAmount = orderService.sumOrder(Order.OrderStatus.COMPLETED, todayStart, todayEnd).orElse(BigDecimal.ZERO);

        // 本周订单数
        LocalDateTime weekStart = LocalDateTime.of(LocalDate.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)), LocalTime.MIN);
        long weekOrders = orderService.countOrder(Order.OrderStatus.COMPLETED, weekStart, todayEnd).orElse(0L);

        // 本月订单数
        LocalDateTime monthStart = LocalDateTime.of(LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        long monthOrders = orderService.countOrder(Order.OrderStatus.COMPLETED, monthStart, todayEnd).orElse(0L);

        // 累计交易额
        BigDecimal totalAmount = orderService.sumOrder(Order.OrderStatus.COMPLETED, null, null).orElse(BigDecimal.ZERO);

        // 按日期分组统计数据
        Map<String, Long> newUsersData = new LinkedHashMap<>();
        Map<String, Long> orderCountData = new LinkedHashMap<>();
        Map<String, BigDecimal> orderAmountData = new LinkedHashMap<>();

        // 计算日期间隔天数
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        // 如果日期范围超过60天，则按周统计
        boolean groupByWeek = daysBetween > 60;

        // 如果日期范围超过365天，则按月统计
        boolean groupByMonth = daysBetween > 365;

        // 按天/周/月统计数据
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            String dateKey;
            LocalDate nextDate;

            if (groupByMonth) {
                // 按月统计
                dateKey = currentDate.getYear() + "-" + currentDate.getMonthValue();
                nextDate = currentDate.plusMonths(1);
            } else if (groupByWeek) {
                // 按周统计
                dateKey = "W" + currentDate.get(java.time.temporal.IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                nextDate = currentDate.plusWeeks(1);
            } else {
                // 按天统计
                dateKey = currentDate.toString();
                nextDate = currentDate.plusDays(1);
            }

            LocalDateTime periodStart = LocalDateTime.of(currentDate, LocalTime.MIN);
            LocalDateTime periodEnd;

            if (groupByMonth) {
                periodEnd = LocalDateTime.of(currentDate.withDayOfMonth(currentDate.lengthOfMonth()), LocalTime.MAX);
            } else if (groupByWeek) {
                periodEnd = LocalDateTime.of(currentDate.plusDays(6), LocalTime.MAX);
            } else {
                periodEnd = LocalDateTime.of(currentDate, LocalTime.MAX);
            }

            // 新增用户数
            long newUsers = userService.countUser(null, periodStart, periodEnd).orElse(0L);
            newUsersData.put(dateKey, newUsers);

            // 订单数量
            long orderCount = orderService.countOrder(Order.OrderStatus.COMPLETED, periodStart, periodEnd).orElse(0L);
            orderCountData.put(dateKey, orderCount);

            // 交易额
            BigDecimal orderAmount = orderService.sumOrder(Order.OrderStatus.COMPLETED, periodStart, periodEnd).orElse(BigDecimal.ZERO);
            orderAmountData.put(dateKey, orderAmount != null ? orderAmount : BigDecimal.ZERO);

            currentDate = nextDate;
        }

        // 获取热门商品排行
        List<PlatformStatsVO.TopFoodItemVO> topFoodsData = orderService.getTopSellingFoods(10)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        List<PlatformStatsVO.TopFoodItemVO> topFoods = topFoodsData.stream()
                .map(item -> {
                    Long foodId = item.getFoodId();
                    String foodName = item.getFoodName();
                    String storeName = item.getStoreName();
                    Long salesCount = item.getSalesCount();
                    return new PlatformStatsVO.TopFoodItemVO(foodId, foodName, storeName, salesCount);
                })
                .collect(Collectors.toList());

        // 获取热门商家排行
        List<PlatformStatsVO.TopStoreItemVO> topStoresData = orderService.getTopStores(10)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        List<PlatformStatsVO.TopStoreItemVO> topStores = topStoresData.stream()
                .map(item -> {
                    Long storeId = item.getStoreId();
                    String storeName = item.getStoreName();
                    Long orderCount = item.getOrderCount();
                    BigDecimal salesAmount = item.getSalesAmount();
                    return new PlatformStatsVO.TopStoreItemVO(storeId, storeName, orderCount, salesAmount);
                })
                .collect(Collectors.toList());

        // 构建并返回统计数据
        return PlatformStatsVO.builder()
                .totalUsers(totalUsers)
                .totalMerchants(totalMerchants)
                .totalRiders(totalRiders)
                .totalOrders(totalOrders)
                .totalAmount(totalAmount)
                .todayOrders(todayOrders)
                .todayAmount(todayAmount)
                .weekOrders(weekOrders)
                .monthOrders(monthOrders)
                .newUsersData(newUsersData)
                .orderCountData(orderCountData)
                .orderAmountData(orderAmountData)
                .topFoods(topFoods)
                .topStores(topStores)
                .build();
    }

    @Override
    public PageVO<StoreCategoryVO> listStoreCategory(int page, int size) {
        PageVO<StoreCategoryVO> storeCategories = storeService.getAllStoreCategory(page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));

        return storeCategories;
    }

    @Override
    public StoreCategoryVO addStoreCategory(StoreCategoryDTO dto) {
        StoreCategory category = new StoreCategory();
        BeanUtils.copyProperties(dto, category);
        category.setCreatedAt(LocalDateTime.now());
        category = storeService.addStoreCategory(category)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        return entity2VO(category, StoreCategoryVO.class);
    }

    @Override
    public StoreCategoryVO updateStoreCategory(Long id, StoreCategoryDTO dto) {
        StoreCategory category = storeService.getStoreCategoryById(id)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_CATEGORY_NOT_FOUND));

        BeanUtils.copyProperties(dto, category);
        category.setCreatedAt(LocalDateTime.now());
        category = storeService.updateStoreCategory(id, category)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        return entity2VO(category, StoreCategoryVO.class);
    }

    @Override
    public void deleteStoreCategory(Long id) {
        StoreCategory category = storeService.getStoreCategoryById(id)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_CATEGORY_NOT_FOUND));
        storeService.deleteStoreCategoryById(id);
    }

    @Override
    public OrderDetailVO getOrderDetail(Long id) {
        return orderService.getOrderDetail(id)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_NOT_FOUND));
    }
}