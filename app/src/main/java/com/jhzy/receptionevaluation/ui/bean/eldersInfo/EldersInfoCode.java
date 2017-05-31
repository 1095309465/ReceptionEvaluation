package com.jhzy.receptionevaluation.ui.bean.eldersInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */

public class EldersInfoCode {

    /**
     * code : A00000
     * data : [{"ElderID":1,"ElderName":"张三哥","PhotoUrl":"https://www.baidu.com/img/bd_logo1.png","BirthDate":"1960-11-29T00:00:00","BedTitle":"004床","Address":"湖北省荆州市.","Age":0},{"ElderID":2,"ElderName":"李四","PhotoUrl":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1488450543573&di=8041b9feb7bd9634d083f51321fefe77&imgtype=0&src=http%3A%2F%2Fimg15.3lian.com%2F2015%2Ff2%2F55%2Fd%2F84.jpg","BirthDate":"1966-10-05T00:00:00","BedTitle":"01房002床","Address":"安徽六安","Age":0}]
     * msg : null
     */

    private String code;
    private Object msg;
    /**
     * ElderID : 1
     * ElderName : 张三哥
     * PhotoUrl : https://www.baidu.com/img/bd_logo1.png
     * BirthDate : 1960-11-29T00:00:00
     * BedTitle : 004床
     * Address : 湖北省荆州市.
     * Age : 0
     */

    private List<Elder> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public List<Elder> getData() {
        return data;
    }

    public void setData(List<Elder> data) {
        this.data = data;
    }


}
