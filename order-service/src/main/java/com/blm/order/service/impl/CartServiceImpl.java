package com.blm.order.service.impl;


import com.blm.common.dto.CartItemDTO;
import com.blm.common.entity.Cart;
import com.blm.common.entity.Food;
import com.blm.common.entity.Store;
import com.blm.common.exception.CommonException;
import com.blm.common.feign.StoreServiceClient;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.vo.CartItemVO;
import com.blm.common.vo.CartVO;
import com.blm.order.repository.CartRepository;
import com.blm.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StoreServiceClient storeService;

    private CartVO buildCartVO(List<Cart> carts) {
        if (carts.isEmpty()) {
            CartVO emptyCart = new CartVO();
            emptyCart.setItems(new ArrayList<>());
            emptyCart.setTotalAmount(BigDecimal.ZERO);
            return emptyCart; // Return empty cart if no items
        }
        // Get store info from the first item
        Long storeId = carts.get(0).getStoreId();
        Store store = storeService.getStoreById(storeId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_NOT_FOUND));

        List<CartItemVO> items = carts.stream().map(c -> {
            Food food = storeService.getFoodById(c.getFoodId())
                    .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND)); // Should not happen ideally
            CartItemVO vo = new CartItemVO();
            vo.setId(c.getId()); // Cart Item ID
            vo.setFoodId(food.getId());
            vo.setFoodName(food.getName());
            vo.setFoodImage(food.getImage());
            vo.setPrice(food.getPrice());
            vo.setQuantity(c.getQuantity());
            vo.setAmount(food.getPrice().multiply(BigDecimal.valueOf(c.getQuantity())));
            return vo;
        }).collect(Collectors.toList());

        BigDecimal total = items.stream()
                .map(CartItemVO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartVO cartVO = new CartVO();
        cartVO.setStoreId(storeId);
        cartVO.setStoreName(store.getName());
        cartVO.setItems(items);
        cartVO.setTotalAmount(total);
        return cartVO;
    }

    @Override
    public List<CartVO> getCarts(Long userId) {
        List<Cart> rawCarts = cartRepository.findAllByUserId(userId);
        Map<Long, List<Cart>> carts = rawCarts.stream()
                .collect(Collectors.groupingBy(Cart::getStoreId));
        List<CartVO> cartVOs = new ArrayList<>();
        if (carts.isEmpty()) {
            return cartVOs; // No items in cart
        }

        carts.forEach((k, v) -> cartVOs.add(buildCartVO(v)));
        return cartVOs;
    }

    @Override
    public CartVO getCart(Long userId, Long storeId) {
        List<Cart> carts = cartRepository.findAllByUserIdAndStoreId(userId, storeId);
        return buildCartVO(carts);
    }

    @Override
    @Transactional
    public CartVO addItem(Long userId, CartItemDTO dto) {
        // 1. Validate Food and Store
        Food food = storeService.getFoodByIdAndStatus(dto.getFoodId(), Food.FoodStatus.ON_SHELF) // Check if food exists and is available
                .orElseThrow(() -> new CommonException(ExceptionConstant.FOOD_NOT_FOUND));
        if (!food.getStoreId().equals(dto.getStoreId())) {
            throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
        }
        storeService.getStoreByIdAndStatus(dto.getStoreId(), Store.StoreStatus.OPEN) // Check if store exists and is open
                .orElseThrow(() -> new CommonException(ExceptionConstant.STORE_CLOSED));

        // 2. Find existing item or create new
        List<Cart> existingCartItems = cartRepository.findAllByUserIdAndStoreId(userId, dto.getStoreId());
        Optional<Cart> existingItemOpt = existingCartItems.stream()
                .filter(item -> item.getFoodId().equals(dto.getFoodId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            // Update quantity
            Cart existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + dto.getQuantity();
            if (newQuantity <= 0) { // If quantity becomes zero or less, remove item
                cartRepository.deleteByIdAndUserId(existingItem.getId(), userId);
            } else {
                // Check stock if necessary
                // if (food.getStock() != null && newQuantity > food.getStock()) {
                //     throw BusinessException.of(400, "商品库存不足");
                // }
                existingItem.setQuantity(newQuantity);
                existingItem.setUpdatedAt(LocalDateTime.now());
                cartRepository.updateQuantity(existingItem);
            }
        } else {
            // Add new item
            if (dto.getQuantity() <= 0) {
                throw new CommonException(ExceptionConstant.REQ_PARAM_ERROR);
            }
            // Check stock if necessary
            // if (food.getStock() != null && dto.getQuantity() > food.getStock()) {
            //     throw BusinessException.of(400, "商品库存不足");
            // }
            Cart newItem = new Cart();
            newItem.setUserId(userId);
            newItem.setStoreId(dto.getStoreId());
            newItem.setFoodId(dto.getFoodId());
            newItem.setQuantity(dto.getQuantity());
            newItem.setCreatedAt(LocalDateTime.now());
            newItem.setUpdatedAt(LocalDateTime.now());
            cartRepository.insert(newItem);
            existingCartItems.add(newItem);
        }

        return buildCartVO(existingCartItems); // Rebuild cart VO after modification
    }

    @Override
    @Transactional
    public CartVO updateItemQuantityById(Long userId, Long cartItemId, Integer quantity) {
        if (quantity == null || quantity <= 0) {
            // If quantity is zero or less, treat as removal
            return removeItemById(userId, cartItemId);
        }

        Cart cartItem = cartRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_CART_ITEM_NOT_FOUND));

        // Optional: Check stock before updating
        // Food food = foodRepository.findById(cartItem.getFoodId()).orElse(null);
        // if (food != null && food.getStock() != null && quantity > food.getStock()) {
        //     throw BusinessException.of(400, "商品库存不足");
        // }

        cartItem.setQuantity(quantity);
        cartItem.setUpdatedAt(LocalDateTime.now());
        cartRepository.updateQuantity(cartItem);

        return getCart(userId, cartItem.getStoreId());
    }

    @Override
    @Transactional
    public CartVO removeItemById(Long userId, Long cartItemId) {
        Cart cartItem = cartRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.ORDER_CART_ITEM_NOT_FOUND));

        cartRepository.deleteByIdAndUserId(cartItemId, userId);
        return getCart(userId, cartItem.getStoreId());
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByUserId(userId);
    }

    @Override
    public void clearCartByStore(Long userId, Long storeId) {
        cartRepository.deleteByUserIdAndStoreId(userId, storeId);
    }
}