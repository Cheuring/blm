package com.blm.store.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.StoreCreateDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.StoreVO;
import com.blm.common.vo.StoreCategoryVO;
import com.blm.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "店铺管理", description = "店铺相关接口")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    
    private final StoreService storeService;
    
    @Operation(summary = "根据店铺ID获取店铺信息", description = "内部服务调用接口")
    @GetMapping("/{storeId}")
    public Result<StoreVO> getStoreById(@PathVariable Long storeId) {
        StoreVO storeVO = storeService.getStoreById(storeId);
        return Result.success(storeVO);
    }
    
    @Operation(summary = "获取店铺分类列表", description = "获取所有店铺分类")
    @GetMapping("/categories")
    public Result<List<StoreCategoryVO>> getStoreCategories() {
        List<StoreCategoryVO> categories = storeService.getStoreCategories();
        return Result.success(categories);
    }
    
    @Operation(summary = "获取店铺所有者ID", description = "内部服务调用接口")
    @GetMapping("/{storeId}/owner")
    public Result<Long> getStoreOwnerId(@PathVariable Long storeId) {
        Long ownerId = storeService.getStoreOwnerId(storeId);
        return Result.success(ownerId);
    }
    
    @Operation(summary = "创建店铺", description = "商家创建店铺")
    @PostMapping
    @RequireRole("MERCHANT")
    public Result<StoreVO> createStore(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @Valid @RequestBody StoreCreateDTO dto) {
        StoreVO storeVO = storeService.createStore(merchantId, dto);
        return Result.success(storeVO);
    }
    
    @Operation(summary = "更新店铺信息", description = "商家更新店铺信息")
    @PutMapping("/{storeId}")
    @RequireRole("MERCHANT")
    public Result<StoreVO> updateStore(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId,
            @Valid @RequestBody StoreCreateDTO dto) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        StoreVO storeVO = storeService.updateStore(storeId, dto);
        return Result.success(storeVO);
    }
    
    @Operation(summary = "获取商家的店铺列表", description = "获取商家名下的所有店铺")
    @GetMapping("/merchant")
    @RequireRole("MERCHANT")
    public Result<List<StoreVO>> getStoresByMerchant(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId) {
        List<StoreVO> stores = storeService.getStoresByMerchantId(merchantId);
        return Result.success(stores);
    }
}
