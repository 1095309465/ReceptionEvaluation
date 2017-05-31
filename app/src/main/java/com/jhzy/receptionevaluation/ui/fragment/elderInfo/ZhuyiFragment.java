package com.jhzy.receptionevaluation.ui.fragment.elderInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.assess.ZhuyiActivity;
import com.jhzy.receptionevaluation.ui.bean.assess.Zhuyi;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

//注意事项
public class ZhuyiFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int id = -1;
    private String mParam2;
    private View eat;
    private View medicine;
    private View other;
    private TextView eatTitle;
    private TextView medicineTitle;
    private TextView otherTitle;
    private ProgressBar progressBar;
    private View main;
    private TextView eatDetail;
    private TextView medicineDetail;
    private TextView otherDetail;
    private TextView ok;


    public ZhuyiFragment() {
        // Required empty public constructor
    }


    public static ZhuyiFragment newInstance(int param1, String param2) {
        ZhuyiFragment fragment = new ZhuyiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_zhuyi, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        eat = view.findViewById(R.id.zhuyi_eat);
        medicine = view.findViewById(R.id.zhuyi_medicine);
        other = view.findViewById(R.id.zhuyi_other);
        main = view.findViewById(R.id.zhuyi_main);
        progressBar = ((ProgressBar) view.findViewById(R.id.zhuyi_progress));
        ok = ((TextView) view.findViewById(R.id.zhuyi_ok));
        ok.setOnClickListener(new OnClickListenerNoDouble() {
            @Override public void myOnClick(View view) {
                Intent intent = new Intent(getActivity(), ZhuyiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ZhuyiActivity.FOOD,eatDetail.getText().toString());
                bundle.putString(ZhuyiActivity.MEDICINE,medicineDetail.getText().toString());
                bundle.putString(ZhuyiActivity.OTHER,otherDetail.getText().toString());
                bundle.putString(ZhuyiActivity.ID,id+"");
                intent.putExtras(bundle);
                getActivity().startActivityForResult(intent,22);
            }
        });
        eatTitle = ((TextView) eat.findViewById(R.id.zhuyi_title));
        eatDetail = ((TextView) eat.findViewById(R.id.zhuyi_detail));
        medicineTitle = ((TextView) medicine.findViewById(R.id.zhuyi_title));
        medicineDetail = ((TextView) medicine.findViewById(R.id.zhuyi_detail));
        otherTitle = ((TextView) other.findViewById(R.id.zhuyi_title));
        otherDetail = ((TextView) other.findViewById(R.id.zhuyi_detail));
        medicineTitle.setText("药品注意信息");
        otherTitle.setText("其他信息");
        if (id != -1) {
            loadData();
        }
    }


    public void loadData() {
        HttpUtils.getInstance().getRetrofitApi().getZhuYi("basic " + SPUtils.find(CONTACT.TOKEN), id + "").enqueue(
                new Callback<Zhuyi>() {
                    @Override
                    public void onResponse(Call<Zhuyi> call, Response<Zhuyi> response) {
                        progressBar.setVisibility(View.GONE);
                        Zhuyi body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            Zhuyi.DataBean dataBean = body.getData().get(0);
                            main.setVisibility(View.VISIBLE);
                            String foodHabit = dataBean.getFoodHabit();
                            if (TextUtils.isEmpty(foodHabit)) {
                                foodHabit = "暂无信息";
                            }
                            eatDetail.setText(foodHabit);
                            String medicineNote = dataBean.getMedicineNote();
                            if (TextUtils.isEmpty(medicineNote)) {
                                medicineNote = "暂无信息";
                            }
                            medicineDetail.setText(medicineNote);
                            String attention = dataBean.getAttention();
                            if (TextUtils.isEmpty(attention)) {
                                attention = "暂无信息";
                            }
                            otherDetail.setText(attention);
                        } else {
                            Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Zhuyi> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        if(NetWorkUtils.isNetworkConnected(getActivity())){
                            Toast.makeText(getActivity(), "服务器异常", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
