package com.guidian.teaching.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Description 
 * @author dhxstart
 * @date 2021/6/26 18:25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Elective {
    /** 课程名 */
    private String courseName;
    /** 学生姓名 */
    private String courseId;
    /** 教师编号 */
    private String teacherId;
    /** 教师姓名 */
    private String teacherName;
    /** 学生编号 */
    private String studentId;
    /** 课程周天 */
    private String courseWhichDay;
    /** 课程节数 */
    private String courseSection;
    /** 课程学时 */
    private String courseCredit;
    /** 课程学分 */
    private String courseHours;
    /** 课程类别 */
    private String courseCategory;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;
}
