package com.blm.store.service.impl;

import com.blm.common.dto.StoreQueryDTO;
import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.entity.StoreCategory;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.OrderServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.service.BaseService;
import com.blm.common.vo.*;
import com.blm.store.repository.FoodCategoryRepository;
import com.blm.store.repository.FoodRepository;
import com.blm.store.repository.StoreRepository;
import com.blm.store.service.FoodCategoryService;
import com.blm.store.service.StoreService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreServiceImpl extends BaseService implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderServiceClient orderService;

    @Autowired
    private FoodCategoryService foodCategoryService;
    @Autowired
    private FoodCategoryRepository categoryRepository;

    @Override
    public PageVO<StoreVO> listStores(StoreQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPage(), queryDTO.getSize());

        List<Store> stores = storeRepository.findByFilters(queryDTO);
        Page<Store> pageInfo = (Page<Store>) stores;
        List<StoreVO> storeVOs = entity2VO(stores, StoreVO.class);

        return new PageVO<>(queryDTO.getPage(), queryDTO.getSize(), pageInfo.getTotal(), pageInfo.getPages(), storeVOs);
    }

    @Override
    public List<StoreVO> listRecommended(Double longitude, Double latitude) {
        // todo: 根据用户位置推荐店铺
        return entity2VO(storeRepository.findFeatured(), StoreVO.class);
    }

    @Override
    public StoreDetailVO getStoreDetail(Long storeId) {
        Store store = getStoreById(storeId);
        StoreDetailVO vo = new StoreDetailVO();
        BeanUtils.copyProperties(store, vo);
        // 分类
        List<FoodCategoryVO> categories = foodCategoryService.getCategoriesByStoreId(storeId);
        vo.setCategories(categories);
        // 推荐商品
        List<Food> foods = foodRepository.findONFeaturedByStoreId(storeId);
        vo.setFeaturedFoods(entity2VO(foods, FoodVO.class));
        return vo;
    }

    @Override
    public List<StoreCategoryVO> listStoreCategories() {
        List<StoreCategory> categories = categoryRepository.getStoreCategories();
        return entity2VO(categories, StoreCategoryVO.class);
    }

    @Override
    public List<FoodVO> listStoreFoods(Long storeId, Long categoryId) {
        List<Food> foods;
        if (categoryId == null) {
            foods = foodRepository.findONByStoreId(storeId);
        } else {
            foods = foodRepository.findONByStoreIdAndCategoryId(storeId, categoryId);
        }

        return entity2VO(foods, FoodVO.class);
    }

    @Override
    public FoodDetailVO getFoodDetail(Long foodId) {
        Food food = foodRepository.findONById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        FoodDetailVO vo = new FoodDetailVO();
        BeanUtils.copyProperties(food, vo);

        List<ReviewVO> reviewVOs = entity2VO(
                orderService.getReviewsByFoodId(foodId)
                        .orElseThrow(() -> new CommonException(ExceptionConstant.REVIEW_NOT_FOUND)),
                ReviewVO.class);
        vo.setReviews(reviewVOs);
        return vo;
    }

    @Override
    public Long getStoreOwnerId(Long storeId) {
        Long ownerId = storeRepository.findOwnerIdByStoreId(storeId);
        if (ownerId == null) {
            throw new CommonException(ExceptionConstant.STORE_NOT_FOUND);
        }
        return ownerId;
    }

    /**
     * getStoreOwnerId helper method
     * do not use this method directly, use getStoreOwnerId instead
     */
//    public Object _getStoreOwnerId(Long storeId) {
//        return storeRepository.findOwnerIdByStoreId(storeId);
//    }
    @Override
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));
    }

    @Override
    public void verifyStoreOwner(Long userId, Long storeId) {
        Long storeOwnerId = getStoreOwnerId(storeId);
        if (!storeOwnerId.equals(userId)) {
            throw new CommonException(ExceptionConstant.STORE_UNAUTHORIZED);
        }
    }
}