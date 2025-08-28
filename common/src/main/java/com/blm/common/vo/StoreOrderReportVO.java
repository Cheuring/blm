package com.blm.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderReportVO {
    //日期，以逗号分隔，例如：2022-10-01,2022-10-02,2022-10-03
    private String dateList;
    //订单总量，以逗号分隔，例如：200,210,220
    private String totalOrderList;
    //新增订单，以逗号分隔，例如：20,21,10
    private String newOrderList;
}
