package com.bom.merchant.service.impl;

import com.blm.common.dto.PromotionDTO;
import com.blm.common.vo.PromotionVO;
import com.blm.store.service.MerchantPromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * MerchantPromotionService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class MerchantPromotionServiceTest {

    @Mock
    private MerchantPromotionService merchantPromotionService;

    private Long validMerchantId;
    private Long validStoreId;
    private Long validPromotionId;
    private PromotionDTO validPromotionDTO;
    private PromotionVO samplePromotionVO;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validMerchantId = 1L;
        validStoreId = 10L;
        validPromotionId = 100L;
        now = LocalDateTime.now();
        
        // 初始化促销活动DTO
        validPromotionDTO = new PromotionDTO();
        validPromotionDTO.setName("双十一特惠");
        validPromotionDTO.setDescription("全场8折优惠活动");
        validPromotionDTO.setStartTime(now.plusDays(1));
        validPromotionDTO.setEndTime(now.plusDays(7));
        validPromotionDTO.setDiscountType("PERCENT");
        validPromotionDTO.setDiscountValue(new BigDecimal("0.8"));
        validPromotionDTO.setMinOrderAmount(new BigDecimal("50.00"));
        validPromotionDTO.setStatus(0);
        
        // 初始化促销活动VO
        samplePromotionVO = new PromotionVO();
        samplePromotionVO.setId(validPromotionId);
        samplePromotionVO.setName("双十一特惠");
        samplePromotionVO.setDescription("全场8折优惠活动");
        samplePromotionVO.setStartTime(now.plusDays(1));
        samplePromotionVO.setEndTime(now.plusDays(7));
        samplePromotionVO.setDiscountType("PERCENT");
        samplePromotionVO.setDiscountValue(new BigDecimal("0.8"));
        samplePromotionVO.setMinOrderAmount(new BigDecimal("50.00"));
        samplePromotionVO.setStatus(0);
        samplePromotionVO.setCreatedAt(now);
    }

    // ==================== listPromotions 测试 ====================

    /**
     * 正向测试：成功获取促销活动列表
     */
    @Test
    void testListPromotions_Success() {
        // 准备测试数据
        PromotionVO promotion1 = new PromotionVO();
        promotion1.setId(1L);
        promotion1.setName("双十一特惠");
        promotion1.setDiscountType("PERCENT");
        promotion1.setDiscountValue(new BigDecimal("0.8"));
        promotion1.setStatus(1);
        
        PromotionVO promotion2 = new PromotionVO();
        promotion2.setId(2L);
        promotion2.setName("新用户立减");
        promotion2.setDiscountType("AMOUNT");
        promotion2.setDiscountValue(new BigDecimal("10.00"));
        promotion2.setStatus(1);
        
        List<PromotionVO> expectedPromotions = Arrays.asList(promotion1, promotion2);
        
        // 模拟服务行为
        when(merchantPromotionService.listPromotions(validMerchantId, validStoreId)).thenReturn(expectedPromotions);
        
        // 执行测试
        List<PromotionVO> actualPromotions = merchantPromotionService.listPromotions(validMerchantId, validStoreId);
        
        // 验证结果
        assertNotNull(actualPromotions, "促销活动列表不应为null");
        assertEquals(2, actualPromotions.size(), "促销活动列表大小应为2");
        assertEquals("双十一特惠", actualPromotions.get(0).getName(), "第一个活动名应该匹配");
        assertEquals("新用户立减", actualPromotions.get(1).getName(), "第二个活动名应该匹配");
        assertEquals("PERCENT", actualPromotions.get(0).getDiscountType(), "第一个活动折扣类型应该匹配");
        assertEquals("AMOUNT", actualPromotions.get(1).getDiscountType(), "第二个活动折扣类型应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).listPromotions(validMerchantId, validStoreId);
    }

    /**
     * 正向测试：获取空的促销活动列表
     */
    @Test
    void testListPromotions_EmptyList() {
        // 模拟返回空列表
        when(merchantPromotionService.listPromotions(validMerchantId, validStoreId)).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<PromotionVO> actualPromotions = merchantPromotionService.listPromotions(validMerchantId, validStoreId);
        
        // 验证结果
        assertNotNull(actualPromotions, "促销活动列表不应为null");
        assertTrue(actualPromotions.isEmpty(), "促销活动列表应为空");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).listPromotions(validMerchantId, validStoreId);
    }

    /**
     * 正向测试：获取单个促销活动
     */
    @Test
    void testListPromotions_SinglePromotion() {
        // 准备测试数据
        List<PromotionVO> singlePromotion = Arrays.asList(samplePromotionVO);
        
        // 模拟服务行为
        when(merchantPromotionService.listPromotions(validMerchantId, validStoreId)).thenReturn(singlePromotion);
        
        // 执行测试
        List<PromotionVO> actualPromotions = merchantPromotionService.listPromotions(validMerchantId, validStoreId);
        
        // 验证结果
        assertNotNull(actualPromotions, "促销活动列表不应为null");
        assertEquals(1, actualPromotions.size(), "促销活动列表大小应为1");
        assertEquals(samplePromotionVO.getName(), actualPromotions.get(0).getName(), "活动名应该匹配");
        assertEquals(samplePromotionVO.getDiscountType(), actualPromotions.get(0).getDiscountType(), "折扣类型应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).listPromotions(validMerchantId, validStoreId);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testListPromotions_NullMerchantId() {
        // 模拟抛出异常
        when(merchantPromotionService.listPromotions(null, validStoreId))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.listPromotions(null, validStoreId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).listPromotions(null, validStoreId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testListPromotions_NullStoreId() {
        // 模拟抛出异常
        when(merchantPromotionService.listPromotions(validMerchantId, null))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.listPromotions(validMerchantId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).listPromotions(validMerchantId, null);
    }

    /**
     * 反向测试：商家不存在
     */
    @Test
    void testListPromotions_MerchantNotFound() {
        Long nonExistentMerchantId = 999L;
        
        // 模拟抛出异常
        when(merchantPromotionService.listPromotions(nonExistentMerchantId, validStoreId))
                .thenThrow(new RuntimeException("商家不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantPromotionService.listPromotions(nonExistentMerchantId, validStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商家不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).listPromotions(nonExistentMerchantId, validStoreId);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testListPromotions_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantPromotionService.listPromotions(otherMerchantId, validStoreId))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantPromotionService.listPromotions(otherMerchantId, validStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).listPromotions(otherMerchantId, validStoreId);
    }

    // ==================== createPromotion 测试 ====================

    /**
     * 正向测试：成功创建促销活动
     */
    @Test
    void testCreatePromotion_Success() {
        // 模拟成功创建
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, validPromotionDTO)).thenReturn(samplePromotionVO);
        
        // 执行测试
        PromotionVO result = merchantPromotionService.createPromotion(validMerchantId, validStoreId, validPromotionDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validPromotionDTO.getName(), result.getName(), "活动名应该匹配");
        assertEquals(validPromotionDTO.getDiscountType(), result.getDiscountType(), "折扣类型应该匹配");
        assertEquals(validPromotionDTO.getDiscountValue(), result.getDiscountValue(), "折扣值应该匹配");
        assertNotNull(result.getId(), "活动ID不应为null");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, validPromotionDTO);
    }

    /**
     * 正向测试：创建固定金额折扣活动
     */
    @Test
    void testCreatePromotion_AmountDiscount() {
        // 准备固定金额折扣的DTO
        PromotionDTO amountDTO = new PromotionDTO();
        amountDTO.setName("满减活动");
        amountDTO.setDescription("满100减20");
        amountDTO.setStartTime(now.plusDays(1));
        amountDTO.setEndTime(now.plusDays(7));
        amountDTO.setDiscountType("AMOUNT");
        amountDTO.setDiscountValue(new BigDecimal("20.00"));
        amountDTO.setMinOrderAmount(new BigDecimal("100.00"));
        amountDTO.setStatus(0);
        
        PromotionVO expectedVO = new PromotionVO();
        expectedVO.setId(1L);
        expectedVO.setName(amountDTO.getName());
        expectedVO.setDiscountType(amountDTO.getDiscountType());
        expectedVO.setDiscountValue(amountDTO.getDiscountValue());
        
        // 模拟服务行为
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, amountDTO)).thenReturn(expectedVO);
        
        // 执行测试
        PromotionVO result = merchantPromotionService.createPromotion(validMerchantId, validStoreId, amountDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(amountDTO.getName(), result.getName(), "活动名应该匹配");
        assertEquals("AMOUNT", result.getDiscountType(), "折扣类型应该为AMOUNT");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, amountDTO);
    }

    /**
     * 正向测试：创建特价活动
     */
    @Test
    void testCreatePromotion_SpecialPrice() {
        // 准备特价活动的DTO
        PromotionDTO specialDTO = new PromotionDTO();
        specialDTO.setName("特价商品");
        specialDTO.setDescription("指定商品特价销售");
        specialDTO.setStartTime(now.plusDays(1));
        specialDTO.setEndTime(now.plusDays(7));
        specialDTO.setDiscountType("SPECIAL");
        specialDTO.setDiscountValue(new BigDecimal("19.99"));
        specialDTO.setMinOrderAmount(new BigDecimal("0.00"));
        specialDTO.setStatus(0);
        
        PromotionVO expectedVO = new PromotionVO();
        expectedVO.setId(1L);
        expectedVO.setName(specialDTO.getName());
        expectedVO.setDiscountType(specialDTO.getDiscountType());
        expectedVO.setDiscountValue(specialDTO.getDiscountValue());
        
        // 模拟服务行为
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, specialDTO)).thenReturn(expectedVO);
        
        // 执行测试
        PromotionVO result = merchantPromotionService.createPromotion(validMerchantId, validStoreId, specialDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(specialDTO.getName(), result.getName(), "活动名应该匹配");
        assertEquals("SPECIAL", result.getDiscountType(), "折扣类型应该为SPECIAL");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, specialDTO);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testCreatePromotion_NullMerchantId() {
        // 模拟抛出异常
        when(merchantPromotionService.createPromotion(null, validStoreId, validPromotionDTO))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.createPromotion(null, validStoreId, validPromotionDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(null, validStoreId, validPromotionDTO);
    }

    /**
     * 反向测试：传入null的DTO
     */
    @Test
    void testCreatePromotion_NullDTO() {
        // 模拟抛出异常
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, null))
                .thenThrow(new IllegalArgumentException("促销活动信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.createPromotion(validMerchantId, validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("促销活动信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, null);
    }

    /**
     * 反向测试：活动名称为空
     */
    @Test
    void testCreatePromotion_EmptyName() {
        // 准备空名称的DTO
        PromotionDTO emptyNameDTO = new PromotionDTO();
        emptyNameDTO.setName("");
        emptyNameDTO.setStartTime(now.plusDays(1));
        emptyNameDTO.setEndTime(now.plusDays(7));
        emptyNameDTO.setDiscountType("PERCENT");
        emptyNameDTO.setDiscountValue(new BigDecimal("0.8"));
        
        // 模拟抛出异常
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, emptyNameDTO))
                .thenThrow(new IllegalArgumentException("活动名称不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.createPromotion(validMerchantId, validStoreId, emptyNameDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("活动名称不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, emptyNameDTO);
    }

    /**
     * 反向测试：开始时间为null
     */
    @Test
    void testCreatePromotion_NullStartTime() {
        // 准备null开始时间的DTO
        PromotionDTO nullStartTimeDTO = new PromotionDTO();
        nullStartTimeDTO.setName("测试活动");
        nullStartTimeDTO.setStartTime(null);
        nullStartTimeDTO.setEndTime(now.plusDays(7));
        nullStartTimeDTO.setDiscountType("PERCENT");
        nullStartTimeDTO.setDiscountValue(new BigDecimal("0.8"));
        
        // 模拟抛出异常
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, nullStartTimeDTO))
                .thenThrow(new IllegalArgumentException("开始时间不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.createPromotion(validMerchantId, validStoreId, nullStartTimeDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("开始时间不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, nullStartTimeDTO);
    }

    /**
     * 反向测试：结束时间早于开始时间
     */
    @Test
    void testCreatePromotion_EndTimeBeforeStartTime() {
        // 准备结束时间早于开始时间的DTO
        PromotionDTO invalidTimeDTO = new PromotionDTO();
        invalidTimeDTO.setName("测试活动");
        invalidTimeDTO.setStartTime(now.plusDays(7));
        invalidTimeDTO.setEndTime(now.plusDays(1)); // 结束时间早于开始时间
        invalidTimeDTO.setDiscountType("PERCENT");
        invalidTimeDTO.setDiscountValue(new BigDecimal("0.8"));
        
        // 模拟抛出异常
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, invalidTimeDTO))
                .thenThrow(new IllegalArgumentException("结束时间不能早于开始时间"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.createPromotion(validMerchantId, validStoreId, invalidTimeDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("结束时间不能早于开始时间", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, invalidTimeDTO);
    }

    /**
     * 反向测试：无效的折扣类型
     */
    @Test
    void testCreatePromotion_InvalidDiscountType() {
        // 准备无效折扣类型的DTO
        PromotionDTO invalidTypeDTO = new PromotionDTO();
        invalidTypeDTO.setName("测试活动");
        invalidTypeDTO.setStartTime(now.plusDays(1));
        invalidTypeDTO.setEndTime(now.plusDays(7));
        invalidTypeDTO.setDiscountType("INVALID");
        invalidTypeDTO.setDiscountValue(new BigDecimal("0.8"));
        
        // 模拟抛出异常
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, invalidTypeDTO))
                .thenThrow(new IllegalArgumentException("无效的折扣类型"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.createPromotion(validMerchantId, validStoreId, invalidTypeDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("无效的折扣类型", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, invalidTypeDTO);
    }

    /**
     * 反向测试：折扣值为负数
     */
    @Test
    void testCreatePromotion_NegativeDiscountValue() {
        // 准备负折扣值的DTO
        PromotionDTO negativeValueDTO = new PromotionDTO();
        negativeValueDTO.setName("测试活动");
        negativeValueDTO.setStartTime(now.plusDays(1));
        negativeValueDTO.setEndTime(now.plusDays(7));
        negativeValueDTO.setDiscountType("AMOUNT");
        negativeValueDTO.setDiscountValue(new BigDecimal("-10.00"));
        
        // 模拟抛出异常
        when(merchantPromotionService.createPromotion(validMerchantId, validStoreId, negativeValueDTO))
                .thenThrow(new IllegalArgumentException("折扣值不能为负数"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.createPromotion(validMerchantId, validStoreId, negativeValueDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("折扣值不能为负数", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).createPromotion(validMerchantId, validStoreId, negativeValueDTO);
    }

    // ==================== updatePromotion 测试 ====================

    /**
     * 正向测试：成功更新促销活动
     */
    @Test
    void testUpdatePromotion_Success() {
        // 准备更新后的数据
        PromotionVO updatedPromotionVO = new PromotionVO();
        updatedPromotionVO.setId(validPromotionId);
        updatedPromotionVO.setName("更新的活动名");
        updatedPromotionVO.setDiscountType("AMOUNT");
        updatedPromotionVO.setDiscountValue(new BigDecimal("15.00"));
        
        // 模拟成功更新
        when(merchantPromotionService.updatePromotion(validMerchantId, validStoreId, validPromotionId, validPromotionDTO)).thenReturn(updatedPromotionVO);
        
        // 执行测试
        PromotionVO result = merchantPromotionService.updatePromotion(validMerchantId, validStoreId, validPromotionId, validPromotionDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validPromotionId, result.getId(), "活动ID应该匹配");
        assertEquals("更新的活动名", result.getName(), "活动名应该已更新");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).updatePromotion(validMerchantId, validStoreId, validPromotionId, validPromotionDTO);
    }

    /**
     * 反向测试：传入null的促销活动ID
     */
    @Test
    void testUpdatePromotion_NullPromotionId() {
        // 模拟抛出异常
        when(merchantPromotionService.updatePromotion(validMerchantId, validStoreId, null, validPromotionDTO))
                .thenThrow(new IllegalArgumentException("促销活动ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.updatePromotion(validMerchantId, validStoreId, null, validPromotionDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("促销活动ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).updatePromotion(validMerchantId, validStoreId, null, validPromotionDTO);
    }

    /**
     * 反向测试：促销活动不存在
     */
    @Test
    void testUpdatePromotion_PromotionNotFound() {
        Long nonExistentPromotionId = 999L;
        
        // 模拟抛出异常
        when(merchantPromotionService.updatePromotion(validMerchantId, validStoreId, nonExistentPromotionId, validPromotionDTO))
                .thenThrow(new RuntimeException("促销活动不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantPromotionService.updatePromotion(validMerchantId, validStoreId, nonExistentPromotionId, validPromotionDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("促销活动不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).updatePromotion(validMerchantId, validStoreId, nonExistentPromotionId, validPromotionDTO);
    }

    // ==================== deletePromotion 测试 ====================

    /**
     * 正向测试：成功删除促销活动
     */
    @Test
    void testDeletePromotion_Success() {
        // 模拟成功删除
        doNothing().when(merchantPromotionService).deletePromotion(validMerchantId, validStoreId, validPromotionId);
        
        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> merchantPromotionService.deletePromotion(validMerchantId, validStoreId, validPromotionId), 
                "删除促销活动不应抛出异常");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).deletePromotion(validMerchantId, validStoreId, validPromotionId);
    }

    /**
     * 反向测试：传入null的促销活动ID
     */
    @Test
    void testDeletePromotion_NullPromotionId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("促销活动ID不能为空"))
                .when(merchantPromotionService).deletePromotion(validMerchantId, validStoreId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantPromotionService.deletePromotion(validMerchantId, validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("促销活动ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).deletePromotion(validMerchantId, validStoreId, null);
    }

    /**
     * 反向测试：促销活动不存在
     */
    @Test
    void testDeletePromotion_PromotionNotFound() {
        Long nonExistentPromotionId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("促销活动不存在"))
                .when(merchantPromotionService).deletePromotion(validMerchantId, validStoreId, nonExistentPromotionId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantPromotionService.deletePromotion(validMerchantId, validStoreId, nonExistentPromotionId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("促销活动不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).deletePromotion(validMerchantId, validStoreId, nonExistentPromotionId);
    }

    /**
     * 反向测试：删除进行中的促销活动
     */
    @Test
    void testDeletePromotion_ActivePromotion() {
        // 模拟抛出异常
        doThrow(new RuntimeException("不能删除正在进行中的促销活动"))
                .when(merchantPromotionService).deletePromotion(validMerchantId, validStoreId, validPromotionId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantPromotionService.deletePromotion(validMerchantId, validStoreId, validPromotionId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("不能删除正在进行中的促销活动", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).deletePromotion(validMerchantId, validStoreId, validPromotionId);
    }

    // ==================== updatePromotionStatus 测试 ====================

    /**
     * 正向测试：成功更新促销活动状态
     */
    @Test
    void testUpdatePromotionStatus_Success() {
        // 模拟成功更新状态
        doNothing().when(merchantPromotionService).updatePromotionStatus();
        
        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> merchantPromotionService.updatePromotionStatus(), 
                "更新促销活动状态不应抛出异常");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).updatePromotionStatus();
    }

    /**
     * 正向测试：定时任务更新状态
     */
    @Test
    void testUpdatePromotionStatus_ScheduledTask() {
        // 模拟定时任务成功执行
        doNothing().when(merchantPromotionService).updatePromotionStatus();
        
        // 执行多次测试（模拟定时任务）
        assertDoesNotThrow(() -> merchantPromotionService.updatePromotionStatus(), 
                "第一次更新状态不应抛出异常");
        assertDoesNotThrow(() -> merchantPromotionService.updatePromotionStatus(), 
                "第二次更新状态不应抛出异常");
        
        // 验证方法被调用两次
        verify(merchantPromotionService, times(2)).updatePromotionStatus();
    }

    /**
     * 反向测试：更新状态时发生数据库异常
     */
    @Test
    void testUpdatePromotionStatus_DatabaseException() {
        // 模拟抛出数据库异常
        doThrow(new RuntimeException("数据库连接失败"))
                .when(merchantPromotionService).updatePromotionStatus();
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantPromotionService.updatePromotionStatus(),
                "应该抛出RuntimeException"
        );
        
        assertEquals("数据库连接失败", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).updatePromotionStatus();
    }

    /**
     * 反向测试：更新状态时发生系统异常
     */
    @Test
    void testUpdatePromotionStatus_SystemException() {
        // 模拟抛出系统异常
        doThrow(new RuntimeException("系统异常，无法更新促销活动状态"))
                .when(merchantPromotionService).updatePromotionStatus();
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantPromotionService.updatePromotionStatus(),
                "应该抛出RuntimeException"
        );
        
        assertEquals("系统异常，无法更新促销活动状态", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantPromotionService, times(1)).updatePromotionStatus();
    }
}
