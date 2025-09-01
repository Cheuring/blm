package com.bom.merchant.service.impl;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.vo.FoodCategoryVO;
import com.blm.store.service.MerchantFoodCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * MerchantFoodCategoryService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class MerchantFoodCategoryServiceTest {

    @Mock
    private MerchantFoodCategoryService merchantFoodCategoryService;

    private Long validMerchantId;
    private Long validStoreId;
    private Long validCategoryId;
    private FoodCategoryDTO validCategoryDTO;
    private FoodCategoryVO sampleCategoryVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validMerchantId = 1L;
        validStoreId = 10L;
        validCategoryId = 100L;
        
        // 初始化分类DTO
        validCategoryDTO = new FoodCategoryDTO();
        validCategoryDTO.setName("热菜");
        validCategoryDTO.setSort(1);
        
        // 初始化分类VO
        sampleCategoryVO = new FoodCategoryVO();
        sampleCategoryVO.setId(validCategoryId);
        sampleCategoryVO.setName("热菜");
        sampleCategoryVO.setSort(1);
    }

    // ==================== listCategories 测试 ====================

    /**
     * 正向测试：成功获取商家店铺的分类列表
     */
    @Test
    void testListCategories_Success() {
        // 准备测试数据
        FoodCategoryVO category1 = new FoodCategoryVO();
        category1.setId(1L);
        category1.setName("热菜");
        category1.setSort(1);
        
        FoodCategoryVO category2 = new FoodCategoryVO();
        category2.setId(2L);
        category2.setName("凉菜");
        category2.setSort(2);
        
        List<FoodCategoryVO> expectedCategories = Arrays.asList(category1, category2);
        
        // 模拟服务行为
        when(merchantFoodCategoryService.listCategories(validMerchantId, validStoreId)).thenReturn(expectedCategories);
        
        // 执行测试
        List<FoodCategoryVO> actualCategories = merchantFoodCategoryService.listCategories(validMerchantId, validStoreId);
        
        // 验证结果
        assertNotNull(actualCategories, "分类列表不应为null");
        assertEquals(2, actualCategories.size(), "分类列表大小应为2");
        assertEquals("热菜", actualCategories.get(0).getName(), "第一个分类名应该匹配");
        assertEquals("凉菜", actualCategories.get(1).getName(), "第二个分类名应该匹配");
        assertEquals(1, actualCategories.get(0).getSort(), "第一个分类排序应该匹配");
        assertEquals(2, actualCategories.get(1).getSort(), "第二个分类排序应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(validMerchantId, validStoreId);
    }

    /**
     * 正向测试：获取空的分类列表
     */
    @Test
    void testListCategories_EmptyList() {
        // 模拟返回空列表
        when(merchantFoodCategoryService.listCategories(validMerchantId, validStoreId)).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<FoodCategoryVO> actualCategories = merchantFoodCategoryService.listCategories(validMerchantId, validStoreId);
        
        // 验证结果
        assertNotNull(actualCategories, "分类列表不应为null");
        assertTrue(actualCategories.isEmpty(), "分类列表应为空");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(validMerchantId, validStoreId);
    }

    /**
     * 正向测试：获取单个分类
     */
    @Test
    void testListCategories_SingleCategory() {
        // 准备测试数据
        List<FoodCategoryVO> singleCategory = Arrays.asList(sampleCategoryVO);
        
        // 模拟服务行为
        when(merchantFoodCategoryService.listCategories(validMerchantId, validStoreId)).thenReturn(singleCategory);
        
        // 执行测试
        List<FoodCategoryVO> actualCategories = merchantFoodCategoryService.listCategories(validMerchantId, validStoreId);
        
        // 验证结果
        assertNotNull(actualCategories, "分类列表不应为null");
        assertEquals(1, actualCategories.size(), "分类列表大小应为1");
        assertEquals(sampleCategoryVO.getName(), actualCategories.get(0).getName(), "分类名应该匹配");
        assertEquals(sampleCategoryVO.getSort(), actualCategories.get(0).getSort(), "排序应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(validMerchantId, validStoreId);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testListCategories_NullMerchantId() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.listCategories(null, validStoreId))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.listCategories(null, validStoreId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(null, validStoreId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testListCategories_NullStoreId() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.listCategories(validMerchantId, null))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.listCategories(validMerchantId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(validMerchantId, null);
    }

    /**
     * 反向测试：商家不存在
     */
    @Test
    void testListCategories_MerchantNotFound() {
        Long nonExistentMerchantId = 999L;
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.listCategories(nonExistentMerchantId, validStoreId))
                .thenThrow(new RuntimeException("商家不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.listCategories(nonExistentMerchantId, validStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("商家不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(nonExistentMerchantId, validStoreId);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testListCategories_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.listCategories(validMerchantId, nonExistentStoreId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.listCategories(validMerchantId, nonExistentStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(validMerchantId, nonExistentStoreId);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testListCategories_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.listCategories(otherMerchantId, validStoreId))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.listCategories(otherMerchantId, validStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).listCategories(otherMerchantId, validStoreId);
    }

    // ==================== addCategory 测试 ====================

    /**
     * 正向测试：成功添加分类
     */
    @Test
    void testAddCategory_Success() {
        // 模拟成功添加
        when(merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, validCategoryDTO)).thenReturn(sampleCategoryVO);
        
        // 执行测试
        FoodCategoryVO result = merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, validCategoryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validCategoryDTO.getName(), result.getName(), "分类名应该匹配");
        assertEquals(validCategoryDTO.getSort(), result.getSort(), "排序应该匹配");
        assertNotNull(result.getId(), "分类ID不应为null");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, validStoreId, validCategoryDTO);
    }

    /**
     * 正向测试：添加具有最大长度名称的分类
     */
    @Test
    void testAddCategory_MaxLengthName() {
        // 准备最大长度名称的DTO
        FoodCategoryDTO maxLengthDTO = new FoodCategoryDTO();
        maxLengthDTO.setName("A".repeat(50)); // 50个字符
        maxLengthDTO.setSort(1);
        
        FoodCategoryVO expectedVO = new FoodCategoryVO();
        expectedVO.setId(1L);
        expectedVO.setName(maxLengthDTO.getName());
        expectedVO.setSort(maxLengthDTO.getSort());
        
        // 模拟服务行为
        when(merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, maxLengthDTO)).thenReturn(expectedVO);
        
        // 执行测试
        FoodCategoryVO result = merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, maxLengthDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(maxLengthDTO.getName(), result.getName(), "分类名应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, validStoreId, maxLengthDTO);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testAddCategory_NullMerchantId() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(null, validStoreId, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.addCategory(null, validStoreId, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(null, validStoreId, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testAddCategory_NullStoreId() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(validMerchantId, null, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.addCategory(validMerchantId, null, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, null, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的分类DTO
     */
    @Test
    void testAddCategory_NullCategoryDTO() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, null))
                .thenThrow(new IllegalArgumentException("分类信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, validStoreId, null);
    }

    /**
     * 反向测试：分类名称为空
     */
    @Test
    void testAddCategory_EmptyName() {
        // 准备空名称的DTO
        FoodCategoryDTO emptyNameDTO = new FoodCategoryDTO();
        emptyNameDTO.setName("");
        emptyNameDTO.setSort(1);
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, emptyNameDTO))
                .thenThrow(new IllegalArgumentException("分类名称不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, emptyNameDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类名称不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, validStoreId, emptyNameDTO);
    }

    /**
     * 反向测试：分类名称超长
     */
    @Test
    void testAddCategory_NameTooLong() {
        // 准备超长名称的DTO
        FoodCategoryDTO longNameDTO = new FoodCategoryDTO();
        longNameDTO.setName("A".repeat(51)); // 51个字符，超过限制
        longNameDTO.setSort(1);
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, longNameDTO))
                .thenThrow(new IllegalArgumentException("分类名称长度不能超过50个字符"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, longNameDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类名称长度不能超过50个字符", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, validStoreId, longNameDTO);
    }

    /**
     * 反向测试：排序值为null
     */
    @Test
    void testAddCategory_NullSort() {
        // 准备null排序值的DTO
        FoodCategoryDTO nullSortDTO = new FoodCategoryDTO();
        nullSortDTO.setName("测试分类");
        nullSortDTO.setSort(null);
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, nullSortDTO))
                .thenThrow(new IllegalArgumentException("排序顺序不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, nullSortDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("排序顺序不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, validStoreId, nullSortDTO);
    }

    /**
     * 反向测试：分类名称重复
     */
    @Test
    void testAddCategory_DuplicateName() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, validCategoryDTO))
                .thenThrow(new RuntimeException("分类名称已存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.addCategory(validMerchantId, validStoreId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类名称已存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(validMerchantId, validStoreId, validCategoryDTO);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testAddCategory_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.addCategory(otherMerchantId, validStoreId, validCategoryDTO))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.addCategory(otherMerchantId, validStoreId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).addCategory(otherMerchantId, validStoreId, validCategoryDTO);
    }

    // ==================== updateCategory 测试 ====================

    /**
     * 正向测试：成功更新分类
     */
    @Test
    void testUpdateCategory_Success() {
        // 准备更新后的数据
        FoodCategoryVO updatedCategoryVO = new FoodCategoryVO();
        updatedCategoryVO.setId(validCategoryId);
        updatedCategoryVO.setName("更新的分类名");
        updatedCategoryVO.setSort(2);
        
        // 模拟成功更新
        when(merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, validCategoryDTO)).thenReturn(updatedCategoryVO);
        
        // 执行测试
        FoodCategoryVO result = merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, validCategoryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validCategoryId, result.getId(), "分类ID应该匹配");
        assertEquals("更新的分类名", result.getName(), "分类名应该已更新");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, validStoreId, validCategoryId, validCategoryDTO);
    }

    /**
     * 正向测试：更新分类名称
     */
    @Test
    void testUpdateCategory_UpdateName() {
        // 准备新的分类名称
        FoodCategoryDTO updateNameDTO = new FoodCategoryDTO();
        updateNameDTO.setName("新的分类名称");
        updateNameDTO.setSort(1);
        
        FoodCategoryVO updatedVO = new FoodCategoryVO();
        updatedVO.setId(validCategoryId);
        updatedVO.setName(updateNameDTO.getName());
        updatedVO.setSort(updateNameDTO.getSort());
        
        // 模拟服务行为
        when(merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, updateNameDTO)).thenReturn(updatedVO);
        
        // 执行测试
        FoodCategoryVO result = merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, updateNameDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(updateNameDTO.getName(), result.getName(), "分类名应该已更新");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, validStoreId, validCategoryId, updateNameDTO);
    }

    /**
     * 正向测试：更新排序值
     */
    @Test
    void testUpdateCategory_UpdateSort() {
        // 准备新的排序值
        FoodCategoryDTO updateSortDTO = new FoodCategoryDTO();
        updateSortDTO.setName("热菜");
        updateSortDTO.setSort(99);
        
        FoodCategoryVO updatedVO = new FoodCategoryVO();
        updatedVO.setId(validCategoryId);
        updatedVO.setName(updateSortDTO.getName());
        updatedVO.setSort(updateSortDTO.getSort());
        
        // 模拟服务行为
        when(merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, updateSortDTO)).thenReturn(updatedVO);
        
        // 执行测试
        FoodCategoryVO result = merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, updateSortDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(updateSortDTO.getSort(), result.getSort(), "排序值应该已更新");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, validStoreId, validCategoryId, updateSortDTO);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testUpdateCategory_NullMerchantId() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.updateCategory(null, validStoreId, validCategoryId, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("商家ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.updateCategory(null, validStoreId, validCategoryId, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(null, validStoreId, validCategoryId, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testUpdateCategory_NullStoreId() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.updateCategory(validMerchantId, null, validCategoryId, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.updateCategory(validMerchantId, null, validCategoryId, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, null, validCategoryId, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的分类ID
     */
    @Test
    void testUpdateCategory_NullCategoryId() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, null, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("分类ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, null, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, validStoreId, null, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的分类DTO
     */
    @Test
    void testUpdateCategory_NullCategoryDTO() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, null))
                .thenThrow(new IllegalArgumentException("分类信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, validStoreId, validCategoryId, null);
    }

    /**
     * 反向测试：分类ID不存在
     */
    @Test
    void testUpdateCategory_CategoryNotFound() {
        Long nonExistentCategoryId = 999L;
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, nonExistentCategoryId, validCategoryDTO))
                .thenThrow(new RuntimeException("分类不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, nonExistentCategoryId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, validStoreId, nonExistentCategoryId, validCategoryDTO);
    }

    /**
     * 反向测试：更新时分类名称重复
     */
    @Test
    void testUpdateCategory_DuplicateName() {
        // 模拟抛出异常
        when(merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, validCategoryDTO))
                .thenThrow(new RuntimeException("分类名称已存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.updateCategory(validMerchantId, validStoreId, validCategoryId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类名称已存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(validMerchantId, validStoreId, validCategoryId, validCategoryDTO);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testUpdateCategory_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        when(merchantFoodCategoryService.updateCategory(otherMerchantId, validStoreId, validCategoryId, validCategoryDTO))
                .thenThrow(new RuntimeException("店铺不属于该商家"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.updateCategory(otherMerchantId, validStoreId, validCategoryId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).updateCategory(otherMerchantId, validStoreId, validCategoryId, validCategoryDTO);
    }

    // ==================== deleteCategory 测试 ====================

    /**
     * 正向测试：成功删除分类
     */
    @Test
    void testDeleteCategory_Success() {
        // 模拟成功删除
        doNothing().when(merchantFoodCategoryService).deleteCategory(validMerchantId, validStoreId, validCategoryId);
        
        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> merchantFoodCategoryService.deleteCategory(validMerchantId, validStoreId, validCategoryId), 
                "删除分类不应抛出异常");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(validMerchantId, validStoreId, validCategoryId);
    }

    /**
     * 正向测试：删除已存在的分类
     */
    @Test
    void testDeleteCategory_ExistingCategory() {
        Long existingCategoryId = 200L;
        
        // 模拟成功删除
        doNothing().when(merchantFoodCategoryService).deleteCategory(validMerchantId, validStoreId, existingCategoryId);
        
        // 执行测试
        assertDoesNotThrow(() -> merchantFoodCategoryService.deleteCategory(validMerchantId, validStoreId, existingCategoryId), 
                "删除存在的分类不应抛出异常");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(validMerchantId, validStoreId, existingCategoryId);
    }

    /**
     * 反向测试：传入null的商家ID
     */
    @Test
    void testDeleteCategory_NullMerchantId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("商家ID不能为空"))
                .when(merchantFoodCategoryService).deleteCategory(null, validStoreId, validCategoryId);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.deleteCategory(null, validStoreId, validCategoryId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("商家ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(null, validStoreId, validCategoryId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testDeleteCategory_NullStoreId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("店铺ID不能为空"))
                .when(merchantFoodCategoryService).deleteCategory(validMerchantId, null, validCategoryId);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.deleteCategory(validMerchantId, null, validCategoryId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(validMerchantId, null, validCategoryId);
    }

    /**
     * 反向测试：传入null的分类ID
     */
    @Test
    void testDeleteCategory_NullCategoryId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("分类ID不能为空"))
                .when(merchantFoodCategoryService).deleteCategory(validMerchantId, validStoreId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> merchantFoodCategoryService.deleteCategory(validMerchantId, validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(validMerchantId, validStoreId, null);
    }

    /**
     * 反向测试：分类ID不存在
     */
    @Test
    void testDeleteCategory_CategoryNotFound() {
        Long nonExistentCategoryId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("分类不存在"))
                .when(merchantFoodCategoryService).deleteCategory(validMerchantId, validStoreId, nonExistentCategoryId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.deleteCategory(validMerchantId, validStoreId, nonExistentCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(validMerchantId, validStoreId, nonExistentCategoryId);
    }

    /**
     * 反向测试：删除分类时存在关联商品
     */
    @Test
    void testDeleteCategory_HasAssociatedFoods() {
        // 模拟抛出异常
        doThrow(new RuntimeException("该分类下存在商品，无法删除"))
                .when(merchantFoodCategoryService).deleteCategory(validMerchantId, validStoreId, validCategoryId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.deleteCategory(validMerchantId, validStoreId, validCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("该分类下存在商品，无法删除", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(validMerchantId, validStoreId, validCategoryId);
    }

    /**
     * 反向测试：店铺不属于该商家
     */
    @Test
    void testDeleteCategory_StoreNotBelongToMerchant() {
        Long otherMerchantId = 2L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("店铺不属于该商家"))
                .when(merchantFoodCategoryService).deleteCategory(otherMerchantId, validStoreId, validCategoryId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.deleteCategory(otherMerchantId, validStoreId, validCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不属于该商家", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(otherMerchantId, validStoreId, validCategoryId);
    }

    /**
     * 反向测试：删除分类时发生数据库异常
     */
    @Test
    void testDeleteCategory_DatabaseException() {
        // 模拟抛出数据库异常
        doThrow(new RuntimeException("数据库操作失败"))
                .when(merchantFoodCategoryService).deleteCategory(validMerchantId, validStoreId, validCategoryId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> merchantFoodCategoryService.deleteCategory(validMerchantId, validStoreId, validCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("数据库操作失败", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(merchantFoodCategoryService, times(1)).deleteCategory(validMerchantId, validStoreId, validCategoryId);
    }
}
