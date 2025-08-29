package com.blm.store.controller;

import com.blm.common.dto.OrderStatusUpdateDTO;
import com.blm.common.entity.Order;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.OrderServiceClient;
import com.blm.common.feign.UserServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.result.Result;
import com.blm.common.vo.OrderDetailVO;
import com.blm.common.vo.OrderVO;
import com.blm.common.vo.PageVO;
import com.blm.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商家API-店铺订单管理")
@RestController
@RequestMapping("/api/merchant/orders/{storeId}")
@SecurityRequirement(name = "bearerAuth")
public class MerchantOrdersController {

    @Autowired
    private OrderServiceClient orderService;

    @Autowired
    private StoreService storeService;

    @Operation(summary = "获取订单列表", description = "获取订单列表")
    @ApiResponse(responseCode = "200", description = "成功",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderVO.class)))
    @GetMapping
    public Result<PageVO<OrderVO>> getOrders(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺id", required = true) @PathVariable Long storeId,
            @Parameter(description = "订单状态") @RequestParam(required = false) Order.OrderStatus status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size
    ) {
        storeService.verifyStoreOwner(userId, storeId);
        PageVO<OrderVO> orderVOPage = orderService.getOrdersByStoreId(storeId, status, page, size)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        return Result.success(orderVOPage);
    }

    @Operation(summary = "店铺根据订单id和店铺id获取订单详情", description = "店铺根据订单id和店铺id获取订单详情")
    @ApiResponse(responseCode = "200", description = "成功",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = OrderDetailVO.class)))
    @GetMapping("/orderDetail")
    public Result<OrderDetailVO> getDetailOrders(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺id", required = true) @PathVariable Long storeId,
            @Parameter(description = "订单id") @RequestParam Long id
    ) {
        storeService.verifyStoreOwner(userId, storeId);
        OrderDetailVO orderDetailVO = orderService.getOrderDetailWithStore(storeId, id)
                .orElseThrow(() -> new CommonException(ExceptionConstant.SYS_DATABASE_ERROR));
        return Result.success(orderDetailVO);

    }

    @Operation(summary = "店铺更新订单状态", description = "店铺根据订单id和店铺id更新订单状态")
    @ApiResponse(responseCode = "200", description = "成功")
    @PutMapping("/{orderId}/status")
    public Result<Void> updateOrderStatus(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺id", required = true) @PathVariable Long storeId,
            @Parameter(description = "订单id", required = true) @PathVariable Long orderId,
            @RequestBody(description = "新的状态信息", required = true, content = @Content(schema = @Schema(implementation = OrderStatusUpdateDTO.class))) @org.springframework.web.bind.annotation.RequestBody OrderStatusUpdateDTO dto
    ) {
        storeService.verifyStoreOwner(userId, storeId);
        orderService.updateOrderStatusByStore(orderId, storeId, dto.getOrderStatus());
        return Result.success(null);
    }
}
