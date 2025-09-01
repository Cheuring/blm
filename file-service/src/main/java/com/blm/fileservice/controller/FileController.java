package com.blm.fileservice.controller;

import com.blm.common.dto.FileDeleteDTO;
import com.blm.common.result.Result;
import com.blm.fileservice.util.MinioUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理控制器
 *
 * @author Generated
 * @version 1.0
 * @since 2025-09-01
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "文件管理", description = "文件上传、下载、管理相关接口")
public class FileController {

    private final MinioUtil minioUtil;

    @Operation(summary = "上传图片", description = "上传图片")
    @ApiResponse(responseCode = "200", description = "图片上传成功", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    @PostMapping(value = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload(@RequestHeader("X-User-Id") Long userId, @RequestParam MultipartFile file) {
        return Result.success(minioUtil.uploadImages(userId, file, MinioUtil.ImageType.MICRO_SERVICE));
    }

    @Operation(summary = "删除url对应的图像")
    @ApiResponse(responseCode = "200", description = "删除成功")
    @DeleteMapping("/image")
    public Result<Void> deleteAvatar(
            @RequestHeader("X-User-Id") Long userId,
            @RequestBody(description = "删除的文件对应的url,不包含endpoint 例如：blm-images/preview/avatar/1234567890.jpg",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FileDeleteDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody FileDeleteDTO dto) {
        minioUtil.deleteUrlImage(userId, dto.getUrl());
        return Result.success(null);
    }
}
