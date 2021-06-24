package com.guidian.teaching.common.lang;

/**
 * @Description 此类用于定义相关常量
 * @author dhxstart
 * @date 2021/6/12 16:05
 */
public class Const {
    /** 学生权限 */
    public static final int STUDENT_AUTHORITY = 0;
    /** 教师权限 */
    public static final Integer TEACHER_AUTHORITY = 1;
    /** 管理员权限 */
    public static final Integer ADMINISTRATOR_AUTHORITY = 2;
    /** 验证码的key */
    public static final String CAPTCHA_KEY = "captcha";
    /** 登录url */
    public static final String LOGIN_URL = "/login";
    /** POST请求方式 */
    public static final String METHOD_POST = "POST";
    /** 插入操作 */
    public static final String SAVE = "save";
    /** 更新操作 */
    public static final String UPDATE = "update";
    /** 默认密码 */
    public static final String DEFAULT_PASSWORD = "123456";
    /** 用户默认url地址 */
    public static final String AVATAR_URL = "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg";
    /** 课程节数集合 */
    public static final String[] COURSE_SECTION = {"1-2节", "3-4节", "5-6节", "7-8节", "9-10节"};
}
