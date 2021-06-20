package com.guidian.teaching.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @author dhxstart
 * @date 2021/6/19 16:13
 */
@Data
public class CourseDto {
    private String section;
    private List<List<String>> course = new ArrayList<>();

    public CourseDto() {
    }

    public CourseDto(String section, List<List<String>> course) {
        this.section = section;
        this.course = course;
    }
}
