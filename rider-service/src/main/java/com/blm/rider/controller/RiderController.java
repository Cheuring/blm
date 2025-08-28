package com.blm.rider.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;
import com.blm.common.vo.RiderVO;
import com.blm.rider.service.RiderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 骑手控制器
 */
@Slf4j
@Tag(name = "骑手管理", description = "骑手相关接口")
@RestController
@RequestMapping("/api/riders")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @Operation(summary = "注册成为骑手", description = "用户注册成为配送骑手")
    @PostMapping("/register")
    @RequireRole({"USER"})
    public Result<Void> register(
            @Valid @RequestBody RiderRegisterDTO dto,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        riderService.registerRider(userId, dto);
        return Result.success();
    }

    @Operation(summary = "更新工作状态", description = "骑手更新工作状态")
    @PutMapping("/status")
    @RequireRole({"RIDER"})
    public Result<Void> updateWorkStatus(
            @Valid @RequestBody WorkStatusUpdateDTO dto,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        riderService.updateWorkStatus(userId, dto);
        return Result.success();
    }

    @Operation(summary = "更新位置信息", description = "骑手更新当前位置")
    @PutMapping("/location")
    @RequireRole({"RIDER"})
    public Result<Void> updateLocation(
            @Valid @RequestBody LocationUpdateDTO dto,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        riderService.updateLocation(userId, dto);
        return Result.success();
    }

    @Operation(summary = "获取可接订单", description = "骑手查看可以接取的订单列表")
    @GetMapping("/available-orders")
    @RequireRole({"RIDER"})
    public Result<List<RiderOrderVO>> getAvailableOrders(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        List<RiderOrderVO> orders = riderService.getAvailableOrders(userId);
        return Result.success(orders);
    }

    @Operation(summary = "获取我的订单", description = "获取当前骑手的订单列表")
    @GetMapping("/my-orders")
    @RequireRole({"RIDER"})
    public Result<List<RiderOrderVO>> getMyOrders(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        List<RiderOrderVO> orders = riderService.getMyOrders(userId);
        return Result.success(orders);
    }

    @Operation(summary = "接受订单", description = "骑手接受配送订单")
    @PostMapping("/orders/{orderId}/accept")
    @RequireRole({"RIDER"})
    public Result<Void> acceptOrder(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        riderService.acceptOrder(userId, orderId);
        return Result.success();
    }

    @Operation(summary = "取餐", description = "骑手从商家取餐")
    @PostMapping("/orders/{orderId}/pickup")
    @RequireRole({"RIDER"})
    public Result<Void> pickupOrder(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        riderService.pickupOrder(userId, orderId);
        return Result.success();
    }

    @Operation(summary = "完成配送", description = "骑手完成配送订单")
    @PostMapping("/orders/{orderId}/complete")
    @RequireRole({"RIDER"})
    public Result<Void> completeDelivery(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        riderService.completeDelivery(userId, orderId);
        return Result.success();
    }

    @Operation(summary = "获取统计数据", description = "获取骑手统计数据")
    @GetMapping("/stats")
    @RequireRole({"RIDER"})
    public Result<RiderStatsVO> getStats(
            @Parameter(description = "统计周期: day-日, week-周, month-月") 
            @RequestParam(defaultValue = "day") String period,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        RiderStatsVO stats = riderService.getStats(userId, period);
        return Result.success(stats);
    }

    @Operation(summary = "获取骑手信息", description = "根据骑手ID获取骑手信息(内部调用)")
    @GetMapping("/{riderId}")
    @RequireRole({"ADMIN", "SYSTEM"})
    public Result<RiderVO> getRiderById(@PathVariable Long riderId) {
        RiderVO rider = riderService.getRiderById(riderId);
        return Result.success(rider);
    }

    @Operation(summary = "根据用户ID获取骑手信息", description = "根据用户ID获取骑手信息(内部调用)")
    @GetMapping("/user/{userId}")
    @RequireRole({"ADMIN", "SYSTEM"})
    public Result<RiderVO> getRiderByUserId(@PathVariable Long userId) {
        RiderVO rider = riderService.getRiderByUserId(userId);
        return Result.success(rider);
    }

    /**
     * 从请求中获取用户ID
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String userIdStr = request.getHeader("X-User-Id");
        return userIdStr != null ? Long.parseLong(userIdStr) : null;
    }
}
