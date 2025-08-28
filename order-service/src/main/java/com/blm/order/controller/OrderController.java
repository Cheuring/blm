package com.blm.order.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.OrderCreateDTO;
import com.blm.common.dto.PaymentDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.OrderDetailVO;
import com.blm.common.vo.OrderVO;
import com.blm.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "订单管理", description = "订单相关接口")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @Operation(summary = "创建订单", description = "根据购物车内容创建订单")
    @ApiResponse(responseCode = "200", description = "创建成功")
    @PostMapping
    @RequireRole({"USER"})
    public Result<OrderVO> createOrder(@Valid @RequestBody OrderCreateDTO dto, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        OrderVO order = orderService.createOrder(userId, dto);
        return Result.success(order);
    }
    
    @Operation(summary = "获取用户订单列表", description = "获取当前用户的订单列表")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping
    @RequireRole({"USER"})
    public Result<List<OrderVO>> getUserOrders(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        List<OrderVO> orders = orderService.getUserOrders(userId, page, size);
        return Result.success(orders);
    }
    
    @Operation(summary = "获取订单详情", description = "获取指定订单的详细信息")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping("/{orderId}")
    @RequireRole({"USER"})
    public Result<OrderDetailVO> getOrderDetail(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        OrderDetailVO orderDetail = orderService.getOrderDetail(userId, orderId);
        return Result.success(orderDetail);
    }
    
    @Operation(summary = "取消订单", description = "取消指定订单")
    @ApiResponse(responseCode = "200", description = "取消成功")
    @PutMapping("/{orderId}/cancel")
    @RequireRole({"USER"})
    public Result<Void> cancelOrder(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        orderService.cancelOrder(userId, orderId);
        return Result.success();
    }
    
    @Operation(summary = "支付订单", description = "支付指定订单")
    @ApiResponse(responseCode = "200", description = "支付成功")
    @PostMapping("/payment")
    @RequireRole({"USER"})
    public Result<Void> payOrder(@Valid @RequestBody PaymentDTO dto, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        orderService.payOrder(userId, dto);
        return Result.success();
    }
    
    @Operation(summary = "确认收货", description = "确认收货完成订单")
    @ApiResponse(responseCode = "200", description = "确认成功")
    @PutMapping("/{orderId}/confirm")
    @RequireRole({"USER"})
    public Result<OrderDetailVO> confirmReceipt(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        OrderDetailVO orderDetail = orderService.confirmReceipt(userId, orderId);
        return Result.success(orderDetail);
    }
    
    // 内部服务调用接口 - 供商家服务调用
    @Operation(summary = "更新订单状态", description = "商家更新订单状态(内部调用)")
    @PutMapping("/{orderId}/status")
    @RequireRole({"STORE", "ADMIN"})
    public Result<Void> updateOrderStatus(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            @Parameter(description = "订单状态") @RequestParam String status,
            HttpServletRequest request) {
        Long storeId = getStoreIdFromRequest(request);
        orderService.updateOrderStatus(orderId, storeId, status);
        return Result.success();
    }
    
    // 内部服务调用接口 - 供商家服务调用
    @Operation(summary = "获取商家订单列表", description = "获取商家的订单列表(内部调用)")
    @GetMapping("/store")
    @RequireRole({"STORE", "ADMIN"})
    public Result<List<OrderVO>> getStoreOrders(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "订单状态") @RequestParam(required = false) String status,
            HttpServletRequest request) {
        Long storeId = getStoreIdFromRequest(request);
        List<OrderVO> orders = orderService.getStoreOrders(storeId, page, size, status);
        return Result.success(orders);
    }
    
    // 内部服务调用接口 - 供骑手服务调用
    @Operation(summary = "接受订单", description = "骑手接受订单(内部调用)")
    @PutMapping("/{orderId}/accept")
    @RequireRole({"RIDER", "ADMIN"})
    public Result<Void> acceptOrder(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long riderId = getRiderIdFromRequest(request);
        orderService.acceptOrder(orderId, riderId);
        return Result.success();
    }
    
    // 内部服务调用接口 - 供骑手服务调用
    @Operation(summary = "完成配送", description = "骑手完成配送(内部调用)")
    @PutMapping("/{orderId}/complete")
    @RequireRole({"RIDER", "ADMIN"})
    public Result<Void> completeDelivery(
            @Parameter(description = "订单ID") @PathVariable Long orderId,
            HttpServletRequest request) {
        Long riderId = getRiderIdFromRequest(request);
        orderService.completeDelivery(orderId, riderId);
        return Result.success();
    }
    
    /**
     * 从请求中获取用户ID (由网关设置到header中)
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String userIdStr = request.getHeader("X-User-Id");
        return userIdStr != null ? Long.parseLong(userIdStr) : null;
    }
    
    /**
     * 从请求中获取商家ID (由网关设置到header中)
     */
    private Long getStoreIdFromRequest(HttpServletRequest request) {
        String storeIdStr = request.getHeader("X-Store-Id");
        return storeIdStr != null ? Long.parseLong(storeIdStr) : null;
    }
    
    /**
     * 从请求中获取骑手ID (由网关设置到header中)
     */
    private Long getRiderIdFromRequest(HttpServletRequest request) {
        String riderIdStr = request.getHeader("X-Rider-Id");
        return riderIdStr != null ? Long.parseLong(riderIdStr) : null;
    }
}
