package com.jhzy.receptionevaluation.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jhzy.receptionevaluation.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by bigyu on 2017/2/28.
 * 日期选择滚轮
 */

public class DatePicklayout extends LinearLayout {

    private Context mContext;
    private WheelView date_1; // year
    private WheelView date_2; // month
    private WheelView date_3; // day

    private ArrayList<String> years, months, days; // 年月日集合
    private ArrayList<Integer> y, m, d;

    public DatePicklayout(Context context) {
        super(context);
        initView(context);
    }

    public DatePicklayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DatePicklayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.widget_date_layout, null);
        date_1 = ((WheelView) inflate.findViewById(R.id.date_1));
        date_2 = ((WheelView) inflate.findViewById(R.id.date_2));
        date_3 = ((WheelView) inflate.findViewById(R.id.date_3));

        years = new ArrayList<>();
        months = new ArrayList<>();
        days = new ArrayList<>();
        y = new ArrayList<>();
        m = new ArrayList<>();
        d = new ArrayList<>();

        initYear();
        initMonth();
        date_1.setData(years);
        date_2.setData(months);
        date_1.setDefault(50);
        date_2.setDefault(0);
        initDay();

        listener();
        addView(inflate);

    }

    private void initDay() {
        days.clear();
        d.clear();
        int yearSelected = date_1.getSelected();
        int year = y.get(yearSelected);

        int monthSelected = date_2.getSelected();
        int month = m.get(monthSelected);
        int yearMonth = getDaysByYearMonth(year, month);
        for (int j = 1; j < yearMonth + 1; j++) {
            d.add(j);
            if (j >= 10) {
                days.add(j + "日");
            } else {
                days.add("0" + j + "日");
            }
        }
        date_3.setData(days);
    }

    private void listener() {
        date_1.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                initDay();
                date_3.setDefault(0);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        date_2.setOnSelectListener(new WheelView.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                initDay();
                date_3.setDefault(0);
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }

    /**
     * 初始化月
     */
    private void initMonth() {
        for (int i = 1; i <= 12; i++) {
            m.add(i);
            if (i < 10) {
                months.add("0" + i + "月");
            } else {
                months.add(i + "月");
            }
        }
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    private int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }


    /**
     * 初始化年数据
     */
    private void initYear() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        for (int i = 1900; i < year + 1; i++) {
            y.add(i);
            years.add(i + "年");
        }
    }

    public int getYear() {
        int selected = date_1.getSelected();
        return y.get(selected);
    }

    public int getMonth() {
        int selected = date_2.getSelected();
        return m.get(selected);
    }

    public int getDay() {
        int selected = date_3.getSelected();
        return d.get(selected);
    }
}
