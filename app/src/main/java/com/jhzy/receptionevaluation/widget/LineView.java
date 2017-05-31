package com.jhzy.receptionevaluation.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.Point;

import java.util.List;

/**
 * Created by 大飞 on 2017/2/25.
 * 折线图
 */

public class LineView extends View {

    private Context mContext;
    private int measuredHeight;
    private int measuredWidth;
    private List<Point> xPointList;
    private List<Point> yPointList;
    private List<Point> pointList;
    private List<Point> pointList1;

    private int oneWidth;
    private int oneHeight;
    private Paint txtPaint;
    Paint paint;

    private int yNumberLength = 1; // y轴数据的长度  必须初始化

    public void setyNumberLength(int yNumberLength) {
        this.yNumberLength = yNumberLength;
    }

    public LineView(Context context) {
        super(context);
        init(context);
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private Paint paintLine_1;
    private Paint paintLine_2;


    private Paint pointPain1;
    private Paint pointPain2;

    private Rect rect;


    private void init(Context context) {
        this.mContext = context;

        pointPain1 = new Paint();
        pointPain1.setAntiAlias(true);
        pointPain1.setColor(mContext.getResources().getColor(R.color.color_blue));

        pointPain2 = new Paint();
        pointPain2.setAntiAlias(true);
        pointPain2.setColor(mContext.getResources().getColor(R.color.apple));

        rect = new Rect();
        paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.color_xy_line));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);

        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setTextSize(16);
        txtPaint.setStyle(Paint.Style.STROKE);
        txtPaint.setColor(mContext.getResources().getColor(R.color.color_xy_line));

        paintLine_1 = new Paint();
        paintLine_1.setColor(mContext.getResources().getColor(R.color.color_blue));
        paintLine_1.setAntiAlias(true);
        paintLine_2 = new Paint();
        paintLine_2.setColor(mContext.getResources().getColor(R.color.apple));
        paintLine_2.setAntiAlias(true);
    }


    private boolean isFirst = true;

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (isFirst && hasWindowFocus) {
            measuredHeight = getMeasuredHeight() - 60;
            measuredWidth = getMeasuredWidth();
            isFirst = false;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int startX = 47;
        int startY = measuredHeight;


//        testPaint.setColor(Color.BLACK);
//        testPaint.setStyle(Paint.Style.FILL);
//        testPaint.setAntiAlias(true);
//        Shader mShader = new LinearGradient(0,0,100,160,new int[] {Color.RED,Color.GREEN,Color.BLUE},null,Shader.TileMode.MIRROR);
////新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，玩过PS的都懂。然后那个数组是渐变的颜色。下一个参数是渐变颜色的分布，如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
//
//        testPaint.setShader(mShader);
        //画 xy 坐标的线颜色
        paint.setStrokeWidth(1);
        //x坐标
        canvas.drawLine(startX, startY, measuredWidth, startY, paint);
        //y坐标
        canvas.drawLine(startX, 3, startX, startY, paint);

//        x坐标数据 点
        drawXPoint(startX, startY, canvas);
//        y坐标数据点
        drawYPoint(startX, startY, canvas);
//  画折线图 1
        if (pointList != null) {
            paintLine_1.setStyle(Paint.Style.STROKE);
            paintLine_1.setStrokeWidth(1.5f);
            drawPoint(canvas, pointList, paintLine_1, pointPain1);
        }
//  画折线图 1
        if (pointList1 != null) {
            paintLine_2.setStyle(Paint.Style.STROKE);
            paintLine_2.setStrokeWidth(1.5f);
            // 设定阴影(柔边, X 轴位移, Y 轴位移, 阴影颜色)
//        paintLine_2.setShadowLayer(6, 5, 5, 0xFFFF00FF);
            drawPoint(canvas, pointList1, paintLine_2, pointPain2);
        }
    }

    //画折线图
    private void drawPoint(Canvas canvas, List<Point> list, Paint paint, Paint paintCircle) {

        //设置 点在x坐标的位置
        for (int i = 0; i < list.size(); i++) {
            Point point = list.get(i);
            for (int j = 0; j < xPointList.size(); j++) {
                Point point1 = xPointList.get(j);
                String text = point1.getText(); // 次数
                if (text.equals(point.getDate())) {
                    point.setX(point1.getX());
                    break;
                }
            }
        }
//设置点在Y坐标的位置
        for (int i = 0; i < list.size(); i++) {
            Point point = list.get(i);
            for (int j = 0; j < yPointList.size(); j++) {
                Point point1 = yPointList.get(j);
                //y轴的数值
                float pointNumber = Float.parseFloat(point.getText());
                //点的数值
                float yNumber = Float.parseFloat(point1.getText());
                if (yNumber > pointNumber) {
                    float rate_1 = (yNumber - pointNumber) / yNumberLength;
                    float v = oneHeight * rate_1;
                    point.setY(point1.getY() + v);
                    break;
                } else if (yNumber == pointNumber) {
                    point.setY(point1.getY());
                    break;
                }
            }
        }

        //去锯齿  画折线图
        Path path = new Path();
        for (int i = 0; i < list.size(); i++) {
            Point point = list.get(i);
            if (i == 0) {
                path.moveTo(point.getX(), point.getY());
            } else {
                path.lineTo(point.getX(), point.getY());
            }
            //画点
            canvas.drawCircle(point.getX(), point.getY(), 3f, paintCircle);
        }
        canvas.drawPath(path, paint);
    }

    private void drawYPoint(int startX, int startY, Canvas canvas) {
        int size = yPointList.size();
        oneHeight = measuredHeight / (size);
        int nextY = startY;


        for (int i = 0; i < size; i++) {
            paint.setStrokeWidth(10);
            Point point = yPointList.get(i);
            point.setX(startX);
            point.setY(nextY);
            if (!yPointList.get(i).getText().contains(".")) {
                paint.setStrokeWidth(1);
                canvas.drawPoint(startX, nextY, paint);
//                写文字
                String text = point.getText();

//                paint.getTextBounds(text, 0, text.length(), rect);
//                float measureText = paint.measureText(text);

                //3. 精确计算文字宽度
                int textWidth = getTextWidth(txtPaint, text);
                canvas.drawText(text, point.getX() - textWidth - 5, point.getY() + 10, txtPaint);
                if (i != 0) {
                    paint.setStrokeWidth(0.5f);
                    // 画线 虚线
                    DashPathEffect effect = new DashPathEffect(new float[]{4, 4, 4, 4}, 0);
                    paint.setPathEffect(effect);
                    Path path = new Path();
                    path.moveTo(startX, nextY);
                    path.lineTo(measuredWidth, nextY);
                    canvas.drawPath(path, paint);
                    paint.setPathEffect(null);
                }
//                canvas.drawLine(startX, nextY, measuredWidth, nextY, paint);
            }
            nextY -= oneHeight;
        }
        Path path = new Path();
        path.moveTo(startX - 4, 5);
        path.lineTo(startX, 2);
        path.lineTo(startX + 4, 5);
//        path.close();
        canvas.drawPath(path, paint);
        int textWidth = getTextWidth(txtPaint, yContent);
        canvas.drawText(yContent, startX - textWidth - 5, 20, txtPaint);
    }


    public int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    private String yContent = "血压";

    public void setyContent(String yContent) {
        this.yContent = yContent;
    }

    private void drawXPoint(int startX, int startY, Canvas canvas) {
        int size = xPointList.size();
        oneWidth = measuredWidth / (size + 2);
        int nextX = startX;
        paint.setStrokeWidth(1);
        for (int i = 0; i < size; i++) {
            Point point = this.xPointList.get(i);
            nextX += oneWidth;
            point.setX(nextX);
            point.setY(startY);
            //画坐标轴的点
//            canvas.drawPoint(point.getX(), point.getY() + 3, paint);
//            整数不画点
            if (point.getText().contains(".")) {
                canvas.drawLine(point.getX(), point.getY(), point.getX(), point.getY() + 5, paint);
            }
            //z整数写文字
            if (!point.getText().contains(".")) {
                String text = point.getText();
                canvas.drawText(text, point.getX() - 5, point.getY() + 30, txtPaint);
            }
        }

//        arrow.setStrokeWidth(2);
        Path path = new Path();
        path.moveTo(measuredWidth - 5, startY + 4);
        path.lineTo(measuredWidth, startY);
        path.lineTo(measuredWidth - 5, startY - 4);
//        path.close();
        paint.setStrokeWidth(1);
        canvas.drawPath(path, paint);
        canvas.drawText("次数", measuredWidth - 40, startY + 30, txtPaint);
    }

    private void XLines() {

    }

    public void setContext(Context c) {
        this.mContext = c;
    }

    public void setXPoint(List<Point> point) {
        this.xPointList = point;
        invalidate();
    }

    public void setYPoint(List<Point> point) {
        this.yPointList = point;
        invalidate();
    }

    public void setPoint(List<Point> point) {
        this.pointList = point;
        invalidate();
    }

    public void setPoint1(List<Point> point) {
        this.pointList1 = point;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                calculRate(event.getX(), event.getY(), pointList);
                calculRate(event.getX(), event.getY(), pointList1);
                Log.e("-----------", "onTouchEvent: " + event.getX() + "  " + event.getY());
                break;
        }

        return super.onTouchEvent(event);
    }

    //点击区域
    private void calculRate(float x, float y, List<Point> p) {
        try {

            for (int i = 0; i < p.size(); i++) {
                Point point = p.get(i);
                float x1 = point.getX();
                float y1 = point.getY();
                if (x > x1 - 20 && x < x1 + 20 && y > y1 - 20 && y < y1 + 20) {
                    Toast.makeText(mContext, "日期=" + point.getDate() + "   温度=" + point.getText() + "℃" + "  " + point.getY(), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
