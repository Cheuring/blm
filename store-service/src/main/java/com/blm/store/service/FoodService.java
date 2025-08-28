package com.blm.store.service;

import com.blm.common.vo.FoodVO;
import java.util.List;

public interface FoodService {
    
    /**
     * 根据ID获取商品信息
     */
    FoodVO getFoodById(Long foodId);
    
    /**
     * 获取店铺的商品列表
     */
    List<FoodVO> getFoodsByStoreId(Long storeId, Long categoryId);
    
    /**
     * 获取店铺的特色商品列表
     */
    List<FoodVO> getFeaturedFoodsByStoreId(Long storeId);
    
    /**
     * 更新商品销量
     */
    void updateFoodSales(Long foodId, Integer quantity);
    
    /**
     * 减少商品库存
     */
    boolean decreaseFoodStock(Long foodId, Integer quantity);
}
