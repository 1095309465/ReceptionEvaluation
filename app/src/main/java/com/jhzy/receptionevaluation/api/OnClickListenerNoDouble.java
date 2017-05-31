package com.jhzy.receptionevaluation.api;


import android.view.View;

/**
 * Created by bigyu on 2017/2/16.
 * 防止二次点击
 */
public abstract class OnClickListenerNoDouble implements View.OnClickListener {
    private final long CLICK_TIME = 500;
    private long lastTime = 0;
    private View lastView;

    @Override
    public void onClick(View view) {
        if (lastView != null) {
            if (lastView.getId() == view.getId()) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime >= CLICK_TIME) {
                    lastTime = currentTime;
                    myOnClick(view);
                }
            } else {
                myOnClick(view);
            }
        } else {
            myOnClick(view);
        }
        lastView = view;
    }

    public abstract void myOnClick(View view);
}
