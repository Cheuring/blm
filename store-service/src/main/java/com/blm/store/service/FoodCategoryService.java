package com.blm.store.service;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.vo.FoodCategoryVO;
import java.util.List;

public interface FoodCategoryService {
    
    /**
     * 获取店铺的商品分类列表
     */
    List<FoodCategoryVO> getCategoriesByStoreId(Long storeId);
    
    /**
     * 添加商品分类
     */
    FoodCategoryVO addCategory(Long storeId, FoodCategoryDTO dto);
    
    /**
     * 更新商品分类
     */
    FoodCategoryVO updateCategory(Long storeId, Long categoryId, FoodCategoryDTO dto);
    
    /**
     * 删除商品分类
     */
    void deleteCategory(Long storeId, Long categoryId);
}
