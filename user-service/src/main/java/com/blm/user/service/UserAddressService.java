package com.blm.user.service;

import com.blm.common.dto.UserAddressDTO;
import com.blm.common.vo.UserAddressVO;

import java.util.List;

/**
 * 用户地址服务接口
 */
public interface UserAddressService {
    List<UserAddressVO> listAddresses(Long userId);
    UserAddressVO addAddress(Long userId, UserAddressDTO dto);
    UserAddressVO updateAddress(Long userId, Long id, UserAddressDTO dto);
    void deleteAddress(Long userId, Long id);
    void setDefaultAddress(Long userId, Long id);
}
