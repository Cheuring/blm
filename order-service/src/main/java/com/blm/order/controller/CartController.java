package com.blm.order.controller;


import com.blm.common.dto.CartItemDTO;
import com.blm.common.dto.CartItemUpdateDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.CartVO;
import com.blm.order.service.CartService;
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

import java.util.List;

@Tag(name = "购物车管理", description = "用户管理自己的购物车")
@RestController
@RequestMapping("/api/cart")
@SecurityRequirement(name = "bearerAuth")
public class CartController {

    @Autowired
    private CartService cartService;

    @Operation(summary = "获取当前用户所有购物车", description = "查询当前登录用户的所有购物车信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartVO.class)))
    @GetMapping
    public Result<List<CartVO>> getCart(@RequestHeader("X-User-Id") Long userId) {
        List<CartVO> carts = cartService.getCarts(userId);
        return Result.success(carts);
    }

    @Operation(summary = "获取对应店铺的用户购物车", description = "查询当前登录用户的指定店铺的购物车信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartVO.class)))
    @GetMapping("/{storeId}")
    public Result<CartVO> getCart(@RequestHeader("X-User-Id") Long userId,
                                  @Parameter(description = "要获取购物车的店铺id", required = true) @PathVariable Long storeId) {
        CartVO cart = cartService.getCart(userId, storeId);
        return Result.success(cart);
    }

    @Operation(summary = "添加商品到购物车", description = "向购物车添加指定商品。如果商品已存在，则增加数量。注意：购物车通常只能包含同一家店铺的商品。")
    @ApiResponse(responseCode = "200", description = "添加/更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartVO.class)))
    @PostMapping
    public Result<CartVO> addItem(@RequestHeader("X-User-Id") Long userId,
                                  @RequestBody(description = "要添加的商品信息", required = true, content = @Content(schema = @Schema(implementation = CartItemDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody CartItemDTO dto) {
        CartVO cart = cartService.addItem(userId, dto);
        return Result.success(cart);
    }

    @Operation(summary = "更新购物车商品数量", description = "更新购物车中指定购物车项的数量")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartVO.class)))
    @PutMapping("/{id}")
    public Result<CartVO> updateItem(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "要更新的购物车项ID", required = true) @PathVariable Long id,
            @RequestBody(description = "包含新数量的信息", required = true, content = @Content(schema = @Schema(implementation = CartItemUpdateDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody CartItemUpdateDTO dto) { // Changed DTO type
        CartVO cart = cartService.updateItemQuantityById(userId, id, dto.getQuantity());
        return Result.success(cart);
    }

    @Operation(summary = "移除购物车商品", description = "从购物车中移除指定的购物车项")
    @ApiResponse(responseCode = "200", description = "移除成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartVO.class))) // Return CartVO after removal
    @DeleteMapping("/item/{id}")
    public Result<CartVO> removeItem(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "要移除的购物车项ID", required = true) @PathVariable Long id) {
        CartVO cart = cartService.removeItemById(userId, id);
        return Result.success(cart);
    }

    @Operation(summary = "清空店铺购物车", description = "清空当前店铺用户购物车中的所有商品")
    @ApiResponse(responseCode = "200", description = "清空成功")
    @DeleteMapping("/{storeId}")
    public Result<Void> clearCartByStore(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "要移除的店铺ID", required = true) @PathVariable Long storeId) {
        cartService.clearCartByStore(userId, storeId);
        return Result.success(null);
    }

    @Operation(summary = "清空购物车", description = "清空当前用户购物车中的所有商品")
    @ApiResponse(responseCode = "200", description = "清空成功")
    @DeleteMapping
    public Result<Void> clearCart(@RequestHeader("X-User-Id") Long userId) {
        cartService.clearCart(userId);
        return Result.success(null);
    }
}