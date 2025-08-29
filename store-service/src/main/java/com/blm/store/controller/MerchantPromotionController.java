package com.blm.store.controller;

import com.blm.common.result.Result;
import com.blm.common.dto.PromotionDTO;
import com.blm.store.service.MerchantPromotionService;
import com.blm.common.vo.PromotionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商家API-商家促销管理", description = "商家管理其店铺的促销活动")
@RestController
@RequestMapping("/api/merchant/stores/{storeId}/promotions")
@SecurityRequirement(name = "bearerAuth")
public class MerchantPromotionController {

    @Autowired
    private MerchantPromotionService promotionService;

    @Operation(summary = "获取店铺促销列表", description = "获取指定店铺的所有促销活动信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = PromotionVO.class)))
    @GetMapping
    public Result<List<PromotionVO>> listPromotions(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId) {
        List<PromotionVO> list = promotionService.listPromotions(userId, storeId);
        return Result.success(list);
    }

    @Operation(summary = "创建促销活动", description = "为指定店铺创建新的促销活动")
    @ApiResponse(responseCode = "200", description = "创建成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionVO.class)))
    @PostMapping
    public Result<PromotionVO> createPromotion(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @RequestBody(description = "新促销活动信息", required = true, content = @Content(schema = @Schema(implementation = PromotionDTO.class))) @org.springframework.web.bind.annotation.RequestBody PromotionDTO dto) {
        PromotionVO vo = promotionService.createPromotion(userId, storeId, dto);
        return Result.success(vo);
    }

    @Operation(summary = "更新促销活动", description = "更新指定店铺内指定ID的促销活动信息")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PromotionVO.class)))
    @PutMapping("/{id}")
    public Result<PromotionVO> updatePromotion(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "促销活动ID", required = true) @PathVariable Long id,
            @RequestBody(description = "更新后的促销活动信息", required = true, content = @Content(schema = @Schema(implementation = PromotionDTO.class))) @org.springframework.web.bind.annotation.RequestBody PromotionDTO dto) {
        PromotionVO vo = promotionService.updatePromotion(userId, storeId, id, dto);
        return Result.success(vo);
    }

    @Operation(summary = "删除促销活动", description = "删除指定店铺内指定ID的促销活动")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{id}")
    public Result<Void> deletePromotion(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "促销活动ID", required = true) @PathVariable Long id) {
        promotionService.deletePromotion(userId, storeId, id);
        return Result.success(null);
    }

    @Scheduled(cron = "0 0 0 * * ?")    // 每天凌晨执行
    public Result<Void> updatePromotionStatus(){
        promotionService.updatePromotionStatus();
        return Result.success(null);
    }
}