package com.blm.store.service;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.vo.FoodCategoryVO;

import java.util.List;

public interface FoodCategoryService {

    /**
     * 获取指定店铺的商品分类列表
     *
     * @param storeId 店铺ID
     * @return 分类列表
     */
    List<FoodCategoryVO> getCategoriesByStoreId(Long storeId);

    /**
     * 添加商品分类
     *
     * @param storeId 店铺ID
     * @param dto     商品分类信息
     * @return 添加后的分类信息
     */
    FoodCategoryVO addCategory(Long storeId, FoodCategoryDTO dto);

    /**
     * 更新商品分类
     *
     * @param storeId    店铺ID
     * @param categoryId 分类ID
     * @param dto        商品分类信息
     * @return 更新后的分类信息
     */
    FoodCategoryVO updateCategory(Long storeId, Long categoryId, FoodCategoryDTO dto);

    /**
     * 删除商品分类
     *
     * @param storeId    店铺ID
     * @param categoryId 分类ID
     */
    void deleteCategory(Long storeId, Long categoryId);
}
