package com.blm.common.dto;

import com.blm.common.entity.Rider;
import lombok.Data;

@Data
public class WorkStatusUpdateDTO {
    private Rider.RiderStatus workStatus;
}