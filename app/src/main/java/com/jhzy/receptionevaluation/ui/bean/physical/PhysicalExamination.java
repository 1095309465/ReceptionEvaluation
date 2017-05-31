package com.jhzy.receptionevaluation.ui.bean.physical;

/**
 * Created by Administrator on 2017/3/9.
 */

public class PhysicalExamination {

/*
    {
        "examDate":"2017-03-09 12:00:00",
            "result":"36.4,
        36.4"
    }
*/


    private String examDate;
    private  String result;
        private  String yearmonth;
    private String num1;

    public String getNum2() {
        return num2;
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    private String num2;

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}