package com.blm.store.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.FoodCategoryVO;
import com.blm.store.service.FoodCategoryService;
import com.blm.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "商品分类管理", description = "商品分类相关接口")
@RestController
@RequestMapping("/api/stores/{storeId}/food-categories")
@RequiredArgsConstructor
public class FoodCategoryController {
    
    private final FoodCategoryService foodCategoryService;
    private final StoreService storeService;
    
    @Operation(summary = "获取商品分类列表", description = "获取店铺的商品分类列表")
    @GetMapping
    @RequireRole("MERCHANT")
    public Result<List<FoodCategoryVO>> getCategories(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        List<FoodCategoryVO> categories = foodCategoryService.getCategoriesByStoreId(storeId);
        return Result.success(categories);
    }
    
    @Operation(summary = "添加商品分类", description = "为店铺添加商品分类")
    @PostMapping
    @RequireRole("MERCHANT")
    public Result<FoodCategoryVO> addCategory(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId,
            @Valid @RequestBody FoodCategoryDTO dto) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        FoodCategoryVO categoryVO = foodCategoryService.addCategory(storeId, dto);
        return Result.success(categoryVO);
    }
    
    @Operation(summary = "更新商品分类", description = "更新店铺的商品分类")
    @PutMapping("/{categoryId}")
    @RequireRole("MERCHANT")
    public Result<FoodCategoryVO> updateCategory(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId,
            @PathVariable Long categoryId,
            @Valid @RequestBody FoodCategoryDTO dto) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        FoodCategoryVO categoryVO = foodCategoryService.updateCategory(storeId, categoryId, dto);
        return Result.success(categoryVO);
    }
    
    @Operation(summary = "删除商品分类", description = "删除店铺的商品分类")
    @DeleteMapping("/{categoryId}")
    @RequireRole("MERCHANT")
    public Result<Void> deleteCategory(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId,
            @PathVariable Long categoryId) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        foodCategoryService.deleteCategory(storeId, categoryId);
        return Result.success();
    }
}
