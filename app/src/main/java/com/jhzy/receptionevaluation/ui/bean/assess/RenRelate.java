package com.jhzy.receptionevaluation.ui.bean.assess;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/3/9.
 */

public class RenRelate {

    /**
     * code : A00000
     * data : [{"GuardianName":"李四女儿","Gender":"女","Relationship":"","ContactPhone":"15689584571","ElderID":2,"GuardianID":197,"Guardianjson":null},{"GuardianName":"fdsfds","Gender":"男","Relationship":"父女","ContactPhone":"15689584571","ElderID":2,"GuardianID":204,"Guardianjson":null}]
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
         * GuardianName : 李四女儿
         * Gender : 女
         * Relationship :
         * ContactPhone : 15689584571
         * ElderID : 2
         * GuardianID : 197
         * Guardianjson : null
         */

        private String GuardianName;
        private String Gender;
        private String Relationship;
        private String ContactPhone;
        private int ElderID;
        private int GuardianID;
        private Object Guardianjson;


        public String getGuardianName() { return GuardianName;}


        public void setGuardianName(String GuardianName) { this.GuardianName = GuardianName;}


        public String getGender() { return Gender;}


        public void setGender(String Gender) { this.Gender = Gender;}


        public String getRelationship() { return Relationship;}


        public void setRelationship(String Relationship) { this.Relationship = Relationship;}


        public String getContactPhone() { return ContactPhone;}


        public void setContactPhone(String ContactPhone) { this.ContactPhone = ContactPhone;}


        public int getElderID() { return ElderID;}


        public void setElderID(int ElderID) { this.ElderID = ElderID;}


        public int getGuardianID() { return GuardianID;}


        public void setGuardianID(int GuardianID) { this.GuardianID = GuardianID;}


        public Object getGuardianjson() { return Guardianjson;}


        public void setGuardianjson(Object Guardianjson) { this.Guardianjson = Guardianjson;}
    }
}
