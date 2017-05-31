package com.jhzy.receptionevaluation.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jhzy.receptionevaluation.R;

/**
 * Created by Administrator on 2016/7/24.
 */
public class MyView extends View {
    public static String[] strs = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};
    private Paint paint;
    private float width;
    private float height;
    private int index = -1;//手指当前移动到的位置


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();//控件的宽度和高度
        height = getHeight();
        //绘制字母
        for (int i = 0; i < strs.length; i++) {
            //计算字母坐标
            if (i == index) {
                // paint.setColor(Color.RED);
                paint.setTextSize(30);
            } else {
                paint.setTextSize(20);
                //  paint.setColor(Color.BLACK);
            }
            float tx = (width - paint.measureText(strs[i])) / 2;
            float ty = height / strs.length * (i + 1);

            canvas.drawText(strs[i], tx, ty, paint);

        }


    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);//设置抗锯齿
        paint.setStrokeWidth(1);//设置绘制的粗细为2
        paint.setTypeface(Typeface.DEFAULT);//设置粗体
        paint.setColor(getResources().getColor(R.color.colorAccent));
        //bebebe
        //这里控件刚开始初始化，得不到控件的高度和宽度
       /* width = getWidth();//控件的宽度和高度
        height = getHeight();*/

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (onTouchLetterListener != null) {
                    onTouchLetterListener.onActionUp();
                }
                //setBackgroundColor(Color.parseColor("#00000000"));
                index = -1;


                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                //setBackgroundColor(Color.CYAN);
                float my = event.getY();
                index = (int) (my * strs.length / height);
                if (onTouchLetterListener != null && index >= 0 && index <= strs.length - 1) {

                    onTouchLetterListener.onActionDownAndMove(strs[index].toCharArray()[0] + "", index * 100 / strs.length);
                }


                break;
        }
        invalidate();


        return true;//注意！！！！！！！！！，不让他讲触摸时间传递到其他控件
    }

    private OnTouchLetterListener onTouchLetterListener;

    public void setOnTouchLetterListener(OnTouchLetterListener onTouchLetterListener) {
        this.onTouchLetterListener = onTouchLetterListener;
    }

    public interface OnTouchLetterListener {
        public void onActionDownAndMove(String c, int percent);

        public void onActionUp();

    }

}
