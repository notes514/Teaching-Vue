package com.guidian.teaching.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 此类是系统菜单实体
 * @author dhxstart
 * @date 2021/6/15 10:10
 */
@Data
public class SystemMenuDto {
    private Long menuId;
    private String name;
    private String title;
    private String icon;
    private String path;
    private String component;
    private List<SystemMenuDto> children = new ArrayList<>();

    public SystemMenuDto() {
    }
}
