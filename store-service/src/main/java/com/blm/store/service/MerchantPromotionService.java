package com.blm.store.service;

import com.blm.common.dto.PromotionDTO;
import com.blm.common.vo.PromotionVO;

import java.util.List;

public interface MerchantPromotionService {
    List<PromotionVO> listPromotions(Long merchantId, Long storeId);
    PromotionVO createPromotion(Long merchantId, Long storeId, PromotionDTO dto);
    PromotionVO updatePromotion(Long merchantId, Long storeId, Long promotionId, PromotionDTO dto);
    void deletePromotion(Long merchantId, Long storeId, Long promotionId);
    void updatePromotionStatus();
}