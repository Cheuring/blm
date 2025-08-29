package com.blm.store.service.impl;

import com.blm.common.dto.FoodCreateDTO;
import com.blm.common.entity.Food;
import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.vo.FoodVO;
import com.blm.store.repository.FoodRepository;
import com.blm.store.service.MerchantFoodService;
import com.blm.store.service.StoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MerchantFoodServiceImpl implements MerchantFoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private StoreService storeService;


    @Override
    public List<FoodVO> listFoods(Long merchantId, Long storeId, Long categoryId, Food.FoodStatus status) {
        storeService.verifyStoreOwner(storeId, merchantId);
        // 根据参数过滤商品
        List<Food> foods;
        if (categoryId != null && status != null) {
            foods = foodRepository.findByStoreIdAndCategoryIdAndStatus(storeId, categoryId, status);
        } else if (categoryId != null) {
            foods = foodRepository.findByStoreIdAndCategoryId(storeId, categoryId);
        } else if (status != null) {
            foods = foodRepository.findByStoreIdAndStatus(storeId, status);
        } else {
            foods = foodRepository.findONByStoreId(storeId);
        }

        return foods.stream().map(f -> {
            FoodVO vo = new FoodVO();
            BeanUtils.copyProperties(f, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FoodVO createFood(Long merchantId, Long storeId, FoodCreateDTO dto) {
        storeService.verifyStoreOwner(storeId, merchantId);
        Food food = new Food();
        BeanUtils.copyProperties(dto, food);
        food.setStoreId(storeId);
        food.setSales(0);
        food.setStatus(Food.FoodStatus.OFF_SHELF);
        food.setCreatedAt(LocalDateTime.now());
        food.setUpdatedAt(LocalDateTime.now());
        foodRepository.insert(food);
        Food saved = foodRepository.findById(food.getId())
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        FoodVO vo = new FoodVO();
        BeanUtils.copyProperties(saved, vo);
        return vo;
    }

    @Override
    @Transactional
    public FoodVO updateFood(Long merchantId, Long storeId, Long foodId, FoodCreateDTO dto) {
        storeService.verifyStoreOwner(storeId, merchantId);
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        if (!food.getStoreId().equals(storeId)) {
            throw new CommonException(ExceptionConstant.STORE_UNAUTHORIZED);
        }
        BeanUtils.copyProperties(dto, food);
        food.setUpdatedAt(LocalDateTime.now());

        if (dto.getStatus() == 0) {
            food.setStatus(Food.FoodStatus.OFF_SHELF);
        } else {
            food.setStatus(Food.FoodStatus.ON_SHELF);
        }

        if (food.getStatus() == Food.FoodStatus.SUSPENDED) {
            food.setStatus(Food.FoodStatus.PENDING);
        }
        foodRepository.update(food);
        Food updated = foodRepository.findById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        FoodVO vo = new FoodVO();
        BeanUtils.copyProperties(updated, vo);
        return vo;
    }

    @Override
    @Transactional
    public void deleteFood(Long merchantId, Long storeId, Long foodId) {
        storeService.verifyStoreOwner(storeId, merchantId);
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        if (!food.getStoreId().equals(storeId)) {
            throw new CommonException(ExceptionConstant.FOOD_UNAUTHORIZED);
        }
        foodRepository.deleteByIdAndStoreId(foodId, storeId);
    }

    @Override
    @Transactional
    public void updateStatus(Long merchantId, Long storeId, Long foodId, Food.FoodStatus status) {
        storeService.verifyStoreOwner(storeId, merchantId);
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        if (!food.getStoreId().equals(storeId)) {
            throw new CommonException(ExceptionConstant.FOOD_UNAUTHORIZED);
        }
        foodRepository.updateStatus(foodId, storeId, status, LocalDateTime.now());
    }

    @Override
    public FoodVO getFoodByStoreIdAndFoodId(Long merchantId, Long storeId, Long foodId) {
        storeService.verifyStoreOwner(storeId, merchantId);
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        if (!food.getStoreId().equals(storeId)) {
            throw new CommonException(ExceptionConstant.FOOD_UNAUTHORIZED);
        }
        FoodVO vo = new FoodVO();
        BeanUtils.copyProperties(food, vo);
        return vo;
    }
}