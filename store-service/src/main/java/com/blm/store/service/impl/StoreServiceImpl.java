package com.blm.store.service.impl;

import com.blm.common.dto.StoreCreateDTO;
import com.blm.common.entity.Store;
import com.blm.common.entity.StoreCategory;
import com.blm.common.exception.BusinessException;
import com.blm.common.vo.StoreVO;
import com.blm.common.vo.StoreCategoryVO;
import com.blm.store.repository.StoreRepository;
import com.blm.store.repository.StoreCategoryRepository;
import com.blm.store.service.StoreService;
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
public class StoreServiceImpl implements StoreService {
    
    private final StoreRepository storeRepository;
    private final StoreCategoryRepository storeCategoryRepository;
    
    @Override
    public StoreVO getStoreById(Long storeId) {
        Store store = getStoreEntity(storeId);
        return convertToVO(store);
    }
    
    @Override
    public Store getStoreEntity(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException("店铺不存在"));
    }
    
    @Override
    @Transactional
    public StoreVO createStore(Long merchantId, StoreCreateDTO dto) {
        Store store = new Store();
        BeanUtils.copyProperties(dto, store);
        store.setMerchantId(merchantId);
        store.setStatus(Store.StoreStatus.PENDING);
        store.setRating(new java.math.BigDecimal("5.0"));
        store.setMonthlySales(0);
        store.setIsFeatured(false);
        
        LocalDateTime now = LocalDateTime.now();
        store.setCreatedAt(now);
        store.setUpdatedAt(now);
        
        storeRepository.insert(store);
        log.info("创建店铺成功，merchantId={}，storeId={}", merchantId, store.getId());
        
        return convertToVO(store);
    }
    
    @Override
    @Transactional
    public StoreVO updateStore(Long storeId, StoreCreateDTO dto) {
        Store store = getStoreEntity(storeId);
        BeanUtils.copyProperties(dto, store);
        store.setUpdatedAt(LocalDateTime.now());
        
        storeRepository.update(store);
        log.info("更新店铺信息成功，storeId={}", storeId);
        
        return convertToVO(store);
    }
    
    @Override
    public List<StoreVO> getStoresByMerchantId(Long merchantId) {
        List<Store> stores = storeRepository.findByMerchantId(merchantId);
        return stores.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<StoreCategoryVO> getStoreCategories() {
        List<StoreCategory> categories = storeCategoryRepository.findAll();
        return categories.stream()
                .map(this::convertCategoryToVO)
                .collect(Collectors.toList());
    }
    
    @Override
    public Long getStoreOwnerId(Long storeId) {
        Long merchantId = storeRepository.findMerchantIdByStoreId(storeId);
        if (merchantId == null) {
            throw new BusinessException("店铺不存在");
        }
        return merchantId;
    }
    
    @Override
    public void verifyStoreOwnership(Long storeId, Long merchantId) {
        Long ownerId = getStoreOwnerId(storeId);
        if (!ownerId.equals(merchantId)) {
            throw new BusinessException("无权操作该店铺");
        }
    }
    
    private StoreVO convertToVO(Store store) {
        StoreVO vo = new StoreVO();
        BeanUtils.copyProperties(store, vo);
        return vo;
    }
    
    private StoreCategoryVO convertCategoryToVO(StoreCategory category) {
        StoreCategoryVO vo = new StoreCategoryVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}
