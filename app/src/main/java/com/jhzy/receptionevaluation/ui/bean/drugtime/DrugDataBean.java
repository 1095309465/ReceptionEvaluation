package com.jhzy.receptionevaluation.ui.bean.drugtime;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/4/27.
 */

public class DrugDataBean {
    /**
     * TimeTypeId : 1
     * TimeTypeName : 餐前药
     * DragUsageTimes : [{"TimeId":1,"TimeTypeId":1,"TimeName":"早餐（餐前）","StartTime":"06:00:00.0000000","EndTime":"07:00:00.0000000"}]
     */

    private int TimeTypeId;
    private String TimeTypeName;
    private List<DragUsageTimesBean> DragUsageTimes;


    public int getTimeTypeId() { return TimeTypeId;}


    public void setTimeTypeId(int TimeTypeId) { this.TimeTypeId = TimeTypeId;}


    public String getTimeTypeName() { return TimeTypeName;}


    public void setTimeTypeName(String TimeTypeName) { this.TimeTypeName = TimeTypeName;}


    public List<DragUsageTimesBean> getDragUsageTimes() { return DragUsageTimes;}


    public void setDragUsageTimes(List<DragUsageTimesBean> DragUsageTimes) {
        this.DragUsageTimes = DragUsageTimes;
    }


    public static class DragUsageTimesBean {
        /**
         * TimeId : 1
         * TimeTypeId : 1
         * TimeName : 早餐（餐前）
         * StartTime : 06:00:00.0000000
         * EndTime : 07:00:00.0000000
         */

        private int TimeId;
        private int TimeTypeId;
        private String TimeName;
        private String StartTime;
        private String EndTime;


        public int getTimeId() { return TimeId;}


        public void setTimeId(int TimeId) { this.TimeId = TimeId;}


        public int getTimeTypeId() { return TimeTypeId;}


        public void setTimeTypeId(int TimeTypeId) { this.TimeTypeId = TimeTypeId;}


        public String getTimeName() { return TimeName;}


        public void setTimeName(String TimeName) { this.TimeName = TimeName;}


        public String getStartTime() { return StartTime;}


        public void setStartTime(String StartTime) { this.StartTime = StartTime;}


        public String getEndTime() { return EndTime;}


        public void setEndTime(String EndTime) { this.EndTime = EndTime;}
    }
}
