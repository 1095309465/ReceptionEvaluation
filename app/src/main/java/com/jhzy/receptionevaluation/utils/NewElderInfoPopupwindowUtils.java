package com.jhzy.receptionevaluation.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.widget.DatePicklayout;

/**
 * Created by bigyu on 2017/2/28.
 * 新建长者资料的弹出框
 */

public class NewElderInfoPopupwindowUtils {

    private Context mContext;
    private int screenWidth;
    private int screenHeight;


    public NewElderInfoPopupwindowUtils(Context mContext) {
        this.mContext = mContext;
        screenWidth = ScreenUtils.getScreenWidth(mContext);
        screenHeight = ScreenUtils.getScreenHeight(mContext);
    }


    public class ViewHolder {
        public TextView male, female, cancel, title;
        public PopupWindow popupWindow;
        public DatePicklayout datePicklayout;
        public View sure;
    }

    /**
     * 选择性别
     *
     * @param v
     * @return
     */
    public ViewHolder popChooseSex(View v) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_sex_choose, null);
        final ViewHolder vh = new ViewHolder();
        vh.popupWindow = new PopupWindow(inflate,
                screenWidth / 2,
                WindowManager.LayoutParams.WRAP_CONTENT);
        vh.male = (TextView) inflate.findViewById(R.id.male);
        vh.female = (TextView) inflate.findViewById(R.id.female);
        vh.cancel = (TextView) inflate.findViewById(R.id.cancel);

        vh.cancel.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                vh.popupWindow.dismiss();
            }
        });

        vh.popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        vh.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        vh.popupWindow.setFocusable(true);
        vh.popupWindow.showAtLocation(v, Gravity.CENTER, 0, -150);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        setBackgroundAlpha(((Activity) mContext), 0.5f);
        vh.popupWindow.setOnDismissListener(new poponDismissListener());
        return vh;
    }

    /**
     * 选择出身年月日
     *
     * @param v
     * @return
     */
    public ViewHolder popChooseBirth(View v) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_birth_choose, null);
        ViewHolder vh = new ViewHolder();
        vh.popupWindow = new PopupWindow(inflate,
                screenWidth / 2,
                WindowManager.LayoutParams.WRAP_CONTENT);
        vh.datePicklayout = (DatePicklayout) inflate.findViewById(R.id.date_pick_layout);
        vh.sure = inflate.findViewById(R.id.sure);
        vh.popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        vh.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        vh.popupWindow.setFocusable(true);
        vh.popupWindow.showAtLocation(v, Gravity.CENTER, 0, -150);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        setBackgroundAlpha(((Activity) mContext), 0.5f);
        vh.popupWindow.setOnDismissListener(new poponDismissListener());
        return vh;
    }

    /**
     * 确定保存
     *
     * @param
     * @return
     */
    public ViewHolder popSureCheckInfo(View v) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.pop_sure_check_info, null);
        final ViewHolder vh = new ViewHolder();
        vh.popupWindow = new PopupWindow(inflate,
                screenWidth / 2,
                WindowManager.LayoutParams.WRAP_CONTENT);
        vh.sure = inflate.findViewById(R.id.sure);
        vh.cancel = (TextView) inflate.findViewById(R.id.cancel);
        vh.title = (TextView) inflate.findViewById(R.id.title);
        vh.cancel.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                vh.popupWindow.dismiss();
            }
        });
        vh.popupWindow.setBackgroundDrawable(new BitmapDrawable());
        vh.popupWindow.setFocusable(true);
        vh.popupWindow.showAtLocation(v, Gravity.CENTER, 0, -150);
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
        setBackgroundAlpha(((Activity) mContext), 0.5f);
        vh.popupWindow.setOnDismissListener(new poponDismissListener());
        return vh;
    }


    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    private static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 主要是为了将背景透明度改回来
     *
     * @author cg
     */
    private class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            setBackgroundAlpha(((Activity) mContext), 1f);
        }
    }
}
