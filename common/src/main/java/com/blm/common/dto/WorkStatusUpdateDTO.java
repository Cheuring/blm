package com.blm.common.dto;

import com.blm.entity.Rider;
import lombok.Data;

@Data
public class WorkStatusUpdateDTO {
    private Rider.RiderStatus workStatus;
}