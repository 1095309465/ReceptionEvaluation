package com.jhzy.receptionevaluation.ui.dispensingdrug;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.MainFragemntAdapter;
import com.jhzy.receptionevaluation.ui.adapter.TimeAdapter;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugCode;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugElders;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.TagsBean;
import com.jhzy.receptionevaluation.ui.bean.drugtime.DrugDataBean;
import com.jhzy.receptionevaluation.ui.bean.drugtime.DrugTime;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.ui.gridadapter.DrugSelectList;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.Globars;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.MyPinYinTool;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.PopWindowUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 发药配药长者列表界面
 */
public class DispenseMainActivity extends BaseActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, DispensedFragment.RefreshListener {
    private int currentPosition = 0;
    private NoScrollViewPager viewPager;    //列表页
    private List<Fragment> fragmentList;    //配药fragment列表
    private MainFragemntAdapter mainFragemntAdapter; // 多页的fragment的适配器
    private int mMonth, mDay, mYear;    //配药月日
    private HttpUtils httpUtils;
    private DispensedFragment unCompleteFragment; // 未完成的fragment
    private DispensedFragment completedFragment; // 已完成的fragment
    //    private PinyinTool tool;
    //界面控件定义
    private ImageView back;     //回退图片按钮
    private TextView txtSelectedtDate;   //选定的日期文本
    private EditText etSearchDispense;  //搜索编辑框
    private TextView btnDispensing;       //完成前按钮
    private TextView btnDispensed;        //完成后按钮
    private ImageView imClearInput;     //清除输入图片按钮
    private TextView oneKeyDrug;

    private int layoutType; // 当前对应的模块  1配药 2发药  3胰岛素
    private int tId; // 大类 类型  餐前药 、 餐中药 等类型
    private int sId; // 小类Id  早餐前  中餐前
    private boolean bottomFunIsShow = false; // 是否显示底部的功能键（发药模块专用！！！！！）
    private LinearLayout drugChooseTop;
    private List<Elder> elderList;
    //底部 一键全选的布局
    private View bottomFunc;
    //底部已完成 未完成的布局
    private View bottomFuncComplete;
    private MyPinYinTool myPinYinTool;
    //选择全部
    private TextView selectAll;
    private View drugChoose;
    private PopWindowUtils popWindowUtils;
    private ImageView bottomArrow, bottomArrowTop;

    private ArrayList<DrugSelectList> drugSelectLists;
    private TextView drugTime, drugTimeTop;
    private View medicine;

    //过滤关键字
    private String filterContent;
    private CustomDialogutils customDialogutils;
    private TextView title;
    private String tName;


    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, DispenseMainActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getContentView() {
        return (R.layout.activity_dispense_main);
    }

    @Override
    public void init() {
        super.init();

        initParams();
        bindViews();
        initView();
        initUtilites();
        initListener();
    }

    /**
     * 根据类型显示不同的布局
     */
    private void initView() {
        if (layoutType == 1) {//显示配药的布局
            oneKeyDrug.setVisibility(View.GONE);
            drugChooseTop.setVisibility(View.GONE);
            title.setText(tName);
        } else if (layoutType == 2) {
            oneKeyDrug.setVisibility(View.VISIBLE);
            drugChooseTop.setVisibility(View.GONE);
            title.setText("发药");
        } else if (layoutType == 3) {
            oneKeyDrug.setVisibility(View.GONE);
            drugChooseTop.setVisibility(View.VISIBLE);
            title.setText("胰岛素");
        }
    }

    private void initParams() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            layoutType = extras.getInt(CONTACT.DispenseDrugContact.TYPE, 0);
            tId = extras.getInt(CONTACT.DispenseDrugContact.TYPE_2, 0);
            tName = extras.getString(CONTACT.DispenseDrugContact.TYPE_NAME_2 );
        }
        Calendar instance = Calendar.getInstance();
        mYear = instance.get(Calendar.YEAR);
        mMonth = instance.get(Calendar.MONTH) + 1;
        mDay = instance.get(Calendar.DAY_OF_MONTH);
        initTime();
    }

    private void initListener() {
        //未完成 已完成 监听
        btnDispensing.setOnClickListener(onClickListener);
        btnDispensed.setOnClickListener(onClickListener);
        back.setOnClickListener(onClickListener);
        //搜索框删除按钮点击事件
        imClearInput.setOnClickListener(onClickListener);
        //一建发药
        oneKeyDrug.setOnClickListener(onClickListener);
        //全选按钮
        selectAll.setOnClickListener(onClickListener);
        drugChoose.setOnClickListener(onClickListener);
        drugChooseTop.setOnClickListener(onClickListener);
        //发药
        medicine.setOnClickListener(onClickListener);
        txtSelectedtDate.setOnClickListener(onClickListener);
    }


    /**
     * 显示已完成的fragment
     */
    private void showCompletedFragment() {
        if (!btnDispensed.isSelected()) {
            btnDispensing.setSelected(false);
            btnDispensed.setSelected(true);
            oneKeyDrug.setVisibility(View.GONE);
            bottomFunc.setVisibility(View.GONE);
            drugChooseTop.setVisibility(View.GONE);
        }
    }

    /**
     * 显示未完成的fragment
     */
    private void showCompletedFragment_UN() {
        if (!btnDispensing.isSelected()) {
            btnDispensing.setSelected(true);
            btnDispensed.setSelected(false);
            oneKeyDrug.setVisibility(View.VISIBLE);
            if (bottomFunIsShow) {
                bottomFuncComplete.setVisibility(View.GONE);
                bottomFunc.setVisibility(View.VISIBLE);
            } else {
                bottomFuncComplete.setVisibility(View.VISIBLE);
                bottomFunc.setVisibility(View.GONE);
            }
        }
        initView();
    }

    /**
     * 监听回掉
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {  //设置完成前页
            int id = v.getId();
            int width;
            int height;
            switch (id) {
                case R.id.btnDispensing0: // 未完成
                    currentPosition = 0;
                    viewPager.setCurrentItem(currentPosition);
                    showCompletedFragment_UN();
                    break;

                case R.id.btnDispensed0://已完成
                    currentPosition = 1;
                    viewPager.setCurrentItem(currentPosition);
                    showCompletedFragment();
                    break;

                case R.id.imBeforeMealBack0: // 返回键
                    finish();
                    break;

                case R.id.imClearInput0://搜索框删除按钮点击事件
                    etSearchDispense.setText("");
                    unCompleteFragment.displayAll();
                    completedFragment.displayAll();
                    break;

                case R.id.imDateDown0: //选择日期
                    chooseData();
                    break;

                case R.id.one_key_drug://一键发药按钮
                    if (bottomFunIsShow) {
                        bottomFunc.setVisibility(View.GONE);
                        oneKeyDrug.setText("一键发药");
                        bottomFuncComplete.setVisibility(View.VISIBLE);
                        selectAll.setText("全选");
                        selectAll.setSelected(false);
                        //显示全部
                        unCompleteFragment.filterKind("");
                    } else {
                        bottomFuncComplete.setVisibility(View.GONE);
                        bottomFunc.setVisibility(View.VISIBLE);
                        oneKeyDrug.setText("取消");
                        selectAll.setText("取消全选");
                        selectAll.setSelected(true);
                        //显示过滤后的
                        unCompleteFragment.filterKind(filterContent);
                    }
                    bottomFunIsShow = !bottomFunIsShow;
                    unCompleteFragment.setCanSelectedItems(bottomFunIsShow, bottomFunIsShow);
                    break;

                case R.id.select_all: // 全选
                    if (selectAll.isSelected()) {
                        unCompleteFragment.setCanSelectedItems(bottomFunIsShow, false);
                        selectAll.setText("全选");
                        selectAll.setSelected(false);
                    } else {
                        unCompleteFragment.setCanSelectedItems(bottomFunIsShow, true);
                        selectAll.setText("取消全选");
                        selectAll.setSelected(true);
                    }
                    break;

                case R.id.drug_choose: // 过滤选择  底部
                    width = drugChoose.getMeasuredWidth();
                    height = drugChoose.getMeasuredHeight();
                    show(false, v, bottomArrow, width, height);
                    break;

                case R.id.drug_choose_top:// 过滤选择  顶部
                    width = drugChooseTop.getMeasuredWidth();
                    height = drugChooseTop.getMeasuredHeight();
                    show(true, v, bottomArrowTop, width, height);
                    break;

                case R.id.medicine: // 发药
                    submitMedicine();
                    break;
            }
        }
    };

    //发药
    private void submitMedicine() {
        String submit = unCompleteFragment.submit();
        if (TextUtils.isEmpty(submit) || "[]".equals(submit)) {
            Toast.makeText(mContext, "请选择长者", Toast.LENGTH_SHORT).show();
            return;
        }
        customDialogutils.setResfreshDialog("提交数据中...", 10 * 1000);
        String token = SPUtils.find(CONTACT.TOKEN);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("UsageTime", mYear + "-" + mMonth + "-" + mDay);
        map.put("ElderIds", submit);
        map.put("TimeId", String.valueOf(sId));
        map.put("sign", GetSign.GetSign(map, token));
        httpUtils.getRetrofitApi().updateDispensingAll("basic " + token, map).enqueue(new Callback<Code>() {
            @Override
            public void onResponse(Call<Code> call, Response<Code> response) {
                Code body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    String data = body.getData();
                    Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                    //重置一键发药
                    oneKeyDrug.performClick();
                    network();
                } else if (body != null) {
                    Toast.makeText(mContext, body.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Code> call, Throwable t) {
                if(NetWorkUtils.isNetworkConnected(mContext)){
                    Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //选择日期
    private void chooseData() {
        if (!txtSelectedtDate.isSelected()) {
            txtSelectedtDate.setSelected(true);
            Calendar now = Calendar.getInstance();
            com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                    DispenseMainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.setThemeDark(false);
            dpd.vibrate(false);
            dpd.dismissOnPause(false);
            dpd.showYearPickerFirst(false);
            dpd.setVersion(com.wdullaer.materialdatetimepicker.date.DatePickerDialog.Version.VERSION_2);
            dpd.setAccentColor(Color.parseColor("#9C27B0"));
            dpd.setTitle("选择配药日期");
            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();
            date2.add(Calendar.WEEK_OF_MONTH, -1);
            Calendar date3 = Calendar.getInstance();
            date3.add(Calendar.WEEK_OF_MONTH, 1);
            Calendar[] days = {date1, date2, date3};
            dpd.setHighlightedDays(days);
            dpd.show(getFragmentManager(), "Datepickerdialog");
            txtSelectedtDate.setSelected(false);
        }
    }

    //显示过滤的弹框
    private void show(boolean isTop, View view, ImageView bottomArrow, int width, int height) {

        Resources resources = mContext.getResources();
        int test = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int naviHeight = resources.getDimensionPixelSize(test);
        final PopWindowUtils.Holder holder = popWindowUtils.showEldersInfo(view, bottomArrow, width, height, naviHeight, isTop);
        TimeAdapter timeAdapter = new TimeAdapter(mContext, drugSelectLists);
        holder.listView.setAdapter(timeAdapter);
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DrugSelectList drugSelectList = drugSelectLists.get(i);
                String timeName = drugSelectList.getTimeName();
                holder.window.dismiss();
                if ("所有".equals(timeName)) {
                    unCompleteFragment.filterKind(null);
                    filterContent = null;
                } else {
                    unCompleteFragment.filterKind(timeName);
                    filterContent = timeName;
                }
                sId = drugSelectList.getTimeId();
                drugTime.setText(timeName);
                drugTimeTop.setText(timeName);
            }
        });
    }

    private void initUtilites() {
//        tool = new PinyinTool();
        myPinYinTool = new MyPinYinTool();
        httpUtils = HttpUtils.getInstance();
        popWindowUtils = new PopWindowUtils(mContext);
        drugSelectLists = new ArrayList<>();
        drugSelectLists.add(new DrugSelectList(0, 0, "所有"));
        customDialogutils = new CustomDialogutils(mContext);
    }


    private void bindViews() {
        //控件初始化
        title = ((TextView) findViewById(R.id.txtBeforeMealWinTitle0));
        medicine = findViewById(R.id.medicine);
        drugTime = ((TextView) findViewById(R.id.drug_time));
        drugTimeTop = ((TextView) findViewById(R.id.drug_time_top));
        bottomArrow = ((ImageView) findViewById(R.id.drug_arrow));
        bottomArrowTop = ((ImageView) findViewById(R.id.drug_arrow_top));
        drugChoose = findViewById(R.id.drug_choose);
        selectAll = ((TextView) findViewById(R.id.select_all));
        bottomFuncComplete = findViewById(R.id.bottom_func_complete);
        bottomFunc = findViewById(R.id.bottom_func);
        drugChooseTop = ((LinearLayout) findViewById(R.id.drug_choose_top));
        oneKeyDrug = ((TextView) findViewById(R.id.one_key_drug));
        back = (ImageView) findViewById(R.id.imBeforeMealBack0);
        txtSelectedtDate = (TextView) findViewById(R.id.imDateDown0);
        etSearchDispense = (EditText) findViewById(R.id.etSearchDispense0);
        imClearInput = (ImageView) findViewById(R.id.imClearInput0);
        btnDispensing = (TextView) findViewById(R.id.btnDispensing0);
        btnDispensing.setSelected(true);
        btnDispensed = (TextView) findViewById(R.id.btnDispensed0);
        viewPager = (NoScrollViewPager) findViewById(R.id.vpDispense0);
        //fragment初始化
        fragmentList = new ArrayList<>();
        unCompleteFragment = DispensedFragment.newInstance(false, layoutType);
        completedFragment = DispensedFragment.newInstance(true, layoutType);
        fragmentList.add(unCompleteFragment);
        fragmentList.add(completedFragment);
        mainFragemntAdapter = new MainFragemntAdapter(getSupportFragmentManager(), fragmentList);
        //适配器放入viewpager中
        viewPager.setAdapter(mainFragemntAdapter);
        //设置当前的fragment
        viewPager.setCurrentItem(currentPosition);
        viewPager.setOffscreenPageLimit(2);


        etSearchDispense.addTextChangedListener(new TextWatcher() {//搜索框文本改变侦听事件
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String content = String.valueOf(s).toUpperCase();
                if (!TextUtils.isEmpty(content)) {
                    unCompleteFragment.filterElder(content);
                    completedFragment.filterElder(content);
                } else {//显示所有的
                    unCompleteFragment.displayAll();
                    completedFragment.displayAll();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {//搜索框有输入
                    imClearInput.setVisibility(View.VISIBLE);
                } else {    //搜索框无输入
                    imClearInput.setVisibility(View.GONE);
                }
            }
        });
        //设置默认的时间
        txtSelectedtDate.setText(mMonth + "月" + mDay + "日");
    }

    /**
     * 网络请求读取长者资料
     */
    public void network() {
//        String json = "{\n" +
//                "    \"code\":\"A00000\",\n" +
//                "    \"data\":[\n" +
//                "        {\n" +
//                "            \"ElderId\":12,\n" +
//                "            \"PhotoUrl\":\"www.baidu.com\",\n" +
//                "            \"name\":\"于飞\",\n" +
//                "            \"age\":85,\n" +
//                "            \"bedCode\":\"A001-03\",\n" +
//                "            \"Gender\":\"\",\n" +
//                "            \"tags\":[\n" +
//                "                {\n" +
//                "                    \"timeid\":1,\n" +
//                "                    \"type\":\"早餐（餐前）\",\n" +
//                "                    \"isCompleted\":0\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"timeid\":2,\n" +
//                "                    \"type\":\"早餐\",\n" +
//                "                    \"isCompleted\":1\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"ElderId\":12,\n" +
//                "            \"PhotoUrl\":\"www.baidu.com\",\n" +
//                "            \"name\":\"任晓峰\",\n" +
//                "            \"age\":85,\n" +
//                "            \"bedCode\":\"A001-03\",\n" +
//                "            \"Gender\":\"\",\n" +
//                "            \"tags\":[\n" +
//                "                {\n" +
//                "                    \"timeid\":1,\n" +
//                "                    \"type\":\"早餐\",\n" +
//                "                    \"isCompleted\":0\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"timeid\":2,\n" +
//                "                    \"type\":\"早餐（餐后）\",\n" +
//                "                    \"isCompleted\":1\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "            \"ElderId\":13,\n" +
//                "            \"PhotoUrl\":\"www.baidu.com\",\n" +
//                "            \"name\":\"得名\",\n" +
//                "            \"age\":85,\n" +
//                "            \"bedCode\":\"A001-03\",\n" +
//                "            \"Gender\":\"\",\n" +
//                "            \"tags\":[\n" +
//                "                {\n" +
//                "                    \"timeid\":1,\n" +
//                "                    \"type\":\"早餐前\",\n" +
//                "                    \"isCompleted\":1\n" +
//                "                },\n" +
//                "                {\n" +
//                "                    \"timeid\":2,\n" +
//                "                    \"type\":\"中餐（餐后）\",\n" +
//                "                    \"isCompleted\":1\n" +
//                "                }\n" +
//                "            ]\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"msg\":\"\"\n" +
//                "}";
//        Gson gson = new Gson();
//        DrugCode drugCode = gson.fromJson(json, DrugCode.class);
//        List<DrugElders> drugCodeData = drugCode.getData();
//        loadEldersData(drugCodeData);

        String token = SPUtils.find(CONTACT.TOKEN);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("UsageTime", mYear + "-" + mMonth + "-" + mDay);
        map.put("cType", String.valueOf(layoutType));
        if (layoutType == 1) {
            map.put("TimeId", String.valueOf(sId));
            map.put("TimeTypeId", String.valueOf(tId));
        }else {
            map.put("TimeId", "0");
            map.put("TimeTypeId","0");
        }
        map.put("sign", GetSign.GetSign(map, token));
        httpUtils.getRetrofitApi().getAllElderUsageDrugInfo("basic " + token, map).enqueue(new Callback<DrugCode>() {
            @Override
            public void onResponse(Call<DrugCode> call, Response<DrugCode> response) {
                unCompleteFragment.cancelRefresh();
                completedFragment.cancelRefresh();
                DrugCode body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    customDialogutils.cancelNetworkDialog("提交成功", true);
                    List<DrugElders> drugCodeData = body.getData();
                    loadEldersData(drugCodeData);
                } else if (body != null) {
                    customDialogutils.cancelNetworkDialog(body.getMsg(), false);
                    Toast.makeText(DispenseMainActivity.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                } else {
                    customDialogutils.cancelNetworkDialog("数据异常", false);
                    Toast.makeText(DispenseMainActivity.this, "数据异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DrugCode> call, Throwable t) {
                if(NetWorkUtils.isNetworkConnected(mContext)){
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                }else{
                    customDialogutils.cancelNetworkDialog("网络异常", false);
                }
                unCompleteFragment.cancelRefresh();
                completedFragment.cancelRefresh();
            }
        });
    }

    /**
     * 加载长者数据
     *
     * @param datas 长者资料列表
     */
    private void loadEldersData(List<DrugElders> datas) {
        if (datas == null || datas.size() == 0) {
            Toast.makeText(mContext, "暂无长者信息", Toast.LENGTH_SHORT).show();
        }
        classify_elder(datas);
    }



    /**
     * 将长者资料生成全拼音字段、首字母字段和每个文字的首字母字段，按首字母分组并排序
     * 生成mList数据，并向适配器发送显示数据，由适配器显示。
     *
     * @param datas
     */
    public void classify_elder(List<DrugElders> datas) {
        //生成长者的全拼音、首字母和拼音缩写
        initSpellWord(datas);
        //按拼音字母对所有长者资料进行排序
        Collections.sort(datas, new MyCollator());

        //已完成列表
        List<DrugElders> mCompleted = new ArrayList<>();
        List<String> characterListCompleted = new ArrayList<>();
        //未完成列表
        List<DrugElders> mCompleted_un = new ArrayList<>();
        List<String> characterListCompleted_un = new ArrayList<>();

        for (int i = 0; i < datas.size(); i++) {
            DrugElders drugElders = datas.get(i);
            List<TagsBean> tags = drugElders.getTags();
            //遍历所有的tag
//            if (typeId == -1) {
                for (int j = 0; j < tags.size(); j++) {
                    TagsBean tagsBean = tags.get(j);
                    int isCompleted = tagsBean.getIsCompleted();
                    if (isCompleted == 0) {
                        mCompleted_un.add(drugElders);
                        break;
                    }
                    if (j == tags.size() - 1) {
                        mCompleted.add(drugElders);
                    }
                }
//            } else {
//                for (int j = 0; j < tags.size(); j++) {
//                    TagsBean tagsBean = tags.get(j);
//                    int isCompleted = tagsBean.getIsCompleted();
//                    int timeid = tagsBean.getTimeid();
//                    if (timeid == typeId) {
//                        if (isCompleted == 0) {
//                            mCompleted_un.add(drugElders);
//                            break;
//                        } else {
//                            mCompleted.add(drugElders);
//                            break;
//                        }
//                    }
//                }
//            }
        }
        initElderData(mCompleted, completedFragment);
        initElderData(mCompleted_un, unCompleteFragment);
        if (layoutType == 3) {
            unCompleteFragment.filterKind(filterContent);
        }
    }

    private void initElderData(List<DrugElders> drugElderses, DispensedFragment fragment) {
        List<DrugElders> elderList = null;
        List<List<DrugElders>> mList = new ArrayList<>();
        List<String> characterList = new ArrayList<>();
        for (DrugElders elder : drugElderses) {//循环所有长者(之前应该进行排序)
            if (!characterList.contains(elder.getFirstLetter())) {//当前首字母未包含在列表中
                elderList = new ArrayList<>();  //创建新列表
                elderList.add(elder);
                characterList.add(elder.getFirstLetter());
                mList.add(elderList);
            } else { // 当首字母包含在列表中
                if (elderList != null) {
                    elderList.add(elder);
                }
            }
        }
        fragment.show(mList, characterList);
    }

    //生成长者的全拼音、首字母和拼音缩写
    private void initSpellWord(List<DrugElders> datas) {
        for (int i = 0; i < datas.size(); i++) {
            try {
                DrugElders person = datas.get(i);
                String name = person.getName();
                person.setAllLetter(myPinYinTool.getAllSpell(name));//全拼音
                person.setSell(myPinYinTool.getAllSell(name));//每个文字的首字母
                person.setFirstLetter(!person.getSell().isEmpty() ? person.getSell().substring(0, 1) : "#");//开头字母
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //下拉刷新回掉
    @Override
    public void refresh() {
        network();
    }

    /**
     * 排序规则
     */
    private class MyCollator implements Comparator<DrugElders> {
        @Override
        public int compare(DrugElders lhs, DrugElders rhs) {
            return String.valueOf(lhs.getFirstLetter()).compareTo(String.valueOf(rhs.getFirstLetter()));
        }
    }

    /**
     * 日历选择回调方法
     *
     * @param view
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        proc_dateChanged(monthOfYear + 1, dayOfMonth, year);
    }

    /**
     * 日期选择处理
     *
     * @param month 月
     * @param day   日
     */
    private void proc_dateChanged(int month, int day, int year) {
        Log.e("rxf", year + "//" + month + "//" + day);
        if ((month != mMonth) || (day != mDay) || (year != mYear)) {    //日期改变
            mMonth = month;
            mDay = day;
            mYear = year;
            initTime();
            txtSelectedtDate.setText(mMonth + "月" + mDay + "日");
            network();
        }
    }

    private void initTime() {
        Globars globars = Globars.newInstance();
        globars.setYear(mYear);
        globars.setMonth(mMonth);
        globars.setDay(mDay);
    }

    @Override
    public void onResume() {
        super.onResume();
        network();
        networkEatList();
        if (bottomFunIsShow){
            oneKeyDrug.performClick();
        }
    }

    /**
     * 获取配药发药大类列表
     */
    private void networkEatList() {
        TreeMap<String, String> map = new TreeMap<>();
        String token = SPUtils.find(CONTACT.TOKEN);
        map.put("sign", GetSign.GetSign(map, token));
        HttpUtils.getInstance().getRetrofitApi().getDrugUseTimeList("basic " + token, map).enqueue(
                new Callback<DrugTime>() {
                    @Override
                    public void onResponse(Call<DrugTime> call, Response<DrugTime> response) {
                        DrugTime body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            //获取配药发药大类列表
                            List<DrugDataBean> drugDataBeanList = body.getData();
                            //初始化一个过滤的列表
                            drugSelectLists.clear();
                            medicine.setEnabled(true);

                            for (int i = 0; i < drugDataBeanList.size(); i++) {
                                DrugDataBean drugDataBean = drugDataBeanList.get(i);
                                List<DrugDataBean.DragUsageTimesBean> drugDataBeanDragUsageTimes = drugDataBean.getDragUsageTimes();
                                for (int d = 0; d < drugDataBeanDragUsageTimes.size(); d++) {
                                    DrugDataBean.DragUsageTimesBean usageTimesBean = drugDataBeanDragUsageTimes.get(d);
                                    //过滤的列表增加元素
                                    drugSelectLists.add(new DrugSelectList(usageTimesBean.getTimeTypeId(), usageTimesBean.getTimeId(), usageTimesBean.getTimeName()));
                                }
                            }
                            //默认选中第一项
                            if (!drugSelectLists.isEmpty()) {
                                DrugSelectList drugSelectList = drugSelectLists.get(0);
                                filterContent = drugSelectList.getTimeName();
                                drugTime.setText(filterContent);
                                drugTimeTop.setText(filterContent);
                                sId = drugSelectList.getTimeId();
                                if (layoutType == 3) {
                                    unCompleteFragment.filterKind(filterContent);
                                }
                            }
                        } else {
                            medicine.setEnabled(false);
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<DrugTime> call, Throwable t) {
                        if(NetWorkUtils.isNetworkConnected(mContext)){
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                        medicine.setEnabled(false);
                    }
                });
    }
}
