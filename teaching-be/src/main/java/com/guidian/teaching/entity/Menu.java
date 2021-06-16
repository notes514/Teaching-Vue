package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 菜单实体
 * @author dhxstart
 * @date 2021/6/16 15:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_menu")
public class Menu extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 菜单编号 */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Long menuId;
    /** 父菜单ID，一级菜单为0 */
    @NotNull(message = "上级菜单不能为空")
    private Long parentMenuId;
    /** 菜单标识名 */
    private String menuName;
    /** 菜单标题 */
    @NotNull(message = "菜单标题不能为空")
    private String menuTitle;
    /** 菜单URL */
    private String menuPath;
    /** 授权(多个用逗号分隔，如：user:list,user:create) */
    private String perms;
    /** 页面跳转组件 */
    private String component;
    /** 类型 0：目录  1：菜单  2：按钮 */
    private Integer type;
    /** 菜单图标 */
    private String icon;
    /** 排序 */
    private Integer orderNum;
    /** 状态 */
    private Integer state;
    /** 用户权限（学生默认为0，老师是1，管理员是2） */
    private Integer authority;
    /** 子菜单数据 */
    @TableField(exist = false)
    private List<Menu> children = new ArrayList<>();
}
