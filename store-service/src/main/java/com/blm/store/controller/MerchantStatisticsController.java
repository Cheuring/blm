package com.blm.store.controller;

import com.blm.common.result.Result;
import com.blm.store.service.MerchantStatisticsService;
import com.blm.common.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 商家统计控制器
 */
@Slf4j
@Tag(name = "商家API-商家统计管理", description = "商家查看自己店铺的各项统计数据")
@RestController
@RequestMapping("/api/merchant/stores/statics/{storeId}")
@SecurityRequirement(name = "bearerAuth")
public class MerchantStatisticsController {

    @Autowired
    private MerchantStatisticsService statisticsService;
    
    /**
     * 获取平台统计数据
     */
    @Operation(summary = "获取店铺统计数据", description = "商家查看店铺总体运营数据")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreStatisticsVO.class)))
    @GetMapping
    public Result<StoreStatisticsVO> getPlatformStatistics(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "开始日期 (格式: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "结束日期 (格式: yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // 如果未指定日期，默认为最近30天
        if (startDate == null) {
            startDate = LocalDate.now().minusDays(30);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }
        StoreStatisticsVO stats = statisticsService.getStoreformStatistics(userId,storeId,startDate, endDate);
        return Result.success(stats);
    }

}