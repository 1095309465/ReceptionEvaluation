package com.jhzy.receptionevaluation.ui.bean.newelder;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nakisaRen
 * on 17/3/8.
 */

public class RenElderInfo implements Serializable {

    /**
     * code : A00000
     * data : [{"ID":2,"ElderName":"李四","Ages":50,"PhotoUrl":null,"ContactPhone":"23232","Gender":"男","BirthDate":"1966-10-05T00:00:00","IDCard":"123213232","Address":"地址666","HealthyState":null,"typeElder":null,"Relationship":null,"Gid":null,"SicknessNote":"很健康","BedTitle":"01房002床","Bedid":52}]
     * msg : null
     */

    private String code;
    private Object msg;
    private List<DataBean> data;


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


    public List<DataBean> getData() {
        return data;
    }


    public void setData(List<DataBean> data) {
        this.data = data;
    }


    public static class DataBean implements Serializable {
        /**
         * ID : 2
         * ElderName : 李四
         * Ages : 50
         * PhotoUrl : null
         * ContactPhone : 23232
         * Gender : 男
         * BirthDate : 1966-10-05T00:00:00
         * IDCard : 123213232
         * Address : 地址666
         * HealthyState : null
         * typeElder : null
         * Relationship : null
         * Gid : null
         * SicknessNote : 很健康
         * BedTitle : 01房002床
         * Bedid : 52
         */

        private int ID;
        private String ElderName;
        private int Ages;
        private String PhotoUrl;
        private String ContactPhone;
        private String Gender;
        private String BirthDate;
        private String IDCard;
        private String Address;
        private Object HealthyState;
        private Object typeElder;
        private String Relationship;
        private Object Gid;
        private String SicknessNote;
        private String BedTitle;
        private int Bedid;


        public int getID() {
            return ID;
        }


        public void setID(int ID) {
            this.ID = ID;
        }


        public String getElderName() {
            return ElderName;
        }


        public void setElderName(String ElderName) {
            this.ElderName = ElderName;
        }


        public int getAges() {
            return Ages;
        }


        public void setAges(int Ages) {
            this.Ages = Ages;
        }


        public String getPhotoUrl() {
            return PhotoUrl;
        }


        public void setPhotoUrl(String PhotoUrl) {
            this.PhotoUrl = PhotoUrl;
        }


        public String getContactPhone() {
            return ContactPhone;
        }


        public void setContactPhone(String ContactPhone) {
            this.ContactPhone = ContactPhone;
        }


        public String getGender() {
            return Gender;
        }


        public void setGender(String Gender) {
            this.Gender = Gender;
        }


        public String getBirthDate() {
            return BirthDate;
        }


        public void setBirthDate(String BirthDate) {
            this.BirthDate = BirthDate;
        }


        public String getIDCard() {
            return IDCard;
        }


        public void setIDCard(String IDCard) {
            this.IDCard = IDCard;
        }


        public String getAddress() {
            return Address;
        }


        public void setAddress(String Address) {
            this.Address = Address;
        }


        public Object getHealthyState() {
            return HealthyState;
        }


        public void setHealthyState(Object HealthyState) {
            this.HealthyState = HealthyState;
        }


        public Object getTypeElder() {
            return typeElder;
        }


        public void setTypeElder(Object typeElder) {
            this.typeElder = typeElder;
        }


        public String getRelationship() {
            return Relationship;
        }


        public void setRelationship(String Relationship) {
            this.Relationship = Relationship;
        }


        public Object getGid() {
            return Gid;
        }


        public void setGid(Object Gid) {
            this.Gid = Gid;
        }


        public String getSicknessNote() {
            return SicknessNote;
        }


        public void setSicknessNote(String SicknessNote) {
            this.SicknessNote = SicknessNote;
        }


        public String getBedTitle() {
            return BedTitle;
        }


        public void setBedTitle(String BedTitle) {
            this.BedTitle = BedTitle;
        }


        public int getBedid() {
            return Bedid;
        }


        public void setBedid(int Bedid) {
            this.Bedid = Bedid;
        }
    }
}
