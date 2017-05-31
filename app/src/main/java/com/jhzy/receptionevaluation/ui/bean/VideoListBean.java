package com.jhzy.receptionevaluation.ui.bean;

import java.io.Serializable;

/**
 * Created by sxmd on 2017/3/2.
 */

public class VideoListBean  implements Serializable{
    private String info;//备注信息
    private String time;//时间
    private boolean isDelete;//是否删除
    private String path;//文件路径

    @Override
    public String toString() {
        return "VideoListBean{" +
                "info='" + info + '\'' +
                ", time='" + time + '\'' +
                ", isDelete=" + isDelete +
                ", path='" + path + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
