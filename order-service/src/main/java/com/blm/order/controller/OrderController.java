package com.blm.order.controller;


import com.blm.common.dto.LocationUpdateDTO;
import com.blm.common.dto.OrderCreateDTO;
import com.blm.common.dto.PaymentDTO;
import com.blm.common.dto.ReviewDTO;
import com.blm.common.entity.Order;
import com.blm.common.result.Result;
import com.blm.common.vo.*;
import com.blm.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户订单管理", description = "用户管理自己的订单")
@RestController
@RequestMapping("/api/orders")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Helper schema for PageVO<OrderVO>
    private static class PageVOSchema {
        @Schema(name = "OrderVOPage")
        private static class OrderVOPage extends PageVO<OrderVO> {}
    }

    @Operation(summary = "创建订单", description = "根据购物车内容和用户选择的地址创建新订单")
    @ApiResponse(responseCode = "200", description = "创建成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderVO.class)))
    @PostMapping
    public Result<OrderVO> createOrder(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody(description = "订单创建信息", required = true, content = @Content(schema = @Schema(implementation = OrderCreateDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody OrderCreateDTO dto) {
        OrderVO order = orderService.createOrder(userId, dto);
        return Result.success(order);
    }

    @Operation(summary = "获取用户订单列表 (分页)", description = "查询当前登录用户的订单列表，支持按状态过滤和分页")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageVOSchema.OrderVOPage.class)))
    @GetMapping
    public Result<PageVO<OrderVO>> listOrders(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单状态过滤 (可选)") @RequestParam(required = false) Order.OrderStatus status,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageVO<OrderVO> orderPage = orderService.listUserOrders(userId, status, page, size);
        return Result.success(orderPage);
    }

    @Operation(summary = "获取订单详细信息", description = "查询指定ID订单的详细信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailVO.class)))
    @GetMapping("/{id}")
    public Result<OrderDetailVO> getOrderDetail(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        OrderDetailVO orderDetail = orderService.getOrderDetail(userId, id);
        return Result.success(orderDetail);
    }

    @Operation(summary = "取消订单", description = "用户取消尚未处理的订单")
    @ApiResponse(responseCode = "200", description = "取消成功")
    @PutMapping("/{id}/cancel")
    public Result<Void> cancelOrder(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        orderService.cancelOrder(userId, id);
        return Result.success(null);
    }

    @Operation(summary = "模拟支付订单", description = "(模拟接口) 用户支付订单。实际应用中应调用支付网关并处理回调。")
    @ApiResponse(responseCode = "200", description = "支付成功 (模拟)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentResultVO.class))) // Updated response schema
    @PostMapping("/pay")
    public Result<PaymentResultVO> payOrder(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody(description = "支付信息", required = true, content = @Content(schema = @Schema(implementation = PaymentDTO.class))) @Valid
            @org.springframework.web.bind.annotation.RequestBody PaymentDTO dto) {
        // In a real app, this would involve payment gateway interaction
        PaymentResultVO paymentResult = orderService.simulatePayment(userId, dto);
        return Result.success(paymentResult);
    }

    @Operation(summary = "催单", description = "用户对配送中的订单进行催单")
    @ApiResponse(responseCode = "200", description = "催单成功")
    @PostMapping("/{id}/urge")
    public Result<Void> urgeOrder(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        orderService.urgeOrder(userId, id);
        return Result.success(null);
    }

    // Keep confirmReceipt and addReview as they are useful, even if not explicitly in 3.3
    @Operation(summary = "确认收货", description = "用户确认收到订单商品")
    @ApiResponse(responseCode = "200", description = "确认成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDetailVO.class)))
    @PutMapping("/{id}/confirm")
    public Result<OrderDetailVO> confirmReceipt(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        OrderDetailVO orderDetail = orderService.confirmReceipt(userId, id);
        return Result.success(orderDetail);
    }

    @Operation(summary = "评价订单", description = "用户对已完成的订单进行评价")
    @ApiResponse(responseCode = "200", description = "评价成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReviewVO.class)))
    @PostMapping("/{id}/reviews")
    public Result<ReviewVO> addReview(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单ID", required = true) @PathVariable Long id,
            @RequestBody(description = "评价信息", required = true, content = @Content(schema = @Schema(implementation = ReviewDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody ReviewDTO reviewDto) {
        ReviewVO review = orderService.addReview(userId, id, reviewDto);
        return Result.success(review);
    }

    @Operation(summary = "获取订单骑手位置", description = "获取指定订单的骑手当前位置")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LocationUpdateDTO.class)))
    @GetMapping("/{id}/rider/location")
    public Result<LocationUpdateDTO> getRiderLocation(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {
        LocationUpdateDTO location = orderService.getRiderLocation(userId, id);
        return Result.success(location);
    }
}
