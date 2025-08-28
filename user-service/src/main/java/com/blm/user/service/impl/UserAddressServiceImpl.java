package com.blm.user.service.impl;

import com.blm.common.dto.UserAddressDTO;
import com.blm.common.entity.UserAddress;
import com.blm.common.exception.CommonException;
import com.blm.common.result.ExceptionConstant;
import com.blm.common.vo.UserAddressVO;
import com.blm.user.repository.UserAddressRepository;
import com.blm.user.service.UserAddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressServiceImpl implements UserAddressService {

    @Autowired
    private UserAddressRepository addressRepository;

    @Override
    public List<UserAddressVO> listAddresses(Long userId) {
        return addressRepository.findAllByUserId(userId).stream().map(addr -> {
            UserAddressVO vo = new UserAddressVO();
            BeanUtils.copyProperties(addr, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserAddressVO addAddress(Long userId, UserAddressDTO dto) {
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            addressRepository.resetDefault(userId);
        }
        UserAddress addr = new UserAddress();
        BeanUtils.copyProperties(dto, addr);
        addr.setUserId(userId);
        addr.setCreatedAt(LocalDateTime.now());
        addr.setUpdatedAt(LocalDateTime.now());
        addressRepository.insert(addr);
        UserAddress saved = addressRepository.findByIdAndUserId(addr.getId(), userId).orElseThrow();
        UserAddressVO vo = new UserAddressVO();
        BeanUtils.copyProperties(saved, vo);
        return vo;
    }

    @Override
    @Transactional
    public UserAddressVO updateAddress(Long userId, Long id, UserAddressDTO dto) {
        UserAddress existing = addressRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new CommonException(ExceptionConstant.USER_NOT_FOUND));
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            addressRepository.resetDefault(userId);
            existing.setIsDefault(1);
        } else {
            existing.setIsDefault(dto.getIsDefault());
        }
        BeanUtils.copyProperties(dto, existing, "id", "userId", "createdAt");
        existing.setUpdatedAt(LocalDateTime.now());
        addressRepository.update(existing);
        UserAddress updated = addressRepository.findByIdAndUserId(id, userId).orElseThrow();
        UserAddressVO vo = new UserAddressVO();
        BeanUtils.copyProperties(updated, vo);
        return vo;
    }

    @Override
    public void deleteAddress(Long userId, Long id) {
        addressRepository.delete(id, userId);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long userId, Long id) {
        addressRepository.resetDefault(userId);
        addressRepository.setDefault(id, userId);
    }
}