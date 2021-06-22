package com.guidian.teaching.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description 此类是学生课程成绩DTO
 * @author dhxstart
 * @date 2021/6/18 8:58
 */
@Data
public class StudentCourseDto {
    /** 学号 */
    private String studentId;
    /** 学生姓名 */
    private String studentName;
    /** 性别 */
    private String gender;
    /** 班级 */
    private String clbumId;
    /** 课程编号 */
    private String courseId;
    /** 课程名称 */
    private String courseName;
    /** 课程类别（0表示必修，1表示选修） */
    private Integer courseCategory;
    /** 教师编号 */
    private String teacherId;
    /** 教师姓名 */
    private String teacherName;
    /** 成绩（0~100） */
    private Integer score;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}
