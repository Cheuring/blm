package com.blm.admin.service;

import com.blm.common.vo.OrderVO;

import java.util.List;

public interface AdminOrderService {
    List<OrderVO> listOrders();
    void updateStatus(Long orderId, String orderStatus);
}