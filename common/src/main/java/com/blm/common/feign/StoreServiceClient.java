package com.blm.common.feign;

import com.blm.common.result.Result;
import com.blm.common.vo.StoreVO;
import com.blm.common.vo.FoodVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 商家服务Feign客户端
 */
@FeignClient(name = "store-service", path = "/api/stores")
public interface StoreServiceClient {

    /**
     * 根据店铺ID获取店铺信息
     */
    @GetMapping("/{storeId}")
    Result<StoreVO> getStoreById(@PathVariable("storeId") Long storeId);

    /**
     * 根据商品ID获取商品信息
     */
    @GetMapping("/foods/{foodId}")
    Result<FoodVO> getFoodById(@PathVariable("foodId") Long foodId);
}
