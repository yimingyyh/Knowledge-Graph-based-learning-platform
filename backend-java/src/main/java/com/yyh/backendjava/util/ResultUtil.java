package com.yyh.backendjava.util;

import lombok.Data;
//自动get set方法
@Data
public class ResultUtil {
    //状态码
    private Integer code;
    //true false
    private Boolean status;
    //说明
    private String msg;
    //结果数据
    private Object data;

    public ResultUtil() {
    }

    public ResultUtil(Integer code, Boolean status, String msg, Object data) {
        this.code = code;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    //成功返回
    public static ResultUtil isSuccess(Object data){
        return new ResultUtil(20000,true,"操作成功!",data);

    }

    //成功并有mes
    public static ResultUtil isSuccess(String msg,Object data){
        return new ResultUtil(20000,true,msg,data);
    }
}
