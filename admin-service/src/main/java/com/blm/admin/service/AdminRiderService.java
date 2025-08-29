package com.blm.admin.service;

import com.blm.common.vo.RiderVO;

import java.util.List;

public interface AdminRiderService {
    List<RiderVO> listRiders();
    void updateWorkStatus(Long riderId, Integer workStatus);
}