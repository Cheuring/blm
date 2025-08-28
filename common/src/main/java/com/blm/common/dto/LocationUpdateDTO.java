package com.blm.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationUpdateDTO {
    private BigDecimal latitude;
    private BigDecimal longitude;
}