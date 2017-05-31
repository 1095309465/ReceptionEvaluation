package com.jhzy.receptionevaluation.ui.bean.assess;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/3/9.
 */

public class Zhuyi {

    /**
     * code : A00000
     * data : [{"ELderID":null,"ELderName":"张三","Gender":"男","BirthDate":"1950-10-10T00:00:00","Ages":null,"FoodHabit":null,"Attention":"注意事项.--","CareNote":"护理注意.--","MedicineNote":"用药注意.--","SicknessNote":"非常好"}]
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


    public static class DataBean {
        /**
         * ELderID : null
         * ELderName : 张三
         * Gender : 男
         * BirthDate : 1950-10-10T00:00:00
         * Ages : null
         * FoodHabit : null
         * Attention : 注意事项.--
         * CareNote : 护理注意.--
         * MedicineNote : 用药注意.--
         * SicknessNote : 非常好
         */

        private Object ELderID;
        private String ELderName;
        private String Gender;
        private String BirthDate;
        private Object Ages;
        private String FoodHabit;
        private String Attention;
        private String CareNote;
        private String MedicineNote;
        private String SicknessNote;


        public Object getELderID() { return ELderID;}


        public void setELderID(Object ELderID) { this.ELderID = ELderID;}


        public String getELderName() { return ELderName;}


        public void setELderName(String ELderName) { this.ELderName = ELderName;}


        public String getGender() { return Gender;}


        public void setGender(String Gender) { this.Gender = Gender;}


        public String getBirthDate() { return BirthDate;}


        public void setBirthDate(String BirthDate) { this.BirthDate = BirthDate;}


        public Object getAges() { return Ages;}


        public void setAges(Object Ages) { this.Ages = Ages;}


        public String getFoodHabit() { return FoodHabit;}


        public void setFoodHabit(String FoodHabit) { this.FoodHabit = FoodHabit;}


        public String getAttention() { return Attention;}


        public void setAttention(String Attention) { this.Attention = Attention;}


        public String getCareNote() { return CareNote;}


        public void setCareNote(String CareNote) { this.CareNote = CareNote;}


        public String getMedicineNote() { return MedicineNote;}


        public void setMedicineNote(String MedicineNote) { this.MedicineNote = MedicineNote;}


        public String getSicknessNote() { return SicknessNote;}


        public void setSicknessNote(String SicknessNote) { this.SicknessNote = SicknessNote;}
    }
}
