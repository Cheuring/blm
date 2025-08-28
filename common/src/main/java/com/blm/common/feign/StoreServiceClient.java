package com.blm.common.feign;

import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
}
