package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 班级表实体
 * @author dhxstart
 * @date 2021/6/11 20:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_clbum")
public class Clbum extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 班级编号 */
    private String clbumId;
    /** 班级名称 */
    private String clbumName;
    /** 所属专业编号 */
    private String professionId;
    /** 班级辅导员 */
    private String clbumCounselor;
    /** 办公室电话 */
    private String officePhone;
}
