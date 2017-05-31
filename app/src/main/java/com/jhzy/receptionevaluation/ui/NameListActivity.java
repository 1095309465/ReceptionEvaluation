package com.jhzy.receptionevaluation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.adapter.NameListAdapter;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.EldersInfoCode;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.PinYinUtil;
import com.jhzy.receptionevaluation.utils.PinyinTool;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.widget.MyTextDialog;
import com.jhzy.receptionevaluation.widget.MyView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 日常检查的老人列表界面
 */
public class NameListActivity extends AppCompatActivity {
    private ListView recycle;
    private EditText ed_researsh;
    private TextView tv_title;
    private ImageView iv_back;
    private ImageView iv_refresh;
    private List<Elder> persons;
    private PinyinTool tool;
    private NameListAdapter adapter;
    private MyTextDialog tv_dialog;
    private MyView view;
    private List<Elder> adapterList;


    private int type; // 当前检查的类型

    private String typeTxt; // 当前标题
    private View back;
    private CustomDialogutils customDialogutils;
    private HttpUtils httpUtils;
    private SwipeRefreshLayout swipeLayout;
    private ImageView clear;


    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, NameListActivity.class);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_list);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        initUtils();
        initView();
        initData();
        network();
    }

    /**
     * 网络请求老人的信息 列表
     */
    private void network() {
        customDialogutils.setResfreshDialog("获取老人信息");
        String token = SPUtils.find(CONTACT.TOKEN);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("sign", GetSign.GetSign(map, token));
        httpUtils.getRetrofitApi().getEldersInfo("basic " + token, map).enqueue(new Callback<EldersInfoCode>() {
            @Override
            public void onResponse(Call<EldersInfoCode> call, Response<EldersInfoCode> response) {
                swipeLayout.setRefreshing(false);
                EldersInfoCode body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    customDialogutils.dismissDialog();
                    loadEldersData(body.getData());
                } else {
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                }
            }

            @Override
            public void onFailure(Call<EldersInfoCode> call, Throwable t) {
                swipeLayout.setRefreshing(false);
                t.printStackTrace();
                if(NetWorkUtils.isNetworkConnected(NameListActivity.this)){
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                }else{
                    customDialogutils.cancelNetworkDialog("网络异常", false);
                }

            }
        });
    }

    private void loadEldersData(List<Elder> data) {
        persons.clear();
        persons.addAll(data);
        for (int i = 0; i < persons.size(); i++) {
            Elder person = persons.get(i);
            String name = person.getElderName();
            person.setAllLetter(PinYinUtil.getAllSpell(name, tool));//全拼音
            person.setFirstLetter(PinYinUtil.getFirstSell(name, tool));//开头字母
            person.setSell(PinYinUtil.getAllSell(name, tool));//每个文字的首字母
        }
        Collections.sort(persons, new MyCollator());
        adapterList.clear();
        adapterList.addAll(persons);

        showAndHideLetter(true);
    }

    private void initUtils() {
        customDialogutils = new CustomDialogutils(this);
        httpUtils = HttpUtils.getInstance();
        tool = new PinyinTool();
        persons = new ArrayList<Elder>();
        adapterList = new ArrayList<>();
    }

    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeLayout.setRefreshing(true);
            network();
        }
    };

    private void initView() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayoutDispense);
        //设置卷内的颜色
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(onRefreshListener);
        view = (MyView) findViewById(R.id.view);
        tv_dialog = (MyTextDialog) findViewById(R.id.tv_dialog);
        tv_dialog.setVisibility(View.GONE);
        recycle = (ListView) findViewById(R.id.recycle);
        ed_researsh = (EditText) findViewById(R.id.ed_researsh);
        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);
        clear = ((ImageView) findViewById(R.id.clear));
        clear.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                ed_researsh.getText().clear();
            }
        });
        view.setOnTouchLetterListener(new MyView.OnTouchLetterListener() {
            @Override
            public void onActionDownAndMove(String c, int perenct) {
                tv_dialog.setVisibility(View.VISIBLE);
                tv_dialog.setTextContent(c);
                Log.e("123", "选中的" + c);
                Log.e("123", "persons" + persons.toString());
                for (int i = 0; i < persons.size(); i++) {
                    String letter = persons.get(i).getFirstLetter();
                    if (letter.equals(c)) {
                        Log.e("123", "匹配的" + i);
                        recycle.setSelection(i);
                        break;

                    }
                    continue;
                }
            }

            @Override
            public void onActionUp() {
                tv_dialog.setVisibility(View.GONE);
            }
        });
        ed_researsh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                show(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }
        });
        recycle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    bundle.putParcelable("elder", adapterList.get(position));
                    CheckActivity.start(NameListActivity.this, bundle);
                } catch (Exception e) {

                }
            }
        });


        iv_back.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                hideKeyBoard(view);
                finish();
            }
        });
        adapter = new NameListAdapter(NameListActivity.this);
        recycle.setAdapter(adapter);

    }

    /**
     * 收起键盘
     *
     * @param v
     */
    private void hideKeyBoard(View v) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 展示
     *
     * @param s
     */
    public void show(String s) {
        Log.e("123", "s=" + s);
        String content = String.valueOf(s).toUpperCase();
        adapterList.clear();
        if (TextUtils.isEmpty(s)) {
            adapterList.addAll(persons);
            showAndHideLetter(true);
            return;
        }

        for (int i = 0; i < persons.size(); i++) {
            Elder person = persons.get(i);
            if (person.getElderName().contains(content)) {
                adapterList.add(person);
                continue;
            } else if (person.getAllLetter().contains(content)) {
                adapterList.add(person);
                continue;
            } else if (person.getSell().contains(content)) {
                adapterList.add(person);
                continue;
            }
        }

        adapter.setmList(adapterList, false);
        showAndHideLetter(false);
    }

    private void initData() {
        switch (type) {
            case 1: // 血压
                typeTxt = "血压";
                break;
            case 2:// 脉搏
                typeTxt = "脉搏";
                break;
            case 3: // 体温
                typeTxt = "体温";
                break;
            case 4:// 呼吸
                typeTxt = "呼吸";
                break;
            case 5:// 空腹血糖
                typeTxt = "空腹血糖";
                break;
            case 6:// 餐后血糖
                typeTxt = "餐后血糖";
                break;
            case 7:// 体重
                typeTxt = "体重";
                break;
            case 8:// 其他
                typeTxt = "其他";
                break;
        }
        tv_title.setText(typeTxt + "检查");
    }


    /**
     * 老人排序
     */
    public class MyCollator implements Comparator<Elder> {
        @Override
        public int compare(Elder lhs, Elder rhs) {
            return String.valueOf(lhs.getFirstLetter()).compareTo(String.valueOf(rhs.getFirstLetter()));
        }//排序规则
    }

    public void showAndHideLetter(boolean isShowLetter) {
        int size = adapterList.size();
        if (size == 0) {
            return;
        }
        String letter = adapterList.get(0).getFirstLetter();
        adapterList.get(0).setShowLetter(true);
        for (int i = 1; i < size; i++) {
            String nextLetter = adapterList.get(i).getFirstLetter();
            if (letter.equals(nextLetter)) {
                adapterList.get(i).setShowLetter(false);
            } else {
                adapterList.get(i).setShowLetter(true);
                letter = nextLetter;
            }
        }
        adapter.setmList(adapterList, isShowLetter);

//        adapter.notifyDataSetChanged();
    }
}
