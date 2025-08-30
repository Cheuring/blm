package com.blm.store.controller;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.FoodCategoryVO;
import com.blm.store.service.FoodCategoryService;
import com.blm.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商家API-店铺商品分类管理")
@RestController
@RequestMapping("/api/merchant/stores/{storeId}/categories")
@SecurityRequirement(name = "bearerAuth")
public class MerchantFoodCategoryController {

    @Autowired
    private FoodCategoryService foodCategoryService;

    @Autowired
    private StoreService storeService;

    @Operation(summary = "获取商品分类列表", description = "根据店铺id查询其有哪些商品分类")
    @ApiResponse(responseCode = "200", description = "成功",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FoodCategoryVO.class)))
    @GetMapping
    public Result<List<FoodCategoryVO>> getCategories(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId) {

        // 校验店铺所有权
        storeService.verifyStoreOwner(storeId, userId);

        List<FoodCategoryVO> categories = foodCategoryService.getCategoriesByStoreId(storeId);
        return Result.success(categories);
    }

    @Operation(summary = "添加商品分类", description = "为指定店铺添加新的商品分类")
    @ApiResponse(responseCode = "200", description = "添加成功",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FoodCategoryVO.class)))
    @PostMapping
    public Result<FoodCategoryVO> addCategory(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "商品分类信息", required = true,
                    content = @Content(schema = @Schema(implementation = FoodCategoryDTO.class)))
            @Valid @RequestBody FoodCategoryDTO dto) {

        // 校验店铺所有权
        storeService.verifyStoreOwner(storeId, userId);

        FoodCategoryVO category = foodCategoryService.addCategory(storeId, dto);
        return Result.success(category);
    }

    @Operation(summary = "更新商品分类", description = "更新指定店铺的商品分类信息")
    @ApiResponse(responseCode = "200", description = "更新成功",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = FoodCategoryVO.class)))
    @PutMapping("/{id}")
    public Result<FoodCategoryVO> updateCategory(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "分类ID", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "商品分类信息", required = true,
                    content = @Content(schema = @Schema(implementation = FoodCategoryDTO.class)))
            @Valid @RequestBody FoodCategoryDTO dto) {

        // 校验店铺所有权
        storeService.verifyStoreOwner(storeId, userId);

        FoodCategoryVO category = foodCategoryService.updateCategory(storeId, id, dto);
        return Result.success(category);
    }

    @Operation(summary = "删除商品分类", description = "删除指定店铺的商品分类")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "分类ID", required = true) @PathVariable Long id) {

        // 校验店铺所有权
        storeService.verifyStoreOwner(storeId, userId);

        foodCategoryService.deleteCategory(storeId, id);
        return Result.success(null);
    }
}
