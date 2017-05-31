package com.jhzy.receptionevaluation.ui.dispensingdrug;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.Toast;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.DispenseAdapter;
import com.jhzy.receptionevaluation.ui.bean.drugtime.DrugDataBean;
import com.jhzy.receptionevaluation.ui.bean.drugtime.DrugTime;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.Globars;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import java.util.List;
import java.util.TreeMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 配药界面
 */
public class DispenseActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recyclerviewArranges;  //列表控件
    private DispenseAdapter dispenseAdapter;    //列表适配器
    private ImageView imBack;   //回退图标按钮
    private int type;
    private List<DrugDataBean> data;
    private ProgressBar progress;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public int getContentView() {
        return (R.layout.activity_dispense);
    }


    @Override
    public void init() {
        Bundle extras = getIntent().getExtras();
        type = extras.getInt(CONTACT.DispenseDrugContact.TYPE, 0);
        imBack = (ImageView) findViewById(R.id.imDispenseback);
        progress = ((ProgressBar) findViewById(R.id.dis_progress));
        imBack.setOnClickListener(this);
        recyclerviewArranges = (RecyclerView) findViewById(R.id.recyclerviewArranges);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DispenseActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerviewArranges.setLayoutManager(linearLayoutManager);
        dispenseAdapter = new DispenseAdapter(DispenseActivity.this);
        dispenseAdapter.setOnItemClick(new DispenseAdapter.OnItemClickListener() {
            @Override
            public void onItemIconClick(View view, int position) {
                Globars.newInstance().setTid(data.get(position).getTimeTypeId());
                /*switch (position) {
                    case 0:    //餐前药
                        startActivity(
                            new Intent(DispenseActivity.this, DispenseMainActivity.class));
                        break;
                    default:
                        break;
                }*/
                Bundle bundle = new Bundle();
                bundle.putInt(CONTACT.DispenseDrugContact.TYPE, type);
                bundle.putInt(CONTACT.DispenseDrugContact.TYPE_2,data.get(position).getTimeTypeId());
                bundle.putString(CONTACT.DispenseDrugContact.TYPE_NAME_2,data.get(position).getTimeTypeName());
                Intent intent = new Intent(mContext,DispenseMainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerviewArranges.setAdapter(dispenseAdapter);
        loadData();
    }


    private void loadData() {
        progress.setVisibility(View.VISIBLE);
        TreeMap<String, String> map = new TreeMap<>();
        String token = SPUtils.find(CONTACT.TOKEN);
        map.put("sign", GetSign.GetSign(map, token));
        HttpUtils.getInstance().getRetrofitApi().getDrugUseTimeList("basic " + token, map).enqueue(
            new Callback<DrugTime>() {
                @Override public void onResponse(Call<DrugTime> call, Response<DrugTime> response) {
                    progress.setVisibility(View.GONE);
                    DrugTime body = response.body();
                    if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                        data = body.getData();
                        dispenseAdapter.setDatas(data);
                    }else{
                        Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override public void onFailure(Call<DrugTime> call, Throwable t) {
                    progress.setVisibility(View.GONE);
                    if(NetWorkUtils.isNetworkConnected(mContext)){
                        Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imDispenseback:   //回退按钮
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
