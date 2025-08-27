package com.blm.user.service.impl;

import com.blm.common.entity.UserAddress;
import com.blm.common.exception.BusinessException;
import com.blm.common.result.ResultCode;
import com.blm.common.vo.UserAddressVO;
import com.blm.user.dto.UserAddressDTO;
import com.blm.user.repository.UserAddressRepository;
import com.blm.user.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户地址服务实现类
 */
@Slf4j
@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Override
    public List<UserAddressVO> getUserAddresses(Long userId) {
        List<UserAddress> addresses = userAddressRepository.findByUserId(userId);
        return addresses.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public UserAddressVO getAddressById(Long addressId) {
        UserAddress address = userAddressRepository.findById(addressId);
        if (address == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "地址不存在");
        }
        return convertToVO(address);
    }

    @Override
    @Transactional
    public UserAddressVO addAddress(Long userId, UserAddressDTO dto) {
        // 如果设置为默认地址，先取消其他地址的默认状态
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            userAddressRepository.cancelDefaultByUserId(userId);
        }

        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(dto, address);
        address.setUserId(userId);
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());

        userAddressRepository.insert(address);
        
        log.info("Address added successfully for user: {}", userId);
        return convertToVO(address);
    }

    @Override
    @Transactional
    public UserAddressVO updateAddress(Long userId, Long addressId, UserAddressDTO dto) {
        UserAddress address = userAddressRepository.findById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.NOT_FOUND, "地址不存在");
        }

        // 如果设置为默认地址，先取消其他地址的默认状态
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            userAddressRepository.cancelDefaultByUserId(userId);
        }

        BeanUtils.copyProperties(dto, address);
        address.setUpdatedAt(LocalDateTime.now());

        userAddressRepository.update(address);
        
        log.info("Address updated successfully: {}", addressId);
        return convertToVO(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        UserAddress address = userAddressRepository.findById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.NOT_FOUND, "地址不存在");
        }

        userAddressRepository.deleteById(addressId);
        log.info("Address deleted successfully: {}", addressId);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long addressId) {
        UserAddress address = userAddressRepository.findById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            throw new BusinessException(ResultCode.NOT_FOUND, "地址不存在");
        }

        // 取消其他地址的默认状态
        userAddressRepository.cancelDefaultByUserId(userId);

        // 设置当前地址为默认
        userAddressRepository.setDefault(addressId);
        
        log.info("Default address set successfully: {}", addressId);
    }

    private UserAddressVO convertToVO(UserAddress address) {
        UserAddressVO vo = new UserAddressVO();
        BeanUtils.copyProperties(address, vo);
        return vo;
    }
}
