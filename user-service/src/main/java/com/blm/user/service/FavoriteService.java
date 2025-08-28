package com.blm.user.service;


import com.blm.common.vo.FoodVO;
import com.blm.common.vo.PageVO;
import com.blm.common.vo.StoreVO;

public interface FavoriteService {
    /**
     * 查询用户收藏的店铺列表
     */
    PageVO<StoreVO> listFavoriteStores(Long userId, int page, int size);
    
    /**
     * 添加店铺收藏
     */
    Integer addStoreFavorite(Long userId, Long storeId);
    
    /**
     * 移除店铺收藏
     */
    Integer removeStoreFavorite(Long userId, Long storeId);
    
    /**
     * 查询用户收藏的商品列表
     */
    PageVO<FoodVO> listFavoriteFoods(Long userId, int page, int size);
    
    /**
     * 添加商品收藏
     */
    Integer addFoodFavorite(Long userId, Long foodId);
    
    Integer removeFoodFavorite(Long userId, Long foodId);

    Integer queryFavorite(Long userId, Long targetId, String targetType);
}