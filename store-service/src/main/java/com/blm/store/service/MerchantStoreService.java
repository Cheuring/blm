package com.blm.store.service;
import com.blm.common.dto.StoreCreateDTO;
import com.blm.common.dto.StoreStatusUpdateDTO;
import com.blm.common.dto.StoreUpdateDTO;
import com.blm.common.vo.StoreVO;

import java.util.List;

public interface MerchantStoreService {
    StoreVO createStore(Long merchantId, StoreCreateDTO dto);
    StoreVO updateStore(Long merchantId, Long storeId, StoreUpdateDTO dto);
    void updateStatus(Long merchantId, Long storeId, StoreStatusUpdateDTO dto);
    List<StoreVO> listMerchantStores(Long merchantId);

    /**
     * 根据商家id和店铺id获取店铺信息
     * @param merchantId
     * @param storeId
     * @return
     */
    StoreVO getStoreById(Long merchantId, Long storeId);
}