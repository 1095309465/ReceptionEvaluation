package com.jhzy.receptionevaluation.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.CheckActivity;
import com.jhzy.receptionevaluation.ui.dispensingdrug.DrugNextActivity;

import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import java.util.ArrayList;


/**
 * bigyu  2017年2月20日11:36:26
 * 体检
 */
public class PhysicalExaminationFragment extends Fragment {


    private ViewHolder vh;
    private Context mContext;

    private ArrayList<String> arrayList;
    private RenElderInfo.DataBean data;

    //点击的类型
    private int type;

    public PhysicalExaminationFragment() {
    }

    public static PhysicalExaminationFragment newInstance(RenElderInfo.DataBean dataBean) {
        PhysicalExaminationFragment fragment = new PhysicalExaminationFragment();
        Bundle args = new Bundle();
        args.putSerializable("data",dataBean);
        fragment.setArguments(args);

        return fragment;
    }

    private void initUtils(View view) {
        vh = new ViewHolder(view);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_physical_examination, container, false);
        mContext = getContext();
        data = (RenElderInfo.DataBean) getArguments().getSerializable("data");
        initUtils(view);
        initViewData();
        initListener();
        return view;
    }


    private void initViewData() {
    }

    /**
     * 设置监听
     */
    private void initListener() {
        vh.xy.setOnClickListener(onClickListener);
        vh.mb.setOnClickListener(onClickListener);
        vh.tw.setOnClickListener(onClickListener);
        vh.hx.setOnClickListener(onClickListener);
        vh.kfxt.setOnClickListener(onClickListener);
        vh.chxt.setOnClickListener(onClickListener);
        vh.tz.setOnClickListener(onClickListener);
        vh.qt.setOnClickListener(onClickListener);
    }

    /**
     * 监听回调
     */
    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.xy: // 血压
                    type = 1;
                    break;
                case R.id.mb:// 脉搏
                    type = 2;
                    break;
                case R.id.tw: // 体温
                    type = 3;
                    break;
                case R.id.hx:// 呼吸
                    type = 4;
                    break;
                case R.id.kfxt:// 空腹血糖
                    type = 5;
                    break;
                case R.id.chxt:// 餐后血糖
                    type = 6;
                    break;
                case R.id.tz:// 体重
                    type = 7;
                    break;
                case R.id.qt:// 其他
                    type = -1;
                    Toast.makeText(mContext, "暂未开放", Toast.LENGTH_SHORT).show();
                    return;
            }
            jumpToNextActivity();
//            CourseRecordActivity.start(mContext, null);
        }
    };

    /**
     * tia
     */
    private void jumpToNextActivity() {
        if (type == -1) {
            return;
        }
        /*Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        NameListActivity.start(mContext, bundle);*/
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putSerializable("elder", data);
        CheckActivity.start(getActivity(), bundle);
    }

    class ViewHolder {
        private GridView gridview;
        private LinearLayout xy; // 血压
        private LinearLayout mb; // 脉搏
        private LinearLayout tw; // 体温
        private LinearLayout hx; // 呼吸
        private LinearLayout kfxt; // 空腹血糖
        private LinearLayout chxt; // 餐后血糖
        private LinearLayout tz; // 体重
        private LinearLayout qt; // 其他

        ViewHolder(View view) {
            gridview = ((GridView) view.findViewById(R.id.grid_view));

            xy = ((LinearLayout) view.findViewById(R.id.xy));
            mb = ((LinearLayout) view.findViewById(R.id.mb));
            tw = ((LinearLayout) view.findViewById(R.id.tw));
            hx = ((LinearLayout) view.findViewById(R.id.hx));
            kfxt = ((LinearLayout) view.findViewById(R.id.kfxt));
            chxt = ((LinearLayout) view.findViewById(R.id.chxt));
            tz = ((LinearLayout) view.findViewById(R.id.tz));
            qt = ((LinearLayout) view.findViewById(R.id.qt));
        }
    }

}
