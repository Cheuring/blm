package com.blm.order.controller.internal;

import com.blm.common.entity.Order;
import com.blm.common.entity.Review;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.vo.*;
import com.blm.order.repository.OrderItemRepository;
import com.blm.order.repository.OrderRepository;
import com.blm.order.repository.ReviewRepository;
import com.blm.order.service.OrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/internal/orders")
public class OrderInternal {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private StoreServiceClient storeService;

    @GetMapping("/{orderId}")
    public Optional<Order> getOrderById(@PathVariable("orderId") Long orderId) {
        try {
            return orderRepository.findById(orderId);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    @GetMapping("/available")
    public Optional<List<Order>> getAvailableOrders() {
        List<Order> orders = null;
        try {
            orders = orderRepository.findAvailableOrders();
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(orders);
    }

    @GetMapping("/rider/{riderId}")
    public Optional<List<Order>> getOrdersByRiderId(@PathVariable("riderId") Long riderId) {
        List<Order> orders = null;
        try {
            orders = orderRepository.findByRiderId(riderId);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(orders);
    }

    @PutMapping("/{orderId}/assignRider/{riderId}")
    public int assignRiderToOrder(@PathVariable("orderId") Long orderId, @PathVariable("riderId") Long riderId) {
        try {
            return orderRepository.assignOrder(orderId, riderId, Order.OrderStatus.RIDER_ASSIGNED, LocalDateTime.now());
        } catch (Exception ignored) {
            return 0;
        }
    }

    @PutMapping("/{orderId}/rider/{riderId}/status")
    public int updateOrderStatusByRider(
            @PathVariable("orderId") Long orderId,
            @PathVariable("riderId") Long riderId,
            @RequestParam("status") Order.OrderStatus status
    ) {
        try {
            return orderRepository.updateOrderStatusByRider(orderId, riderId, status, LocalDateTime.now());
        } catch (Exception ignored) {
            return 0;
        }
    }

    @GetMapping("/rider/{riderId}/status")
    public Optional<List<Order>> getOrdersByRiderIdAndStatusByTime(
            @PathVariable("riderId") Long riderId,
            @RequestParam("status") Order.OrderStatus status,
            @RequestParam("date") LocalDate date
    ) {
        List<Order> orders = null;
        try {
            if (Order.OrderStatus.DELIVERED.equals(status)) {
                orders = orderRepository.findCompletedOrdersByRiderAndDate(riderId, date);
            } else if (Order.OrderStatus.CANCELLED.equals(status)) {
                orders = orderRepository.findCanceledOrdersByRiderAndDate(riderId, date);
            }
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(orders);
    }

    @GetMapping("/reviews/{reviewId}")
    public Optional<Review> getReviewsById(@PathVariable("reviewId") Long reviewId) {
        try {
            return reviewRepository.findById(reviewId);
        } catch (Exception ignored) {
            return Optional.empty();
        }
    }

    @GetMapping("/reviews/food/{foodId}")
    public Optional<List<Review>> getReviewsByFoodId(@PathVariable("foodId") Long foodId) {
        List<Review> reviews = null;
        try {
            reviews = reviewRepository.findByFoodId(foodId);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(reviews);
    }

    @GetMapping("/reviews/store/{storeId}")
    public Optional<List<Review>> getReviewsByStoreId(
            @PathVariable("storeId") Long storeId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {
        List<Review> reviews = null;
        try {
            if (page != null && size != null) {
                // 使用 PageHelper 进行分页
                PageHelper.startPage(page, size);
                reviews = reviewRepository.findByStoreId(storeId);
            } else {
                // 不分页，获取所有评论
                reviews = reviewRepository.findByStoreId(storeId);
            }
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(reviews);
    }

    @GetMapping("/reviews/store/{storeId}/rate")
    public Optional<List<Review>> getReviewsByStoreIdAndRating(
            @PathVariable("storeId") Long storeId,
            @RequestParam("rating") int rating,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        List<Review> reviews = null;
        try {
            PageHelper.startPage(page, size);
            reviews = reviewRepository.findByStoreIdAndRating(storeId, rating);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(reviews);
    }

    @GetMapping("/store/{storeId}/stats/count")
    public Optional<Long> countStoreByStatusAndCreatedAtBetween(
            @RequestParam("status") Order.OrderStatus status,
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime end,
            @PathVariable("storeId") Long storeId
    ) {
        Long count = null;
        try {
            count = orderRepository.countStoreByStatusAndCreatedAtBetween(status, start, end, storeId);
        } catch (Exception ignored) {
            log.warn("Error counting orders for storeId {}: {}", storeId, ignored.getMessage());
        }
        return Optional.ofNullable(count);
    }

    @GetMapping("/store/{storeId}/stats/sum")
    public Optional<BigDecimal> sumStoreTotalByStatusAndCreatedAtBetween(
            @RequestParam("status") Order.OrderStatus status,
            @RequestParam("start") @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime end,
            @PathVariable("storeId") Long storeId
    ) {
        BigDecimal sum = null;
        try {
            sum = orderRepository.sumStoreTotalByStatusAndCreatedAtBetween(status, start, end, storeId);
        } catch (Exception e) {
            log.warn("Error summing orders for storeId {}: {}", storeId, e.getMessage());
        }
        return Optional.ofNullable(sum);
    }

    @GetMapping("/store/{storeId}/stats/count/total")
    public Optional<Long> countStoreByStatus(
            @RequestParam("status") Order.OrderStatus status,
            @PathVariable("storeId") Long storeId
    ) {
        Long count = null;
        try {
            count = orderRepository.countStoreByStatus(status, storeId);
        } catch (Exception e) {
            log.warn("Error counting orders for storeId {}: {}", storeId, e.getMessage());
        }
        return Optional.ofNullable(count);
    }

    @GetMapping("/store/{storeId}/stats/sum/total")
    public Optional<BigDecimal> sumStoreTotalByStatus(
            @RequestParam("status") Order.OrderStatus status,
            @PathVariable("storeId") Long storeId
    ) {
        BigDecimal sum = null;
        try {
            sum = orderRepository.sumStoreTotalByStatus(status, storeId);
        } catch (Exception e) {
            log.warn("Error summing orders for storeId {}: {}", storeId, e.getMessage());
        }
        return Optional.ofNullable(sum);
    }

    @GetMapping("/store/{storeId}/food/top")
    public Optional<List<StoreStatisticsVO.HotFoodVO>> getStoreTopSellingFoods(
            @PathVariable("storeId") Long storeId,
            @RequestParam("limit") int limit
    ) {
        List<StoreStatisticsVO.HotFoodVO> foods = null;
        try {
            foods = orderItemRepository.StoreFindTopSellingFoods(storeId, limit);
        } catch (Exception e) {
            log.warn("Error fetching top foods for storeId {}: {}", storeId, e.getMessage());
        }
        return Optional.ofNullable(foods);
    }

    @GetMapping("/food/top")
    public Optional<List<PlatformStatsVO.TopFoodItemVO>> getTopSellingFoods(
            @RequestParam("limit") int limit
    ) {
        List<PlatformStatsVO.TopFoodItemVO> foods = null;
        try {
            foods = orderItemRepository.findTopSellingFoods(limit);
            for (PlatformStatsVO.TopFoodItemVO food : foods) {
                // 调用 StoreServiceClient 获取店铺名称
                storeService.getFoodById(food.getFoodId()).ifPresent(foodInfo -> {
                    food.setFoodName(foodInfo.getName());
                });
                // 获取店铺名称
                storeService.getStoreById(food.getStoreId()).ifPresent(storeInfo -> {
                    food.setStoreName(storeInfo.getName());
                });
            }
        } catch (Exception e) {
            log.warn("Error fetching top foods for storeId: {}", e.getMessage());
        }
        return Optional.ofNullable(foods);
    }

    @GetMapping("/store/top")
    public Optional<List<PlatformStatsVO.TopStoreItemVO>> getTopStores(
            @RequestParam("limit") int limit
    ) {
        List<PlatformStatsVO.TopStoreItemVO> stores = null;
        try {
            stores = orderRepository.findTopStores(limit);
            for (PlatformStatsVO.TopStoreItemVO store : stores) {
                // 调用 StoreServiceClient 获取店铺名称
                storeService.getStoreById(store.getStoreId()).ifPresent(storeInfo -> {
                    store.setStoreName(storeInfo.getName());
                });
            }
        } catch (Exception e) {
            log.warn("Error fetching top stores: {}", e.getMessage());
        }
        return Optional.ofNullable(stores);
    }

    @GetMapping("/store/{storeId}/rate/aggregate")
    public Optional<List<RatingAggregateVO>> aggregateRatingsByStoreId(@PathVariable("storeId") Long storeId) {
        List<RatingAggregateVO> ratings = null;
        try {
            ratings = reviewRepository.aggregateRatingsByStoreId(storeId);
        } catch (Exception e) {
            log.warn("Error aggregating ratings for storeId {}: {}", storeId, e.getMessage());
        }
        return Optional.ofNullable(ratings);
    }

    @GetMapping("/store/{storeId}")
    public Optional<PageVO<OrderVO>> getOrdersByStoreId(
            @PathVariable("storeId") Long storeId,
            @RequestParam(value = "status", required = false) Order.OrderStatus status,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        PageVO<OrderVO> orders = null;
        try {
            orders = orderService.listStoreOrders(storeId, status, page, size);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(orders);
    }

    @GetMapping("/store/{storeId}/detail")
    public Optional<OrderDetailVO> getOrderDetailWithStore(
            @PathVariable("storeId") Long storeId,
            @RequestParam("orderId") Long orderId
    ) {
        OrderDetailVO orderDetail = null;
        try {
            orderDetail = orderService.getOrderDetailwithStore(orderId, storeId);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(orderDetail);
    }

    @GetMapping("/detail/{orderId}")
    public Optional<OrderDetailVO> getOrderDetail(@PathVariable("orderId") Long orderId) {
        OrderDetailVO orderDetail = null;
        try {
            orderDetail = orderService.getOrderDetail(orderId);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(orderDetail);
    }

    @PutMapping("/{orderId}/store/{storeId}/status")
    public int updateOrderStatusByStore(
            @PathVariable("orderId") Long orderId,
            @PathVariable("storeId") Long storeId,
            @RequestParam("status") Order.OrderStatus status
    ) {
        try {
            return orderRepository.updateStatusByStore(orderId, storeId, status, LocalDateTime.now());
        } catch (Exception ignored) {
            return 0;
        }
    }

    @GetMapping("/")
    public Optional<PageVO<OrderVO>> getOrderByConditions(
            @RequestParam(value = "status", required = false) Order.OrderStatus status,
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "riderId", required = false) Long riderId,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        PageVO<OrderVO> orders = null;
        try {
            orders = orderService.findByConditions(status, userId, storeId, riderId, page, size);
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(orders);
    }

    @GetMapping("/reviews")
    public Optional<PageVO<ReviewVO>> getReviewByConditions(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "storeId", required = false) Long storeId,
            @RequestParam(value = "foodId", required = false) Long foodId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    ) {
        PageVO<ReviewVO> res = null;
        try {
            // 使用PageHelper进行分页
            PageHelper.startPage(page, size);

            // 根据条件查询评价列表
            List<Review> reviews = reviewRepository.findByConditionsForAdmin(userId, storeId, foodId, rating);

            // 获取PageHelper分页信息
            Page<Review> pageInfo = (Page<Review>) reviews;
            // 转换为VO列表并填充额外信息
            List<ReviewVO> reviewVOs = reviews.stream()
                    .map(review -> {
                        ReviewVO vo = new ReviewVO();
                        BeanUtils.copyProperties(review, vo);

                        // 填充用户信息
                        userService.getUserById(review.getUserId()).ifPresent(user -> {
                            vo.setUserName(user.getUsername());
                            vo.setUserAvatar(user.getAvatar());
                        });

                        // 填充店铺信息
                        storeService.getStoreById(review.getStoreId()).ifPresent(store -> {
                            vo.setStoreName(store.getName());
                        });

                        return vo;
                    })
                    .collect(Collectors.toList());

            // 构造分页结果
            res = new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), reviewVOs);
        } catch (Exception ignored) {
        }

        return Optional.ofNullable(res);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public int deleteReviewById(@PathVariable("reviewId") Long reviewId) {
        try {
            return reviewRepository.deleteById(reviewId);
        } catch (Exception ignored) {
            return 0;
        }
    }

    @GetMapping("/count")
    public Optional<Long> countOrder(
            @RequestParam(value = "status", required = false) Order.OrderStatus status,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime end
    ) {
        Long count = null;
        try {
            if (status != null && start != null && end != null) {
                count = orderRepository.countByStatusAndCreatedAtBetween(status, start, end);
            } else if (status == null && start == null && end == null) {
                count = orderRepository.count();
            } else {
                count = 0L; // 需要实现其他条件的计数方法
            }
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(count);
    }

    @GetMapping("/sum")
    public Optional<BigDecimal> sumOrder(
            @RequestParam(value = "status", required = false) Order.OrderStatus status,
            @RequestParam(value = "start", required = false) @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime start,
            @RequestParam(value = "end", required = false) @DateTimeFormat(pattern = "yyyy/M/d HH:mm") LocalDateTime end
    ) {
        BigDecimal sum = null;
        try {
            if (status != null && start != null && end != null) {
                sum = orderRepository.sumTotalByStatusAndCreatedAtBetween(status, start, end);
            } else if (status != null) {
                sum = orderRepository.sumTotalByStatus(status);
            } else {
                // 需要实现总和计算方法
                sum = BigDecimal.ZERO; // 临时实现
            }
        } catch (Exception ignored) {
        }
        return Optional.ofNullable(sum);
    }
}
