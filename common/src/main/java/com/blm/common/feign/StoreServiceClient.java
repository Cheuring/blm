package com.blm.common.feign;

import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.entity.StoreCategory;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreCategoryVO;
import com.blm.common.vo.StoreVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 商家服务Feign客户端
 */
@FeignClient(name = "store-service", path = "/internal/stores")
public interface StoreServiceClient {

    /**
     * 根据店铺ID获取店铺信息
     */
    @GetMapping("/{storeId}")
    Optional<Store> getStoreById(@PathVariable("storeId") Long storeId);

    @GetMapping("/{storeId}/status")
    Optional<Store> getStoreByIdAndStatus(@PathVariable("storeId") Long storeId, @RequestParam("status") Store.StoreStatus status);

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/foods/{foodId}")
    Optional<Food> getFoodById(@PathVariable("foodId") Long foodId);

    @GetMapping("/foods/{foodId}/status")
    Optional<Food> getFoodByIdAndStatus(@PathVariable("foodId") Long foodId, @RequestParam("status") Food.FoodStatus status);

    @GetMapping("/{storeId}/owner")
    Optional<Long> getStoreOwnerIdByStoreId(@PathVariable("storeId") Long storeId);

    @GetMapping("/")
    Optional<PageVO<StoreVO>> getStoreByConditions(
            @RequestParam(value = "status", required = false) Store.StoreStatus status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @GetMapping("/{storeId}/foods")
    Optional<PageVO<FoodVO>> getFoodByConditions(
            @PathVariable("storeId") Long storeId,
            @RequestParam(value = "status", required = false) Food.FoodStatus status,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam("page") int page,
            @RequestParam("size") int size
    );

    @PutMapping("/{storeId}/audit")
    int auditStore(
            @PathVariable("storeId") Long storeId,
            @RequestParam(value = "status", required = false) Store.StoreStatus status,
            @RequestParam(value = "reason", required = false) String reason,
            @RequestParam("updateAt") LocalDateTime updateAt
            );

    @PutMapping("/food/{foodId}/audit")
    int auditFood(
            @PathVariable("foodId") Long foodId,
            @RequestParam(value = "status", required = false) Food.FoodStatus status,
            @RequestParam(value = "reason", required = false) String reason,
            @RequestParam("updateAt") LocalDateTime updateAt
    );

    @PutMapping("/{storeId}/rating")
    int updateStoreRating(
            @PathVariable("storeId") Long storeId,
            @RequestParam("newRating") double newRating,
            @RequestParam("updateAt") LocalDateTime updateAt
    );
    
    @GetMapping("/category")
    Optional<PageVO<StoreCategoryVO>> getAllStoreCategory(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size
    );

    @GetMapping("/category/{id}")
    Optional<StoreCategory> getStoreCategoryById(
            @PathVariable("id") Long id
    );

    @PutMapping("/category/{id}")
    Optional<StoreCategory> updateStoreCategory(
            @PathVariable("id") Long id,
            @RequestBody StoreCategory storeCategory
    );

    @DeleteMapping("/category/{id}")
    int deleteStoreCategoryById(
            @PathVariable("id") Long id
    );

    @PostMapping("/category")
    Optional<StoreCategory> addStoreCategory(@RequestBody StoreCategory storeCategory);
}
