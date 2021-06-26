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
    private String studentId;
    /** 课程编号 */
    private String courseId;
    /** 教师编号 */
    private String teacherId;
    /** 成绩（0~100） */
    private Integer score;
    /** 表示该课程在第几节 */
    private String courseSection;
    /**
     * 表示该课程在周几上课使用逗号分隔，
     * 0,1,2,3,4,5,6分别表示周日、一、二、三、四、五、六
     */
    private String courseWhichDay;
}
