package com.blm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class GoodsSalesDTO {
    //商品名称
    private String name;
    //销量
    private Integer number;
}
