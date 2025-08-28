package com.blm.common.dto;

import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    private String username;
    private String email;
    private String avatar;
}