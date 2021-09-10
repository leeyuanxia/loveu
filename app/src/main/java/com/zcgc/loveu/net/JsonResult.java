package com.zcgc.loveu.net;

import java.util.List;

public class JsonResult<T> {
    private int code;
    private String msg;
    private List<T> list;
    private T data;
    private long lastUptime;
    public static final int OK=200;
    public static final int NO_NEW=0;
    public static final int FAIL=500;
    public static JsonResult OK_RESULT=new JsonResult(JsonResult.OK);
    public static JsonResult FAIL_RESULT=new JsonResult(JsonResult.FAIL);

    public long getLastUptime() {
        return lastUptime;
    }

    public void setLastUptime(long lastUptime) {
        this.lastUptime = lastUptime;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public JsonResult(int code) {
        this.code = code;
    }

    public JsonResult(int code, List<T> list) {
        this.code = code;
        this.list = list;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public JsonResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JsonResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public JsonResult() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public JsonResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
