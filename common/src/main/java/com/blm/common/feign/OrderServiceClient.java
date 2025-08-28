package com.blm.common.feign;

import com.blm.common.entity.Order;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
}
