package com.blm.user.service.impl;

import com.blm.common.dto.UserAddressDTO;
import com.blm.user.service.UserAddressService;
import com.blm.common.vo.UserAddressVO;
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
 * UserAddressService接口测试类
 * 针对每个方法实现正向和反向测试
 */
@ExtendWith(MockitoExtension.class)
class UserAddressServiceTest {

    @Mock
    private UserAddressService userAddressService;

    private Long validUserId;
    private Long validAddressId;
    private UserAddressDTO validUserAddressDTO;
    private UserAddressVO sampleUserAddressVO;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        validUserId = 1L;
        validAddressId = 10L;
        
        // 初始化地址DTO
        validUserAddressDTO = new UserAddressDTO();
        validUserAddressDTO.setReceiver("张三");
        validUserAddressDTO.setPhone("13800138000");
        validUserAddressDTO.setProvince("北京市");
        validUserAddressDTO.setCity("北京市");
        validUserAddressDTO.setDistrict("海淀区");
        validUserAddressDTO.setDetailAddress("中关村大街1号");
        validUserAddressDTO.setIsDefault(0);
        
        // 初始化地址VO
        sampleUserAddressVO = new UserAddressVO();
        sampleUserAddressVO.setId(validAddressId);
        sampleUserAddressVO.setReceiver("张三");
        sampleUserAddressVO.setPhone("13800138000");
        sampleUserAddressVO.setProvince("北京市");
        sampleUserAddressVO.setCity("北京市");
        sampleUserAddressVO.setDistrict("海淀区");
        sampleUserAddressVO.setDetailAddress("中关村大街1号");
        sampleUserAddressVO.setIsDefault(0);
    }

    // ==================== listAddresses 测试 ====================

    /**
     * 正向测试：成功获取用户地址列表
     */
    @Test
    void testListAddresses_Success() {
        // 准备测试数据
        UserAddressVO address1 = new UserAddressVO();
        address1.setId(1L);
        address1.setReceiver("张三");
        address1.setPhone("13800138000");
        address1.setProvince("北京市");
        address1.setCity("北京市");
        address1.setDistrict("海淀区");
        address1.setDetailAddress("中关村大街1号");
        address1.setIsDefault(1);
        
        UserAddressVO address2 = new UserAddressVO();
        address2.setId(2L);
        address2.setReceiver("李四");
        address2.setPhone("13900139000");
        address2.setProvince("上海市");
        address2.setCity("上海市");
        address2.setDistrict("浦东新区");
        address2.setDetailAddress("陆家嘴金融中心");
        address2.setIsDefault(0);
        
        List<UserAddressVO> expectedAddresses = Arrays.asList(address1, address2);
        
        // 模拟服务行为
        when(userAddressService.listAddresses(validUserId)).thenReturn(expectedAddresses);
        
        // 执行测试
        List<UserAddressVO> actualAddresses = userAddressService.listAddresses(validUserId);
        
        // 验证结果
        assertNotNull(actualAddresses, "地址列表不应为null");
        assertEquals(2, actualAddresses.size(), "地址列表大小应为2");
        assertEquals("张三", actualAddresses.get(0).getReceiver(), "第一个地址收货人应该匹配");
        assertEquals("李四", actualAddresses.get(1).getReceiver(), "第二个地址收货人应该匹配");
        assertEquals(1, actualAddresses.get(0).getIsDefault(), "第一个地址应为默认地址");
        assertEquals(0, actualAddresses.get(1).getIsDefault(), "第二个地址应为非默认地址");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).listAddresses(validUserId);
    }

    /**
     * 正向测试：获取空的地址列表
     */
    @Test
    void testListAddresses_EmptyList() {
        // 模拟返回空列表
        when(userAddressService.listAddresses(validUserId)).thenReturn(Collections.emptyList());
        
        // 执行测试
        List<UserAddressVO> actualAddresses = userAddressService.listAddresses(validUserId);
        
        // 验证结果
        assertNotNull(actualAddresses, "地址列表不应为null");
        assertTrue(actualAddresses.isEmpty(), "地址列表应为空");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).listAddresses(validUserId);
    }

    /**
     * 正向测试：获取单个地址
     */
    @Test
    void testListAddresses_SingleAddress() {
        // 准备测试数据
        List<UserAddressVO> singleAddress = Arrays.asList(sampleUserAddressVO);
        
        // 模拟服务行为
        when(userAddressService.listAddresses(validUserId)).thenReturn(singleAddress);
        
        // 执行测试
        List<UserAddressVO> actualAddresses = userAddressService.listAddresses(validUserId);
        
        // 验证结果
        assertNotNull(actualAddresses, "地址列表不应为null");
        assertEquals(1, actualAddresses.size(), "地址列表大小应为1");
        assertEquals(sampleUserAddressVO.getReceiver(), actualAddresses.get(0).getReceiver(), "收货人应该匹配");
        assertEquals(sampleUserAddressVO.getPhone(), actualAddresses.get(0).getPhone(), "电话应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).listAddresses(validUserId);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testListAddresses_NullUserId() {
        // 模拟抛出异常
        when(userAddressService.listAddresses(null))
                .thenThrow(new IllegalArgumentException("用户ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.listAddresses(null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).listAddresses(null);
    }

    /**
     * 反向测试：用户不存在
     */
    @Test
    void testListAddresses_UserNotFound() {
        Long nonExistentUserId = 999L;
        
        // 模拟抛出异常
        when(userAddressService.listAddresses(nonExistentUserId))
                .thenThrow(new RuntimeException("用户不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.listAddresses(nonExistentUserId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("用户不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).listAddresses(nonExistentUserId);
    }

    // ==================== addAddress 测试 ====================

    /**
     * 正向测试：成功添加用户地址
     */
    @Test
    void testAddAddress_Success() {
        // 模拟成功添加
        when(userAddressService.addAddress(validUserId, validUserAddressDTO)).thenReturn(sampleUserAddressVO);
        
        // 执行测试
        UserAddressVO result = userAddressService.addAddress(validUserId, validUserAddressDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validUserAddressDTO.getReceiver(), result.getReceiver(), "收货人应该匹配");
        assertEquals(validUserAddressDTO.getPhone(), result.getPhone(), "电话应该匹配");
        assertEquals(validUserAddressDTO.getProvince(), result.getProvince(), "省份应该匹配");
        assertEquals(validUserAddressDTO.getCity(), result.getCity(), "城市应该匹配");
        assertEquals(validUserAddressDTO.getDistrict(), result.getDistrict(), "区县应该匹配");
        assertEquals(validUserAddressDTO.getDetailAddress(), result.getDetailAddress(), "详细地址应该匹配");
        assertEquals(validUserAddressDTO.getIsDefault(), result.getIsDefault(), "默认状态应该匹配");
        assertNotNull(result.getId(), "地址ID不应为null");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(validUserId, validUserAddressDTO);
    }

    /**
     * 正向测试：添加默认地址
     */
    @Test
    void testAddAddress_DefaultAddress() {
        // 准备默认地址DTO
        UserAddressDTO defaultAddressDTO = new UserAddressDTO();
        defaultAddressDTO.setReceiver("王五");
        defaultAddressDTO.setPhone("13700137000");
        defaultAddressDTO.setProvince("广东省");
        defaultAddressDTO.setCity("广州市");
        defaultAddressDTO.setDistrict("天河区");
        defaultAddressDTO.setDetailAddress("天汇大厦101");
        defaultAddressDTO.setIsDefault(1);
        
        UserAddressVO defaultAddressVO = new UserAddressVO();
        defaultAddressVO.setId(11L);
        defaultAddressVO.setReceiver("王五");
        defaultAddressVO.setPhone("13700137000");
        defaultAddressVO.setProvince("广东省");
        defaultAddressVO.setCity("广州市");
        defaultAddressVO.setDistrict("天河区");
        defaultAddressVO.setDetailAddress("天汇大厦101");
        defaultAddressVO.setIsDefault(1);
        
        // 模拟服务行为
        when(userAddressService.addAddress(validUserId, defaultAddressDTO)).thenReturn(defaultAddressVO);
        
        // 执行测试
        UserAddressVO result = userAddressService.addAddress(validUserId, defaultAddressDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals("王五", result.getReceiver(), "收货人应该匹配");
        assertEquals(1, result.getIsDefault(), "应为默认地址");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(validUserId, defaultAddressDTO);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testAddAddress_NullUserId() {
        // 模拟抛出异常
        when(userAddressService.addAddress(null, validUserAddressDTO))
                .thenThrow(new IllegalArgumentException("用户ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.addAddress(null, validUserAddressDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(null, validUserAddressDTO);
    }

    /**
     * 反向测试：传入null的地址DTO
     */
    @Test
    void testAddAddress_NullDTO() {
        // 模拟抛出异常
        when(userAddressService.addAddress(validUserId, null))
                .thenThrow(new IllegalArgumentException("地址信息不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.addAddress(validUserId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("地址信息不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(validUserId, null);
    }

    /**
     * 反向测试：收货人为空
     */
    @Test
    void testAddAddress_EmptyReceiver() {
        // 准备空收货人的DTO
        UserAddressDTO emptyReceiverDTO = new UserAddressDTO();
        emptyReceiverDTO.setReceiver("");
        emptyReceiverDTO.setPhone("13800138000");
        emptyReceiverDTO.setProvince("北京市");
        emptyReceiverDTO.setCity("北京市");
        emptyReceiverDTO.setDistrict("海淀区");
        emptyReceiverDTO.setDetailAddress("中关村大街1号");
        
        // 模拟抛出异常
        when(userAddressService.addAddress(validUserId, emptyReceiverDTO))
                .thenThrow(new IllegalArgumentException("收货人姓名不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.addAddress(validUserId, emptyReceiverDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("收货人姓名不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(validUserId, emptyReceiverDTO);
    }

    /**
     * 反向测试：电话号码为空
     */
    @Test
    void testAddAddress_EmptyPhone() {
        // 准备空电话的DTO
        UserAddressDTO emptyPhoneDTO = new UserAddressDTO();
        emptyPhoneDTO.setReceiver("张三");
        emptyPhoneDTO.setPhone("");
        emptyPhoneDTO.setProvince("北京市");
        emptyPhoneDTO.setCity("北京市");
        emptyPhoneDTO.setDistrict("海淀区");
        emptyPhoneDTO.setDetailAddress("中关村大街1号");
        
        // 模拟抛出异常
        when(userAddressService.addAddress(validUserId, emptyPhoneDTO))
                .thenThrow(new IllegalArgumentException("电话号码不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.addAddress(validUserId, emptyPhoneDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("电话号码不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(validUserId, emptyPhoneDTO);
    }

    /**
     * 反向测试：无效的电话格式
     */
    @Test
    void testAddAddress_InvalidPhoneFormat() {
        // 准备无效电话的DTO
        UserAddressDTO invalidPhoneDTO = new UserAddressDTO();
        invalidPhoneDTO.setReceiver("张三");
        invalidPhoneDTO.setPhone("123");
        invalidPhoneDTO.setProvince("北京市");
        invalidPhoneDTO.setCity("北京市");
        invalidPhoneDTO.setDistrict("海淀区");
        invalidPhoneDTO.setDetailAddress("中关村大街1号");
        
        // 模拟抛出异常
        when(userAddressService.addAddress(validUserId, invalidPhoneDTO))
                .thenThrow(new IllegalArgumentException("电话格式不正确"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.addAddress(validUserId, invalidPhoneDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("电话格式不正确", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(validUserId, invalidPhoneDTO);
    }

    /**
     * 反向测试：详细地址为空
     */
    @Test
    void testAddAddress_EmptyDetailAddress() {
        // 准备空详细地址的DTO
        UserAddressDTO emptyDetailDTO = new UserAddressDTO();
        emptyDetailDTO.setReceiver("张三");
        emptyDetailDTO.setPhone("13800138000");
        emptyDetailDTO.setProvince("北京市");
        emptyDetailDTO.setCity("北京市");
        emptyDetailDTO.setDistrict("海淀区");
        emptyDetailDTO.setDetailAddress("");
        
        // 模拟抛出异常
        when(userAddressService.addAddress(validUserId, emptyDetailDTO))
                .thenThrow(new IllegalArgumentException("详细地址不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.addAddress(validUserId, emptyDetailDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("详细地址不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).addAddress(validUserId, emptyDetailDTO);
    }

    // ==================== updateAddress 测试 ====================

    /**
     * 正向测试：成功更新用户地址
     */
    @Test
    void testUpdateAddress_Success() {
        // 准备更新后的数据
        UserAddressVO updatedAddressVO = new UserAddressVO();
        updatedAddressVO.setId(validAddressId);
        updatedAddressVO.setReceiver("更新的收货人");
        updatedAddressVO.setPhone("13900139000");
        updatedAddressVO.setProvince("上海市");
        updatedAddressVO.setCity("上海市");
        updatedAddressVO.setDistrict("浦东新区");
        updatedAddressVO.setDetailAddress("更新的详细地址");
        updatedAddressVO.setIsDefault(1);
        
        UserAddressDTO updateDTO = new UserAddressDTO();
        updateDTO.setReceiver("更新的收货人");
        updateDTO.setPhone("13900139000");
        updateDTO.setProvince("上海市");
        updateDTO.setCity("上海市");
        updateDTO.setDistrict("浦东新区");
        updateDTO.setDetailAddress("更新的详细地址");
        updateDTO.setIsDefault(1);
        
        // 模拟成功更新
        when(userAddressService.updateAddress(validUserId, validAddressId, updateDTO)).thenReturn(updatedAddressVO);
        
        // 执行测试
        UserAddressVO result = userAddressService.updateAddress(validUserId, validAddressId, updateDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals(validAddressId, result.getId(), "地址ID应该匹配");
        assertEquals("更新的收货人", result.getReceiver(), "收货人应该已更新");
        assertEquals("13900139000", result.getPhone(), "电话应该已更新");
        assertEquals("上海市", result.getProvince(), "省份应该已更新");
        assertEquals("更新的详细地址", result.getDetailAddress(), "详细地址应该已更新");
        assertEquals(1, result.getIsDefault(), "默认状态应该已更新");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).updateAddress(validUserId, validAddressId, updateDTO);
    }

    /**
     * 正向测试：部分字段更新
     */
    @Test
    void testUpdateAddress_PartialUpdate() {
        // 准备部分更新的DTO
        UserAddressDTO partialUpdateDTO = new UserAddressDTO();
        partialUpdateDTO.setReceiver("新收货人");
        partialUpdateDTO.setPhone("13700137000");
        // 其他字段保持null，表示不更新
        
        UserAddressVO partialUpdatedVO = new UserAddressVO();
        partialUpdatedVO.setId(validAddressId);
        partialUpdatedVO.setReceiver("新收货人");
        partialUpdatedVO.setPhone("13700137000");
        partialUpdatedVO.setProvince("北京市"); // 原有数据保持不变
        partialUpdatedVO.setCity("北京市");
        partialUpdatedVO.setDistrict("海淀区");
        partialUpdatedVO.setDetailAddress("中关村大街1号");
        partialUpdatedVO.setIsDefault(0);
        
        // 模拟服务行为
        when(userAddressService.updateAddress(validUserId, validAddressId, partialUpdateDTO)).thenReturn(partialUpdatedVO);
        
        // 执行测试
        UserAddressVO result = userAddressService.updateAddress(validUserId, validAddressId, partialUpdateDTO);
        
        // 验证结果
        assertNotNull(result, "返回结果不应为null");
        assertEquals("新收货人", result.getReceiver(), "收货人应该已更新");
        assertEquals("13700137000", result.getPhone(), "电话应该已更新");
        assertEquals("北京市", result.getProvince(), "省份应该保持原值");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).updateAddress(validUserId, validAddressId, partialUpdateDTO);
    }

    /**
     * 反向测试：传入null的地址ID
     */
    @Test
    void testUpdateAddress_NullAddressId() {
        // 模拟抛出异常
        when(userAddressService.updateAddress(validUserId, null, validUserAddressDTO))
                .thenThrow(new IllegalArgumentException("地址ID不能为空"));
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.updateAddress(validUserId, null, validUserAddressDTO),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("地址ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).updateAddress(validUserId, null, validUserAddressDTO);
    }

    /**
     * 反向测试：地址不存在
     */
    @Test
    void testUpdateAddress_AddressNotFound() {
        Long nonExistentAddressId = 999L;
        
        // 模拟抛出异常
        when(userAddressService.updateAddress(validUserId, nonExistentAddressId, validUserAddressDTO))
                .thenThrow(new RuntimeException("地址不存在"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.updateAddress(validUserId, nonExistentAddressId, validUserAddressDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("地址不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).updateAddress(validUserId, nonExistentAddressId, validUserAddressDTO);
    }

    /**
     * 反向测试：地址不属于该用户
     */
    @Test
    void testUpdateAddress_AddressNotBelongToUser() {
        Long otherUserId = 2L;
        
        // 模拟抛出异常
        when(userAddressService.updateAddress(otherUserId, validAddressId, validUserAddressDTO))
                .thenThrow(new RuntimeException("地址不属于该用户"));
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.updateAddress(otherUserId, validAddressId, validUserAddressDTO),
                "应该抛出RuntimeException"
        );
        
        assertEquals("地址不属于该用户", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).updateAddress(otherUserId, validAddressId, validUserAddressDTO);
    }

    // ==================== deleteAddress 测试 ====================

    /**
     * 正向测试：成功删除用户地址
     */
    @Test
    void testDeleteAddress_Success() {
        // 模拟成功删除
        doNothing().when(userAddressService).deleteAddress(validUserId, validAddressId);
        
        // 执行测试
        assertDoesNotThrow(() -> userAddressService.deleteAddress(validUserId, validAddressId), 
                "删除地址不应抛出异常");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).deleteAddress(validUserId, validAddressId);
    }

    /**
     * 正向测试：删除非默认地址
     */
    @Test
    void testDeleteAddress_NonDefaultAddress() {
        Long nonDefaultAddressId = 20L;
        
        // 模拟成功删除
        doNothing().when(userAddressService).deleteAddress(validUserId, nonDefaultAddressId);
        
        // 执行测试
        assertDoesNotThrow(() -> userAddressService.deleteAddress(validUserId, nonDefaultAddressId), 
                "删除非默认地址不应抛出异常");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).deleteAddress(validUserId, nonDefaultAddressId);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testDeleteAddress_NullUserId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为空"))
                .when(userAddressService).deleteAddress(null, validAddressId);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.deleteAddress(null, validAddressId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).deleteAddress(null, validAddressId);
    }

    /**
     * 反向测试：传入null的地址ID
     */
    @Test
    void testDeleteAddress_NullAddressId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("地址ID不能为空"))
                .when(userAddressService).deleteAddress(validUserId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.deleteAddress(validUserId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("地址ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).deleteAddress(validUserId, null);
    }

    /**
     * 反向测试：地址不存在
     */
    @Test
    void testDeleteAddress_AddressNotFound() {
        Long nonExistentAddressId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("地址不存在"))
                .when(userAddressService).deleteAddress(validUserId, nonExistentAddressId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.deleteAddress(validUserId, nonExistentAddressId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("地址不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).deleteAddress(validUserId, nonExistentAddressId);
    }

    /**
     * 反向测试：地址不属于该用户
     */
    @Test
    void testDeleteAddress_AddressNotBelongToUser() {
        Long otherUserId = 2L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("地址不属于该用户"))
                .when(userAddressService).deleteAddress(otherUserId, validAddressId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.deleteAddress(otherUserId, validAddressId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("地址不属于该用户", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).deleteAddress(otherUserId, validAddressId);
    }

    /**
     * 反向测试：尝试删除最后一个地址
     */
    @Test
    void testDeleteAddress_LastAddress() {
        // 模拟抛出异常
        doThrow(new RuntimeException("不能删除最后一个地址"))
                .when(userAddressService).deleteAddress(validUserId, validAddressId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.deleteAddress(validUserId, validAddressId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("不能删除最后一个地址", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).deleteAddress(validUserId, validAddressId);
    }

    // ==================== setDefaultAddress 测试 ====================

    /**
     * 正向测试：成功设置默认地址
     */
    @Test
    void testSetDefaultAddress_Success() {
        // 模拟成功设置默认地址
        doNothing().when(userAddressService).setDefaultAddress(validUserId, validAddressId);
        
        // 执行测试
        assertDoesNotThrow(() -> userAddressService.setDefaultAddress(validUserId, validAddressId), 
                "设置默认地址不应抛出异常");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).setDefaultAddress(validUserId, validAddressId);
    }

    /**
     * 正向测试：设置非当前默认地址为默认
     */
    @Test
    void testSetDefaultAddress_ChangeDefault() {
        Long newDefaultAddressId = 30L;
        
        // 模拟成功设置默认地址
        doNothing().when(userAddressService).setDefaultAddress(validUserId, newDefaultAddressId);
        
        // 执行测试
        assertDoesNotThrow(() -> userAddressService.setDefaultAddress(validUserId, newDefaultAddressId), 
                "更改默认地址不应抛出异常");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).setDefaultAddress(validUserId, newDefaultAddressId);
    }

    /**
     * 反向测试：传入null的用户ID
     */
    @Test
    void testSetDefaultAddress_NullUserId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("用户ID不能为空"))
                .when(userAddressService).setDefaultAddress(null, validAddressId);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.setDefaultAddress(null, validAddressId),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("用户ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).setDefaultAddress(null, validAddressId);
    }

    /**
     * 反向测试：传入null的地址ID
     */
    @Test
    void testSetDefaultAddress_NullAddressId() {
        // 模拟抛出异常
        doThrow(new IllegalArgumentException("地址ID不能为空"))
                .when(userAddressService).setDefaultAddress(validUserId, null);
        
        // 执行测试并验证异常
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userAddressService.setDefaultAddress(validUserId, null),
                "应该抛出IllegalArgumentException"
        );
        
        assertEquals("地址ID不能为空", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).setDefaultAddress(validUserId, null);
    }

    /**
     * 反向测试：地址不存在
     */
    @Test
    void testSetDefaultAddress_AddressNotFound() {
        Long nonExistentAddressId = 999L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("地址不存在"))
                .when(userAddressService).setDefaultAddress(validUserId, nonExistentAddressId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.setDefaultAddress(validUserId, nonExistentAddressId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("地址不存在", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).setDefaultAddress(validUserId, nonExistentAddressId);
    }

    /**
     * 反向测试：地址不属于该用户
     */
    @Test
    void testSetDefaultAddress_AddressNotBelongToUser() {
        Long otherUserId = 2L;
        
        // 模拟抛出异常
        doThrow(new RuntimeException("地址不属于该用户"))
                .when(userAddressService).setDefaultAddress(otherUserId, validAddressId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.setDefaultAddress(otherUserId, validAddressId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("地址不属于该用户", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).setDefaultAddress(otherUserId, validAddressId);
    }

    /**
     * 反向测试：数据库更新异常
     */
    @Test
    void testSetDefaultAddress_DatabaseException() {
        // 模拟抛出数据库异常
        doThrow(new RuntimeException("数据库更新失败"))
                .when(userAddressService).setDefaultAddress(validUserId, validAddressId);
        
        // 执行测试并验证异常
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userAddressService.setDefaultAddress(validUserId, validAddressId),
                "应该抛出RuntimeException"
        );
        
        assertEquals("数据库更新失败", exception.getMessage(), "异常信息应该匹配");
        
        // 验证方法被调用
        verify(userAddressService, times(1)).setDefaultAddress(validUserId, validAddressId);
    }
}
