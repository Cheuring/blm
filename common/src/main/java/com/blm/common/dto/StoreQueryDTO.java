package com.blm.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter @Setter
@Schema(description = "店铺列表查询条件")
public class StoreQueryDTO {
    public enum SearchSort {
        DISTANCE,
        RATING,
        PRICE,
        DELIVERY_TIME
    }

    @Schema(description = "搜索关键字 (店铺名称)")
    private String keyword;

    @Schema(description = "店铺分类ID")
    private Long categoryId;

    @Schema(description = "用户当前经度 (用于计算距离和排序)")
    private Double longitude;

    @Schema(description = "用户当前纬度 (用于计算距离和排序)")
    private Double latitude;

    @Schema(description = "排序方式 (distance/rating/price/deliveryTime)")
    private SearchSort sortBy;

    @Schema(description = "最低价格区间(起送费)")
    private Double minPrice;

    @Schema(description = "最高价格区间")
    private Double maxPrice;

    @Min(1)
    @Schema(description = "页码 (从1开始)", defaultValue = "1")
    private Integer page = 1;

    @Schema(description = "每页数量", defaultValue = "10")
    @Min(1) @Max(100)
    private Integer size = 10;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Optional: common optimization
        if (o == null || getClass() != o.getClass()) return false;
        StoreQueryDTO that = (StoreQueryDTO) o;
        return Objects.equals(keyword, that.keyword) &&
                Objects.equals(page, that.page) &&
                Objects.equals(size, that.size) &&
                sortBy == that.sortBy &&
                (sortBy != SearchSort.DISTANCE || (Objects.equals(latitude, that.latitude) && Objects.equals(longitude, that.longitude))) &&
                Objects.equals(categoryId, that.categoryId) &&
                Objects.equals(minPrice, that.minPrice) &&
                Objects.equals(maxPrice, that.maxPrice);
    }

    @Override
    public int hashCode() {
        // Fields included in hash calculation now align with equals() logic
        return Objects.hash(
                keyword,                // Always considered in equals
                page,                   // Always considered in equals
                size,                   // Always considered in equals
                sortBy,                 // Always considered in equals
                categoryId,             // Always considered in equals
                minPrice,               // Always considered in equals
                maxPrice,               // Always considered in equals
                // Conditionally consider latitude and longitude, same as in equals
                (sortBy == SearchSort.DISTANCE ? latitude : null),
                (sortBy == SearchSort.DISTANCE ? longitude : null)
        );
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(keyword);
        sb.append("_").append(categoryId);

        if (sortBy == SearchSort.DISTANCE) {
            sb.append("_").append(longitude);
            sb.append("_").append(latitude);
        }

        sb.append("_").append(sortBy);
        sb.append("_").append(minPrice);
        sb.append("_").append(maxPrice);
        sb.append("_").append(page);
        sb.append("_").append(size);

        return sb.toString();
    }
}