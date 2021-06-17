package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 学生选课表实体
 * @author dhxstart
 * @date 2021/6/11 20:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_student_course")
public class StudentCourse extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 学号 */
    @TableId
    private String studentId;
    /** 课程编号 */
    private String courseId;
    /** 教师编号 */
    private String teacherId;
    /** 成绩（0~100） */
    private Integer score;
}
