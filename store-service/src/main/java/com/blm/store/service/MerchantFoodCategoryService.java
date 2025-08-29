package com.blm.store.service;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.vo.FoodCategoryVO;

import java.util.List;

public interface MerchantFoodCategoryService {
    List<FoodCategoryVO> listCategories(Long merchantId, Long storeId);
    FoodCategoryVO addCategory(Long merchantId, Long storeId, FoodCategoryDTO dto);
    FoodCategoryVO updateCategory(Long merchantId, Long storeId, Long id, FoodCategoryDTO dto);
    void deleteCategory(Long merchantId, Long storeId, Long id);
}