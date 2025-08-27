package com.blm.order.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.result.Result;
import com.blm.order.dto.OrderCreateDTO;
import com.blm.order.service.OrderService;
import com.blm.order.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单控制器 - 权限控制示例
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理", description = "订单相关接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单 - 需要用户角色
     */
    @Operation(summary = "创建订单", description = "用户创建新订单")
    @PostMapping
    @RequireRole(value = {"USER"}, message = "需要用户权限才能下单")
    public Result<OrderVO> createOrder(@RequestHeader("X-User-Id") String userId,
                                       @Validated @RequestBody OrderCreateDTO dto) {
        log.info("用户 {} 创建订单", userId);
        OrderVO orderVO = orderService.createOrder(Long.valueOf(userId), dto);
        return Result.success("订单创建成功", orderVO);
    }

    /**
     * 获取我的订单列表 - 需要用户角色
     */
    @Operation(summary = "获取我的订单", description = "获取当前用户的订单列表")
    @GetMapping("/my")
    @RequireRole(value = {"USER"}, message = "需要用户权限")
    public Result<List<OrderVO>> getMyOrders(@RequestHeader("X-User-Id") String userId,
                                              @RequestParam(value = "status", required = false) String status) {
        List<OrderVO> orders = orderService.getUserOrders(Long.valueOf(userId), status);
        return Result.success(orders);
    }

    /**
     * 获取店铺订单列表 - 需要商家角色
     */
    @Operation(summary = "获取店铺订单", description = "商家获取自己店铺的订单列表")
    @GetMapping("/store/{storeId}")
    @RequireRole(value = {"MERCHANT"}, message = "需要商家权限")
    public Result<List<OrderVO>> getStoreOrders(@RequestHeader("X-User-Id") String userId,
                                                 @PathVariable("storeId") Long storeId,
                                                 @RequestParam(value = "status", required = false) String status) {
        List<OrderVO> orders = orderService.getStoreOrders(Long.valueOf(userId), storeId, status);
        return Result.success(orders);
    }

    /**
     * 接单 - 需要商家角色
     */
    @Operation(summary = "商家接单", description = "商家确认接收订单")
    @PostMapping("/{orderId}/accept")
    @RequireRole(value = {"MERCHANT"}, message = "需要商家权限")
    public Result<Void> acceptOrder(@RequestHeader("X-User-Id") String userId,
                                    @PathVariable("orderId") Long orderId) {
        log.info("商家 {} 接单 {}", userId, orderId);
        orderService.acceptOrder(Long.valueOf(userId), orderId);
        return Result.success("接单成功");
    }

    /**
     * 取消订单 - 用户或商家
     */
    @Operation(summary = "取消订单", description = "用户或商家取消订单")
    @PostMapping("/{orderId}/cancel")
    @RequireRole(value = {"USER", "MERCHANT"}, requireAll = false, message = "需要用户或商家权限")
    public Result<Void> cancelOrder(@RequestHeader("X-User-Id") String userId,
                                    @RequestHeader("X-User-Roles") String userRoles,
                                    @PathVariable("orderId") Long orderId,
                                    @RequestParam("reason") String reason) {
        log.info("用户 {} (角色: {}) 取消订单 {}, 原因: {}", userId, userRoles, orderId, reason);
        orderService.cancelOrder(Long.valueOf(userId), orderId, reason, userRoles);
        return Result.success("订单已取消");
    }

    /**
     * 分配骑手 - 需要商家或管理员角色
     */
    @Operation(summary = "分配骑手", description = "商家或管理员为订单分配配送骑手")
    @PostMapping("/{orderId}/assign-rider")
    @RequireRole(value = {"MERCHANT", "ADMIN"}, requireAll = false, message = "需要商家或管理员权限")
    public Result<Void> assignRider(@RequestHeader("X-User-Id") String userId,
                                    @PathVariable("orderId") Long orderId,
                                    @RequestParam("riderId") Long riderId) {
        log.info("用户 {} 为订单 {} 分配骑手 {}", userId, orderId, riderId);
        orderService.assignRider(Long.valueOf(userId), orderId, riderId);
        return Result.success("骑手分配成功");
    }

    /**
     * 查看所有订单 - 仅管理员
     */
    @Operation(summary = "查看所有订单", description = "管理员查看平台所有订单")
    @GetMapping("/admin/all")
    @RequireRole(value = {"ADMIN"}, message = "需要管理员权限")
    public Result<List<OrderVO>> getAllOrders(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "size", defaultValue = "20") Integer size,
                                               @RequestParam(value = "status", required = false) String status) {
        List<OrderVO> orders = orderService.getAllOrders(page, size, status);
        return Result.success(orders);
    }
}
