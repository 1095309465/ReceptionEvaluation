package com.jhzy.receptionevaluation.ui.bean;

/**
 * Created by 大飞 on 2017/2/25.
 * 折线图对象
 */

public class Point {

    private float x;
    private float y;
    private String text;

    private String date;

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", text='" + text + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public Point(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Point(String text) {
        this.text = text;
    }

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
