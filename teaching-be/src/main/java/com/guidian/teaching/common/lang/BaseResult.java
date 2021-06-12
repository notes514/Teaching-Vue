package com.guidian.teaching.common.lang;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description 请求统一返回结果类
 * @author dhxstart
 * @date 2021/6/5 22:06
 */
@Data
public class BaseResult implements Serializable {
    /** 200表示请求成功，反之表示请求异常 */
    private Integer code;
    /** 成功或失败的提示信息 */
    private String msg;
    /** 数据集 */
    private Object data;

    /**
     * @Description 请求成功的方法（单参数）
     * @author dhxstart
     * @date 2021/6/5 22:13
     * @param data 数据
     * @return com.laodai.vueblogstudy.common.lang.BaseResult
     */
    public static BaseResult success(Object data) {
        return success(200, "操作成功！", data);
    }

    /**
     * @Description 请求成功的方法（多参数）
     * @author dhxstart
     * @date 2021/6/5 22:12
     * @param code 状态码
     * @param msg 提示信息
     * @param data 数据
     * @return com.laodai.vueblogstudy.common.lang.BaseResult
     */
    public static BaseResult success(Integer code, String msg, Object data) {
        BaseResult result = new BaseResult();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * @Description 请求失败的方法（单参数）
     * @author dhxstart
     * @date 2021/6/5 23:19
     * @param msg 提示信息
     * @return com.laodai.vueblogstudy.common.lang.BaseResult
     */
    public static BaseResult failure(String msg) {
        BaseResult result = new BaseResult();
        result.setCode(-1);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * @Description 请求失败的方法（多参数）
     * @author dhxstart
     * @date 2021/6/5 23:18
     * @param code 状态码
     * @param msg 提示信息
     * @param data 泛型结果集
     * @return com.laodai.vueblogstudy.common.lang.BaseResult
     */
    public static BaseResult failure(Integer code, String msg, Object data) {
        BaseResult result = new BaseResult();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
