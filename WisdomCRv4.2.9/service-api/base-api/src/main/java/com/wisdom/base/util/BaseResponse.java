package com.wisdom.base.util;

/**
 * @author WisdomCR
 * @since 2022-05-10
 */
public class BaseResponse {
    private int code = 0;
    private String msg = "success";
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
