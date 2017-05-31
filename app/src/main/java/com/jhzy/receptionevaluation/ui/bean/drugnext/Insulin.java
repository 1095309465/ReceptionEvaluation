package com.jhzy.receptionevaluation.ui.bean.drugnext;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nakisaRen
 * on 17/5/4.
 */

public class Insulin implements Serializable{

    /**
     * code : A00000
     * data : [{"timeId":1,"timeName":"早餐前","dosage":1,"DoseUnit":"片","Stock":1,"isCompleted":0}]
     * msg : null
     */

    private String code;
    private Object msg;
    private List<DataBean> data;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public Object getMsg() { return msg;}


    public void setMsg(Object msg) { this.msg = msg;}


    public List<DataBean> getData() { return data;}


    public void setData(List<DataBean> data) { this.data = data;}


    public static class DataBean implements Serializable{
        /**
         * timeId : 1
         * timeName : 早餐前
         * dosage : 1
         * DoseUnit : 片
         * Stock : 1
         * isCompleted : 0
         */

        private int timeId;
        private String timeName;
        private double dosage;
        private String DoseUnit;
        private double Stock;
        private int isCompleted;
        private String InjectionTime;
        private int DrugId;
        private int RecordId;
        private double InventoryAlarm;

        public void setDosage(double dosage) {
            this.dosage = dosage;
        }

        public double getInventoryAlarm() {
            return InventoryAlarm;
        }

        public void setInventoryAlarm(double inventoryAlarm) {
            InventoryAlarm = inventoryAlarm;
        }

        public int getTimeId() { return timeId;}


        public void setTimeId(int timeId) { this.timeId = timeId;}


        public String getTimeName() { return timeName;}


        public void setTimeName(String timeName) { this.timeName = timeName;}


        public Double getDosage() { return dosage;}


        public void setDosage(Double dosage) { this.dosage = dosage;}


        public String getDoseUnit() { return DoseUnit;}


        public void setDoseUnit(String DoseUnit) { this.DoseUnit = DoseUnit;}


        public double getStock() { return Stock;}


        public void setStock(double Stock) { this.Stock = Stock;}


        public int getIsCompleted() { return isCompleted;}


        public void setIsCompleted(int isCompleted) { this.isCompleted = isCompleted;}


        public String getInjectionTime() {
            return InjectionTime;
        }


        public void setInjectionTime(String injectionTime) {
            InjectionTime = injectionTime;
        }


        public int getDrugId() {
            return DrugId;
        }


        public void setDrugId(int drugId) {
            DrugId = drugId;
        }


        public int getRecordId() {
            return RecordId;
        }


        public void setRecordId(int recordId) {
            RecordId = recordId;
        }
    }
}
