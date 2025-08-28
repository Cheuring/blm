package com.blm.store.service;

import com.blm.common.dto.StoreCreateDTO;
import com.blm.common.entity.Store;
import com.blm.common.vo.StoreVO;
import com.blm.common.vo.StoreCategoryVO;
import java.util.List;

public interface StoreService {
    
    /**
     * 根据ID获取店铺信息
     */
    StoreVO getStoreById(Long storeId);
    
    /**
     * 获取店铺实体
     */
    Store getStoreEntity(Long storeId);
    
    /**
     * 创建店铺
     */
    StoreVO createStore(Long merchantId, StoreCreateDTO dto);
    
    /**
     * 更新店铺信息
     */
    StoreVO updateStore(Long storeId, StoreCreateDTO dto);
    
    /**
     * 获取商家的店铺列表
     */
    List<StoreVO> getStoresByMerchantId(Long merchantId);
    
    /**
     * 获取店铺分类列表
     */
    List<StoreCategoryVO> getStoreCategories();
    
    /**
     * 获取店铺所有者ID
     */
    Long getStoreOwnerId(Long storeId);
    
    /**
     * 验证商家对店铺的所有权
     */
    void verifyStoreOwnership(Long storeId, Long merchantId);
}
