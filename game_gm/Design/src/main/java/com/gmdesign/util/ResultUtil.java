package com.gmdesign.util;

/**
 *
 * http请求返回的最外层对象
 */
public class ResultUtil<T> {
    //总结:A-B-C
    //A:首先固定格式,通过Resuit对象固定这三个字段-
    // 跳转到B:GirlService类-public void getAge(Integer id) throws Exception{}下看

    //错误码
    private Integer code;
    //提示信息
    private String msg;
    //具体的内容
    private T data;


    public Integer getCode() {
        return code;
    }


    public void setCode(Integer code) {
        this.code = code;
    }


    public String getMsg() {
        return msg;
    }


    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return data;
    }


    public void setData(T data) {
        this.data = data;
    }
}