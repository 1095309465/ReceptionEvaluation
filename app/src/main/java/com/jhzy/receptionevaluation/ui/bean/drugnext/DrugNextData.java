package com.jhzy.receptionevaluation.ui.bean.drugnext;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/5/2.
 */

public class DrugNextData {
        /**
         * TimeId : 1
         * TimeName : 早餐前
         * TimeTypeId : 1
         * TimeTypeName : 餐前
         * TimeColor : #FFFF
         * Drugs : [{"DrugId":1,"DrugName":"青霉素","DoseUnit":"单位","DoseId":1,"Dosage":1}]
         */

        private int TimeId;
        private String TimeName;
        private int TimeTypeId;
        private String TimeTypeName;
        private String TimeColor;
        private String FYDatetime;
        private List<DrugsBean> Drugs;



        public int getTimeId() { return TimeId;}


        public void setTimeId(int TimeId) { this.TimeId = TimeId;}


        public String getTimeName() { return TimeName;}


        public void setTimeName(String TimeName) { this.TimeName = TimeName;}


        public int getTimeTypeId() { return TimeTypeId;}


        public void setTimeTypeId(int TimeTypeId) { this.TimeTypeId = TimeTypeId;}


        public String getTimeTypeName() { return TimeTypeName;}


        public void setTimeTypeName(String TimeTypeName) { this.TimeTypeName = TimeTypeName;}


        public String getTimeColor() { return TimeColor;}


        public void setTimeColor(String TimeColor) { this.TimeColor = TimeColor;}


        public String getFYDatetime() {
                return FYDatetime;
        }


        public void setFYDatetime(String FYDatetime) {
                this.FYDatetime = FYDatetime;
        }


        public List<DrugsBean> getDrugs() { return Drugs;}


        public void setDrugs(List<DrugsBean> Drugs) { this.Drugs = Drugs;}

}
