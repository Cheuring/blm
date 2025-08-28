package com.blm.store.service.impl;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.entity.FoodCategory;
import com.blm.common.exception.BusinessException;
import com.blm.common.vo.FoodCategoryVO;
import com.blm.store.repository.FoodCategoryRepository;
import com.blm.store.service.FoodCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoodCategoryServiceImpl implements FoodCategoryService {
    
    private final FoodCategoryRepository foodCategoryRepository;
    
    @Override
    public List<FoodCategoryVO> getCategoriesByStoreId(Long storeId) {
        List<FoodCategory> categories = foodCategoryRepository.findByStoreId(storeId);
        return categories.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public FoodCategoryVO addCategory(Long storeId, FoodCategoryDTO dto) {
        FoodCategory category = new FoodCategory();
        BeanUtils.copyProperties(dto, category);
        category.setStoreId(storeId);
        
        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);
        
        foodCategoryRepository.insert(category);
        log.info("添加商品分类成功，storeId={}，categoryId={}", storeId, category.getId());
        
        return convertToVO(category);
    }
    
    @Override
    @Transactional
    public FoodCategoryVO updateCategory(Long storeId, Long categoryId, FoodCategoryDTO dto) {
        FoodCategory category = foodCategoryRepository.findByIdAndStoreId(categoryId, storeId)
                .orElseThrow(() -> new BusinessException("商品分类不存在"));
        
        BeanUtils.copyProperties(dto, category);
        category.setUpdatedAt(LocalDateTime.now());
        
        foodCategoryRepository.update(category);
        log.info("更新商品分类成功，storeId={}，categoryId={}", storeId, categoryId);
        
        return convertToVO(category);
    }
    
    @Override
    @Transactional
    public void deleteCategory(Long storeId, Long categoryId) {
        int deleted = foodCategoryRepository.delete(categoryId, storeId);
        if (deleted == 0) {
            throw new BusinessException("商品分类不存在");
        }
        log.info("删除商品分类成功，storeId={}，categoryId={}", storeId, categoryId);
    }
    
    private FoodCategoryVO convertToVO(FoodCategory category) {
        FoodCategoryVO vo = new FoodCategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}
