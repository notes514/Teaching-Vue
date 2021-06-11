package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 专业表实体
 * @author dhxstart
 * @date 2021/6/11 20:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_profession")
public class Profession extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 专业编号 */
    private String professionId;
    /** 专业名称 */
    private String professionName;
    /** 所属学院编号 */
    private String academyId;
    /** 办公室电话 */
    private String officePhone;
}
