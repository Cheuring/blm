package com.blm.store.service.impl;

import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.dto.PromotionDTO;
import com.blm.common.entity.Promotion;
import com.blm.store.repository.PromotionRepository;
import com.blm.store.service.MerchantPromotionService;
import com.blm.store.service.StoreService;
import com.blm.common.vo.PromotionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerchantPromotionServiceImpl implements MerchantPromotionService {

    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private StoreService storeService;
    

    @Override
    public List<PromotionVO> listPromotions(Long merchantId, Long storeId) {
        storeService.verifyStoreOwner(storeId, merchantId);
        return promotionRepository.findByStoreId(storeId).stream().map(p -> {
            PromotionVO vo = new PromotionVO();
            BeanUtils.copyProperties(p, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PromotionVO createPromotion(Long merchantId, Long storeId, PromotionDTO dto) {
        storeService.verifyStoreOwner(storeId, merchantId);
        Promotion p = new Promotion();
        BeanUtils.copyProperties(dto, p);
        p.setStoreId(storeId);
        p.setCreatedAt(LocalDateTime.now());
        promotionRepository.insert(p);
        Promotion saved = promotionRepository.findByIdAndStoreId(p.getId(), storeId)
            .orElseThrow(() -> new CommonException(ExceptionConstant.PROMOTION_NOT_FOUND.getCode(), ExceptionConstant.PROMOTION_NOT_FOUND.getMessage()));
        PromotionVO vo = new PromotionVO();
        BeanUtils.copyProperties(saved, vo);
        return vo;
    }

    @Override
    @Transactional
    public PromotionVO updatePromotion(Long merchantId, Long storeId, Long promotionId, PromotionDTO dto) {
        storeService.verifyStoreOwner(storeId, merchantId);
        Promotion p = promotionRepository.findByIdAndStoreId(promotionId, storeId)
            .orElseThrow(() -> new CommonException(ExceptionConstant.PROMOTION_NOT_FOUND.getCode(), ExceptionConstant.PROMOTION_NOT_FOUND.getMessage()));
        BeanUtils.copyProperties(dto, p);
        promotionRepository.update(p);
        Promotion updated = promotionRepository.findByIdAndStoreId(promotionId, storeId)
            .orElseThrow(() -> new CommonException(ExceptionConstant.PROMOTION_NOT_FOUND.getCode(), ExceptionConstant.PROMOTION_NOT_FOUND.getMessage()));
        PromotionVO vo = new PromotionVO();
        BeanUtils.copyProperties(updated, vo);
        return vo;
    }

    @Override
    @Transactional
    public void deletePromotion(Long merchantId, Long storeId, Long promotionId) {
        storeService.verifyStoreOwner(storeId, merchantId);
        promotionRepository.delete(promotionId, storeId);
    }

    /**
     * 定时任务，每天凌晨0点执行，更新优惠券状态
     */
    @Transactional
    public void updatePromotionStatus() {
        log.info("定时任务开始执行，更新优惠券状态");
        List<Promotion> promotions=promotionRepository.findAll();
        System.out.println(promotions);
        for (Promotion promotion : promotions) {
            if (promotion.getEndTime().isBefore(LocalDateTime.now())) {
                // 如果当前时间超过了优惠券的结束时间，则更新状态为不可用
                promotion.setStatus(0); // 假设 0 表示不可用
                promotionRepository.update(promotion);
            }
        }
    }

}