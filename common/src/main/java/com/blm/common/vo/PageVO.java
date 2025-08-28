package com.blm.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@Schema(description = "分页查询结果视图对象")
public class PageVO<T> {

    @Schema(description = "当前页码")
    private int number; // 将page改为number以与Spring Data一致

    @Schema(description = "每页数量")
    private int size;

    @Schema(description = "总记录数")
    private long totalElements;

    @Schema(description = "总页数")
    private int totalPages;

    @Schema(description = "当前页数据列表")
    private List<T> content;
    
    // 构造函数，支持使用page参数来保持兼容性
    public PageVO(int page, int size, long totalElements, int totalPages, List<T> content) {
        this.number = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.content = content;
    }
    
    // 兼容性方法
    public int getPage() {
        return number;
    }
    
    public void setPage(int page) {
        this.number = page;
    }
}
