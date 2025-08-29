package com.blm.store.service;

import com.blm.common.dto.FoodCreateDTO;
import com.blm.common.entity.Food;
import com.blm.common.vo.FoodVO;

import java.util.List;

public interface MerchantFoodService {
    /**
     * 获取店铺下的商品列表，支持按分类和状态筛选
     */
    List<FoodVO> listFoods(Long merchantId, Long storeId, Long categoryId, Food.FoodStatus status);
    
    /**
     * 创建新商品
     */
    FoodVO createFood(Long merchantId, Long storeId, FoodCreateDTO dto);
    
    /**
     * 更新商品信息
     */
    FoodVO updateFood(Long merchantId, Long storeId, Long foodId, FoodCreateDTO dto);
    
    /**
     * 删除商品
     */
    void deleteFood(Long merchantId, Long storeId, Long foodId);
    
    /**
     * 更新商品状态（上架/下架）
     */
    void updateStatus(Long merchantId, Long storeId, Long foodId, Food.FoodStatus status);

    /**
     * 根据店铺id和商品id获取商品信息
     * @param merchantId
     * @param storeId
     * @param foodId
     * @return
     */
    FoodVO getFoodByStoreIdAndFoodId(Long merchantId, Long storeId, Long foodId);
}