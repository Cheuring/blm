package com.blm.store.controller;

import com.blm.common.result.Result;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.FoodCategoryVO;
import com.blm.store.service.FoodService;
import com.blm.store.service.FoodCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品管理", description = "商品相关接口")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class FoodController {
    
    private final FoodService foodService;
    private final FoodCategoryService foodCategoryService;
    
    @Operation(summary = "根据商品ID获取商品信息", description = "内部服务调用接口")
    @GetMapping("/foods/{foodId}")
    public Result<FoodVO> getFoodById(@PathVariable Long foodId) {
        FoodVO foodVO = foodService.getFoodById(foodId);
        return Result.success(foodVO);
    }
    
    @Operation(summary = "获取店铺的商品分类列表", description = "内部服务调用接口")
    @GetMapping("/{storeId}/categories")
    public Result<List<FoodCategoryVO>> getStoreCategories(@PathVariable Long storeId) {
        List<FoodCategoryVO> categories = foodCategoryService.getCategoriesByStoreId(storeId);
        return Result.success(categories);
    }
    
    @Operation(summary = "获取店铺的商品列表", description = "内部服务调用接口")
    @GetMapping("/{storeId}/foods")
    public Result<List<FoodVO>> getStoreFoods(
            @PathVariable Long storeId,
            @Parameter(description = "商品分类ID", required = false) @RequestParam(required = false) Long categoryId) {
        List<FoodVO> foods = foodService.getFoodsByStoreId(storeId, categoryId);
        return Result.success(foods);
    }
    
    @Operation(summary = "获取店铺的特色商品列表", description = "内部服务调用接口")
    @GetMapping("/{storeId}/featured-foods")
    public Result<List<FoodVO>> getFeaturedFoods(@PathVariable Long storeId) {
        List<FoodVO> foods = foodService.getFeaturedFoodsByStoreId(storeId);
        return Result.success(foods);
    }
}
