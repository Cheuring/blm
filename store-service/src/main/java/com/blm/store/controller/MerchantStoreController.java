package com.blm.store.controller;

import com.blm.common.result.Result;
import com.blm.common.dto.StoreCreateDTO;
import com.blm.common.dto.StoreStatusUpdateDTO;
import com.blm.common.dto.StoreUpdateDTO;
import com.blm.store.service.MerchantStoreService;
import com.blm.common.vo.StoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商家API-商家店铺管理", description = "商家管理自己的店铺信息")
@RestController
@RequestMapping("/api/merchant/stores")
@SecurityRequirement(name = "bearerAuth") // Requires authentication
public class MerchantStoreController {

    @Autowired
    private MerchantStoreService storeService;

    // Note: The api.md specifies GET /api/merchant/stores returns a single StoreVO,
    // but the implementation returns List<StoreVO>. Assuming list is correct for a merchant potentially having multiple stores.
    // If a merchant can only have one store, this should return Result<StoreVO> and the service method adjusted.
    @Operation(summary = "获取商家的店铺列表", description = "获取当前登录商家的所有店铺信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = StoreVO.class)))
    @GetMapping
    public Result<List<StoreVO>> listStores(@RequestHeader("X-User-Id") Long userId) {
        List<StoreVO> list = storeService.listMerchantStores(userId);
        return Result.success(list);
    }

    @Operation(summary = "获取商家单个店铺信息", description = "根据storeId获取当前登录商家的店铺信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreVO.class)))
    @GetMapping("/{id}")
    public Result<StoreVO> getStoreById(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long id) {
        StoreVO storeVO = storeService.getStoreById(userId,id);
        return Result.success(storeVO);
    }

    @Operation(summary = "创建店铺", description = "商家创建新的店铺，提交审核")
    @ApiResponse(responseCode = "200", description = "创建成功，等待审核", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreVO.class)))
    @PostMapping
    public Result<StoreVO> createStore(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody(description = "新店铺信息", required = true, content = @Content(schema = @Schema(implementation = StoreCreateDTO.class))) @org.springframework.web.bind.annotation.RequestBody StoreCreateDTO dto) {
        StoreVO vo = storeService.createStore(userId, dto);
        return Result.success(vo);
    }

    @Operation(summary = "更新店铺信息", description = "商家更新指定ID的店铺信息")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreVO.class)))
    @PutMapping("/{id}")
    public Result<StoreVO> updateStore(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long id,
            @RequestBody(description = "更新后的店铺信息", required = true, content = @Content(schema = @Schema(implementation = StoreUpdateDTO.class))) @org.springframework.web.bind.annotation.RequestBody StoreUpdateDTO dto) {
        StoreVO vo = storeService.updateStore(userId, id, dto);
        return Result.success(vo);
    }

    @Operation(summary = "更改店铺状态", description = "商家更改店铺的营业状态 (例如: OPEN-营业中, CLOSED-已关闭) 注意不能在PENDING 或者 SUSPENDED 状态下更改")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long id,
            @RequestBody(description = "新的状态信息", required = true, content = @Content(schema = @Schema(implementation = StoreStatusUpdateDTO.class))) @org.springframework.web.bind.annotation.RequestBody StoreStatusUpdateDTO dto) {
        storeService.updateStatus(userId, id, dto);
        return Result.success(null);
    }

    // todo: file service
//    @Operation(summary = "上传商家头像", description = "上传当前登录商家的头像")
//    @ApiResponse(responseCode = "200", description = "头像上传成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
//    @PostMapping(value = "/{storeId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Result<String> upload(@RequestParam MultipartFile file,
//                                 @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId) {
//        Long merchantId = securityUtil.getCurrentUserId();
//        return Result.success(minioUtil.uploadImages(merchantId, file, MinioUtil.ImageType.AVATAR));
//    }
//
//    @Operation(summary = "删除url对应的图像")
//    @ApiResponse(responseCode = "200", description = "删除成功")
//    @DeleteMapping("/{storeId}/avatar")
//    public Result<Void> deleteAvatar(
//            @RequestBody(description = "删除的文件对应的url,不包含endpoint 例如：blm-images/preview/avatar/1234567890.jpg",
//                    required = true,
//                    content = @Content(schema = @Schema(implementation = FileDeleteDTO.class)))
//            @org.springframework.web.bind.annotation.RequestBody FileDeleteDTO dto,
//            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId) {
//        Long merchantId = securityUtil.getCurrentUserId();
//        minioUtil.deleteUrlImage(merchantId, dto.getUrl());
//        return Result.success(null);
//    }
}