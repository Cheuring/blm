package com.blm.order.service.impl;

import com.blm.common.dto.CartItemDTO;
import com.blm.common.vo.CartVO;
import com.blm.order.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CartService 单元测试
 * 测试购物车服务的所有方法
 */
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartService cartService;

    @BeforeEach
    void setUp() {
        // 在每个测试方法执行前的准备工作
    }

    // ==================== getCarts() 方法测试 ====================

    /**
     * 正向测试：成功获取用户所有购物车
     */
    @Test
    void testGetCarts_Success() {
        // 准备测试数据
        Long userId = 1L;
        
        CartVO cartVO1 = new CartVO();
        cartVO1.setStoreId(1L);
        cartVO1.setStoreName("餐厅A");
        cartVO1.setTotalAmount(new BigDecimal("50.00"));
        
        CartVO cartVO2 = new CartVO();
        cartVO2.setStoreId(2L);
        cartVO2.setStoreName("餐厅B");
        cartVO2.setTotalAmount(new BigDecimal("30.00"));
        
        List<CartVO> expectedCarts = Arrays.asList(cartVO1, cartVO2);
        
        // 模拟服务行为
        when(cartService.getCarts(userId)).thenReturn(expectedCarts);
        
        // 执行测试
        List<CartVO> actualCarts = cartService.getCarts(userId);
        
        // 验证结果
        assertNotNull(actualCarts, "购物车列表不应为null");
        assertEquals(2, actualCarts.size(), "购物车列表大小应为2");
        assertEquals("餐厅A", actualCarts.get(0).getStoreName(), "第一个餐厅名称应该匹配");
        assertEquals("餐厅B", actualCarts.get(1).getStoreName(), "第二个餐厅名称应该匹配");
        assertEquals(new BigDecimal("50.00"), actualCarts.get(0).getTotalAmount(), "第一个购物车总金额应该匹配");
        assertEquals(new BigDecimal("30.00"), actualCarts.get(1).getTotalAmount(), "第二个购物车总金额应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).getCarts(userId);
    }

    /**
     * 正向测试：获取空购物车列表
     */
    @Test
    void testGetCarts_EmptyList() {
        // 准备测试数据
        Long userId = 1L;
        List<CartVO> expectedCarts = Collections.emptyList();
        
        // 模拟服务行为
        when(cartService.getCarts(userId)).thenReturn(expectedCarts);
        
        // 执行测试
        List<CartVO> actualCarts = cartService.getCarts(userId);
        
        // 验证结果
        assertNotNull(actualCarts, "购物车列表不应为null");
        assertTrue(actualCarts.isEmpty(), "购物车列表应为空");
        
        // 验证方法被调用
        verify(cartService, times(1)).getCarts(userId);
    }

    /**
     * 反向测试：用户ID为null
     */
    @Test
    void testGetCarts_NullUserId() {
        // 模拟服务抛出异常
        when(cartService.getCarts(null)).thenThrow(new IllegalArgumentException("用户ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCarts(null);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).getCarts(null);
    }

    /**
     * 反向测试：用户ID为负数
     */
    @Test
    void testGetCarts_NegativeUserId() {
        // 准备测试数据
        Long userId = -1L;
        
        // 模拟服务抛出异常
        when(cartService.getCarts(userId)).thenThrow(new IllegalArgumentException("用户ID必须为正数"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCarts(userId);
        });
        
        assertEquals("用户ID必须为正数", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).getCarts(userId);
    }

    // ==================== getCart() 方法测试 ====================

    /**
     * 正向测试：成功获取特定店铺的购物车
     */
    @Test
    void testGetCart_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 1L;
        
        CartVO expectedCart = new CartVO();
        expectedCart.setStoreId(storeId);
        expectedCart.setStoreName("测试餐厅");
        expectedCart.setTotalAmount(new BigDecimal("45.50"));
        
        // 模拟服务行为
        when(cartService.getCart(userId, storeId)).thenReturn(expectedCart);
        
        // 执行测试
        CartVO actualCart = cartService.getCart(userId, storeId);
        
        // 验证结果
        assertNotNull(actualCart, "购物车不应为null");
        assertEquals(storeId, actualCart.getStoreId(), "店铺ID应该匹配");
        assertEquals("测试餐厅", actualCart.getStoreName(), "店铺名称应该匹配");
        assertEquals(new BigDecimal("45.50"), actualCart.getTotalAmount(), "总金额应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).getCart(userId, storeId);
    }

    /**
     * 反向测试：店铺购物车为空
     */
    @Test
    void testGetCart_EmptyCart() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 1L;
        
        // 模拟服务返回null或空购物车
        when(cartService.getCart(userId, storeId)).thenReturn(null);
        
        // 执行测试
        CartVO actualCart = cartService.getCart(userId, storeId);
        
        // 验证结果
        assertNull(actualCart, "空购物车应该返回null");
        
        // 验证方法被调用
        verify(cartService, times(1)).getCart(userId, storeId);
    }

    /**
     * 反向测试：店铺ID为null
     */
    @Test
    void testGetCart_NullStoreId() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟服务抛出异常
        when(cartService.getCart(userId, null)).thenThrow(new IllegalArgumentException("店铺ID不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.getCart(userId, null);
        });
        
        assertEquals("店铺ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).getCart(userId, null);
    }

    // ==================== addItem() 方法测试 ====================

    /**
     * 正向测试：成功添加商品到购物车
     */
    @Test
    void testAddItem_Success() {
        // 准备测试数据
        Long userId = 1L;
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setStoreId(1L);
        cartItemDTO.setFoodId(1L);
        cartItemDTO.setQuantity(2);
        
        CartVO expectedCart = new CartVO();
        expectedCart.setStoreId(1L);
        expectedCart.setStoreName("测试餐厅");
        expectedCart.setTotalAmount(new BigDecimal("60.00"));
        
        // 模拟服务行为
        when(cartService.addItem(userId, cartItemDTO)).thenReturn(expectedCart);
        
        // 执行测试
        CartVO actualCart = cartService.addItem(userId, cartItemDTO);
        
        // 验证结果
        assertNotNull(actualCart, "添加商品后的购物车不应为null");
        assertEquals(1L, actualCart.getStoreId(), "店铺ID应该匹配");
        assertEquals("测试餐厅", actualCart.getStoreName(), "店铺名称应该匹配");
        assertEquals(new BigDecimal("60.00"), actualCart.getTotalAmount(), "总金额应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).addItem(userId, cartItemDTO);
    }

    /**
     * 反向测试：购物车项DTO为null
     */
    @Test
    void testAddItem_NullCartItemDTO() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟服务抛出异常
        when(cartService.addItem(userId, null)).thenThrow(new IllegalArgumentException("购物车项不能为null"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addItem(userId, null);
        });
        
        assertEquals("购物车项不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).addItem(userId, null);
    }

    /**
     * 反向测试：商品数量为零或负数
     */
    @Test
    void testAddItem_InvalidQuantity() {
        // 准备测试数据
        Long userId = 1L;
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setStoreId(1L);
        cartItemDTO.setFoodId(1L);
        cartItemDTO.setQuantity(0); // 无效数量
        
        // 模拟服务抛出异常
        when(cartService.addItem(userId, cartItemDTO)).thenThrow(new IllegalArgumentException("商品数量必须大于0"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addItem(userId, cartItemDTO);
        });
        
        assertEquals("商品数量必须大于0", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).addItem(userId, cartItemDTO);
    }

    // ==================== updateItemQuantityById() 方法测试 ====================

    /**
     * 正向测试：成功更新购物车项数量
     */
    @Test
    void testUpdateItemQuantityById_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long cartItemId = 1L;
        Integer quantity = 3;
        
        CartVO expectedCart = new CartVO();
        expectedCart.setStoreId(1L);
        expectedCart.setStoreName("测试餐厅");
        expectedCart.setTotalAmount(new BigDecimal("90.00"));
        
        // 模拟服务行为
        when(cartService.updateItemQuantityById(userId, cartItemId, quantity)).thenReturn(expectedCart);
        
        // 执行测试
        CartVO actualCart = cartService.updateItemQuantityById(userId, cartItemId, quantity);
        
        // 验证结果
        assertNotNull(actualCart, "更新后的购物车不应为null");
        assertEquals(1L, actualCart.getStoreId(), "店铺ID应该匹配");
        assertEquals(new BigDecimal("90.00"), actualCart.getTotalAmount(), "更新后的总金额应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).updateItemQuantityById(userId, cartItemId, quantity);
    }

    /**
     * 反向测试：购物车项ID不存在
     */
    @Test
    void testUpdateItemQuantityById_ItemNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long cartItemId = 999L;
        Integer quantity = 2;
        
        // 模拟服务抛出异常
        when(cartService.updateItemQuantityById(userId, cartItemId, quantity))
                .thenThrow(new RuntimeException("购物车项不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.updateItemQuantityById(userId, cartItemId, quantity);
        });
        
        assertEquals("购物车项不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).updateItemQuantityById(userId, cartItemId, quantity);
    }

    /**
     * 反向测试：更新数量为零或负数
     */
    @Test
    void testUpdateItemQuantityById_InvalidQuantity() {
        // 准备测试数据
        Long userId = 1L;
        Long cartItemId = 1L;
        Integer quantity = -1;
        
        // 模拟服务抛出异常
        when(cartService.updateItemQuantityById(userId, cartItemId, quantity))
                .thenThrow(new IllegalArgumentException("商品数量必须大于0"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.updateItemQuantityById(userId, cartItemId, quantity);
        });
        
        assertEquals("商品数量必须大于0", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).updateItemQuantityById(userId, cartItemId, quantity);
    }

    // ==================== removeItemById() 方法测试 ====================

    /**
     * 正向测试：成功移除购物车项
     */
    @Test
    void testRemoveItemById_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long cartItemId = 1L;
        
        CartVO expectedCart = new CartVO();
        expectedCart.setStoreId(1L);
        expectedCart.setStoreName("测试餐厅");
        expectedCart.setTotalAmount(new BigDecimal("30.00"));
        
        // 模拟服务行为
        when(cartService.removeItemById(userId, cartItemId)).thenReturn(expectedCart);
        
        // 执行测试
        CartVO actualCart = cartService.removeItemById(userId, cartItemId);
        
        // 验证结果
        assertNotNull(actualCart, "移除商品后的购物车不应为null");
        assertEquals(1L, actualCart.getStoreId(), "店铺ID应该匹配");
        assertEquals(new BigDecimal("30.00"), actualCart.getTotalAmount(), "移除后的总金额应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).removeItemById(userId, cartItemId);
    }

    /**
     * 反向测试：移除不存在的购物车项
     */
    @Test
    void testRemoveItemById_ItemNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long cartItemId = 999L;
        
        // 模拟服务抛出异常
        when(cartService.removeItemById(userId, cartItemId))
                .thenThrow(new RuntimeException("购物车项不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.removeItemById(userId, cartItemId);
        });
        
        assertEquals("购物车项不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).removeItemById(userId, cartItemId);
    }

    // ==================== clearCart() 方法测试 ====================

    /**
     * 正向测试：成功清空用户购物车
     */
    @Test
    void testClearCart_Success() {
        // 准备测试数据
        Long userId = 1L;
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(cartService).clearCart(userId);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            cartService.clearCart(userId);
        }, "清空购物车不应该抛出异常");
        
        // 验证方法被调用
        verify(cartService, times(1)).clearCart(userId);
    }

    /**
     * 反向测试：清空购物车时用户ID为null
     */
    @Test
    void testClearCart_NullUserId() {
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为null")).when(cartService).clearCart(null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.clearCart(null);
        });
        
        assertEquals("用户ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).clearCart(null);
    }

    // ==================== clearCartByStore() 方法测试 ====================

    /**
     * 正向测试：成功清空特定店铺的购物车
     */
    @Test
    void testClearCartByStore_Success() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 1L;
        
        // 模拟服务行为（void方法不需要返回值）
        doNothing().when(cartService).clearCartByStore(userId, storeId);
        
        // 执行测试
        assertDoesNotThrow(() -> {
            cartService.clearCartByStore(userId, storeId);
        }, "清空店铺购物车不应该抛出异常");
        
        // 验证方法被调用
        verify(cartService, times(1)).clearCartByStore(userId, storeId);
    }

    /**
     * 反向测试：清空店铺购物车时参数为null
     */
    @Test
    void testClearCartByStore_NullParameters() {
        // 模拟服务抛出异常
        doThrow(new IllegalArgumentException("用户ID和店铺ID不能为null"))
                .when(cartService).clearCartByStore(null, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.clearCartByStore(null, null);
        });
        
        assertEquals("用户ID和店铺ID不能为null", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).clearCartByStore(null, null);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testClearCartByStore_StoreNotFound() {
        // 准备测试数据
        Long userId = 1L;
        Long storeId = 999L;
        
        // 模拟服务抛出异常
        doThrow(new RuntimeException("店铺不存在"))
                .when(cartService).clearCartByStore(userId, storeId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.clearCartByStore(userId, storeId);
        });
        
        assertEquals("店铺不存在", exception.getMessage(), "异常消息应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).clearCartByStore(userId, storeId);
    }

    /**
     * 正向测试：添加大量商品到购物车
     */
    @Test
    void testAddItem_LargeQuantity() {
        // 准备测试数据
        Long userId = 1L;
        CartItemDTO cartItemDTO = new CartItemDTO();
        cartItemDTO.setStoreId(1L);
        cartItemDTO.setFoodId(1L);
        cartItemDTO.setQuantity(100); // 大数量
        
        CartVO expectedCart = new CartVO();
        expectedCart.setStoreId(1L);
        expectedCart.setTotalAmount(new BigDecimal("3000.00"));
        
        // 模拟服务行为
        when(cartService.addItem(userId, cartItemDTO)).thenReturn(expectedCart);
        
        // 执行测试
        CartVO actualCart = cartService.addItem(userId, cartItemDTO);
        
        // 验证结果
        assertNotNull(actualCart, "添加大量商品后的购物车不应为null");
        assertEquals(new BigDecimal("3000.00"), actualCart.getTotalAmount(), "大数量商品的总金额应该匹配");
        
        // 验证方法被调用
        verify(cartService, times(1)).addItem(userId, cartItemDTO);
    }
}
