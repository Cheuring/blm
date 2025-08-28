package com.blm.order.service.impl;

import com.blm.common.dto.CartItemDTO;
import com.blm.common.entity.Cart;
import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.exception.BusinessException;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.result.Result;
import com.blm.common.vo.CartItemVO;
import com.blm.common.vo.CartVO;
import com.blm.common.vo.FoodVO;
import com.blm.common.vo.StoreVO;
import com.blm.order.repository.CartRepository;
import com.blm.order.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    
    private final CartRepository cartRepository;
    private final StoreServiceClient storeServiceClient;
    
    @Override
    public CartVO getCart(Long userId, Long storeId) {
        List<Cart> carts = cartRepository.findByUserIdAndStoreId(userId, storeId);
        return buildCartVO(carts, storeId);
    }
    
    @Override
    @Transactional
    public CartVO addItem(Long userId, CartItemDTO dto) {
        // 验证商品存在且可购买
        Result<FoodVO> foodResult = storeServiceClient.getFoodById(dto.getFoodId());
        if (!foodResult.isSuccess() || foodResult.getData() == null) {
            throw new BusinessException("商品不存在");
        }
        
        FoodVO food = foodResult.getData();
        if (!Food.FoodStatus.ON_SHELF.equals(food.getStatus())) {
            throw new BusinessException("商品已下架");
        }
        
        if (!food.getStoreId().equals(dto.getStoreId())) {
            throw new BusinessException("商品不属于该店铺");
        }
        
        // 检查是否已存在该商品
        Optional<Cart> existingCart = cartRepository.findByUserIdAndStoreIdAndFoodId(
                userId, dto.getStoreId(), dto.getFoodId());
        
        if (existingCart.isPresent()) {
            // 更新数量
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + dto.getQuantity());
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.updateQuantity(cart.getId(), cart.getQuantity(), cart.getUpdatedAt());
        } else {
            // 新增购物车项
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setStoreId(dto.getStoreId());
            cart.setFoodId(dto.getFoodId());
            cart.setQuantity(dto.getQuantity());
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            cartRepository.insert(cart);
        }
        
        return getCart(userId, dto.getStoreId());
    }
    
    @Override
    @Transactional
    public CartVO updateItem(Long userId, Long cartItemId, Integer quantity) {
        Cart cart = cartRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new BusinessException("购物车项不存在"));
        
        if (quantity <= 0) {
            cartRepository.deleteByIdAndUserId(cartItemId, userId);
        } else {
            cartRepository.updateQuantity(cartItemId, quantity, LocalDateTime.now());
        }
        
        return getCart(userId, cart.getStoreId());
    }
    
    @Override
    @Transactional
    public CartVO removeItem(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new BusinessException("购物车项不存在"));
        
        Long storeId = cart.getStoreId();
        cartRepository.deleteByIdAndUserId(cartItemId, userId);
        
        return getCart(userId, storeId);
    }
    
    @Override
    @Transactional
    public void clearCart(Long userId, Long storeId) {
        cartRepository.deleteByUserIdAndStoreId(userId, storeId);
    }
    
    /**
     * 构建购物车VO
     */
    private CartVO buildCartVO(List<Cart> carts, Long storeId) {
        if (carts.isEmpty()) {
            CartVO cartVO = new CartVO();
            cartVO.setStoreId(storeId);
            cartVO.setItems(List.of());
            cartVO.setTotalAmount(BigDecimal.ZERO);
            cartVO.setTotalItems(0);
            return cartVO;
        }
        
        // 获取店铺信息
        StoreVO store = null;
        try {
            Result<StoreVO> storeResult = storeServiceClient.getStoreById(storeId);
            if (storeResult.isSuccess()) {
                store = storeResult.getData();
            }
        } catch (Exception e) {
            log.warn("获取店铺信息失败: {}", e.getMessage());
        }
        
        // 构建购物车项
        List<CartItemVO> items = carts.stream().map(cart -> {
            CartItemVO itemVO = new CartItemVO();
            itemVO.setId(cart.getId());
            itemVO.setFoodId(cart.getFoodId());
            itemVO.setQuantity(cart.getQuantity());
            
            // 获取商品信息
            try {
                Result<FoodVO> foodResult = storeServiceClient.getFoodById(cart.getFoodId());
                if (foodResult.isSuccess() && foodResult.getData() != null) {
                    FoodVO food = foodResult.getData();
                    itemVO.setFoodName(food.getName());
                    itemVO.setFoodImage(food.getImage());
                    itemVO.setPrice(food.getPrice());
                    itemVO.setAmount(food.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
                } else {
                    // 商品不存在时的默认值
                    itemVO.setFoodName("商品已下架");
                    itemVO.setPrice(BigDecimal.ZERO);
                    itemVO.setAmount(BigDecimal.ZERO);
                }
            } catch (Exception e) {
                log.warn("获取商品信息失败: {}", e.getMessage());
                itemVO.setFoodName("获取商品信息失败");
                itemVO.setPrice(BigDecimal.ZERO);
                itemVO.setAmount(BigDecimal.ZERO);
            }
            
            return itemVO;
        }).collect(Collectors.toList());
        
        // 计算总金额和总数量
        BigDecimal totalAmount = items.stream()
                .map(CartItemVO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Integer totalItems = items.stream()
                .mapToInt(CartItemVO::getQuantity)
                .sum();
        
        CartVO cartVO = new CartVO();
        cartVO.setStoreId(storeId);
        cartVO.setStoreName(store != null ? store.getName() : "未知店铺");
        cartVO.setItems(items);
        cartVO.setTotalAmount(totalAmount);
        cartVO.setTotalItems(totalItems);
        
        return cartVO;
    }
}
