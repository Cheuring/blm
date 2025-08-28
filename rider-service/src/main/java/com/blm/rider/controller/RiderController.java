package com.blm.rider.controller;


import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.RiderRegisterDTO;
import com.blm.common.dto.WorkStatusUpdateDTO;
import com.blm.common.entity.Rider;
import com.blm.common.result.Result;
import com.blm.common.vo.OrderVO;
import com.blm.common.vo.RiderOrderVO;
import com.blm.common.vo.RiderStatsVO;
import com.blm.rider.service.RiderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "5. 骑手相关API", description = "骑手管理接单、配送")
@RestController
@RequestMapping("/api/rider")
@SecurityRequirement(name = "bearerAuth")
public class RiderController {

    @Autowired
    private RiderService riderService;

    @Operation(summary = "注册成为骑手", description = "用户注册成为配送骑手，需提供实名和车辆信息")
    @ApiResponse(responseCode = "200", description = "注册成功")
    @PostMapping("/register")
    public Result<Void> register(
            @RequestHeader("X-User-Id") Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "骑手注册信息", required = true, content = @Content(schema = @Schema(implementation = RiderRegisterDTO.class))) @RequestBody RiderRegisterDTO dto) {
        riderService.registerRider(userId, dto);
        return Result.success(null);
    }

    @Operation(summary = "更新工作状态", description = "更改骑手工作状态")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/status")
    public Result<Void> updateWorkStatus(
            @RequestHeader("X-User-Id") Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "工作状态信息", required = true, content = @Content(schema = @Schema(implementation = WorkStatusUpdateDTO.class))) @RequestBody WorkStatusUpdateDTO dto) {
        riderService.updateWorkStatus(userId, dto);
        return Result.success(null);
    }

    @Operation(summary = "获取工作状态", description = "获取骑手工作状态")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rider.RiderStatus.class)))
    @GetMapping("/status")
    public Result<Rider.RiderStatus> getWorkStatus(@RequestHeader("X-User-Id") Long userId) {
        Rider.RiderStatus status = riderService.getWorkStatus(userId);
        return Result.success(status);
    }

    @Operation(summary = "更新骑手位置", description = "更新骑手当前地理位置坐标")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/location")
    public Result<Void> updateLocation(
            @RequestHeader("X-User-Id") Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "位置更新信息", required = true, content = @Content(schema = @Schema(implementation = LocationUpdateDTO.class))) @RequestBody LocationUpdateDTO dto) {
        riderService.updateLocation(userId, dto);
        return Result.success(null);
    }

    @Operation(summary = "获取可接订单列表", description = "获取系统中等待骑手接单的订单列表")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RiderOrderVO.class)))
    @GetMapping("/orders/available")
    public Result<List<RiderOrderVO>> listAvailableOrders() {
        List<RiderOrderVO> list = riderService.listAvailableOrders();
        return Result.success(list);
    }

    @Operation(summary = "获取骑手订单列表", description = "获取当前骑手正在处理的订单列表")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderVO.class)))
    @GetMapping("/orders")
    public Result<List<RiderOrderVO>> listMyOrders(@RequestHeader("X-User-Id") Long userId) {
        List<RiderOrderVO> list = riderService.listMyOrders(userId);
        return Result.success(list);
    }

    @Operation(summary = "接单", description = "骑手接受指定订单进行配送")
    @ApiResponse(responseCode = "200", description = "接单成功")
    @PostMapping("/orders/{orderId}/accept")
    public Result<Void> acceptOrder(@RequestHeader("X-User-Id") Long userId,
                                    @Parameter(description = "订单ID", required = true) @PathVariable Long orderId) {
        riderService.acceptOrder(userId, orderId);
        return Result.success(null);
    }

    @Operation(summary = "取餐", description = "骑手从商家取餐开始配送")
    @ApiResponse(responseCode = "200", description = "取餐成功")
    @PostMapping("/orders/{orderId}/pickup")
    public Result<Void> pickupOrder(@RequestHeader("X-User-Id") Long userId,
                                    @Parameter(description = "订单ID", required = true) @PathVariable Long orderId) {
        riderService.pickupOrder(userId, orderId);
        return Result.success(null);
    }

    @Operation(summary = "送达订单", description = "骑手将订单送达到用户手中")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PostMapping("/orders/{orderId}/delivered")
    public Result<Void> orderDelivered(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单ID", required = true) @PathVariable Long orderId) {
        riderService.delivered(userId, orderId);
        return Result.success(null);
    }

    @Operation(summary = "获取统计数据", description = "获取骑手的订单统计数据(当日/本周/本月)")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RiderStatsVO.class)))
    @ApiResponse(responseCode = "400", description = "请求参数错误")
    @ApiResponse(responseCode = "401", description = "未授权或Token无效")
    @ApiResponse(responseCode = "404", description = "骑手不存在")
    @GetMapping("/stats")
    public Result<RiderStatsVO> getStats(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "统计周期: day-日, week-周, month-月", schema = @Schema(allowableValues = {"day", "week", "month"})) @RequestParam(defaultValue = "day") String period) {
        RiderStatsVO stats = riderService.getStats(userId, period);
        return Result.success(stats);
    }
}