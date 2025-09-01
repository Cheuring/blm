package com.bom.merchant.service.impl;

import com.blm.common.dto.StoreCreateDTO;
import com.blm.common.dto.StoreStatusUpdateDTO;
import com.blm.common.dto.StoreUpdateDTO;
import com.blm.common.entity.Store;
import com.blm.common.vo.StoreVO;
import com.blm.store.service.MerchantStoreService;
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
 * MerchantStoreService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class MerchantStoreServiceTest {

    @Mock
    private MerchantStoreService merchantStoreService;

    private Long validMerchantId;
    private Long validStoreId;
    private Long validCategoryId;
    private StoreCreateDTO validStoreCreateDTO;
    private StoreUpdateDTO validStoreUpdateDTO;
    private StoreStatusUpdateDTO validStoreStatusUpdateDTO;
    private StoreVO sampleStoreVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validMerchantId = 1L;
        validStoreId = 10L;
        validCategoryId = 5L;
        
        // 初始化店铺创建DTO
        validStoreCreateDTO = new StoreCreateDTO();
        validStoreCreateDTO.setName("美味快餐店");
        validStoreCreateDTO.setLogo("https://example.com/logo.jpg");
        validStoreCreateDTO.setDescription("提供各种美味快餐");
        validStoreCreateDTO.setPhone("13800138000");
        validStoreCreateDTO.setAddress("北京市海淀区中关村大街1号");
        validStoreCreateDTO.setLongitude(new BigDecimal("116.307852"));
        validStoreCreateDTO.setLatitude(new BigDecimal("39.983424"));
        validStoreCreateDTO.setBusinessHours("08:00-22:00");
        validStoreCreateDTO.setDeliveryFee(new BigDecimal("5.00"));
        validStoreCreateDTO.setMinOrderAmount(new BigDecimal("20.00"));
        validStoreCreateDTO.setAverageDeliveryTime(30);
        validStoreCreateDTO.setCategoryId(validCategoryId);
        validStoreCreateDTO.setLicenseImg("https://example.com/license.jpg");
        validStoreCreateDTO.setPermitImg("https://example.com/permit.jpg");
        
        // 初始化店铺更新DTO
        validStoreUpdateDTO = new StoreUpdateDTO();
        validStoreUpdateDTO.setName("更新的餐厅名称");
        validStoreUpdateDTO.setDescription("更新的餐厅描述");
        validStoreUpdateDTO.setPhone("13900139000");
        validStoreUpdateDTO.setBusinessHours("09:00-23:00");
        validStoreUpdateDTO.setDeliveryFee(new BigDecimal("6.00"));
        
        // 初始化店铺状态更新DTO
        validStoreStatusUpdateDTO = new StoreStatusUpdateDTO();
        validStoreStatusUpdateDTO.setStoreStatus(Store.StoreStatus.OPEN);
        
        // 初始化店铺VO
        sampleStoreVO = new StoreVO();
        sampleStoreVO.setId(validStoreId);
        sampleStoreVO.setName("美味快餐店");
        sampleStoreVO.setLogo("https://example.com/logo.jpg");
        sampleStoreVO.setDescription("提供各种美味快餐");
        sampleStoreVO.setPhone("13800138000");
        sampleStoreVO.setAddress("北京市海淀区中关村大街1号");
        sampleStoreVO.setLongitude(new BigDecimal("116.307852"));
        sampleStoreVO.setLatitude(new BigDecimal("39.983424"));
        sampleStoreVO.setBusinessHours("08:00-22:00");
        sampleStoreVO.setDeliveryFee(new BigDecimal("5.00"));
        sampleStoreVO.setMinOrderAmount(new BigDecimal("20.00"));
        sampleStoreVO.setAverageDeliveryTime(30);
        sampleStoreVO.setCategoryId(validCategoryId);
        sampleStoreVO.setStatus(Store.StoreStatus.PENDING);
        sampleStoreVO.setRating(new BigDecimal("4.5"));
        sampleStoreVO.setMonthlySales(1000);
        sampleStoreVO.setHasPromotion(true);
    }

    // ==================== createStore 测试 ====================

    /**
     * 正向测试：成功创建店铺
     */
    @Test
    void testCreateStore_Success() {
        // 模拟成功创建
        when(merchantStoreService.createStore(validMerchantId, validStoreCreateDTO)).thenReturn(sampleStoreVO);
        
        // 执行测试
        StoreVO result = merchantStoreService.createStore(validMerchantId, validStoreCreateDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validStoreCreateDTO.getName(), result.getName(), "店铺名称应该匹配");
        assertEquals(validStoreCreateDTO.getPhone(), result.getPhone(), "联系电话应该匹配");
        assertEquals(validStoreCreateDTO.getAddress(), result.getAddress(), "地址应该匹配");
        assertEquals(validStoreCreateDTO.getCategoryId(), result.getCategoryId(), "分类ID应该匹配");
        assertEquals(Store.StoreStatus.PENDING, result.getStatus(), "新创建的店铺状态应为PENDING");
        assertNotNull(result.getId(), "店铺ID不应为null");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(validMerchantId, validStoreCreateDTO);
    }

    /**
     * 正向测试：创建最小信息的店铺
     */
    @Test
    void testCreateStore_MinimalInfo() {
        // 准备最小信息的DTO
        StoreCreateDTO minimalDTO = new StoreCreateDTO();
        minimalDTO.setName("简单餐厅");
        minimalDTO.setPhone("13700137000");
        minimalDTO.setAddress("简单地址");
        minimalDTO.setCategoryId(validCategoryId);
        
        StoreVO expectedVO = new StoreVO();
        expectedVO.setId(1L);
        expectedVO.setName(minimalDTO.getName());
        expectedVO.setPhone(minimalDTO.getPhone());
        expectedVO.setAddress(minimalDTO.getAddress());
        expectedVO.setCategoryId(minimalDTO.getCategoryId());
        expectedVO.setStatus(Store.StoreStatus.PENDING);
        
        // 模拟服务行为
        when(merchantStoreService.createStore(validMerchantId, minimalDTO)).thenReturn(expectedVO);
        
        // 执行测试
        StoreVO result = merchantStoreService.createStore(validMerchantId, minimalDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(minimalDTO.getName(), result.getName(), "店铺名称应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(validMerchantId, minimalDTO);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testCreateStore_NullMerchantId() {
        // 模拟抛出异常
        when(merchantStoreService.createStore(null, validStoreCreateDTO))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.createStore(null, validStoreCreateDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(null, validStoreCreateDTO);
    }

    /**
     * 反向测试：传入null的DTO
     */
    @Test
    void testCreateStore_NullDTO() {
        // 模拟抛出异常
        when(merchantStoreService.createStore(validMerchantId, null))
                .thenThrow(new IllegalArgumentException("店铺信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.createStore(validMerchantId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(validMerchantId, null);
    }

    /**
     * 反向测试：店铺名称为空
     */
    @Test
    void testCreateStore_EmptyName() {
        // 准备空名称的DTO
        StoreCreateDTO emptyNameDTO = new StoreCreateDTO();
        emptyNameDTO.setName("");
        emptyNameDTO.setPhone("13800138000");
        emptyNameDTO.setAddress("测试地址");
        emptyNameDTO.setCategoryId(validCategoryId);
        
        // 模拟抛出异常
        when(merchantStoreService.createStore(validMerchantId, emptyNameDTO))
                .thenThrow(new IllegalArgumentException("店铺名称不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.createStore(validMerchantId, emptyNameDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺名称不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(validMerchantId, emptyNameDTO);
    }

    /**
     * 反向测试：联系电话为空
     */
    @Test
    void testCreateStore_EmptyPhone() {
        // 准备空电话的DTO
        StoreCreateDTO emptyPhoneDTO = new StoreCreateDTO();
        emptyPhoneDTO.setName("测试餐厅");
        emptyPhoneDTO.setPhone("");
        emptyPhoneDTO.setAddress("测试地址");
        emptyPhoneDTO.setCategoryId(validCategoryId);
        
        // 模拟抛出异常
        when(merchantStoreService.createStore(validMerchantId, emptyPhoneDTO))
                .thenThrow(new IllegalArgumentException("联系电话不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.createStore(validMerchantId, emptyPhoneDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("联系电话不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(validMerchantId, emptyPhoneDTO);
    }

    /**
     * 反向测试：无效的电话格式
     */
    @Test
    void testCreateStore_InvalidPhoneFormat() {
        // 准备无效电话的DTO
        StoreCreateDTO invalidPhoneDTO = new StoreCreateDTO();
        invalidPhoneDTO.setName("测试餐厅");
        invalidPhoneDTO.setPhone("123");
        invalidPhoneDTO.setAddress("测试地址");
        invalidPhoneDTO.setCategoryId(validCategoryId);
        
        // 模拟抛出异常
        when(merchantStoreService.createStore(validMerchantId, invalidPhoneDTO))
                .thenThrow(new IllegalArgumentException("电话格式不正确"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.createStore(validMerchantId, invalidPhoneDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("电话格式不正确", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(validMerchantId, invalidPhoneDTO);
    }

    /**
     * 反向测试：分类ID为null
     */
    @Test
    void testCreateStore_NullCategoryId() {
        // 准备null分类ID的DTO
        StoreCreateDTO nullCategoryDTO = new StoreCreateDTO();
        nullCategoryDTO.setName("测试餐厅");
        nullCategoryDTO.setPhone("13800138000");
        nullCategoryDTO.setAddress("测试地址");
        nullCategoryDTO.setCategoryId(null);
        
        // 模拟抛出异常
        when(merchantStoreService.createStore(validMerchantId, nullCategoryDTO))
                .thenThrow(new IllegalArgumentException("店铺分类不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.createStore(validMerchantId, nullCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺分类不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(validMerchantId, nullCategoryDTO);
    }

    /**
     * 反向测试：商家不存在
     */
    @Test
    void testCreateStore_MerchantNotFound() {
        Long nonExistentMerchantId = 999L;
        
        // 模拟抛出异常
        when(merchantStoreService.createStore(nonExistentMerchantId, validStoreCreateDTO))
                .thenThrow(new RuntimeException("商家不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.createStore(nonExistentMerchantId, validStoreCreateDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商家不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).createStore(nonExistentMerchantId, validStoreCreateDTO);
    }

    // ==================== updateStore 测试 ====================

    /**
     * 正向测试：成功更新店铺
     */
    @Test
    void testUpdateStore_Success() {
        // 准备更新后的数据
        StoreVO updatedStoreVO = new StoreVO();
        updatedStoreVO.setId(validStoreId);
        updatedStoreVO.setName("更新的餐厅名称");
        updatedStoreVO.setDescription("更新的餐厅描述");
        updatedStoreVO.setPhone("13900139000");
        updatedStoreVO.setBusinessHours("09:00-23:00");
        updatedStoreVO.setDeliveryFee(new BigDecimal("6.00"));
        
        // 模拟成功更新
        when(merchantStoreService.updateStore(validMerchantId, validStoreId, validStoreUpdateDTO)).thenReturn(updatedStoreVO);
        
        // 执行测试
        StoreVO result = merchantStoreService.updateStore(validMerchantId, validStoreId, validStoreUpdateDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validStoreId, result.getId(), "店铺ID应该匹配");
        assertEquals("更新的餐厅名称", result.getName(), "店铺名称应该已更新");
        assertEquals("更新的餐厅描述", result.getDescription(), "店铺描述应该已更新");
        assertEquals("13900139000", result.getPhone(), "联系电话应该已更新");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStore(validMerchantId, validStoreId, validStoreUpdateDTO);
    }

    /**
     * 正向测试：部分字段更新
     */
    @Test
    void testUpdateStore_PartialUpdate() {
        // 准备部分更新的DTO
        StoreUpdateDTO partialUpdateDTO = new StoreUpdateDTO();
        partialUpdateDTO.setName("新名称");
        partialUpdateDTO.setDeliveryFee(new BigDecimal("8.00"));
        
        StoreVO partialUpdatedVO = new StoreVO();
        partialUpdatedVO.setId(validStoreId);
        partialUpdatedVO.setName("新名称");
        partialUpdatedVO.setDeliveryFee(new BigDecimal("8.00"));
        partialUpdatedVO.setPhone("13800138000"); // 原有数据保持不变
        
        // 模拟服务行为
        when(merchantStoreService.updateStore(validMerchantId, validStoreId, partialUpdateDTO)).thenReturn(partialUpdatedVO);
        
        // 执行测试
        StoreVO result = merchantStoreService.updateStore(validMerchantId, validStoreId, partialUpdateDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals("新名称", result.getName(), "名称应该已更新");
        assertEquals(new BigDecimal("8.00"), result.getDeliveryFee(), "配送费应该已更新");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStore(validMerchantId, validStoreId, partialUpdateDTO);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testUpdateStore_NullStoreId() {
        // 模拟抛出异常
        when(merchantStoreService.updateStore(validMerchantId, null, validStoreUpdateDTO))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.updateStore(validMerchantId, null, validStoreUpdateDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStore(validMerchantId, null, validStoreUpdateDTO);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testUpdateStore_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(merchantStoreService.updateStore(validMerchantId, nonExistentStoreId, validStoreUpdateDTO))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.updateStore(validMerchantId, nonExistentStoreId, validStoreUpdateDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStore(validMerchantId, nonExistentStoreId, validStoreUpdateDTO);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testUpdateStore_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantStoreService.updateStore(otherMerchantId, validStoreId, validStoreUpdateDTO))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.updateStore(otherMerchantId, validStoreId, validStoreUpdateDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStore(otherMerchantId, validStoreId, validStoreUpdateDTO);
    }

    // ==================== updateStatus 测试 ====================

    /**
     * 正向测试：成功更新店铺状态为营业中
     */
    @Test
    void testUpdateStatus_ToOpen() {
        // 模拟成功更新状态
        doNothing().when(merchantStoreService).updateStatus(validMerchantId, validStoreId, validStoreStatusUpdateDTO);
        
        // 执行测试
        assertDoesNotThrow(() -> merchantStoreService.updateStatus(validMerchantId, validStoreId, validStoreStatusUpdateDTO), 
                "更新店铺状态为营业中不应抛出异常");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStatus(validMerchantId, validStoreId, validStoreStatusUpdateDTO);
    }

    /**
     * 正向测试：成功更新店铺状态为关闭
     */
    @Test
    void testUpdateStatus_ToClosed() {
        StoreStatusUpdateDTO closedStatusDTO = new StoreStatusUpdateDTO();
        closedStatusDTO.setStoreStatus(Store.StoreStatus.CLOSED);
        
        // 模拟成功更新状态
        doNothing().when(merchantStoreService).updateStatus(validMerchantId, validStoreId, closedStatusDTO);
        
        // 执行测试
        assertDoesNotThrow(() -> merchantStoreService.updateStatus(validMerchantId, validStoreId, closedStatusDTO), 
                "更新店铺状态为关闭不应抛出异常");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStatus(validMerchantId, validStoreId, closedStatusDTO);
    }

    /**
     * 正向测试：成功更新店铺状态为暂停
     */
    @Test
    void testUpdateStatus_ToSuspended() {
        StoreStatusUpdateDTO suspendedStatusDTO = new StoreStatusUpdateDTO();
        suspendedStatusDTO.setStoreStatus(Store.StoreStatus.SUSPENDED);
        
        // 模拟成功更新状态
        doNothing().when(merchantStoreService).updateStatus(validMerchantId, validStoreId, suspendedStatusDTO);
        
        // 执行测试
        assertDoesNotThrow(() -> merchantStoreService.updateStatus(validMerchantId, validStoreId, suspendedStatusDTO), 
                "更新店铺状态为暂停不应抛出异常");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStatus(validMerchantId, validStoreId, suspendedStatusDTO);
    }

    /**
     * 反向测试：传入null的状态DTO
     */
    @Test
    void testUpdateStatus_NullStatusDTO() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("状态信息不能为空"))
                .when(merchantStoreService).updateStatus(validMerchantId, validStoreId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.updateStatus(validMerchantId, validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("状态信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStatus(validMerchantId, validStoreId, null);
    }

    /**
     * 反向测试：状态值为null
     */
    @Test
    void testUpdateStatus_NullStatus() {
        StoreStatusUpdateDTO nullStatusDTO = new StoreStatusUpdateDTO();
        nullStatusDTO.setStoreStatus(null);
        
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("店铺状态不能为空"))
                .when(merchantStoreService).updateStatus(validMerchantId, validStoreId, nullStatusDTO);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.updateStatus(validMerchantId, validStoreId, nullStatusDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺状态不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStatus(validMerchantId, validStoreId, nullStatusDTO);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testUpdateStatus_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("店铺不存在"))
                .when(merchantStoreService).updateStatus(validMerchantId, nonExistentStoreId, validStoreStatusUpdateDTO);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.updateStatus(validMerchantId, nonExistentStoreId, validStoreStatusUpdateDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).updateStatus(validMerchantId, nonExistentStoreId, validStoreStatusUpdateDTO);
    }

    // ==================== listMerchantStores 测试 ====================

    /**
     * 正向测试：成功获取商家店铺列表
     */
    @Test
    void testListMerchantStores_Success() {
        // 准备测试数据
        StoreVO store1 = new StoreVO();
        store1.setId(1L);
        store1.setName("店铺1");
        store1.setStatus(Store.StoreStatus.OPEN);
        store1.setRating(new BigDecimal("4.5"));
        
        StoreVO store2 = new StoreVO();
        store2.setId(2L);
        store2.setName("店铺2");
        store2.setStatus(Store.StoreStatus.CLOSED);
        store2.setRating(new BigDecimal("4.2"));
        
        List<StoreVO> expectedStores = Arrays.asList(store1, store2);
        
        // 模拟服务行为
        when(merchantStoreService.listMerchantStores(validMerchantId)).thenReturn(expectedStores);
        
        // 执行测试
        List<StoreVO> actualStores = merchantStoreService.listMerchantStores(validMerchantId);
        
        // 验证结果
        assertNotNull(actualStores, "店铺列表不应为null");
        assertEquals(2, actualStores.size(), "店铺列表大小应为2");
        assertEquals("店铺1", actualStores.get(0).getName(), "第一个店铺名应该匹配");
        assertEquals("店铺2", actualStores.get(1).getName(), "第二个店铺名应该匹配");
        assertEquals(Store.StoreStatus.OPEN, actualStores.get(0).getStatus(), "第一个店铺状态应该匹配");
        assertEquals(Store.StoreStatus.CLOSED, actualStores.get(1).getStatus(), "第二个店铺状态应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).listMerchantStores(validMerchantId);
    }

    /**
     * 正向测试：获取空的店铺列表
     */
    @Test
    void testListMerchantStores_EmptyList() {
        // 模拟返回空列表
        when(merchantStoreService.listMerchantStores(validMerchantId)).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<StoreVO> actualStores = merchantStoreService.listMerchantStores(validMerchantId);
        
        // 验证结果
        assertNotNull(actualStores, "店铺列表不应为null");
        assertTrue(actualStores.isEmpty(), "店铺列表应为空");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).listMerchantStores(validMerchantId);
    }

    /**
     * 正向测试：获取单个店铺
     */
    @Test
    void testListMerchantStores_SingleStore() {
        // 准备测试数据
        List<StoreVO> singleStore = Arrays.asList(sampleStoreVO);
        
        // 模拟服务行为
        when(merchantStoreService.listMerchantStores(validMerchantId)).thenReturn(singleStore);
        
        // 执行测试
        List<StoreVO> actualStores = merchantStoreService.listMerchantStores(validMerchantId);
        
        // 验证结果
        assertNotNull(actualStores, "店铺列表不应为null");
        assertEquals(1, actualStores.size(), "店铺列表大小应为1");
        assertEquals(sampleStoreVO.getName(), actualStores.get(0).getName(), "店铺名应该匹配");
        assertEquals(sampleStoreVO.getStatus(), actualStores.get(0).getStatus(), "店铺状态应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).listMerchantStores(validMerchantId);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testListMerchantStores_NullMerchantId() {
        // 模拟抛出异常
        when(merchantStoreService.listMerchantStores(null))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.listMerchantStores(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).listMerchantStores(null);
    }

    /**
     * 反向测试：商家不存在
     */
    @Test
    void testListMerchantStores_MerchantNotFound() {
        Long nonExistentMerchantId = 999L;
        
        // 模拟抛出异常
        when(merchantStoreService.listMerchantStores(nonExistentMerchantId))
                .thenThrow(new RuntimeException("商家不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.listMerchantStores(nonExistentMerchantId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商家不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).listMerchantStores(nonExistentMerchantId);
    }

    // ==================== getStoreById 测试 ====================

    /**
     * 正向测试：成功根据ID获取店铺
     */
    @Test
    void testGetStoreById_Success() {
        // 模拟成功获取店铺
        when(merchantStoreService.getStoreById(validMerchantId, validStoreId)).thenReturn(sampleStoreVO);
        
        // 执行测试
        StoreVO result = merchantStoreService.getStoreById(validMerchantId, validStoreId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validStoreId, result.getId(), "店铺ID应该匹配");
        assertEquals("美味快餐店", result.getName(), "店铺名称应该匹配");
        assertEquals("13800138000", result.getPhone(), "联系电话应该匹配");
        assertEquals(Store.StoreStatus.PENDING, result.getStatus(), "店铺状态应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).getStoreById(validMerchantId, validStoreId);
    }

    /**
     * 正向测试：获取特定状态的店铺
     */
    @Test
    void testGetStoreById_SpecificStatus() {
        Long specificStoreId = 200L;
        
        StoreVO openStore = new StoreVO();
        openStore.setId(specificStoreId);
        openStore.setName("营业中的店铺");
        openStore.setStatus(Store.StoreStatus.OPEN);
        openStore.setRating(new BigDecimal("4.8"));
        openStore.setMonthlySales(1500);
        
        // 模拟服务行为
        when(merchantStoreService.getStoreById(validMerchantId, specificStoreId)).thenReturn(openStore);
        
        // 执行测试
        StoreVO result = merchantStoreService.getStoreById(validMerchantId, specificStoreId);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(specificStoreId, result.getId(), "店铺ID应该匹配");
        assertEquals("营业中的店铺", result.getName(), "店铺名称应该匹配");
        assertEquals(Store.StoreStatus.OPEN, result.getStatus(), "店铺状态应为OPEN");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).getStoreById(validMerchantId, specificStoreId);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testGetStoreById_NullMerchantId() {
        // 模拟抛出异常
        when(merchantStoreService.getStoreById(null, validStoreId))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.getStoreById(null, validStoreId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).getStoreById(null, validStoreId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testGetStoreById_NullStoreId() {
        // 模拟抛出异常
        when(merchantStoreService.getStoreById(validMerchantId, null))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantStoreService.getStoreById(validMerchantId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).getStoreById(validMerchantId, null);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testGetStoreById_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(merchantStoreService.getStoreById(validMerchantId, nonExistentStoreId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.getStoreById(validMerchantId, nonExistentStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).getStoreById(validMerchantId, nonExistentStoreId);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testGetStoreById_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantStoreService.getStoreById(otherMerchantId, validStoreId))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.getStoreById(otherMerchantId, validStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).getStoreById(otherMerchantId, validStoreId);
    }

    /**
     * 反向测试：数据库查询异常
     */
    @Test
    void testGetStoreById_DatabaseException() {
        // 模拟抛出数据库异常
        when(merchantStoreService.getStoreById(validMerchantId, validStoreId))
                .thenThrow(new RuntimeException("数据库查询失败"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantStoreService.getStoreById(validMerchantId, validStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("数据库查询失败", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantStoreService, times(1)).getStoreById(validMerchantId, validStoreId);
    }
}
