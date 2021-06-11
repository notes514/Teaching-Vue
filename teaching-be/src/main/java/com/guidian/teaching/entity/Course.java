package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 课程表实体
 * @author dhxstart
 * @date 2021/6/11 20:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_course")
public class Course extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 课程编号 */
    private String courseId;
    /** 课程名称 */
    private String courseName;
    /** 课程学分 */
    private Integer courseCredit;
    /** 课程学时 */
    private String courseHours;
    /** 开课时间 */
    private LocalDateTime startTime;
}
