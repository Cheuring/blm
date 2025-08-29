package com.blm.store.service.impl;

import com.blm.common.dto.StoreCreateDTO;
import com.blm.common.dto.StoreStatusUpdateDTO;
import com.blm.common.dto.StoreUpdateDTO;
import com.blm.common.entity.Store;
import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.service.BaseService;
import com.blm.common.vo.StoreVO;
import com.blm.store.repository.StoreRepository;
import com.blm.store.service.MerchantStoreService;
import com.blm.store.service.StoreService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MerchantStoreServiceImpl extends BaseService implements MerchantStoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreService storeService;

    @Override
    @Transactional
    public StoreVO createStore(Long merchantId, StoreCreateDTO dto) {
        Store store = new Store();
        BeanUtils.copyProperties(dto, store);
        store.setMerchantId(merchantId);
        store.setStatus(Store.StoreStatus.PENDING); // 待审核
        store.setRating(BigDecimal.valueOf(5.0));
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedAt(LocalDateTime.now());
        int count = storeRepository.insertStore(store);
        if (count != 1) {
            throw new CommonException(ExceptionConstant.STORE_CREATE_FAILED);
        }

        StoreVO vo = new StoreVO();
        BeanUtils.copyProperties(store, vo);
        return vo;
    }

    @Override
    @Transactional
    public StoreVO updateStore(Long merchantId, Long storeId, StoreUpdateDTO dto) {
        Store store = storeService.getStoreById(storeId);
        if (!store.getMerchantId().equals(merchantId)) {
            throw new CommonException(ExceptionConstant.STORE_UNAUTHORIZED);
        }
        BeanUtils.copyProperties(dto, store);
        store.setUpdatedAt(LocalDateTime.now());

        // 如果是 SUSPENDED 状态，更新为 PENDING
        if (store.getStatus() == Store.StoreStatus.SUSPENDED) {
            store.setStatus(Store.StoreStatus.PENDING);
        }

        int count = storeRepository.updateStore(store);
        if (count != 1) {
            throw new CommonException(ExceptionConstant.STORE_UPDATE_FAILED);
        }
        StoreVO vo = new StoreVO();
        BeanUtils.copyProperties(store, vo);
        return vo;
    }

    @Override
    @Transactional
    public void updateStatus(Long merchantId, Long storeId, StoreStatusUpdateDTO dto) {
        Store store = storeService.getStoreById(storeId);
        if (!store.getMerchantId().equals(merchantId)) {
            throw new CommonException(ExceptionConstant.STORE_UNAUTHORIZED);
        }
        // 商家自己只能更改 OPEN 或 CLOSED 状态
        if (store.getStatus() != Store.StoreStatus.OPEN && store.getStatus() != Store.StoreStatus.CLOSED) {
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }
        storeRepository.updateStatus(storeId, merchantId, dto.getStoreStatus(), LocalDateTime.now());
    }

    @Override
    public List<StoreVO> listMerchantStores(Long merchantId) {
        List<Store> stores = storeRepository.findByMerchantId(merchantId);
        return entity2VO(stores, StoreVO.class);
    }

    @Override
    public StoreVO getStoreById(Long merchantId, Long storeId) {

        List<Store> stores = storeRepository.findByMerchantId(merchantId);
        List<StoreVO> storeVOs = entity2VO(stores, StoreVO.class);
        StoreVO vo = new StoreVO();
        for (StoreVO storeVO : storeVOs) {
            if (storeVO.getId().equals(storeId)) {
                BeanUtils.copyProperties(storeVO, vo);
                return vo;
            }
        }
        throw new CommonException(ExceptionConstant.STORE_NOT_FOUND);
    }
}