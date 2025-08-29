package com.blm.store.service.impl;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.entity.FoodCategory;
import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.service.BaseService;
import com.blm.common.vo.FoodCategoryVO;
import com.blm.store.repository.CategoryRepository;
import com.blm.store.service.FoodCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class FoodCategoryServiceImpl extends BaseService implements FoodCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<FoodCategoryVO> getCategoriesByStoreId(Long storeId) {
        List<FoodCategory> categories = categoryRepository.findByStoreId(storeId);
        return entity2VO(categories, FoodCategoryVO.class);
    }

    @Override
    @Transactional
    public FoodCategoryVO addCategory(Long storeId, FoodCategoryDTO dto) {
        FoodCategory category = new FoodCategory();
        category.setStoreId(storeId);
        category.setName(dto.getName());
        category.setSort(dto.getSort());

        LocalDateTime now = LocalDateTime.now();
        category.setCreatedAt(now);
        category.setUpdatedAt(now);

        categoryRepository.insert(category);
        log.info("添加商品分类成功，storeId={}，categoryId={}", storeId, category.getId());

        return convertToVO(category);
    }

    @Override
    @Transactional
    public FoodCategoryVO updateCategory(Long storeId, Long categoryId, FoodCategoryDTO dto) {
        FoodCategory category = categoryRepository.findById(categoryId);
        if (category == null) {
            throw new CommonException(ExceptionConstant.FOOD_CATEGORY_NOT_FOUND);
        }

        if (!category.getStoreId().equals(storeId)) {
            throw new CommonException(ExceptionConstant.FOOD_CATEGORY_UNAUTHORIZED);
        }

        category.setName(dto.getName());
        category.setSort(dto.getSort());
        category.setUpdatedAt(LocalDateTime.now());

        categoryRepository.update(category);
        log.info("更新商品分类成功，categoryId={}", categoryId);

        return convertToVO(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long storeId, Long categoryId) {
        FoodCategory category = categoryRepository.findById(categoryId);
        if (category == null) {
            throw new CommonException(ExceptionConstant.FOOD_CATEGORY_NOT_FOUND);
        }

        if (!category.getStoreId().equals(storeId)) {
            throw new CommonException(ExceptionConstant.FOOD_CATEGORY_UNAUTHORIZED);
        }

        categoryRepository.delete(categoryId, storeId);
        log.info("删除商品分类成功，categoryId={}", categoryId);
    }

    /**
     * 将实体对象转换为VO对象
     */
    private FoodCategoryVO convertToVO(FoodCategory category) {
        FoodCategoryVO vo = new FoodCategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}
