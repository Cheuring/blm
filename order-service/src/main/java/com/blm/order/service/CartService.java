package com.blm.order.service;

import com.blm.common.dto.CartItemDTO;
import com.blm.common.vo.CartVO;

public interface CartService {
    
    /**
     * 获取购物车
     */
    CartVO getCart(Long userId, Long storeId);
    
    /**
     * 添加商品到购物车
     */
    CartVO addItem(Long userId, CartItemDTO dto);
    
    /**
     * 更新购物车商品数量
     */
    CartVO updateItem(Long userId, Long cartItemId, Integer quantity);
    
    /**
     * 移除购物车商品
     */
    CartVO removeItem(Long userId, Long cartItemId);
    
    /**
     * 清空购物车
     */
    void clearCart(Long userId, Long storeId);
}
