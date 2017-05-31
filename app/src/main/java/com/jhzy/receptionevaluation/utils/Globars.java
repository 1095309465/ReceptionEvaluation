package com.jhzy.receptionevaluation.utils;

import com.jhzy.receptionevaluation.ui.bean.MusicListBean;
import com.jhzy.receptionevaluation.ui.bean.PictureListBean;
import com.jhzy.receptionevaluation.ui.bean.VideoListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxmd on 2017/3/14.
 */

public class Globars {
    private static Globars globars;
    private List<MusicListBean> mListMusic;//录音的数据源
    private List<VideoListBean> mListVideo;//录像的数据源
    private List<PictureListBean> mListPicture;//照片的数据源

    private int year;
    private int month;
    private int day;

    private int tid;//大类


    public int getTid() {
        return tid;
    }


    public void setTid(int tid) {
        this.tid = tid;
    }


    public int getYear() {
        return year;
    }


    public void setYear(int year) {
        this.year = year;
    }


    public int getMonth() {
        return month;
    }


    public void setMonth(int month) {
        this.month = month;
    }


    public int getDay() {
        return day;
    }


    public void setDay(int day) {
        this.day = day;
    }


    private Globars() {
        mListMusic = new ArrayList<>();
        mListVideo = new ArrayList<>();
        mListPicture = new ArrayList<>();
    }

    public void setNull() {
        if (globars != null) ;
        globars = null;
    }


    public List<MusicListBean> getMusicList() {
        return mListMusic;
    }

    public List<VideoListBean> getVideoList() {
        return mListVideo;
    }

    public List<PictureListBean> getPictureList() {
        return mListPicture;
    }

    public static Globars newInstance() {
        if (globars == null) {
            globars = new Globars();
        }
        return globars;

    }
}
