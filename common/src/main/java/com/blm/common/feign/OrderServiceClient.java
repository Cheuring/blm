package com.blm.common.feign;

import com.blm.common.result.Result;
import com.blm.common.vo.RiderOrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单服务Feign客户端
 */
@FeignClient(name = "order-service", path = "/api/orders")
public interface OrderServiceClient {

    /**
     * 获取可接订单列表
     */
    @GetMapping("/available")
    Result<List<RiderOrderVO>> getAvailableOrders();

    /**
     * 获取骑手订单列表
     */
    @GetMapping("/rider/{riderId}")
    Result<List<RiderOrderVO>> getRiderOrders(@PathVariable("riderId") Long riderId);

    /**
     * 骑手接受订单
     */
    @PutMapping("/{orderId}/accept")
    Result<Void> acceptOrder(@PathVariable("orderId") Long orderId,
                           @RequestHeader("X-Rider-Id") Long riderId);

    /**
     * 骑手完成配送
     */
    @PutMapping("/{orderId}/complete")
    Result<Void> completeDelivery(@PathVariable("orderId") Long orderId,
                                @RequestHeader("X-Rider-Id") Long riderId);

    /**
     * 更新订单状态为取餐中
     */
    @PutMapping("/{orderId}/pickup")
    Result<Void> pickupOrder(@PathVariable("orderId") Long orderId,
                           @RequestHeader("X-Rider-Id") Long riderId);
}
