package com.jhzy.receptionevaluation.ui.bean;

/**
 * Created by nakisaRen
 * on 17/3/9.
 */

public class Code {

    /**
     * code : A00000
     * data : 修改成功
     * msg : null
     */

    private String code;
    private String data;
    private String msg;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public String getData() { return data;}


    public void setData(String data) { this.data = data;}


    public String getMsg() { return msg;}


    public void setMsg(String msg) { this.msg = msg;}

    @Override
    public String toString() {
        return "Code{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                ", msg=" + msg +
                '}';
    }
}
