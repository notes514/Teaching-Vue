package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 用户表实体
 * @author dhxstart
 * @date 2021/6/11 20:43
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用户账号 */
    private String userId;
    /** 密码 */
    private String userPassword;
    /** 用户权限（学生默认为0，老师是1，管理员是2） */
    private Integer userAuthority;
}
