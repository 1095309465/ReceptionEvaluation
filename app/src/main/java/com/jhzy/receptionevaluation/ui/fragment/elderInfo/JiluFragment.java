package com.jhzy.receptionevaluation.ui.fragment.elderInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.CourseRecordActivity;
import com.jhzy.receptionevaluation.ui.adapter.JiLuAdapter;
import com.jhzy.receptionevaluation.ui.bean.CourseRecordBean;
import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 病程记录
 */
public class JiluFragment extends Fragment {
    private static final String ARG_PARAM1 = "RenElderInfo.DrugElders";
    private static final String ARG_PARAM2 = "CourseRecordBean.DrugElders";

    private String name;
    private String elderId;
    private RecyclerView history;
    private ImageView add;
    private JiLuAdapter jiLuAdapter;
    private List<CourseRecordBean.DataBean> infoJiluList;
    private RenElderInfo.DataBean bean;
    private int requestCode = 2000;
    private TextView noJilu;


    public JiluFragment() {
        // Required empty public constructor
    }

    public static JiluFragment newInstance(RenElderInfo.DataBean bean) {
        JiluFragment fragment = new JiluFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, bean);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bean = (RenElderInfo.DataBean) getArguments().getSerializable(ARG_PARAM1);
            name = bean.getElderName();
            elderId = bean.getID() + "";
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_jilu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        history = ((RecyclerView) view.findViewById(R.id.jilu_history));
        history.setLayoutManager(new LinearLayoutManager(getActivity()));
        add = ((ImageView) view.findViewById(R.id.jilu_new));
//        新建病程按钮 bigyu
        add.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                Intent intent = new Intent(getActivity(), CourseRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ARG_PARAM1, bean);
                bundle.putBoolean("newAssess", true);
                intent.putExtras(bundle);
                startActivityForResult(intent, requestCode);
                ((Activity) getContext()).overridePendingTransition(R.anim.activity_in, 0);
            }
        });
        infoJiluList = new ArrayList<>();
        jiLuAdapter = new JiLuAdapter(infoJiluList);
        history.setAdapter(jiLuAdapter);
        jiLuAdapter.setOnItemClickListener(new JiLuAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), CourseRecordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ARG_PARAM1, bean);
                bundle.putSerializable(ARG_PARAM2, infoJiluList.get(position));
                intent.putExtras(bundle);
                startActivityForResult(intent, requestCode);
            }
        });
        noJilu = ((TextView) view.findViewById(R.id.no_jilu));
        netWork();
    }

    /**
     * 网络请求
     */
    public void netWork() {//getCourseRecord
        HttpUtils.getInstance().getRetrofitApi().getCourseRecord("basic " + SPUtils.find(CONTACT.TOKEN), elderId).enqueue(new Callback<CourseRecordBean>() {
            @Override
            public void onResponse(Call<CourseRecordBean> call, Response<CourseRecordBean> response) {
                CourseRecordBean body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    List<CourseRecordBean.DataBean> dataBean = body.getData();
                    infoJiluList.clear();
                    infoJiluList.addAll(dataBean);
                    jiLuAdapter.notifyDataSetChanged();
                    if(dataBean.size() == 0){
                        noJilu.setVisibility(View.VISIBLE);
                    }else{
                        noJilu.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "数据错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CourseRecordBean> call, Throwable t) {
                if(NetWorkUtils.isNetworkConnected(getActivity())){
                    Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
