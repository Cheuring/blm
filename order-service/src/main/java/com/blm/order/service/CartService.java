package com.blm.order.service;


import com.blm.common.dto.CartItemDTO;
import com.blm.common.vo.CartVO;

import java.util.List;

public interface CartService {

    /**
     * todo: 分页
     * 获取用户购物车信息
     * @param userId 用户ID
     * @return 购物车视图对象
     */
    List<CartVO> getCarts(Long userId);

    /**
     * 获取用户对应店铺购物车信息
     * @param userId 用户ID
     * @return 购物车视图对象
     */
    CartVO getCart(Long userId, Long storeId);

    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param dto 购物车项数据传输对象 (包含 storeId, foodId, quantity)
     * @return 更新后的购物车视图对象
     */
    CartVO addItem(Long userId, CartItemDTO dto);

    /**
     * 更新购物车中指定项的数量
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @param quantity 新的数量
     * @return 更新后的购物车视图对象
     */
    CartVO updateItemQuantityById(Long userId, Long cartItemId, Integer quantity);

    /**
     * 从购物车中移除指定项
     * @param userId 用户ID
     * @param cartItemId 购物车项ID
     * @return 更新后的购物车视图对象
     */
    CartVO removeItemById(Long userId, Long cartItemId);

    /**
     * 清空用户购物车
     * @param userId 用户ID
     */
    void clearCart(Long userId);

    /**
     * 清空店铺用户购物车
     * @param userId 用户ID
     * @param storeId 店铺ID
     */
    void clearCartByStore(Long userId, Long storeId);
}