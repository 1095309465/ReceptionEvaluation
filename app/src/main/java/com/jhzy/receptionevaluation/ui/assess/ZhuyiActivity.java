package com.jhzy.receptionevaluation.ui.assess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import java.util.TreeMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZhuyiActivity extends BaseActivity {
    private String temp = "暂无记录";
    public static final String FOOD = "food";
    public static final String MEDICINE = "medicine";
    public static final String OTHER = "other";
    public static final String ID = "id";

    private Holder holder;
    private String food;
    private String medicine;
    private String other;
    private String id;

    private CustomDialogutils customDialogutils;
    private boolean isChanged = false;


    @Override public int getContentView() {
        return R.layout.activity_zhuyi;
    }


    @Override public void init() {
        initPara();
        initView();
        customDialogutils = new CustomDialogutils(this);
    }


    private void initPara() {
        Bundle extras = getIntent().getExtras();
        food = extras.getString(FOOD,temp);
        medicine = extras.getString(MEDICINE,temp);
        other = extras.getString(OTHER,temp);
        id = extras.getString(ID,"0");
    }


    private void initView() {
        holder = new Holder();
        holder.foodEdit.setText(food);
        holder.medicineEdit.setText(medicine);
        holder.otherEdit.setText(other);
    }

    OnClickListenerNoDouble onClickListenerNoDouble = new OnClickListenerNoDouble() {
        @Override public void myOnClick(View view) {
            switch (view.getId()){
                case R.id.zhuyi_edit_back:
                    backEvent();
                    break;
                case R.id.zhuyi_edit_ok:
                    submit();
                    break;
            }
        }
    };


    /**
     * 提交
     */
    private void submit() {
        customDialogutils.setResfreshDialog("正在加载",5000);
        TreeMap<String,String> map = new TreeMap<>();
        map.put("ELderID",id);
        map.put("FoodHabit",holder.foodEdit.getText().toString().trim());
        map.put("MedicineNote",holder.medicineEdit.getText().toString().trim());
        map.put("Attention",holder.otherEdit.getText().toString().trim());
        HttpUtils.getInstance().getRetrofitApi().submitAttention("basic " + SPUtils.find(CONTACT.TOKEN),map).enqueue(
            new Callback<Code>() {
                @Override public void onResponse(Call<Code> call, Response<Code> response) {
                    Code body = response.body();
                    if(body != null && CONTACT.DataSuccess.equals(body.getCode())){
                        customDialogutils.cancelNetworkDialog("修改成功", true);
                        isChanged = true;
                    }else{
                        customDialogutils.cancelNetworkDialog("服务器异常", false);
                    }
                }


                @Override public void onFailure(Call<Code> call, Throwable t) {
                    if(NetWorkUtils.isNetworkConnected(mContext)){
                        customDialogutils.cancelNetworkDialog("服务器异常", false);
                    }else{
                        customDialogutils.cancelNetworkDialog("网络异常", false);
                    }
                }
            });
    }


    @Override public void onBackPressed() {
        backEvent();
    }

    private void backEvent(){
        Intent intent = new Intent();
        if (isChanged) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }


    private class Holder{
        private final ImageView back;
        private final TextView foodTitle;
        private final EditText foodEdit;
        private final TextView medicineTitle;
        private final EditText medicineEdit;
        private final TextView otherTitle;
        private final EditText otherEdit;
        private final TextView ok;


        public Holder() {
            back = ((ImageView) findViewById(R.id.zhuyi_edit_back));

            View foodView = findViewById(R.id.zhuyi_edit_food);
            foodTitle = ((TextView) foodView.findViewById(R.id.zhuyi_title));
            foodEdit = ((EditText) foodView.findViewById(R.id.zhuyi_detail));

            View medicineView = findViewById(R.id.zhuyi_edit_medicine);
            medicineTitle = ((TextView) medicineView.findViewById(R.id.zhuyi_title));
            medicineTitle.setText("药品注意信息");
            medicineEdit = ((EditText) medicineView.findViewById(R.id.zhuyi_detail));

            View otherView = findViewById(R.id.zhuyi_edit_other);
            otherTitle = ((TextView) otherView.findViewById(R.id.zhuyi_title));
            otherTitle.setText("其他信息");
            otherEdit = ((EditText) otherView.findViewById(R.id.zhuyi_detail));

            ok = ((TextView) findViewById(R.id.zhuyi_edit_ok));
            back.setOnClickListener(onClickListenerNoDouble);
            ok.setOnClickListener(onClickListenerNoDouble);
        }
    }
}
