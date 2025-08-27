package com.blm.rider.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.result.Result;
import com.blm.rider.dto.RiderLocationDTO;
import com.blm.rider.service.RiderService;
import com.blm.rider.vo.OrderVO;
import com.blm.rider.vo.RiderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 骑手控制器 - 权限控制示例
 */
@Slf4j
@RestController
@RequestMapping("/api/riders")
@Tag(name = "骑手管理", description = "骑手相关接口")
public class RiderController {

    @Autowired
    private RiderService riderService;

    /**
     * 申请成为骑手 - 需要用户角色
     */
    @Operation(summary = "申请成为骑手", description = "用户申请成为配送骑手")
    @PostMapping("/apply")
    @RequireRole(value = {"USER"}, message = "需要用户权限才能申请成为骑手")
    public Result<Void> applyToBeRider(@RequestHeader("X-User-Id") String userId,
                                       @Validated @RequestBody RiderApplicationDTO dto) {
        log.info("用户 {} 申请成为骑手", userId);
        riderService.applyToBeRider(Long.valueOf(userId), dto);
        return Result.success("骑手申请已提交");
    }

    /**
     * 获取可接订单列表 - 需要骑手角色
     */
    @Operation(summary = "获取可接订单", description = "骑手查看可以接取的订单列表")
    @GetMapping("/available-orders")
    @RequireRole(value = {"RIDER"}, message = "需要骑手权限")
    public Result<List<OrderVO>> getAvailableOrders(@RequestHeader("X-User-Id") String userId) {
        List<OrderVO> orders = riderService.getAvailableOrders(Long.valueOf(userId));
        return Result.success(orders);
    }

    /**
     * 接取订单 - 需要骑手角色
     */
    @Operation(summary = "接取订单", description = "骑手接取配送订单")
    @PostMapping("/orders/{orderId}/accept")
    @RequireRole(value = {"RIDER"}, message = "需要骑手权限")
    public Result<Void> acceptOrder(@RequestHeader("X-User-Id") String userId,
                                    @PathVariable("orderId") Long orderId) {
        log.info("骑手 {} 接取订单 {}", userId, orderId);
        riderService.acceptOrder(Long.valueOf(userId), orderId);
        return Result.success("订单接取成功");
    }

    /**
     * 更新位置信息 - 需要骑手角色
     */
    @Operation(summary = "更新位置", description = "骑手更新当前位置")
    @PostMapping("/location")
    @RequireRole(value = {"RIDER"}, message = "需要骑手权限")
    public Result<Void> updateLocation(@RequestHeader("X-User-Id") String userId,
                                       @Validated @RequestBody RiderLocationDTO dto) {
        riderService.updateLocation(Long.valueOf(userId), dto);
        return Result.success("位置更新成功");
    }

    /**
     * 获取我的配送订单 - 需要骑手角色
     */
    @Operation(summary = "获取我的配送订单", description = "骑手查看自己的配送订单")
    @GetMapping("/my-orders")
    @RequireRole(value = {"RIDER"}, message = "需要骑手权限")
    public Result<List<OrderVO>> getMyOrders(@RequestHeader("X-User-Id") String userId,
                                              @RequestParam(value = "status", required = false) String status) {
        List<OrderVO> orders = riderService.getRiderOrders(Long.valueOf(userId), status);
        return Result.success(orders);
    }

    /**
     * 完成配送 - 需要骑手角色
     */
    @Operation(summary = "完成配送", description = "骑手标记订单配送完成")
    @PostMapping("/orders/{orderId}/complete")
    @RequireRole(value = {"RIDER"}, message = "需要骑手权限")
    public Result<Void> completeDelivery(@RequestHeader("X-User-Id") String userId,
                                          @PathVariable("orderId") Long orderId) {
        log.info("骑手 {} 完成订单 {} 的配送", userId, orderId);
        riderService.completeDelivery(Long.valueOf(userId), orderId);
        return Result.success("配送已完成");
    }

    /**
     * 更新工作状态 - 需要骑手角色
     */
    @Operation(summary = "更新工作状态", description = "骑手更新在线/离线状态")
    @PostMapping("/status")
    @RequireRole(value = {"RIDER"}, message = "需要骑手权限")
    public Result<Void> updateWorkStatus(@RequestHeader("X-User-Id") String userId,
                                          @RequestParam("status") String status) {
        log.info("骑手 {} 更新工作状态为 {}", userId, status);
        riderService.updateWorkStatus(Long.valueOf(userId), status);
        return Result.success("状态更新成功");
    }

    /**
     * 审核骑手申请 - 仅管理员
     */
    @Operation(summary = "审核骑手申请", description = "管理员审核骑手申请")
    @PostMapping("/{riderId}/approve")
    @RequireRole(value = {"ADMIN"}, message = "只有管理员才能审核骑手申请")
    public Result<Void> approveRider(@RequestHeader("X-User-Id") String userId,
                                     @PathVariable("riderId") Long riderId,
                                     @RequestParam("approved") Boolean approved,
                                     @RequestParam(value = "reason", required = false) String reason) {
        log.info("管理员 {} 审核骑手 {}, 结果: {}", userId, riderId, approved);
        riderService.approveRider(Long.valueOf(userId), riderId, approved, reason);
        return Result.success(approved ? "骑手申请通过" : "骑手申请拒绝");
    }

    /**
     * 查看所有骑手 - 仅管理员
     */
    @Operation(summary = "查看所有骑手", description = "管理员查看平台所有骑手")
    @GetMapping("/admin/all")
    @RequireRole(value = {"ADMIN"}, message = "需要管理员权限")
    public Result<List<RiderVO>> getAllRiders(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "size", defaultValue = "20") Integer size,
                                               @RequestParam(value = "status", required = false) String status) {
        List<RiderVO> riders = riderService.getAllRiders(page, size, status);
        return Result.success(riders);
    }
}
