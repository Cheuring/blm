package com.bom.merchant.service.impl;

import com.blm.common.dto.FoodCategoryDTO;
import com.blm.common.vo.FoodCategoryVO;
import com.blm.store.service.FoodCategoryService;
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
 * FoodCategoryService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class FoodCategoryServiceTest {

    @Mock
    private FoodCategoryService foodCategoryService;

    private FoodCategoryDTO validCategoryDTO;
    private FoodCategoryVO sampleCategoryVO;
    private Long validStoreId;
    private Long validCategoryId;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validStoreId = 1L;
        validCategoryId = 1L;
        
        validCategoryDTO = new FoodCategoryDTO();
        validCategoryDTO.setName("热菜");
        validCategoryDTO.setSort(1);
        
        sampleCategoryVO = new FoodCategoryVO();
        sampleCategoryVO.setId(1L);
        sampleCategoryVO.setName("热菜");
        sampleCategoryVO.setSort(1);
    }

    // ==================== getCategoriesByStoreId 测试 ====================

    /**
     * 正向测试：成功获取商品分类列表
     */
    @Test
    void testGetCategoriesByStoreId_Success() {
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
        when(foodCategoryService.getCategoriesByStoreId(validStoreId)).thenReturn(expectedCategories);
        
        // 执行测试
        List<FoodCategoryVO> actualCategories = foodCategoryService.getCategoriesByStoreId(validStoreId);
        
        // 验证结果
        assertNotNull(actualCategories, "分类列表不应为null");
        assertEquals(2, actualCategories.size(), "分类列表大小应为2");
        assertEquals("热菜", actualCategories.get(0).getName(), "第一个分类名应该匹配");
        assertEquals("凉菜", actualCategories.get(1).getName(), "第二个分类名应该匹配");
        assertEquals(1, actualCategories.get(0).getSort(), "第一个分类排序应该匹配");
        assertEquals(2, actualCategories.get(1).getSort(), "第二个分类排序应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).getCategoriesByStoreId(validStoreId);
    }

    /**
     * 正向测试：获取空的商品分类列表
     */
    @Test
    void testGetCategoriesByStoreId_EmptyList() {
        // 模拟返回空列表
        when(foodCategoryService.getCategoriesByStoreId(validStoreId)).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<FoodCategoryVO> actualCategories = foodCategoryService.getCategoriesByStoreId(validStoreId);
        
        // 验证结果
        assertNotNull(actualCategories, "分类列表不应为null");
        assertTrue(actualCategories.isEmpty(), "分类列表应为空");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).getCategoriesByStoreId(validStoreId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testGetCategoriesByStoreId_NullStoreId() {
        // 模拟抛出异常
        when(foodCategoryService.getCategoriesByStoreId(null))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.getCategoriesByStoreId(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).getCategoriesByStoreId(null);
    }

    /**
     * 反向测试：传入无效的店铺ID（负数）
     */
    @Test
    void testGetCategoriesByStoreId_InvalidStoreId() {
        Long invalidStoreId = -1L;
        
        // 模拟抛出异常
        when(foodCategoryService.getCategoriesByStoreId(invalidStoreId))
                .thenThrow(new IllegalArgumentException("店铺ID必须为正数"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.getCategoriesByStoreId(invalidStoreId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID必须为正数", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).getCategoriesByStoreId(invalidStoreId);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testGetCategoriesByStoreId_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        when(foodCategoryService.getCategoriesByStoreId(nonExistentStoreId))
                .thenThrow(new RuntimeException("店铺不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> foodCategoryService.getCategoriesByStoreId(nonExistentStoreId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).getCategoriesByStoreId(nonExistentStoreId);
    }

    // ==================== addCategory 测试 ====================

    /**
     * 正向测试：成功添加商品分类
     */
    @Test
    void testAddCategory_Success() {
        // 模拟成功添加
        when(foodCategoryService.addCategory(validStoreId, validCategoryDTO)).thenReturn(sampleCategoryVO);
        
        // 执行测试
        FoodCategoryVO result = foodCategoryService.addCategory(validStoreId, validCategoryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validCategoryDTO.getName(), result.getName(), "分类名应该匹配");
        assertEquals(validCategoryDTO.getSort(), result.getSort(), "排序应该匹配");
        assertNotNull(result.getId(), "分类ID不应为null");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).addCategory(validStoreId, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testAddCategory_NullStoreId() {
        // 模拟抛出异常
        when(foodCategoryService.addCategory(null, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.addCategory(null, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).addCategory(null, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的分类DTO
     */
    @Test
    void testAddCategory_NullCategoryDTO() {
        // 模拟抛出异常
        when(foodCategoryService.addCategory(validStoreId, null))
                .thenThrow(new IllegalArgumentException("分类信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.addCategory(validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).addCategory(validStoreId, null);
    }

    /**
     * 反向测试：分类名称重复
     */
    @Test
    void testAddCategory_DuplicateName() {
        // 模拟抛出异常
        when(foodCategoryService.addCategory(validStoreId, validCategoryDTO))
                .thenThrow(new RuntimeException("分类名称已存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> foodCategoryService.addCategory(validStoreId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类名称已存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).addCategory(validStoreId, validCategoryDTO);
    }

    // ==================== updateCategory 测试 ====================

    /**
     * 正向测试：成功更新商品分类
     */
    @Test
    void testUpdateCategory_Success() {
        // 准备更新后的数据
        FoodCategoryVO updatedCategoryVO = new FoodCategoryVO();
        updatedCategoryVO.setId(validCategoryId);
        updatedCategoryVO.setName("更新的分类名");
        updatedCategoryVO.setSort(2);
        
        // 模拟成功更新
        when(foodCategoryService.updateCategory(validStoreId, validCategoryId, validCategoryDTO)).thenReturn(updatedCategoryVO);
        
        // 执行测试
        FoodCategoryVO result = foodCategoryService.updateCategory(validStoreId, validCategoryId, validCategoryDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validCategoryId, result.getId(), "分类ID应该匹配");
        assertEquals("更新的分类名", result.getName(), "分类名应该已更新");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).updateCategory(validStoreId, validCategoryId, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testUpdateCategory_NullStoreId() {
        // 模拟抛出异常
        when(foodCategoryService.updateCategory(null, validCategoryId, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("店铺ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.updateCategory(null, validCategoryId, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).updateCategory(null, validCategoryId, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的分类ID
     */
    @Test
    void testUpdateCategory_NullCategoryId() {
        // 模拟抛出异常
        when(foodCategoryService.updateCategory(validStoreId, null, validCategoryDTO))
                .thenThrow(new IllegalArgumentException("分类ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.updateCategory(validStoreId, null, validCategoryDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).updateCategory(validStoreId, null, validCategoryDTO);
    }

    /**
     * 反向测试：传入null的分类DTO
     */
    @Test
    void testUpdateCategory_NullCategoryDTO() {
        // 模拟抛出异常
        when(foodCategoryService.updateCategory(validStoreId, validCategoryId, null))
                .thenThrow(new IllegalArgumentException("分类信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.updateCategory(validStoreId, validCategoryId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).updateCategory(validStoreId, validCategoryId, null);
    }

    /**
     * 反向测试：分类ID不存在
     */
    @Test
    void testUpdateCategory_CategoryNotFound() {
        Long nonExistentCategoryId = 999L;
        
        // 模拟抛出异常
        when(foodCategoryService.updateCategory(validStoreId, nonExistentCategoryId, validCategoryDTO))
                .thenThrow(new RuntimeException("分类不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> foodCategoryService.updateCategory(validStoreId, nonExistentCategoryId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).updateCategory(validStoreId, nonExistentCategoryId, validCategoryDTO);
    }

    /**
     * 反向测试：更新时分类名称重复
     */
    @Test
    void testUpdateCategory_DuplicateName() {
        // 模拟抛出异常
        when(foodCategoryService.updateCategory(validStoreId, validCategoryId, validCategoryDTO))
                .thenThrow(new RuntimeException("分类名称已存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> foodCategoryService.updateCategory(validStoreId, validCategoryId, validCategoryDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类名称已存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).updateCategory(validStoreId, validCategoryId, validCategoryDTO);
    }

    // ==================== deleteCategory 测试 ====================

    /**
     * 正向测试：成功删除商品分类
     */
    @Test
    void testDeleteCategory_Success() {
        // 模拟成功删除
        doNothing().when(foodCategoryService).deleteCategory(validStoreId, validCategoryId);
        
        // 执行测试（应该不抛出异常）
        assertDoesNotThrow(() -> foodCategoryService.deleteCategory(validStoreId, validCategoryId), 
                "删除分类不应抛出异常");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).deleteCategory(validStoreId, validCategoryId);
    }

    /**
     * 反向测试：传入null的店铺ID
     */
    @Test
    void testDeleteCategory_NullStoreId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("店铺ID不能为空"))
                .when(foodCategoryService).deleteCategory(null, validCategoryId);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.deleteCategory(null, validCategoryId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("店铺ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).deleteCategory(null, validCategoryId);
    }

    /**
     * 反向测试：传入null的分类ID
     */
    @Test
    void testDeleteCategory_NullCategoryId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("分类ID不能为空"))
                .when(foodCategoryService).deleteCategory(validStoreId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> foodCategoryService.deleteCategory(validStoreId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("分类ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).deleteCategory(validStoreId, null);
    }

    /**
     * 反向测试：分类ID不存在
     */
    @Test
    void testDeleteCategory_CategoryNotFound() {
        Long nonExistentCategoryId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("分类不存在"))
                .when(foodCategoryService).deleteCategory(validStoreId, nonExistentCategoryId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> foodCategoryService.deleteCategory(validStoreId, nonExistentCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("分类不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).deleteCategory(validStoreId, nonExistentCategoryId);
    }

    /**
     * 反向测试：删除分类时存在关联商品
     */
    @Test
    void testDeleteCategory_HasAssociatedFoods() {
        // 模拟抛出异常
        doThrow(new RuntimeException("该分类下存在商品，无法删除"))
                .when(foodCategoryService).deleteCategory(validStoreId, validCategoryId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> foodCategoryService.deleteCategory(validStoreId, validCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("该分类下存在商品，无法删除", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).deleteCategory(validStoreId, validCategoryId);
    }

    /**
     * 反向测试：店铺不存在
     */
    @Test
    void testDeleteCategory_StoreNotFound() {
        Long nonExistentStoreId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("店铺不存在"))
                .when(foodCategoryService).deleteCategory(nonExistentStoreId, validCategoryId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> foodCategoryService.deleteCategory(nonExistentStoreId, validCategoryId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("店铺不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(foodCategoryService, times(1)).deleteCategory(nonExistentStoreId, validCategoryId);
    }
}
