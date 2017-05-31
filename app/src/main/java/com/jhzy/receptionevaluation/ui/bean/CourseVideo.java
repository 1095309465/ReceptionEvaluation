package com.jhzy.receptionevaluation.ui.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CourseVideo implements Serializable {
        private String courseid;
        private String time;
        private String path;
        private String note;
        private int type;

    public String getCourseid() {
        return courseid;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CourseVideo{" +
                "courseid='" + courseid + '\'' +
                ", time='" + time + '\'' +
                ", path='" + path + '\'' +
                ", note='" + note + '\'' +
                ", type=" + type +
                '}';
    }
}
