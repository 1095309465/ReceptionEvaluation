package com.jhzy.receptionevaluation.ui.bean.drugtime;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/4/27.
 */

public class DrugTime {

    /**
     * code : A00000
     * data : [{"TimeTypeId":1,"TimeTypeName":"餐前药","DragUsageTimes":[{"TimeId":1,"TimeTypeId":1,"TimeName":"早餐（餐前）","StartTime":"06:00:00.0000000","EndTime":"07:00:00.0000000"}]},{"TimeTypeId":2,"TimeTypeName":"餐中药","DragUsageTimes":[]},{"TimeTypeId":3,"TimeTypeName":"餐后药","DragUsageTimes":[{"TimeId":2,"TimeTypeId":3,"TimeName":"早餐（餐后）","StartTime":"07:00:00.0000000","EndTime":"08:00:00.0000000"}]},{"TimeTypeId":4,"TimeTypeName":"睡前药","DragUsageTimes":[]},{"TimeTypeId":5,"TimeTypeName":"其他药","DragUsageTimes":[]}]
     * msg : null
     */

    private String code;
    private Object msg;
    private List<DrugDataBean> data;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public Object getMsg() { return msg;}


    public void setMsg(Object msg) { this.msg = msg;}


    public List<DrugDataBean> getData() { return data;}


    public void setData(List<DrugDataBean> data) { this.data = data;}



}
