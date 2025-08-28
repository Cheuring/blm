package com.blm.user.controller;

import com.blm.common.result.Result;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;
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

@Tag(name = "收藏与历史", description = "用户管理收藏的店铺和商品")
@RestController
@RequestMapping("/api/favorites")
@SecurityRequirement(name = "bearerAuth")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    private static class PageVOSchema {
        private static class StoreVOPage extends PageVO<StoreVO> {
        }

        private static class FoodVOPage extends PageVO<FoodVO> {
        }
    }

    // --- Store Favorites --- 

    @Operation(summary = "获取收藏的店铺列表", description = "查询当前登录用户收藏的所有店铺")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = PageVOSchema.StoreVOPage.class)))
    @GetMapping("/stores")
    public Result<PageVO<StoreVO>> listFavoriteStores(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageVO<StoreVO> list = favoriteService.listFavoriteStores(userId, page, size);
        return Result.success(list);
    }

    @Operation(summary = "添加店铺收藏", description = "将指定店铺添加到当前用户的收藏夹")
    @ApiResponse(responseCode = "200", description = "添加成功")
    @PostMapping("/stores")
    public Result<Void> addStoreFavorite(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody(description = "要收藏的店铺信息", required = true, content = @Content(schema = @Schema(implementation = FavoriteDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody FavoriteDTO dto) {
        favoriteService.addStoreFavorite(userId, dto.getTargetId());
        return Result.success(null);
    }

    @Operation(summary = "取消店铺收藏", description = "将指定店铺从当前用户的收藏夹中移除")
    @ApiResponse(responseCode = "200", description = "移除成功")
    @DeleteMapping("/stores/{storeId}")
    public Result<Void> removeStoreFavorite(@RequestHeader("X-User-Id") Long userId,
                                            @Parameter(description = "要移除收藏的店铺ID", required = true) @PathVariable Long storeId) {
        favoriteService.removeStoreFavorite(userId, storeId);
        return Result.success(null);
    }

    // --- Food Favorites --- 

    @Operation(summary = "获取收藏的商品列表", description = "查询当前登录用户收藏的所有商品")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = PageVOSchema.FoodVOPage.class)))
    @GetMapping("/foods")
    public Result<PageVO<FoodVO>> listFavoriteFoods(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "页码 (从1开始)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        PageVO<FoodVO> list = favoriteService.listFavoriteFoods(userId, page, size);
        return Result.success(list);
    }

    @Operation(summary = "添加商品收藏", description = "将指定商品添加到当前用户的收藏夹")
    @ApiResponse(responseCode = "200", description = "添加成功")
    @PostMapping("/foods")
    public Result<Void> addFoodFavorite(@RequestHeader("X-User-Id") Long userId,
                                        @RequestBody(description = "要收藏的商品信息", required = true, content = @Content(schema = @Schema(implementation = FavoriteDTO.class))) @Valid @org.springframework.web.bind.annotation.RequestBody FavoriteDTO dto) {
        favoriteService.addFoodFavorite(userId, dto.getTargetId());
        return Result.success(null);
    }

    @Operation(summary = "取消商品收藏", description = "将指定商品从当前用户的收藏夹中移除")
    @ApiResponse(responseCode = "200", description = "移除成功")
    @DeleteMapping("/foods/{foodId}")
    public Result<Void> removeFoodFavorite(@RequestHeader("X-User-Id") Long userId,
                                           @Parameter(description = "要移除收藏的商品ID", required = true) @PathVariable Long foodId) {
        favoriteService.removeFoodFavorite(userId, foodId);
        return Result.success(null);
    }

    @Operation(summary = "查询商品是否收藏", description = "查询当前登录用户是否收藏了指定商品")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", description = "1-已收藏, 0-未收藏")))
    @GetMapping("/foods/{foodId}")
    public Result<Integer> queryFavorite(@RequestHeader("X-User-Id") Long userId,
                                         @Parameter(description = "要检查的商品ID", required = true) @PathVariable Long foodId) {
        Integer isFavorite = favoriteService.queryFavorite(userId, foodId, Favorite.TYPE_FOOD);
        return Result.success(isFavorite);
    }

    @Operation(summary = "查询店铺是否收藏", description = "查询当前登录用户是否收藏了指定店铺")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "integer", description = "1-已收藏, 0-未收藏")))
    @GetMapping("/stores/{storeId}")
    public Result<Integer> queryStoreFavorite(@RequestHeader("X-User-Id") Long userId,
                                              @Parameter(description = "要检查的店铺ID", required = true) @PathVariable Long storeId) {
        Integer isFavorite = favoriteService.queryFavorite(userId, storeId, Favorite.TYPE_STORE);
        return Result.success(isFavorite);
    }
}