package com.jhzy.receptionevaluation.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.adapter.EvaluationGridViewAdapter;
import com.jhzy.receptionevaluation.ui.adapter.EvaluationInfoAdapter;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.EldersInfoCode;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.MyPinYinTool;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
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
 * Created by sxmd on 2017/3/1.
 */

public class AssessmentInfoFragment extends Fragment {
    private ListView lv;
    private EditText ed_researsh;
    private ImageView iv_back;
//    private PinyinTool tool;
    private MyView view;
    private MyTextDialog textDialog;

    private List<Elder> persons;
    private List<Elder> adapterList;

    private List<List<Elder>> mList;

    private EvaluationInfoAdapter infoAdapter;

    private GridView lv_easy;
    private EvaluationGridViewAdapter gridAdapter;
    private HttpUtils httpUtils;
    private CustomDialogutils customDialogutils;
    private SwipeRefreshLayout swipeLayout;
    private ImageView clear;
    private MyPinYinTool myPinYinTool;


    public static AssessmentInfoFragment newInstance(String param1, String param2) {
        AssessmentInfoFragment fragment = new AssessmentInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.activity_evaluation_info, null);
            initUtils(contentView);
            initView(contentView);
        } else {
            ViewParent parent = contentView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeAllViews();
            }
        }
        return contentView;
    }

    private void initUtils(View view) {
        httpUtils = HttpUtils.getInstance();
        customDialogutils = new CustomDialogutils(getContext());
//        tool = new PinyinTool();
        persons = new ArrayList<>();
        mList = new ArrayList<>();
        adapterList = new ArrayList<>();
        myPinYinTool = new MyPinYinTool();
    }

    /**
     * 网络请求老人的信息 列表
     */
    private void network() {
        String token = SPUtils.find(CONTACT.TOKEN);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("sign", GetSign.GetSign(map, token));
        httpUtils.getRetrofitApi().getEldersInfo("basic " + token, map).enqueue(new Callback<EldersInfoCode>() {
            @Override
            public void onResponse(Call<EldersInfoCode> call, Response<EldersInfoCode> response) {
                swipeLayout.setRefreshing(false);
                EldersInfoCode body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    loadEldersData(body.getData());
                } else {
                    Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EldersInfoCode> call, Throwable t) {
                if(NetWorkUtils.isNetworkConnected(getActivity())){
                    Toast.makeText(getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "网络异常", Toast.LENGTH_SHORT).show();
                }
                swipeLayout.setRefreshing(false);
            }
        });
    }

    /**
     * 加载网络老人数据
     *
     * @param data
     */
    private void loadEldersData(List<Elder> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        int size = persons.size();
        if (size != 0) {
            persons.clear();
        }
        mList.clear();
        //把所有的老人添加到本地的集合里面
        persons.addAll(data);
        for (int i = 0; i < persons.size(); i++) {
            try {
                Elder person = persons.get(i);
                String name = person.getElderName();
//                person.setAllLetter(PinYinUtil.getAllSpell(name, tool));//全拼音
//                person.setFirstLetter(PinYinUtil.getFirstSell(name, tool));//开头字母
//                person.setSell(PinYinUtil.getAllSell(name, tool));//每个文字的首字母

                person.setAllLetter(myPinYinTool.getAllSpell(name));//全拼音
                person.setSell(myPinYinTool.getAllSell(name));//每个文字的首字母
                person.setFirstLetter(!person.getSell().isEmpty()?person.getSell().substring(0,1):"#");//开头字母
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(persons, new MyCollator());//对数据源进行排序
        adapterList.clear();
        adapterList.addAll(persons);
        gridAdapter.setLetter(adapterList);


        String letter = persons.get(0).getFirstLetter();
        List<String> letterList = new ArrayList<>();
        letterList.add(letter);
        for (int i = 1; i < persons.size(); i++) {//对数据源进行字母分组
            try {
                Elder person = persons.get(i);
                String nextLetter = person.getFirstLetter();
                if (letter.equals(nextLetter)) {
                } else {
                    letterList.add(nextLetter);
                    letter = nextLetter;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < letterList.size(); i++) {
            try {
                String str = letterList.get(i);
                List<Elder> pList = new ArrayList<>();
                for (int j = 0; j < persons.size(); j++) {
                    Elder person = persons.get(j);
                    String str2 = person.getFirstLetter();
                    if (str.equals(str2)) {
                        pList.add(person);
                    }
                }
                mList.add(pList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        infoAdapter.setMlist(mList);
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

    private void initView(View contentView) {
        swipeLayout = (SwipeRefreshLayout) contentView.findViewById(R.id.swipeLayoutDispense);
        //设置卷内的颜色
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeLayout.setOnRefreshListener(onRefreshListener);
        lv_easy = (GridView) contentView.findViewById(R.id.lv_easy);
        view = (MyView) contentView.findViewById(R.id.view);
        textDialog = (MyTextDialog) contentView.findViewById(R.id.tv_dialog);
        lv = (ListView) contentView.findViewById(R.id.lv);
        ed_researsh = (EditText) contentView.findViewById(R.id.ed_researsh);
        iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
        view = (MyView) contentView.findViewById(R.id.view);
        clear = ((ImageView) contentView.findViewById(R.id.iv_clear));
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
        view.setOnTouchLetterListener(new MyView.OnTouchLetterListener() {
            @Override
            public void onActionDownAndMove(String c, int percent) {
                lv_easy.setVisibility(View.GONE);
                textDialog.setVisibility(View.VISIBLE);
                textDialog.setTextContent(c);
                textDialog.setDotPoints(percent);

                for (int i = 0; i < mList.size(); i++) {
                    String letter = mList.get(i).get(0).getFirstLetter();
                    if (c.equals(letter)) {
                        lv.setSelection(i);
                    }
                }
            }

            @Override
            public void onActionUp() {
                textDialog.setVisibility(View.GONE);

            }
        });

        gridAdapter = new EvaluationGridViewAdapter(getContext());
        lv_easy.setAdapter(gridAdapter);

        infoAdapter = new EvaluationInfoAdapter(getActivity().getApplicationContext());
        lv.setAdapter(infoAdapter);

        clear.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                ed_researsh.getText().clear();
            }
        });
    }

    public void show(String content) {
        content = String.valueOf(content).toUpperCase();
        if (TextUtils.isEmpty(content)) {
            lv_easy.setVisibility(View.GONE);
            return;
        }
        lv_easy.setVisibility(View.VISIBLE);
        adapterList.clear();
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
        gridAdapter.setLetter(adapterList);
    }


    public class MyCollator implements Comparator<Elder> {
        @Override
        public int compare(Elder lhs, Elder rhs) {
            return String.valueOf(lhs.getFirstLetter()).compareTo(String.valueOf(rhs.getFirstLetter()));
        }//排序规则
    }


    @Override
    public void onResume() {
        super.onResume();
        network();
    }
}
