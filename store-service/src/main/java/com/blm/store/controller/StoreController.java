package com.blm.store.controller;

import com.blm.common.result.Result;
import com.blm.common.dto.StoreQueryDTO;
import com.blm.store.service.StoreService;
import com.blm.common.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "店铺与商品浏览 (公开)", description = "用户浏览店铺和商品信息")
@RestController
@RequestMapping("/api")
public class StoreController {

    @Autowired
    private StoreService storeService;

    // Define a helper schema for PageVO<StoreVO> for Swagger UI
    private static class PageVOSchema {
        @Schema(name = "StoreVOPage")
        private static class StoreVOPage extends PageVO<StoreVO> {}
        @Schema(name = "FoodVOPage")
        private static class FoodVOPage extends PageVO<FoodVO> {}
    }

    @Operation(summary = "搜索/浏览店铺列表 (分页)", description = "根据关键字、分类、位置、排序方式等条件分页查询店铺列表")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageVOSchema.StoreVOPage.class)))
    @GetMapping("/stores")
    public Result<PageVO<StoreVO>> listStores(@Valid @ModelAttribute StoreQueryDTO queryDTO) {

        // The service method needs to be updated to handle pagination and filtering including minPrice and maxPrice
        PageVO<StoreVO> storePage = storeService.listStores(queryDTO);
        return Result.success(storePage);
    }

    @Operation(summary = "获取推荐店铺列表", description = "根据用户位置或其他推荐算法获取推荐店铺")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = StoreVO.class)))
    @GetMapping("/stores/recommended")
    public Result<List<StoreVO>> listRecommended(
            @Parameter(description = "用户当前经度") @RequestParam(required = false) Double longitude,
            @Parameter(description = "用户当前纬度") @RequestParam(required = false) Double latitude) {
        // Service method might need longitude/latitude for location-based recommendations
        List<StoreVO> list = storeService.listRecommended(longitude, latitude);
        return Result.success(list);
    }

    @Operation(summary = "获取店铺详细信息", description = "获取指定ID店铺的详细信息，包括分类和特色商品")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreDetailVO.class)))
    @GetMapping("/stores/{id}")
    public Result<StoreDetailVO> getStoreDetail(
            @Parameter(description = "店铺ID", required = true) @PathVariable Long id) {
        StoreDetailVO vo = storeService.getStoreDetail(id);
        return Result.success(vo);
    }

    @Operation(summary = "获取店铺内商品列表", description = "获取指定店铺的所有商品，可按分类过滤")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = FoodVO.class))) // Consider pagination if list can be large
    @GetMapping("/stores/{id}/foods")
    public Result<List<FoodVO>> listStoreFoods(
            @Parameter(description = "店铺ID", required = true) @PathVariable Long id,
            @Parameter(description = "商品分类ID (可选, 用于过滤)") @RequestParam(required = false) Long categoryId) {
        // Consider adding pagination parameters (page, size) if needed
        List<FoodVO> list = storeService.listStoreFoods(id, categoryId);
        return Result.success(list);
    }

    @Operation(summary = "获取店铺分类列表", description = "获取所有店铺分类")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = StoreCategoryVO.class)))
    @GetMapping("/categories")
    public Result<List<StoreCategoryVO>> listStoreCategories() {
        List<StoreCategoryVO> categories = storeService.listStoreCategories();
        return Result.success(categories);
    }

    @Operation(summary = "获取商品详细信息", description = "获取指定ID商品的详细信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDetailVO.class)))
    @GetMapping("/foods/{id}")
    public Result<FoodDetailVO> getFoodDetail(
            @Parameter(description = "商品ID", required = true) @PathVariable Long id) {
        FoodDetailVO vo = storeService.getFoodDetail(id);
        return Result.success(vo);
    }
}