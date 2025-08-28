package com.blm.common.feign;

import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.result.Result;
import com.blm.common.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
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

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/foods/{foodId}")
    Optional<Food> getFoodById(@PathVariable("foodId") Long foodId);
    
    /**
     * 获取店铺的商品分类列表
     */
    @GetMapping("/{storeId}/categories")
    Optional<List<FoodCategoryVO>> getStoreCategories(@PathVariable("storeId") Long storeId);
    
    /**
     * 获取店铺的商品列表
     */
    @GetMapping("/{storeId}/foods")
    Optional<List<FoodVO>> getStoreFoods(@PathVariable("storeId") Long storeId,
                                       @RequestParam(required = false) Long categoryId);
    
    /**
     * 获取店铺的促销活动列表
     */
    @GetMapping("/{storeId}/promotions")
    Optional<List<PromotionVO>> getStorePromotions(@PathVariable("storeId") Long storeId);
    
    /**
     * 验证商家对店铺的所有权
     */
    @GetMapping("/{storeId}/owner")
    Result<Long> getStoreOwnerId(@PathVariable("storeId") Long storeId);
}
