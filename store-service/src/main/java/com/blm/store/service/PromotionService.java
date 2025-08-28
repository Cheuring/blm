package com.blm.store.service;

import com.blm.common.dto.PromotionDTO;
import com.blm.common.vo.PromotionVO;
import java.util.List;

public interface PromotionService {
    
    /**
     * 获取店铺的促销活动列表
     */
    List<PromotionVO> getPromotionsByStoreId(Long storeId);
    
    /**
     * 获取店铺的有效促销活动列表
     */
    List<PromotionVO> getActivePromotionsByStoreId(Long storeId);
    
    /**
     * 创建促销活动
     */
    PromotionVO createPromotion(Long storeId, PromotionDTO dto);
    
    /**
     * 更新促销活动
     */
    PromotionVO updatePromotion(Long storeId, Long promotionId, PromotionDTO dto);
    
    /**
     * 删除促销活动
     */
    void deletePromotion(Long storeId, Long promotionId);
}
