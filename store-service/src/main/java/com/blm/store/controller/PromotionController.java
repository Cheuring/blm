package com.blm.store.controller;

import com.blm.common.annotation.RequireRole;
import com.blm.common.dto.PromotionDTO;
import com.blm.common.result.Result;
import com.blm.common.vo.PromotionVO;
import com.blm.store.service.PromotionService;
import com.blm.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "促销活动管理", description = "促销活动相关接口")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class PromotionController {
    
    private final PromotionService promotionService;
    private final StoreService storeService;
    
    @Operation(summary = "获取店铺的促销活动列表", description = "内部服务调用接口")
    @GetMapping("/{storeId}/promotions")
    public Result<List<PromotionVO>> getStorePromotions(@PathVariable Long storeId) {
        List<PromotionVO> promotions = promotionService.getPromotionsByStoreId(storeId);
        return Result.success(promotions);
    }
    
    @Operation(summary = "获取店铺的有效促销活动列表", description = "内部服务调用接口")
    @GetMapping("/{storeId}/promotions/active")
    public Result<List<PromotionVO>> getActivePromotions(@PathVariable Long storeId) {
        List<PromotionVO> promotions = promotionService.getActivePromotionsByStoreId(storeId);
        return Result.success(promotions);
    }
    
    @Operation(summary = "创建促销活动", description = "商家创建促销活动")
    @PostMapping("/{storeId}/promotions")
    @RequireRole("MERCHANT")
    public Result<PromotionVO> createPromotion(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId,
            @Valid @RequestBody PromotionDTO dto) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        PromotionVO promotionVO = promotionService.createPromotion(storeId, dto);
        return Result.success(promotionVO);
    }
    
    @Operation(summary = "更新促销活动", description = "商家更新促销活动")
    @PutMapping("/{storeId}/promotions/{promotionId}")
    @RequireRole("MERCHANT")
    public Result<PromotionVO> updatePromotion(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId,
            @PathVariable Long promotionId,
            @Valid @RequestBody PromotionDTO dto) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        PromotionVO promotionVO = promotionService.updatePromotion(storeId, promotionId, dto);
        return Result.success(promotionVO);
    }
    
    @Operation(summary = "删除促销活动", description = "商家删除促销活动")
    @DeleteMapping("/{storeId}/promotions/{promotionId}")
    @RequireRole("MERCHANT")
    public Result<Void> deletePromotion(
            @Parameter(description = "商家ID", required = true) @RequestHeader("userId") Long merchantId,
            @PathVariable Long storeId,
            @PathVariable Long promotionId) {
        // 验证店铺所有权
        storeService.verifyStoreOwnership(storeId, merchantId);
        promotionService.deletePromotion(storeId, promotionId);
        return Result.success();
    }
}
