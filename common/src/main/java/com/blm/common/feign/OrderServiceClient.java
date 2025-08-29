package com.blm.common.feign;

import com.blm.common.entity.Order;
import com.blm.common.entity.Review;
import com.blm.common.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务Feign客户端
 */
@FeignClient(name = "order-service", path = "/internal/orders")
public interface OrderServiceClient {

    @GetMapping("/{orderId}")
    Optional<Order> getOrderById(@PathVariable("orderId") Long orderId);

    @GetMapping("/available")
    Optional<List<Order>> getAvailableOrders();

    @GetMapping("/rider/{riderId}")
    Optional<List<Order>> getOrdersByRiderId(@PathVariable("riderId") Long riderId);

    @PutMapping("/{orderId}/assignRider/{riderId}")
    int assignRiderToOrder(@PathVariable("orderId") Long orderId, @PathVariable("riderId") Long riderId);

    @PutMapping("/{orderId}/rider/{riderId}/status")
    int updateOrderStatusByRider(
            @PathVariable("orderId") Long orderId,
            @PathVariable("riderId") Long riderId,
            @RequestParam("status") Order.OrderStatus status
    );

    @GetMapping("/rider/{riderId}/status")
    Optional<List<Order>> getOrdersByRiderIdAndStatusByTime(
            @PathVariable("riderId") Long riderId,
            @RequestParam("status") Order.OrderStatus status,
            @RequestParam("date") LocalDate date
    );

    @GetMapping("/reviews/food/{foodId}")
    Optional<List<Review>> getReviewsByFoodId(@PathVariable("foodId") Long foodId);

    @GetMapping("/reviews/store/{storeId}")
    Optional<List<Review>> getReviewsByStoreId(
            @PathVariable("storeId") Long storeId,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @GetMapping("/reviews/store/{storeId}/rate")
    Optional<List<Review>> getReviewsByStoreIdAndRating(
            @PathVariable("storeId") Long storeId,
            @RequestParam("rating") int rating,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );


    @GetMapping("/store/{storeId}/stats/count")
    Optional<Long> countStoreByStatusAndCreatedAtBetween(
            @RequestParam("status") Order.OrderStatus status,
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end,
            @PathVariable("storeId") Long storeId
    );

    @GetMapping("/store/{storeId}/stats/sum")
    Optional<BigDecimal> sumStoreTotalByStatusAndCreatedAtBetween(
            @RequestParam("status") Order.OrderStatus status,
            @RequestParam("start") LocalDateTime start,
            @RequestParam("end") LocalDateTime end,
            @PathVariable("storeId") Long storeId
    );

    @GetMapping("/store/{storeId}/stats/count/total")
    Optional<Long> countStoreByStatus(
            @RequestParam("status") Order.OrderStatus status,
            @PathVariable("storeId") Long storeId
    );

    @GetMapping("/store/{storeId}/stats/sum/total")
    Optional<BigDecimal> sumStoreTotalByStatus(
            @RequestParam("status") Order.OrderStatus status,
            @PathVariable("storeId") Long storeId
    );

    @GetMapping("/store/{storeId}/food/top")
    Optional<List<StoreStatisticsVO.HotFoodVO>> getTopSellingFoods(
            @PathVariable("storeId") Long storeId,
            @RequestParam("limit") int limit
    );

    @GetMapping("/store/{storeId}/rate/aggregate")
    Optional<List<RatingAggregateVO>> aggregateRatingsByStoreId(@PathVariable("storeId") Long storeId);

    @GetMapping("/store/{storeId}")
    Optional<PageVO<OrderVO>> getOrdersByStoreId(
            @PathVariable("storeId") Long storeId,
            @RequestParam(value = "status", required = false) Order.OrderStatus status,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @GetMapping("/store/{storeId}/detail")
    Optional<OrderDetailVO> getOrderDetailWithStore(
            @PathVariable("storeId") Long storeId,
            @RequestParam("orderId") Long orderId
    );

    @PutMapping("/{orderId}/store/{storeId}/status")
    int updateOrderStatusByStore(
            @PathVariable("orderId") Long orderId,
            @PathVariable("storeId") Long storeId,
            @RequestParam("status") Order.OrderStatus status
    );

}
