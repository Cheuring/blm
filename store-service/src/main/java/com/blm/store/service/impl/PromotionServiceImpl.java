package com.blm.store.service.impl;

import com.blm.common.dto.PromotionDTO;
import com.blm.common.entity.Promotion;
import com.blm.common.exception.BusinessException;
import com.blm.common.vo.PromotionVO;
import com.blm.store.repository.PromotionRepository;
import com.blm.store.service.PromotionService;
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
public class PromotionServiceImpl implements PromotionService {
    
    private final PromotionRepository promotionRepository;
    
    @Override
    public List<PromotionVO> getPromotionsByStoreId(Long storeId) {
        List<Promotion> promotions = promotionRepository.findByStoreId(storeId);
        return promotions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PromotionVO> getActivePromotionsByStoreId(Long storeId) {
        List<Promotion> promotions = promotionRepository.findActiveByStoreId(storeId);
        return promotions.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public PromotionVO createPromotion(Long storeId, PromotionDTO dto) {
        Promotion promotion = new Promotion();
        BeanUtils.copyProperties(dto, promotion);
        promotion.setStoreId(storeId);
        promotion.setStatus(Promotion.PromotionStatus.ACTIVE);
        promotion.setCreatedAt(LocalDateTime.now());
        
        promotionRepository.insert(promotion);
        log.info("创建促销活动成功，storeId={}，promotionId={}", storeId, promotion.getId());
        
        return convertToVO(promotion);
    }
    
    @Override
    @Transactional
    public PromotionVO updatePromotion(Long storeId, Long promotionId, PromotionDTO dto) {
        Promotion promotion = promotionRepository.findByIdAndStoreId(promotionId, storeId)
                .orElseThrow(() -> new BusinessException("促销活动不存在"));
        
        BeanUtils.copyProperties(dto, promotion);
        
        promotionRepository.update(promotion);
        log.info("更新促销活动成功，storeId={}，promotionId={}", storeId, promotionId);
        
        return convertToVO(promotion);
    }
    
    @Override
    @Transactional
    public void deletePromotion(Long storeId, Long promotionId) {
        int deleted = promotionRepository.delete(promotionId, storeId);
        if (deleted == 0) {
            throw new BusinessException("促销活动不存在");
        }
        log.info("删除促销活动成功，storeId={}，promotionId={}", storeId, promotionId);
    }
    
    private PromotionVO convertToVO(Promotion promotion) {
        PromotionVO vo = new PromotionVO();
        BeanUtils.copyProperties(promotion, vo);
        return vo;
    }
}
