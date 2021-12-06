package com.dun.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private int code;//200是正常，非200表示异常
    private String msg;
    private Object data;
    public static Result succ(Object data){
        return succ(200,"操作成功",data);
    }
    public static Result succ(int code, String mess,Object data){
        Result m = new Result();
        m.setCode(code);
        m.setData(data);
        m.setMsg(mess);
        return m;
    }
    public static Result fail(String mess){
        return fail(400,mess,null);
    }
    public static Result fail(String mess, Object data){
        return fail(400,mess,data);
    }
    public static Result fail(int code, String mess, Object data){
        Result m = new Result();
        m.setCode(code);
        m.setData(data);
        m.setMsg(mess);
        return m;
    }
}
