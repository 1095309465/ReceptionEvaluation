package com.jhzy.receptionevaluation.ui.bean.dispensingdrug;

import java.util.List;

/**
 * Created by bigyu2012 on 2017/4/27.
 */

public class DrugCode {

    /**
     * code : A00000
     * data : {"ElderId":12,"name":"于飞","age":85,"bedCode":"A001-03","tags":[{"timeid":1,"type":"早餐前","isCompleted":0},{"timeid":1,"type":"中餐前","isCompleted":0}]}
     * msg :
     */

    private String code;
    private List<DrugElders> data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DrugElders> getData() {
        return data;
    }

    public void setData(List<DrugElders> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
