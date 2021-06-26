package com.guidian.teaching.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * @Description 学生选课Dto
 * @author dhxstart
 * @date 2021/6/26 18:21
 */
public class ElectiveDto {
    /** 课程名 */
    private String courseName;
    /** 学生姓名 */
    private String courseId;
    /** 教师姓名 */
    private String teacherName;
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
