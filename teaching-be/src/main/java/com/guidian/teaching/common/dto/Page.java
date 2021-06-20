package com.guidian.teaching.common.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description 自定义分页处理类
 * @author dhxstart
 * @date 2021/6/18 10:32
 */
@Data
public class Page<T> {
    private List<T> records;
    private Integer total;
    private Integer size;
    private Integer current;

    public Page(List<T> records, Integer total, Integer size, Integer current) {
        this.records = records;
        this.total = total;
        this.size = size;
        this.current = current;
    }
}
