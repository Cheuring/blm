package com.blm.store.controller;

import com.blm.common.dto.FoodCreateDTO;
import com.blm.common.dto.FoodStatusUpdateDTO;
import com.blm.common.entity.Food;
import com.blm.common.result.Result;
import com.blm.common.vo.FoodVO;
import com.blm.store.service.MerchantFoodService;
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

@Tag(name = "商家API-商家商品管理", description = "商家管理其店铺内的商品")
@RestController
@RequestMapping("/api/merchant/stores/{storeId}/foods")
@SecurityRequirement(name = "bearerAuth")
public class MerchantFoodController {

    @Autowired
    private MerchantFoodService foodService;

    @Operation(summary = "获取店铺商品列表", description = "获取指定店铺的所有商品信息")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = "application/json", schema = @Schema(type = "array", implementation = FoodVO.class)))
    @GetMapping
    public Result<List<FoodVO>> listFoods(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "商品分类ID (可选)") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "商品状态 (可选, OFF_SHELF, ON_SHELF,SUSPENDED,PENDING)") @RequestParam(required = false) Food.FoodStatus status) {
        // Modify service method to accept categoryId and status if filtering is needed
        List<FoodVO> list = foodService.listFoods(userId, storeId, categoryId, status);
        return Result.success(list);
    }

    @Operation(summary = "获取单个商品信息", description = "根据storeId和foodId获取指定商品信息")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodVO.class)))
    @GetMapping("/{foodId}")
    public Result<FoodVO> getFoodByStoreIdAndFoodId(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "商品ID", required = true) @PathVariable Long foodId) { // Using FoodCreateDTO for update as well
        FoodVO vo = foodService.getFoodByStoreIdAndFoodId(userId, storeId, foodId);
        return Result.success(vo);
    }

    @Operation(summary = "添加商品", description = "向指定店铺添加新商品")
    @ApiResponse(responseCode = "200", description = "添加成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodVO.class)))
    @PostMapping
    public Result<FoodVO> createFood(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @RequestBody(description = "新商品信息", required = true, content = @Content(schema = @Schema(implementation = FoodCreateDTO.class))) @org.springframework.web.bind.annotation.RequestBody FoodCreateDTO dto) {
        FoodVO vo = foodService.createFood(userId, storeId, dto);
        return Result.success(vo);
    }

    @Operation(summary = "更新商品信息", description = "更新指定店铺内指定ID的商品信息")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodVO.class)))
    @PutMapping("/{foodId}")
    public Result<FoodVO> updateFood(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "商品ID", required = true) @PathVariable Long foodId,
            @RequestBody(description = "更新后的商品信息", required = true, content = @Content(schema = @Schema(implementation = FoodCreateDTO.class))) @org.springframework.web.bind.annotation.RequestBody FoodCreateDTO dto) { // Using FoodCreateDTO for update as well
        FoodVO vo = foodService.updateFood(userId, storeId, foodId, dto);
        return Result.success(vo);
    }

    @Operation(summary = "删除商品", description = "删除指定店铺内指定ID的商品")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/{foodId}")
    public Result<Void> deleteFood(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "商品ID", required = true) @PathVariable Long foodId) {
        foodService.deleteFood(userId, storeId, foodId);
        return Result.success(null);
    }

    @Operation(summary = "更改商品状态", description = "更改指定商品的上架/下架状态")
    @ApiResponse(responseCode = "200", description = "状态更新成功")
    @PutMapping("/{foodId}/status")
    public Result<Void> updateStatus(
            @RequestHeader("X-User-Id") Long userId,
            @Parameter(description = "店铺ID", required = true) @PathVariable Long storeId,
            @Parameter(description = "商品ID", required = true) @PathVariable Long foodId,
            @RequestBody(description = "新的状态信息", required = true, content = @Content(schema = @Schema(implementation = FoodStatusUpdateDTO.class))) @org.springframework.web.bind.annotation.RequestBody FoodStatusUpdateDTO dto) {
        foodService.updateStatus(userId, storeId, foodId, dto.getFoodStatus());
        return Result.success(null);
    }

    // todo: file service
//    @Operation(summary = "上传商品图片", description = "上传当前登录商家的商品图片")
//    @ApiResponse(responseCode = "200", description = "头像上传成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
//    @PostMapping(value = "/dishes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Result<String> upload(@RequestParam MultipartFile file) {
//        Long merchantId = securityUtil.getCurrentUserId();
//        return Result.success(minioUtil.uploadImages(merchantId, file, MinioUtil.ImageType.DISHES));
//    }
//
//    @Operation(summary = "删除url对应的图像")
//    @ApiResponse(responseCode = "200", description = "删除成功")
//    @DeleteMapping("/dishes")
//    public Result<Void> deleteAvatar(
//            @RequestBody(description = "删除的文件对应的url,不包含endpoint 例如：blm-images/preview/avatar/1234567890.jpg",
//                    required = true,
//                    content = @Content(schema = @Schema(implementation = FileDeleteDTO.class)))
//            @org.springframework.web.bind.annotation.RequestBody FileDeleteDTO dto) {
//        Long merchantId = securityUtil.getCurrentUserId();
//        minioUtil.deleteUrlImage(merchantId, dto.getUrl());
//        return Result.success(null);
//    }
}