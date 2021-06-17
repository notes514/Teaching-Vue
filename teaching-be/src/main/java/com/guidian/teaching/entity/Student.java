package com.guidian.teaching.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guidian.teaching.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 学生表实体
 * @author dhxstart
 * @date 2021/6/11 20:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("tb_student")
public class Student extends BaseEntity {
    private static final long serialVersionUID = 1L;
    /** 学号 */
    @TableId
    private String studentId;
    /** 学生姓名 */
    private String studentName;
    /** 性别 */
    private String gender;
    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime birthday;
    /** 民族 */
    private String nationality;
    /** 班级编号 */
    private String clbumId;
    /** 学生电话 */
    private String studentPhone;
    /** 学生住址 */
    private String studentAddress;
}
