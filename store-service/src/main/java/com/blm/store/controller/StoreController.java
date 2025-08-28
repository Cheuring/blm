package com.blm.store.controller;

import com.blm.common.entity.Store;
import com.blm.common.result.Result;
import com.blm.common.vo.StoreVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "商家管理", description = "商家相关接口")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {
    
    @Operation(summary = "根据店铺ID获取店铺信息", description = "内部服务调用接口")
    @GetMapping("/{storeId}")
    public Result<StoreVO> getStoreById(@PathVariable Long storeId) {
        // 模拟数据，实际应该从数据库查询
        StoreVO storeVO = new StoreVO();
        storeVO.setId(storeId);
        storeVO.setName("川菜小馆");
        storeVO.setDescription("正宗川菜，香辣美味");
        storeVO.setImage("/images/store.jpg");
        storeVO.setAddress("北京市朝阳区某某街道123号");
        storeVO.setPhone("010-12345678");
        storeVO.setDeliveryFee(new BigDecimal("5.00"));
        storeVO.setMinDeliveryAmount(new BigDecimal("20.00"));
        storeVO.setStatus(Store.StoreStatus.OPEN);
        
        return Result.success(storeVO);
    }
}
