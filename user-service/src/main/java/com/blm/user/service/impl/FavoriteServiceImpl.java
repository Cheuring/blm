package com.blm.user.service.impl;

import com.blm.common.entity.Favorite;
import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;
import com.blm.user.repository.FavoriteRepository;
import com.blm.user.service.FavoriteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StoreServiceClient storeService;

    @Override
    public PageVO<StoreVO> listFavoriteStores(Long userId, int page, int size) {
        PageHelper.startPage(page, size);

        List<Favorite> favStores = favoriteRepository.findAllByUserId(userId, Favorite.TYPE_STORE);
        Page<Favorite> pageInfo = (Page<Favorite>) favStores;

        List<StoreVO> storeVOs = favStores.stream().map(fav -> {
            Store store = storeService.getStoreById(fav.getTargetId())
                    .orElse(new Store());
            StoreVO vo = new StoreVO();
            BeanUtils.copyProperties(store, vo);
            return vo;
        }).collect(Collectors.toList());

        return new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), storeVOs);
    }

    @Override
    @Transactional
    public Integer addStoreFavorite(Long userId, Long storeId) {
        storeService.getStoreById(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));
        int count = queryFavorite(userId, storeId, Favorite.TYPE_STORE);
        if (count > 0) {
            throw new CommonException(ExceptionConstant.STORE_ALREADY_FAV);
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setTargetId(storeId);
        favorite.setType(Favorite.TYPE_STORE);
        favorite.setCreatedAt(LocalDateTime.now());
        favoriteRepository.insert(favorite);

        return 1;
    }

    @Override
    @Transactional
    public Integer removeStoreFavorite(Long userId, Long storeId) {
        favoriteRepository.deleteByUserIdAndTargetId(userId, storeId, Favorite.TYPE_STORE);
        return 0;
    }
    
    @Override
    public PageVO<FoodVO> listFavoriteFoods(Long userId, int page, int size) {
        PageHelper.startPage(page, size);

        List<Favorite> favFoods = favoriteRepository.findAllByUserId(userId, Favorite.TYPE_FOOD);
        Page<Favorite> pageInfo = (Page<Favorite>) favFoods;

        List<FoodVO> foodVOs = favFoods.stream().map(fav -> {
            Food food = storeService.getFoodById(fav.getTargetId())
                    .orElse(new Food());
            FoodVO vo = new FoodVO();
            BeanUtils.copyProperties(food, vo);
            return vo;
        }).collect(Collectors.toList());

        return new PageVO<>(page, size, pageInfo.getTotal(), pageInfo.getPages(), foodVOs);
    }
    
    @Override
    @Transactional
    public Integer addFoodFavorite(Long userId, Long foodId) {
        storeService.getFoodById(foodId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        int count = queryFavorite(userId, foodId, Favorite.TYPE_FOOD);
        if (count > 0) {
            throw new CommonException(ExceptionConstant.FOOD_ALREADY_FAV);
        }
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setTargetId(foodId);
        favorite.setType(Favorite.TYPE_FOOD); // 添加类型标识
        favorite.setCreatedAt(LocalDateTime.now());
        favoriteRepository.insert(favorite);

        return 1;
    }
    
    @Override
    @Transactional
    public Integer removeFoodFavorite(Long userId, Long foodId) {
        favoriteRepository.deleteByUserIdAndTargetId(userId, foodId, Favorite.TYPE_FOOD);
        return 0;
    }

    @Override
    public Integer queryFavorite(Long userId, Long targetId, String targetType) {
        return favoriteRepository.countByUserIdAndTargetId(userId, targetId, targetType);
    }
}