package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 老师表实体
 * @author dhxstart
 * @date 2021/6/11 20:44
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_teacher")
public class Teacher extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 教师编号 */
    private String teacherId;
    /** 教师姓名 */
    private String teacherName;
    /** 性别 */
    private String gender;
    /** 教师职称 */
    private String teacherTitle;
    /** 所属院系编号 */
    private String academyId;
    /** 教师电话 */
    private String teacherPhone;
}
