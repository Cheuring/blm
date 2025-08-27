package com.blm.user.service;

import com.blm.common.vo.UserAddressVO;
import com.blm.user.dto.UserAddressDTO;

import java.util.List;

/**
 * 用户地址服务接口
 */
public interface UserAddressService {

    /**
     * 获取用户地址列表
     */
    List<UserAddressVO> getUserAddresses(Long userId);

    /**
     * 根据地址ID获取地址信息
     */
    UserAddressVO getAddressById(Long addressId);

    /**
     * 添加地址
     */
    UserAddressVO addAddress(Long userId, UserAddressDTO dto);

    /**
     * 更新地址
     */
    UserAddressVO updateAddress(Long userId, Long addressId, UserAddressDTO dto);

    /**
     * 删除地址
     */
    void deleteAddress(Long userId, Long addressId);

    /**
     * 设置默认地址
     */
    void setDefaultAddress(Long userId, Long addressId);
}
