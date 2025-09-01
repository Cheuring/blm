package com.blm.fileservice.util;

import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.fileservice.config.MinioConfig;
import io.minio.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MinioUtil { // todo: 图片重复

    private final static String ORIGIN = "origin";
    private final static String PREVIEW = "preview";

    @Getter
    public enum ImageType {
        AVATAR("avatars"),
        DISHES("dishes"),
        REVIEW("review"),
        APP("app"),
        MICRO_SERVICE("micro_service")
        ;

        private final String directory;

        ImageType(String directory) {
            this.directory = directory;
        }

    }

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    /**
     * Upload file to Minio
     * @param userId User ID for authorization
     * @param file File to be uploaded
     * @param imageType Image type (e.g., avatar, dish)
     * @return URL of the preview image
     */
    public String uploadImages(Long userId, MultipartFile file, ImageType imageType) {
        String filename = "u_" + userId + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            return uploadImage(file, imageType.getDirectory(), filename);
        } catch (Exception e) {
            throw new CommonException(ExceptionConstant.SYS_FILE_UPLOAD_FAILED);
        }
    }

    /**
     * Upload image to Minio with both original and compressed versions
     */
    private String uploadImage(MultipartFile file, String directory, String filename) throws Exception {
        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type: " + contentType);
        }

        // Validate file size
        if (file.getSize() > minioConfig.getMaxFileSizeInBytes()) { // 10MB limit
            throw new IllegalArgumentException("File size exceeds limit: " + file.getSize());
        }

        // Check if bucket exists, create if not
        createBucketIfNotExists();
        
        // Upload original image to /origin
        String originalPath = String.join("/", ORIGIN, directory, filename);
        uploadFile(originalPath, file.getInputStream(), file.getContentType());
        
        // Create thumbnail
        ByteArrayOutputStream thumbnailOutput = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(800, 800)
                .keepAspectRatio(true)
                .outputQuality(0.7)
                .toOutputStream(thumbnailOutput);
        
        // Upload thumbnail to /preview
        String previewPath = PREVIEW + "/" + directory + "/" + filename;
        uploadFile(previewPath, new ByteArrayInputStream(thumbnailOutput.toByteArray()), file.getContentType());
        
        // Return the preview image URL
        return getFileUrl(previewPath);
    }
    
    /**
     * Upload file to Minio
     */
    private void uploadFile(String objectName, InputStream inputStream, String contentType) throws Exception {
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .stream(inputStream, inputStream.available(), -1)
                .contentType(contentType)
                .build());
    }
    
    /**
     * Create bucket if it doesn't exist
     */
    private void createBucketIfNotExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioConfig.getBucketName())
                .build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .build());
        }
    }
    
    private String getFileUrl(String objectName) {
        return minioConfig.getPrefix() +  minioConfig.getBucketName() + "/" + objectName;
    }
    
    /**
     * Verify if user has permission to access the file based on filename
     */
    private boolean verifyFileAccess(String objectName, String userId) {
        String filename = objectName.substring(objectName.lastIndexOf("/") + 1);
        return filename.startsWith("u_" + userId + "_") ||
               filename.startsWith("m_" + userId + "_");
    }

    /**
     * Delete file from Minio
     */
    private void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .build());
    }

    /**
     * Delete url file from Minio
     */
    public void deleteUrlImage(Long userId, String url) {
        // Validate URL format
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        // Check if URL start with bucket name
        if (!url.startsWith(minioConfig.getBucketName())) {
            throw new IllegalArgumentException("Invalid URL: URL does not start with bucket name");
        }

        // Expected URL format: bucketName/preview/directory/filename
        String previewObjectName = url.substring(minioConfig.getBucketName().length() + 1);

        // Validate the object path starts with preview
        if (!previewObjectName.startsWith(PREVIEW + "/")) {
            throw new IllegalArgumentException("Invalid URL: Not a preview image URL");
        }

        // Check if the user has permission to delete the file
        if (!verifyFileAccess(previewObjectName, String.valueOf(userId))) {
            throw new IllegalArgumentException("Permission denied: User does not have access to this file");
        }

        try {
            // Delete the preview file
            deleteFile(previewObjectName);
            // Delete the original file
            String originalObjectName = previewObjectName.replace(PREVIEW, ORIGIN);
            deleteFile(originalObjectName);
        } catch (Exception e) {
            throw new CommonException(ExceptionConstant.SYS_FILE_REMOVE_FAILED);
        }
    }
}
