package com.blm.store.controller;

import com.blm.common.entity.Food;
import com.blm.common.result.Result;
import com.blm.common.vo.FoodVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "商品管理", description = "商品相关接口")
@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class FoodController {
    
    @Operation(summary = "根据商品ID获取商品信息", description = "内部服务调用接口")
    @GetMapping("/foods/{foodId}")
    public Result<FoodVO> getFoodById(@PathVariable Long foodId) {
        // 模拟数据，实际应该从数据库查询
        FoodVO foodVO = new FoodVO();
        foodVO.setId(foodId);
        foodVO.setStoreId(1L);
        foodVO.setCategoryId(1L);
        
        // 根据foodId返回不同的商品信息
        switch (foodId.intValue()) {
            case 1:
                foodVO.setName("宫保鸡丁");
                foodVO.setPrice(new BigDecimal("28.00"));
                break;
            case 2:
                foodVO.setName("麻婆豆腐");
                foodVO.setPrice(new BigDecimal("12.00"));
                break;
            case 3:
                foodVO.setName("红烧肉");
                foodVO.setPrice(new BigDecimal("32.00"));
                break;
            case 4:
                foodVO.setName("青椒土豆丝");
                foodVO.setPrice(new BigDecimal("13.00"));
                break;
            default:
                foodVO.setName("特色菜品");
                foodVO.setPrice(new BigDecimal("25.00"));
        }
        
        foodVO.setDescription("美味佳肴，值得品尝");
        foodVO.setImage("/images/food" + foodId + ".jpg");
        foodVO.setStock(100);
        foodVO.setStatus(Food.FoodStatus.ON_SHELF);
        
        return Result.success(foodVO);
    }
}
