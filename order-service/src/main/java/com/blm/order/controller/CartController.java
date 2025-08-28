package com.blm.order.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.CartItemDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.CartVO;
import com.blm.order.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "购物车管理", description = "购物车相关接口")
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    @Operation(summary = "获取购物车", description = "获取指定店铺的购物车内容")
    @ApiResponse(responseCode = "200", description = "查询成功")
    @GetMapping
    @RequireRole({"USER"})
    public Result<CartVO> getCart(
            @Parameter(description = "店铺ID") @RequestParam Long storeId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        CartVO cart = cartService.getCart(userId, storeId);
        return Result.success(cart);
    }
    
    @Operation(summary = "添加商品到购物车", description = "添加或更新购物车商品")
    @ApiResponse(responseCode = "200", description = "添加成功")
    @PostMapping
    @RequireRole({"USER"})
    public Result<CartVO> addItem(@Valid @RequestBody CartItemDTO dto, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        CartVO cart = cartService.addItem(userId, dto);
        return Result.success(cart);
    }
    
    @Operation(summary = "更新购物车商品数量", description = "更新指定商品的数量")
    @ApiResponse(responseCode = "200", description = "更新成功")
    @PutMapping("/{cartItemId}")
    @RequireRole({"USER"})
    public Result<CartVO> updateItem(
            @Parameter(description = "购物车项目ID") @PathVariable Long cartItemId,
            @Parameter(description = "新数量") @RequestParam Integer quantity,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        CartVO cart = cartService.updateItem(userId, cartItemId, quantity);
        return Result.success(cart);
    }
    
    @Operation(summary = "移除购物车商品", description = "从购物车中移除指定商品")
    @ApiResponse(responseCode = "200", description = "移除成功")
    @DeleteMapping("/{cartItemId}")
    @RequireRole({"USER"})
    public Result<CartVO> removeItem(
            @Parameter(description = "购物车项目ID") @PathVariable Long cartItemId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        CartVO cart = cartService.removeItem(userId, cartItemId);
        return Result.success(cart);
    }
    
    @Operation(summary = "清空购物车", description = "清空指定店铺的购物车")
    @ApiResponse(responseCode = "200", description = "清空成功")
    @DeleteMapping
    @RequireRole({"USER"})
    public Result<Void> clearCart(
            @Parameter(description = "店铺ID") @RequestParam Long storeId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        cartService.clearCart(userId, storeId);
        return Result.success();
    }
    
    /**
     * 从请求中获取用户ID (由网关设置到header中)
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String userIdStr = request.getHeader("X-User-Id");
        return userIdStr != null ? Long.parseLong(userIdStr) : null;
    }
}
