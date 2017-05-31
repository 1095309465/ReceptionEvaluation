package com.jhzy.receptionevaluation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.AppUtils;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.Point;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import com.jhzy.receptionevaluation.ui.bean.physical.PhysicalExamination;
import com.jhzy.receptionevaluation.ui.bean.physical.PhysicalExaminationCode;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.NewElderInfoPopupwindowUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.utils.TimeUtils;
import com.jhzy.receptionevaluation.widget.LineView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * bigyu
 * 检查的界面
 */
public class CheckActivity extends BaseActivity {

    private ViewHolder vh;

    private String[] checkContent = new String[] { "血压", "脉搏", "体温", "呼吸", "血糖", "血糖", "体重", "其他" };
    private int type; // 当前检查的类型

    private String typeTxt; // 当前标题

    private final float szyMax = 100f; //最大舒张压
    private final float szyMin = 50f;  // 最新舒张压
    private final float ssyMax = 160f;
    private final float ssyMin = 100f;

    //脉搏
    private final float mbMax = 120f;
    private final float mbMin = 50f;

    //体温
    private final float twMax = 38f;
    private final float twMin = 36.5f;

    //呼吸
    private final float hxMax = 4000f;
    private final float hxMin = 2500f;

    //空腹血糖
    private final float kfxtMax = 6.1f;
    private final float kfxtMin = 3.8f;

    //餐后血糖
    private final float chxtMax = 7.8f;

    //体重
    private final float tzMax = 200f;

    private float currentMax;
    private float currenMin;
    // 老人的基本数据
    private RenElderInfo.DataBean elder;
    private HttpUtils httpUtils;
    private String token;
    private CustomDialogutils customDialogutils;
    private NewElderInfoPopupwindowUtils newElderInfoPopupwindowUtils;


    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, CheckActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getContentView() {
        return (R.layout.activity_check);
    }


    @Override
    public void init() {
        super.init();
        initParams();
        initUtils();
        isShowHistory(false);
        initViewData();
        draLine();
        initListener();
        loadElderData();
        network();
    }


    /**
     * 获取网络数据
     */
    private void network() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Type", String.valueOf(type));
        map.put("ElderID", String.valueOf(elder.getID()));
        map.put("QueryType", "2");
        map.put("sign", GetSign.GetSign(map, token));
        httpUtils.getRetrofitApi()
            .getPhysicalExaminationList("basic " + token, map)
            .enqueue(new Callback<PhysicalExaminationCode>() {
                @Override
                public void onResponse(Call<PhysicalExaminationCode> call, Response<PhysicalExaminationCode> response) {
                    PhysicalExaminationCode body = response.body();
                    if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                        List<PhysicalExamination> bodyData = body.getData();
                        if (bodyData != null && bodyData.size() != 0) {
                            isShowHistory(true);
                        } else {
                            isShowHistory(false);
                        }
                        switch (type) {
                            case 1://血压的折线图
                                loadXYLine(bodyData);
                                break;
                            case 2: // 脉搏
                                loadMBLine(bodyData, 0, 200);
                                break;
                            case 3: //体温
                                loadMBLine(bodyData, 30, 45);
                                break;
                            case 4:
                                loadMBLine(bodyData, 2000, 5000);
                                break;
                            case 5:
                                loadMBLine(bodyData, 0, 10);
                                break;
                            case 6:
                                loadMBLine(bodyData, 0, 10);
                                break;
                            case 7:
                                loadMBLine(bodyData, 0, 140);
                                break;
                        }
                    }
                }


                private void loadMBLine(List<PhysicalExamination> data, int start, int max) {
                    if (data == null || data.size() == 0) {
                        return;
                    }
                    vh.linelayout.setVisibility(View.VISIBLE);
                    vh.line1Text.setText(typeTxt);
                    Collections.reverse(data);
                    int size = data.size();
                    String startTime = data.get(0).getExamDate();
                    String endTime = data.get(size - 1).getExamDate();
                    vh.lineIntroduce.setText("近7次" + typeTxt + "检查结果折线图");
                    vh.timeRange.setText(
                        TimeUtils.dateString(startTime) + "至" + TimeUtils.dateString(endTime));
                    //y轴的单位
                    vh.lineView.setyContent(checkContent[type - 1]);
                    //y轴刻度大小
                    ArrayList<Point> yPoint = new ArrayList<>();
                    int one = (max - start) / 5;
                    vh.lineView.setyNumberLength(one);
                    for (int i = 0; i < 6; i++) {
                        yPoint.add(new Point("" + (start + one * i)));
                    }
                    vh.lineView.setYPoint(yPoint);

                    //设置具体的线
                    ArrayList<Point> points = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        try {
                            PhysicalExamination physicalExamination = data.get(i);
                            String result = physicalExamination.getResult();
                            if (TextUtils.isEmpty(result) || Double.parseDouble(result) >= max ||
                                Double.parseDouble(result) <= start) {
                                continue;
                            }
                            points.add(
                                new Point(physicalExamination.getResult(), String.valueOf(i + 1)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    vh.lineView.setPoint(points);
                }


                /**
                 * 加载血压的折线图
                 * @param data
                 */
                private void loadXYLine(List<PhysicalExamination> data) {
                    if (data == null || data.size() == 0) {
                        return;
                    }
                    vh.linelayout.setVisibility(View.VISIBLE);
                    vh.line1Text.setText("收缩压");
                    vh.line2Text.setText("舒张压");
                    vh.line2Text.setVisibility(View.VISIBLE);
                    vh.colorView.setVisibility(View.VISIBLE);
                    Collections.reverse(data);
                    int size = data.size();
                    String startTime = data.get(0).getExamDate();
                    String endTime = data.get(size - 1).getExamDate();
                    vh.lineIntroduce.setText("近7次" + typeTxt + "检查结果折线图");
                    vh.timeRange.setText(
                        TimeUtils.dateString(startTime) + "至" + TimeUtils.dateString(endTime));
                    //y轴的单位
                    vh.lineView.setyContent("血压");
                    //y轴刻度大小
                    ArrayList<Point> yPoint = new ArrayList<>();
                    int start = 0;
                    int max = 200;
                    int one = (max - start) / 5;
                    vh.lineView.setyNumberLength(one);
                    for (int i = 0; i < max; i += one) {
                        yPoint.add(new Point("" + (start + i)));
                    }
                    vh.lineView.setYPoint(yPoint);

                    //设置具体的线
                    ArrayList<Point> points = new ArrayList<>();
                    ArrayList<Point> points1 = new ArrayList<>();

                    for (int i = 0; i < size; i++) {
                        try {
                            PhysicalExamination physicalExamination = data.get(i);
                            String result = physicalExamination.getResult();
                            if (TextUtils.isEmpty(result)) {
                                continue;
                            }
                            String[] split = result.split(",");
                            physicalExamination.setNum1(split[0]);
                            physicalExamination.setNum2(split[1]);
                            points.add(
                                new Point(physicalExamination.getNum1(), String.valueOf(i + 1)));
                            points1.add(
                                new Point(physicalExamination.getNum2(), String.valueOf(i + 1)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    vh.lineView.setPoint(points);
                    vh.lineView.setPoint1(points1);
                }


                @Override
                public void onFailure(Call<PhysicalExaminationCode> call, Throwable t) {
                    if (NetWorkUtils.isNetworkConnected(mContext)) {
                        customDialogutils.cancelNetworkDialog("服务器异常", false);
                    } else {
                        customDialogutils.cancelNetworkDialog("网络异常", false);
                    }
                }
            });

    }


    /**
     * 加载老人的基本数据
     */
    private void loadElderData() {
        if (elder == null) {
            return;
        }
        //        姓名
        String elderName = elder.getElderName();
        vh.name.setText(elderName);
        //        性别
        String gender = elder.getGender();
        vh.sex.setText(gender);
        //        年龄
        int age = elder.getAges();
        vh.age.setText(age + "岁");
        //        房间号
        String bedTitle = elder.getBedTitle();
        vh.bed.setText(bedTitle);
        //        地址
        String address = elder.getAddress();
        vh.address.setText(address);
        //        头像
        ImageLoaderUtils.load(vh.headIcon, elder.getPhotoUrl());
    }


    private void initListener() {
        // 历史记录的点击事件
        vh.history.setOnClickListener(onClickListener);
        //返回
        vh.back.setOnClickListener(onClickListener);
    }


    /**
     * 折线图
     */
    private void draLine() {
        vh.lineView.setyNumberLength(1);
        //        画x坐标
        List<Point> point = new ArrayList<>();
        point.add(new Point("1"));
        point.add(new Point("1.5"));
        point.add(new Point("2"));
        point.add(new Point("2.5"));
        point.add(new Point("3"));
        point.add(new Point("3.5"));
        point.add(new Point("4"));
        point.add(new Point("4.5"));
        point.add(new Point("5"));
        point.add(new Point("5.5"));
        point.add(new Point("6"));
        point.add(new Point("6.5"));
        point.add(new Point("7"));
        point.add(new Point("7.5"));
        vh.lineView.setXPoint(point);

        ArrayList<Point> yPoint = new ArrayList<>();
        int start = 32;
        for (int i = 0; i < 8; i++) {
            yPoint.add(new Point("" + (start + i)));
        }
        vh.lineView.setYPoint(yPoint);
        //
        //        ArrayList<Point> points = new ArrayList<>();
        //        points.add(new Point("34.7", "1"));
        //        points.add(new Point("33.1", "2"));
        //        points.add(new Point("35", "3"));
        //        points.add(new Point("34", "4"));
        //        points.add(new Point("35", "5"));
        //        points.add(new Point("33", "6"));
        //        points.add(new Point("37", "7"));
        //        vh.lineView.setPoint(points);
        //        ArrayList<Point> points1 = new ArrayList<>();
        //        points1.add(new Point("39", "1"));
        //        points1.add(new Point("37", "2"));
        //        points1.add(new Point("39", "3"));
        //        points1.add(new Point("37", "4"));
        //        points1.add(new Point("39", "5"));
        //        points1.add(new Point("37", "6"));
        //        points1.add(new Point("38", "7"));
        //        vh.lineView.setPoint1(points1);
    }


    private void initParams() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        elder = ((RenElderInfo.DataBean) bundle.getSerializable("elder"));
        token = SPUtils.find(CONTACT.TOKEN);
        if (elder == null || type <= 0 || type > 7 || TextUtils.isEmpty(token)) {
            Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void initUtils() {
        vh = new ViewHolder();
        httpUtils = HttpUtils.getInstance();
        customDialogutils = new CustomDialogutils(mContext);
        newElderInfoPopupwindowUtils = new NewElderInfoPopupwindowUtils(mContext);
    }


    private void initViewData() {
        View view = null;
        //检查布局是否有子控件
        int childCount = vh.checkLayout.getChildCount();
        if (childCount != 0) {
            vh.checkLayout.removeAllViews();
        }
        switch (type) {
            case 1: // 血压
                typeTxt = "血压";
                view = LayoutInflater.from(mContext).inflate(R.layout.item_xy_layout, null);
                break;
            case 2:// 脉搏
                currentMax = mbMax;
                currenMin = mbMin;
                typeTxt = "脉搏";
                view = LayoutInflater.from(mContext).inflate(R.layout.item_mb_layout, null);
                break;
            case 3: // 体温
                currentMax = twMax;
                currenMin = twMin;
                typeTxt = "体温";
                view = LayoutInflater.from(mContext).inflate(R.layout.item_tw_layout, null);
                break;
            case 4:// 呼吸
                currentMax = hxMax;
                currenMin = hxMin;
                typeTxt = "呼吸";
                view = LayoutInflater.from(mContext).inflate(R.layout.item_hx_layout, null);
                break;
            case 5:// 空腹血糖
                currentMax = kfxtMax;
                currenMin = kfxtMin;
                typeTxt = "空腹血糖";
                view = LayoutInflater.from(mContext).inflate(R.layout.item_kfxt_layout, null);
                break;
            case 6:// 餐后血糖
                currentMax = chxtMax;
                currenMin = 0;
                typeTxt = "餐后血糖";
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chxt_layout, null);
                break;
            case 7:// 体重
                currentMax = tzMax;
                currenMin = 0;
                typeTxt = "体重";
                view = LayoutInflater.from(mContext).inflate(R.layout.item_tz_layout, null);
                break;
            case 8:// 其他
                typeTxt = "其他";
                break;
        }

        //设置标题
        setTitle(typeTxt + "检查");
        //加载不同的布局
        loadView(view);
    }


    //加载不同的布局
    private void loadView(View view) {
        if (view != null) {
            vh.checkLayout.addView(view);
        }
        final Check check = new Check();
        check.nativeCheck = (TextView) findViewById(R.id.native_check);
        check.submit = (TextView) findViewById(R.id.submit);
        check.submit.setEnabled(false);
        check.num1 = (EditText) findViewById(R.id.num_1);
        check.nativeCheck.setText(typeTxt + "数值不在正常范围内");
        if (type == 1) {
            check.num2 = (EditText) findViewById(R.id.num_2);
            check.nativeCheck2 = (TextView) findViewById(R.id.native_check_2);
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }


                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }


                @Override
                public void afterTextChanged(Editable editable) {
                    checkMuch(check);
                }
            };
            check.num2.addTextChangedListener(textWatcher);
            check.num1.addTextChangedListener(textWatcher);
        } else if (type == 2 || type == 3 || type == 4 || type == 5 || type == 6 || type == 7) {
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }


                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }


                @Override
                public void afterTextChanged(Editable editable) {
                    checkSingle(check);
                }
            };
            check.num1.addTextChangedListener(textWatcher);
        }
        /**
         * 点击保存按钮
         */
        check.submit.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                AppUtils.hideKeyBoard(view, ((Activity) mContext));
                final NewElderInfoPopupwindowUtils.ViewHolder viewHolder
                    = newElderInfoPopupwindowUtils.popSureCheckInfo(view);
                if (type == 1) {
                    String num1 = check.num1.getText().toString().trim();
                    String num2 = check.num2.getText().toString().trim();
                    viewHolder.title.setText("确定保存  舒张压" + num1 + "/收缩压" + num2 + "?");
                } else {
                    String num1 = check.num1.getText().toString().trim();
                    viewHolder.title.setText("确定保存  " + checkContent[type - 1] + num1 + "?");
                }
                viewHolder.sure.setOnClickListener(new OnClickListenerNoDouble() {
                    @Override
                    public void myOnClick(View view) {
                        viewHolder.popupWindow.dismiss();
                        submitPhysicalData(check);
                    }
                });
            }
        });
    }


    private void submitPhysicalData(Check check) {
        customDialogutils.networkRefreshDiallog("提交数据中...");
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Type", String.valueOf(type));
        map.put("ElderID", String.valueOf(elder.getID()));
        if (type == 1) { // 提交血压的值
            String num1 = check.num1.getText().toString().trim();
            String num2 = check.num2.getText().toString().trim();
            check.num1.setText("");
            check.num2.setText("");
            map.put("Value", num1 + "," + num2);
        } else { //
            String num1 = check.num1.getText().toString().trim();
            check.num1.setText("");
            map.put("Value", num1);
        }
        map.put("sign", GetSign.GetSign(map, token));
        Log.e("--------",
            "submitPhysicalData: " + String.valueOf(type) + "　　" + String.valueOf(elder.getID()) +
                "   " + check.num1.getText().toString().trim());
        HttpUtils.getInstance()
            .getRetrofitApi()
            .submitPhysicalData("basic " + token, map)
            .enqueue(new Callback<Code>() {
                @Override
                public void onResponse(Call<Code> call, Response<Code> response) {
                    Code body = response.body();
                    if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                        customDialogutils.cancelNetworkDialog("数据提交成功", true);
                        finish();
                    } else {
                        customDialogutils.cancelNetworkDialog("数据提交失败", false);
                    }
                    //                network();
                }


                @Override
                public void onFailure(Call<Code> call, Throwable t) {
                    if(NetWorkUtils.isNetworkConnected(mContext)){
                        customDialogutils.cancelNetworkDialog("服务器异常,数据提交失败", false);
                    }else{
                        customDialogutils.cancelNetworkDialog("网络异常,数据提交失败", false);
                    }
                }
            });

    }


    /**
     * 检查单项
     */
    private void checkSingle(Check check) {
        String num1 = check.num1.getText().toString().trim();
        boolean emptyNum1 = TextUtils.isEmpty(num1);
        if (!emptyNum1) {
            float v = Float.parseFloat(num1);
            if (v > currenMin && v < currentMax) {
                check.nativeCheck.setVisibility(View.INVISIBLE);
            } else {
                check.nativeCheck.setVisibility(View.VISIBLE);
            }
        }

        if (!emptyNum1) {
            check.submit.setEnabled(true);
        } else {
            check.submit.setEnabled(false);
        }
    }


    //检查血压范围
    private void checkMuch(Check check) {
        String num1 = check.num1.getText().toString().trim();
        String num2 = check.num2.getText().toString().trim();
        boolean emptyNum1 = TextUtils.isEmpty(num1);
        boolean emptyNum2 = TextUtils.isEmpty(num2);
        if (!emptyNum1) {
            float v = Float.parseFloat(num1);
            if (v > ssyMin && v < ssyMax) {
                check.nativeCheck.setVisibility(View.INVISIBLE);
            } else if (check.nativeCheck2.getVisibility() == View.INVISIBLE) {
                check.nativeCheck.setVisibility(View.VISIBLE);
            }
        }
        if (!emptyNum2) {
            float v = Float.parseFloat(num2);
            if (v > szyMin && v < szyMax) {
                check.nativeCheck2.setVisibility(View.INVISIBLE);
            } else if (check.nativeCheck.getVisibility() == View.INVISIBLE) {
                check.nativeCheck2.setVisibility(View.VISIBLE);
            }
        }
        if (!emptyNum1 && !emptyNum2) {
            check.submit.setEnabled(true);
        } else {
            check.submit.setEnabled(false);
        }
    }


    class Check {
        EditText num1, num2;
        TextView submit, nativeCheck, nativeCheck2;
    }


    /**
     * 设置标题
     */
    private void setTitle(String title) {
        vh.title.setText(title);
    }


    //评估的控件
    class ViewHolder {
        private LinearLayout checkLayout; // 检查的布局

        private TextView title; // 标题

        private LineView lineView; // 折线图

        private TextView history; // 历史记录的按钮

        private View back; // 返回
        private SimpleDraweeView headIcon;
        private TextView name; // 姓名
        private TextView sex;// 性别
        //        年龄
        private TextView age;
        //        床位
        private TextView bed;
        //        地址
        private TextView address;
        private TextView timeRange;
        private TextView lineIntroduce;
        private View linelayout;
        private TextView line1Text;
        private TextView line2Text;
        private View colorView;


        private ViewHolder() {
            checkLayout = (LinearLayout) findViewById(R.id.chek_layout);
            title = (TextView) findViewById(R.id.title);
            lineView = ((LineView) findViewById(R.id.line_view));
            history = (TextView) findViewById(R.id.history);
            back = findViewById(R.id.back);
            headIcon = ((SimpleDraweeView) findViewById(R.id.head_icon));
            name = ((TextView) findViewById(R.id.name));
            sex = ((TextView) findViewById(R.id.sex));
            age = (TextView) findViewById(R.id.age);
            bed = (TextView) findViewById(R.id.bed);
            address = ((TextView) findViewById(R.id.address));
            timeRange = ((TextView) findViewById(R.id.time_range));
            lineIntroduce = ((TextView) findViewById(R.id.line_introduce));
            linelayout = findViewById(R.id.line_layout);
            line1Text = ((TextView) findViewById(R.id.line_1_text));
            line2Text = ((TextView) findViewById(R.id.line_2_text));
            colorView = findViewById(R.id.color_view);
        }
    }


    /**
     * 监听回调
     */
    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.history: // 历史记录的按钮
                    Bundle bundle = new Bundle();
                    bundle.putString("type", String.valueOf(type));
                    bundle.putString("elderID", String.valueOf(elder.getID()));
                    bundle.putString("typeText", typeTxt);
                    PhysicalHistoryActivity.start(mContext, bundle);
                    break;
                case R.id.back:// 返回
                    finish();
                    break;
            }
        }
    };


    /**
     * 是否可点击  历史记录
     */
    private void isShowHistory(boolean isShow) {
        if (isShow) {
            vh.history.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_daily_right, 0);
            vh.history.setEnabled(true);
            vh.history.setTextColor(getResources().getColor(R.color.color_bar_uncheck));
            vh.history.setText("历史记录");
        } else {
            vh.history.setTextColor(getResources().getColor(R.color.color_font_gray));
            vh.history.setEnabled(false);
            vh.history.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            vh.history.setText("无检查记录");
        }
    }

}
