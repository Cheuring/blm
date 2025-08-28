package com.blm.store.service.impl;

import com.blm.common.entity.Food;
import com.blm.common.exception.BusinessException;
import com.blm.common.vo.FoodVO;
import com.blm.store.repository.FoodRepository;
import com.blm.store.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {
    
    private final FoodRepository foodRepository;
    
    @Override
    public FoodVO getFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new BusinessException("商品不存在"));
        return convertToVO(food);
    }
    
    @Override
    public List<FoodVO> getFoodsByStoreId(Long storeId, Long categoryId) {
        List<Food> foods;
        if (categoryId != null) {
            foods = foodRepository.findByStoreIdAndCategoryId(storeId, categoryId);
        } else {
            foods = foodRepository.findByStoreId(storeId);
        }
        
        return foods.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<FoodVO> getFeaturedFoodsByStoreId(Long storeId) {
        List<Food> foods = foodRepository.findFeaturedByStoreId(storeId);
        return foods.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void updateFoodSales(Long foodId, Integer quantity) {
        int updated = foodRepository.updateSales(foodId, quantity);
        if (updated == 0) {
            log.warn("更新商品销量失败，foodId={}", foodId);
        } else {
            log.info("更新商品销量成功，foodId={}，增加销量={}", foodId, quantity);
        }
    }
    
    @Override
    @Transactional
    public boolean decreaseFoodStock(Long foodId, Integer quantity) {
        int updated = foodRepository.decreaseStock(foodId, quantity);
        if (updated == 0) {
            log.warn("减少商品库存失败，可能库存不足，foodId={}，数量={}", foodId, quantity);
            return false;
        } else {
            log.info("减少商品库存成功，foodId={}，减少数量={}", foodId, quantity);
            return true;
        }
    }
    
    private FoodVO convertToVO(Food food) {
        FoodVO vo = new FoodVO();
        BeanUtils.copyProperties(food, vo);
        return vo;
    }
}
