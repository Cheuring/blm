package com.blm.store.service;

import com.blm.common.dto.StoreQueryDTO;
import com.blm.common.entity.Store;
import com.blm.common.vo.*;

import java.util.List;

public interface StoreService {

    /**
     * 根据条件分页查询店铺列表
     *
     * @param queryDTO 查询条件
     * @return 分页后的店铺列表
     */
    PageVO<StoreVO> listStores(StoreQueryDTO queryDTO);

    /**
     * 获取推荐店铺列表
     *
     * @param longitude 用户当前经度 (可选)
     * @param latitude  用户当前纬度 (可选)
     * @return 推荐店铺列表
     */
    List<StoreVO> listRecommended(Double longitude, Double latitude);

    /**
     * 获取店铺详细信息
     *
     * @param storeId 店铺ID
     * @return 店铺详细信息
     */
    StoreDetailVO getStoreDetail(Long storeId);

    /**
     * 获取店铺内商品列表
     *
     * @param storeId    店铺ID
     * @param categoryId 商品分类ID (可选)
     * @return 商品列表
     */
    List<FoodVO> listStoreFoods(Long storeId, Long categoryId);

    /**
     * 获取商品详细信息
     *
     * @param foodId 商品ID
     * @return 商品详细信息 (包含评价)
     */
    FoodDetailVO getFoodDetail(Long foodId); // Changed return type to FoodDetailVO

    Long getStoreOwnerId(Long storeId);

    Store getStoreById(Long storeId);

    void verifyStoreOwner(Long storeId, Long merchantId);

    List<StoreCategoryVO> listStoreCategories();
}