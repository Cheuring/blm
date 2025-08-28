package com.blm.common.feign;

import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;
import com.blm.common.vo.RiderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 骑手服务Feign客户端
 */
@FeignClient(name = "rider-service", path = "/api/riders")
public interface RiderServiceClient {

    /**
     * 注册成为骑手
     */
    @PostMapping("/register")
    Result<Void> register(@RequestBody RiderRegisterDTO dto,
                         @RequestHeader("X-User-Id") Long userId);

    /**
     * 更新工作状态
     */
    @PutMapping("/status")
    Result<Void> updateWorkStatus(@RequestBody WorkStatusUpdateDTO dto,
                                 @RequestHeader("X-User-Id") Long userId);

    /**
     * 更新位置信息
     */
    @PutMapping("/location")
    Result<Void> updateLocation(@RequestBody LocationUpdateDTO dto,
                               @RequestHeader("X-User-Id") Long userId);

    /**
     * 获取骑手信息
     */
    @GetMapping("/{riderId}")
    Result<RiderVO> getRiderById(@PathVariable("riderId") Long riderId);

    /**
     * 根据用户ID获取骑手信息
     */
    @GetMapping("/user/{userId}")
    Result<RiderVO> getRiderByUserId(@PathVariable("userId") Long userId);

    /**
     * 获取可接订单列表
     */
    @GetMapping("/available-orders")
    Result<List<RiderOrderVO>> getAvailableOrders(@RequestHeader("X-User-Id") Long userId);

    /**
     * 获取我的订单列表
     */
    @GetMapping("/my-orders")
    Result<List<RiderOrderVO>> getMyOrders(@RequestHeader("X-User-Id") Long userId);

    /**
     * 接受订单
     */
    @PostMapping("/orders/{orderId}/accept")
    Result<Void> acceptOrder(@PathVariable("orderId") Long orderId,
                           @RequestHeader("X-User-Id") Long userId);

    /**
     * 取餐
     */
    @PostMapping("/orders/{orderId}/pickup")
    Result<Void> pickupOrder(@PathVariable("orderId") Long orderId,
                           @RequestHeader("X-User-Id") Long userId);

    /**
     * 完成配送
     */
    @PostMapping("/orders/{orderId}/complete")
    Result<Void> completeDelivery(@PathVariable("orderId") Long orderId,
                                @RequestHeader("X-User-Id") Long userId);

    /**
     * 获取统计数据
     */
    @GetMapping("/stats")
    Result<RiderStatsVO> getStats(@RequestParam(defaultValue = "day") String period,
                                 @RequestHeader("X-User-Id") Long userId);
}
