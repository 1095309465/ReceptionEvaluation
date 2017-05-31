package com.jhzy.receptionevaluation.ui.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sxmd on 2017/3/9.
 */

public class CourseRecordBean implements Serializable {


    /**
     * code : A00000
     * data : [{"Id":2,"InspectionTime":"2015-09-09T00:00:00","TreatSituation":"TESTSWE"},{"Id":4,"InspectionTime":"2015-09-09T00:00:00","TreatSituation":"TESTSWE"}]
     * msg : null
     */

    private String code;
    private Object msg;
    /**
     * Id : 2
     * InspectionTime : 2015-09-09T00:00:00
     * TreatSituation : TESTSWE
     */

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
        private int Id;
        private String InspectionTime;
        private String TreatSituation;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getInspectionTime() {
            return InspectionTime;
        }

        public void setInspectionTime(String InspectionTime) {
            this.InspectionTime = InspectionTime;
        }

        public String getTreatSituation() {
            return TreatSituation;
        }

        public void setTreatSituation(String TreatSituation) {
            this.TreatSituation = TreatSituation;
        }
    }
}
