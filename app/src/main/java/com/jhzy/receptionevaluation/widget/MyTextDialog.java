package com.jhzy.receptionevaluation.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
public class MyTextDialog extends View {

    private Paint paint;
    private float width;
    private float height;
    private String content = "";
    private Path path;


    public MyTextDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setTextContent(String s) {
        this.content = s;
        invalidate();
    }

    private void init() {
        path = new Path();
        paint = new Paint();


    }

    private int dotY;

    public void setDotPoints(int y) {
        this.dotY = y;
    }


    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(content)) return;
        width = getWidth();
        height = getHeight();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#FF7AFEC6"));
        paint.setDither(true);//是否抖动，使图片更加圆滑
        // paint.setStyle(Paint.Style.FILL);//实心

        //画阴影
        /**
         *   mPath.moveTo(startPoint.x, startPoint.y);
         // 重要的就是这句
         mPath.quadTo(assistPoint.x,
         * radius：阴影半径

         dx：X轴方向的偏移量

         dy：Y轴方向的偏移量

         color：阴影颜色
         */
        //  paint.setShadowLayer(20f, 5f, 5f, Color.parseColor("#FF000000"));
        //颜色渲染
      /*  LinearGradient lg = new LinearGradient(0, 0, 100, 100, Color.parseColor("#FF7AFEC6"), Color.parseColor("#337AFEC6"), Shader.TileMode.MIRROR);
        paint.setShader(lg);*/
        //   canvas.drawCircle(width / 2, width / 2, height / 2, paint);

      /*  paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        canvas.drawLine(0, height / 2, width, height / 2, paint);*/
        // paint.setShader(null);

      /*  paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        //   canvas.drawCircle(width / 2, height / 2, width / 2, paint);
        path.reset();
        path.moveTo(width / 2, height);
        path.lineTo(width / 2, 0);
        path.quadTo(width * 3 / 4, 0, width, height * dotY * 1.0f / 100);
        path.quadTo(width, height * dotY * 1.0f / 100, width * 3 / 4, height);
        path.close();
        canvas.drawPath(path, paint);*/


        paint.setColor(Color.parseColor("#FF000000"));
        paint.setTextSize(100);
        float tX = width / 2 - paint.measureText(content) / 2;
        float tY = height / 2 + getFontHeight(60);
        canvas.drawText(content, tX, tY, paint);

    }

    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();

        return (int) Math.ceil(fm.bottom - fm.ascent) / 2;
    }
}
