package com.jhzy.receptionevaluation.ui.bean;

/**
 * Created by Administrator on 2017/2/28.
 */

@Deprecated
public class History {

    private String date;
    private String content;

    public History(String date, String content) {
        this.date = date;
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
