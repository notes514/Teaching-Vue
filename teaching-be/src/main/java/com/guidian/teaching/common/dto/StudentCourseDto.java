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
    /** 表示该课程在第几节 */
    private String courseSection;
    /**
     * 表示该课程在周几上课使用逗号分隔，
     * 1,2,3,4,5,6,7分别表示周日、一、二、三、四、五、六
     */
    private String courseWhichDay;
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
