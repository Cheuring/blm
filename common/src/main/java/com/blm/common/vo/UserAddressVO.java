package com.blm.common.vo;

import lombok.Data;

@Data
public class UserAddressVO {
    private Long id;
    private String receiver;
    private String phone;
    private String province;
    private String city;
    private String district;
    private String detailAddress;
    private Integer isDefault;
}