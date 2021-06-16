package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description 用户表实体
 * @author dhxstart
 * @date 2021/6/11 20:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 用户账号 */
    @TableId
    private String userId;
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    /** 用户头像 */ 
    private String avatar;
    /** 用户权限（学生默认为0，老师是1，管理员是2） */
    private Integer authority;
}
