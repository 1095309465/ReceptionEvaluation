package com.jhzy.receptionevaluation.ui.bean.physical;

import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class PhysicalExaminationCode {


    /**
     * code : A00000
     * data : [{"Id":15,"OrganizationID":15,"ElderID":62,"examItem":"2","examDate":"2017-03-09 02:16:43","examUser":"","result":"66","ExamDesc":"","Validity":0,"ValidityTime":"2017-03-09 02:16:43","ValidityUser":""}]
     * msg :
     */

    private String code;
    private String msg;
    /**
     * Id : 15
     * OrganizationID : 15
     * ElderID : 62
     * examItem : 2
     * examDate : 2017-03-09 02:16:43
     * examUser :
     * result : 66
     * ExamDesc :
     * Validity : 0
     * ValidityTime : 2017-03-09 02:16:43
     * ValidityUser :
     */

    private List<PhysicalExamination> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<PhysicalExamination> getData() {
        return data;
    }

    public void setData(List<PhysicalExamination> data) {
        this.data = data;
    }

}
