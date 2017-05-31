package com.jhzy.receptionevaluation.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.RVDispenseAdapter;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.utils.PinYinUtil;
import com.jhzy.receptionevaluation.utils.PinyinTool;
import com.jhzy.receptionevaluation.widget.MyView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@Deprecated
public class UnDispensedFragment extends Fragment {
    public List<Elder> persons = new ArrayList<>(); //读取的长者资料列表
    public List<List<Elder>> mList = new ArrayList<>(); //按字母分组的长者资料
    public List<Elder> adapterList;     //筛选后的长者资料列表
    private PinyinTool tool;
    private GridLayoutManager layoutManager;

    //界面控件
    private RecyclerView recyclViewDispense;
    private MyView sidebarDispense;
    private RVDispenseAdapter rvDispenseAdapter;    //
    private TextView txtDialog;


    public UnDispensedFragment() {
        // Required empty public constructor
        tool = new PinyinTool();
    }

    public static UnDispensedFragment newInstance(String param1, String param2) {
        UnDispensedFragment fragment = new UnDispensedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_un_dispensed, container, false);
        sidebarDispense = (MyView) view.findViewById(R.id.sidebarDispense);
        recyclViewDispense = (RecyclerView)view.findViewById(R.id.recyclViewDispense);
        txtDialog = (TextView)view.findViewById(R.id.txtDialog);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        rvDispenseAdapter = new RVDispenseAdapter(getActivity(),dm.widthPixels/4);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        recyclViewDispense.setLayoutManager(layoutManager);
        recyclViewDispense.setAdapter(rvDispenseAdapter);
        sidebarDispense.setOnTouchLetterListener(new MyView.OnTouchLetterListener() {
            @Override
            public void onActionDownAndMove(String c, int percent) {
                txtDialog.setVisibility(View.VISIBLE);
                txtDialog.setText(c);
                int position = rvDispenseAdapter.getScrollPosition(c);
                layoutManager.scrollToPositionWithOffset(position,0);
            }

            @Override
            public void onActionUp() {
                txtDialog.setVisibility(View.GONE);

            }
        });
    }

    public void showhideRecycleView(boolean bool){
        if(bool)
            recyclViewDispense.setVisibility(View.VISIBLE);
        else
            recyclViewDispense.setVisibility(View.GONE);
    }

    /**
     * 将长者资料按字母顺序进行分组，结果存入mList中
     * @param datas 长者资料列表
     */
    public void classifyState(List<Elder> datas){
        for (int i = 0; i < datas.size(); i++) {
            try {
                Elder person = datas.get(i);
                String name = person.getElderName();
                person.setAllLetter(PinYinUtil.getAllSpell(name, tool));//全拼音
                person.setFirstLetter(PinYinUtil.getFirstSell(name, tool));//开头字母
                person.setSell(PinYinUtil.getAllSell(name, tool));//每个文字的首字母
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collections.sort(datas, new MyCollator());//对数据源进行排序
        rvDispenseAdapter.classify_elder(datas);
    }

    public void filterElder(String content){
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
        //fragment
        //gridAdapter.setLetter(adapterList);

    }
    public void setAdapter(List<Elder> adapterList){
        //rvDispenseAdapter
    }

    private class MyCollator implements Comparator<Elder> {
        @Override
        public int compare(Elder lhs, Elder rhs) {
            return String.valueOf(lhs.getFirstLetter()).compareTo(String.valueOf(rhs.getFirstLetter()));
        }//排序规则
    }
}
