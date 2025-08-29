package com.blm.admin.controller;

import com.blm.common.result.Result;
import com.blm.admin.service.AdminService;
import com.blm.common.vo.PlatformStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 管理员平台统计控制器
 */
@Tag(name = "6. 管理员API - 平台统计", description = "管理员查看平台统计数据")
@RestController
@RequestMapping("/api/admin/statistics")
@SecurityRequirement(name = "bearerAuth")
public class AdminStatisticsController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取平台统计数据
     */
    @Operation(summary = "获取平台统计数据", description = "管理员查看平台总体运营数据")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlatformStatsVO.class)))
    @ApiResponse(responseCode = "401", description = "未授权或Token无效")
    @ApiResponse(responseCode = "403", description = "无权限 (非管理员)")
    @GetMapping
    public Result<PlatformStatsVO> getPlatformStatistics(
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
        
        PlatformStatsVO stats = adminService.getPlatformStatistics(startDate, endDate);
        return Result.success(stats);
    }


}