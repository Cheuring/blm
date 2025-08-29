package com.blm.admin.service;

import com.blm.common.vo.StoreVO;

import java.util.List;

public interface AdminStoreService {
    List<StoreVO> listStores();
    void updateStatus(Long storeId, Integer status);
}