package com.blm.admin.controller;

import com.blm.common.result.Result;
import com.blm.common.entity.Order;
import com.blm.admin.service.AdminService;
import com.blm.common.vo.OrderDetailVO;
import com.blm.common.vo.OrderVO;
import com.blm.common.vo.PageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员订单管理控制器
 */
@Tag(name = "管理员API - 订单管理", description = "管理员管理所有订单")
@RestController
@RequestMapping("/api/admin/orders")
@SecurityRequirement(name = "bearerAuth")
public class AdminOrderController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取所有订单列表 (分页)
     */
    @Operation(summary = "获取所有订单", description = "管理员获取所有订单，可按状态、用户ID、商家ID和骑手ID过滤")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageOrderVO.class)))
    @GetMapping
    public Result<PageVO<OrderVO>> listOrders(
            @Parameter(description = "订单状态") @RequestParam(required = false) Order.OrderStatus status,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "商家ID") @RequestParam(required = false) Long storeId,
            @Parameter(description = "骑手ID") @RequestParam(required = false) Long riderId,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        
        PageVO<OrderVO> orders = adminService.listAllOrders(status, userId, storeId, riderId, page, size);
        return Result.success(orders);
    }

    @GetMapping("/{id}")
    public Result<OrderDetailVO> getOrderDetail(
            @Parameter(description = "订单ID", required = true) @PathVariable Long id) {

        OrderDetailVO orderDetail = adminService.getOrderDetail(id);
        return Result.success(orderDetail);
    }

    // 用于Swagger文档
    private static class PageOrderVO extends PageVO<OrderVO> {}
}