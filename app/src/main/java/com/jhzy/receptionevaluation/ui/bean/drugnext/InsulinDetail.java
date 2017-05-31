package com.jhzy.receptionevaluation.ui.bean.drugnext;

/**
 * Created by nakisaRen
 * on 17/5/4.
 */

public class InsulinDetail {

    /**
     * code : A00000
     * data : {"Id":1,"InjectionTime":"2017-05-02","InjectionDosage":3,"parts":1,"DrugID":1,"DurgName":"胰岛素"}
     * msg : null
     */

    private String code;
    private DataBean data;
    private Object msg;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public DataBean getData() { return data;}


    public void setData(DataBean data) { this.data = data;}


    public Object getMsg() { return msg;}


    public void setMsg(Object msg) { this.msg = msg;}


    public static class DataBean {
        /**
         * Id : 1
         * InjectionTime : 2017-05-02
         * InjectionDosage : 3
         * parts : 1
         * DrugID : 1
         * DurgName : 胰岛素
         */

        private int Id;
        private String InjectionTime;
        private double InjectionDosage;
        private int parts;
        private int DrugID;
        private String DurgName;
        private double currentAmount;//当前库存
        private double minimum;//预警值


        public double getCurrentAmount() {
            return currentAmount;
        }


        public void setCurrentAmount(double currentAmount) {
            this.currentAmount = currentAmount;
        }


        public double getMinimum() {
            return minimum;
        }


        public void setMinimum(double minimum) {
            this.minimum = minimum;
        }


        public int getId() { return Id;}


        public void setId(int Id) { this.Id = Id;}


        public String getInjectionTime() { return InjectionTime;}


        public void setInjectionTime(String InjectionTime) { this.InjectionTime = InjectionTime;}


        public double getInjectionDosage() { return InjectionDosage;}


        public void setInjectionDosage(double InjectionDosage) {
            this.InjectionDosage = InjectionDosage;
        }


        public int getParts() { return parts;}


        public void setParts(int parts) { this.parts = parts;}


        public int getDrugID() { return DrugID;}


        public void setDrugID(int DrugID) { this.DrugID = DrugID;}


        public String getDurgName() { return DurgName;}


        public void setDurgName(String DurgName) { this.DurgName = DurgName;}
    }
}
