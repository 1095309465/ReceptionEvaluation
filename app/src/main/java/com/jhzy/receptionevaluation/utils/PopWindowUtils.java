package com.jhzy.receptionevaluation.utils;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;

/**
 * Created by sxmd on 2017/2/23.
 */

public class PopWindowUtils {
    private Context context;
    private Holder holder;

    public PopWindowUtils(Context context) {
        this.context = context;
        holder = new Holder();
    }


    /**
     * 显示视频编辑弹窗
     */
    public interface OnBackInfoListener {
        void onBackInfo(String content);
    }

    public static void showVideoBianJiPop(Context mContext, View view, final Window window, String info, final OnBackInfoListener onBackInfoListener) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.video_bianji_dialog, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        final EditText ed_info = (EditText) contentView.findViewById(R.id.ed_info);
        ed_info.setText(info);
        ed_info.setSelection(info.length());
        ed_info.selectAll();
        TextView btn_cancle = (TextView) contentView.findViewById(R.id.btn_cancle);
        TextView btn_ok = (TextView) contentView.findViewById(R.id.btn_ok);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btn_ok.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                if (onBackInfoListener != null) {
                    String content = ed_info.getText().toString();
                    if (!TextUtils.isEmpty(content)) {
                        onBackInfoListener.onBackInfo(ed_info.getText().toString());
                    }
                }
                popupWindow.dismiss();
            }
        });
        popupWindow.setAnimationStyle(R.style.from_bottom_anim);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams wl = window.getAttributes();
                wl.alpha = 1f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
                window.setAttributes(wl);
            }
        });
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.alpha = 0.6f;   //这句就是设置窗口里崆件的透明度的．０.０全透明．１.０不透明．
        window.setAttributes(wl);
    }

    public Holder showEldersInfo(View view, final ImageView arrow, int width, int height, int naviHeight, boolean isTop) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.pop_time, null);
        holder.window = new PopupWindow(inflate,
                width, LinearLayout.LayoutParams.WRAP_CONTENT);
        holder.listView = (ListView) inflate.findViewById(R.id.listview);
        /*holder.space = inflate.findViewById(R.id.space);
        holder.space.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                holder.window.dismiss();
            }
        });*/
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0];
        int y = location[1];
        holder.window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rotate(arrow, true);
            }
        });
        rotate(arrow, false);
        holder.window.setFocusable(true);
        holder.window.setOutsideTouchable(true);
        holder.window.setBackgroundDrawable(new BitmapDrawable());
        int screenWidth = ScreenUtils.getScreenWidth(context);
        int px = (screenWidth - width) / 2 - x;
        if (isTop) {
            holder.window.showAtLocation(view, Gravity.TOP, -px, y+height);
        } else {
            holder.window.showAtLocation(view, Gravity.BOTTOM, -px, ScreenUtils.getScreenHeight(context) - y + naviHeight);
        }
        Log.e("-----------", "showEldersInfo: " + x + " " + y + "   " + height + "   " + ScreenUtils.getScreenHeight(context));
        return holder;
    }

    public void rotate(View view, boolean dis) {
        ObjectAnimator anim;
        if (dis) {
            anim = ObjectAnimator.ofFloat(view, "rotation", 180f, 360f);
        } else {
            anim = ObjectAnimator.ofFloat(view, "rotation", 0f, 180f);
        }
        anim.setDuration(500);
        anim.start();
    }

    public class Holder {
        public ListView listView;
        public PopupWindow window;
        public View space;
    }


}
