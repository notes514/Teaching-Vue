package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 学院表实体
 * @author dhxstart
 * @date 2021/6/11 20:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_academy")
public class Academy extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 院系编号 */
    @TableId
    private String academyId;
    /** 院系名称 */
    private String academyName;
    /** 院系院长 */
    private String academyDean;
    /** 办公室电话 */
    private String officePhone;
}
