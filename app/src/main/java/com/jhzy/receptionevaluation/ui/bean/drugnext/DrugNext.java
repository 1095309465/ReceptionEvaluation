package com.jhzy.receptionevaluation.ui.bean.drugnext;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/5/2.
 */

public class DrugNext {

    /**
     * code : A00000
     * data : [{"TimeId":1,"TimeName":"早餐前","TimeTypeId":1,"TimeTypeName":"餐前","TimeColor":"#FFFF","Drugs":[{"DrugId":1,"DrugName":"青霉素","DoseUnit":"单位","DoseId":1,"Dosage":1}]},{"TimeId":2,"TimeName":"中餐前","TimeTypeId":1,"TimeTypeName":"餐前","TimeColor":"#FFFF","Drugs":[{"DrugId":1,"DrugName":"青霉素","DoseUnit":"单位","DoseId":1,"Dosage":1}]}]
     * msg : null
     */

    private String code;
    private Object msg;
    private List<DrugNextData> data;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public Object getMsg() { return msg;}


    public void setMsg(Object msg) { this.msg = msg;}


    public List<DrugNextData> getData() { return data;}


    public void setData(List<DrugNextData> data) { this.data = data;}



}
