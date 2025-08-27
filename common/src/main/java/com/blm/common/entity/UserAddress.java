package com.blm.common.entity;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户地址实体类
 */
@Data
public class UserAddress {
    private Long id;
    private Long userId;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public String toString() {
        return province + city + district + detailAddress;
    }
}
