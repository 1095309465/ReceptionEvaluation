package com.jhzy.receptionevaluation.ui.gridadapter;

/**
 * Created by bigyu2012 on 2017/5/2.
 * 配药发药胰岛素的过滤列表
 */
public class DrugSelectList {

    private int timeTypeId;
    private int timeId;
    private String timeName;

    public DrugSelectList(int timeTypeId, int timeId, String timeName) {
        this.timeTypeId = timeTypeId;
        this.timeId = timeId;
        this.timeName = timeName;
    }

    public int getTimeTypeId() {

        return timeTypeId;
    }

    public void setTimeTypeId(int timeTypeId) {
        this.timeTypeId = timeTypeId;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public String getTimeName() {
        return timeName;
    }

    public void setTimeName(String timeName) {
        this.timeName = timeName;
    }
}
