package com.jhzy.receptionevaluation.ui.dispensingdrug;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugElders;
import com.jhzy.receptionevaluation.ui.bean.drugnext.Insulin;
import com.jhzy.receptionevaluation.ui.bean.drugnext.InsulinDetail;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 胰岛素界面
 */
public class InsulinActivity extends BaseActivity {

    public static final String STATETAG = "complete";
    public static final String ELDERTAG = "elder";


    private Holder holder;
    private int currentId = 0;
    private View currentView;
    private GradientDrawable gradientDrawable;
    private Insulin.DataBean insulinData;
    private DrugElders drugElders;

    private String sex;
    private String age;
    private String bedCode;
    private String token;

    private InsulinDetail.DataBean data;


    @Override public int getContentView() {
        return R.layout.activity_insulin;
    }


    @Override public void init() {
        initParams();
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        holder = new Holder();
        initData();
    }

    private void initParams() {
        insulinData = (Insulin.DataBean)getIntent().getSerializableExtra(STATETAG);
        drugElders = getIntent().getParcelableExtra(ELDERTAG);
    }


    private void initData() {
        token = SPUtils.find(CONTACT.TOKEN);
        holder.name.setText(drugElders.getName());
        if(TextUtils.isEmpty(drugElders.getGender())){
            sex = "暂无数据";
        }else{
            sex = drugElders.getGender();
        }
        if(TextUtils.isEmpty(drugElders.getAge()+"")){
            age = "暂无数据";
        }else{
            age = drugElders.getAge()+"";
        }
        if(TextUtils.isEmpty(drugElders.getBedCode())){
            bedCode = "暂无数据";
        }else{
            bedCode = drugElders.getBedCode();
        }
        holder.message.setText(sex + " | " + age + " | " + bedCode);
        ImageLoaderUtils.load(holder.photo,drugElders.getPhotoUrl());
        holder.unit.setText(insulinData.getDosage() + insulinData.getDoseUnit());
//        if(data.getCurrentAmount() <= data.getMinimum()){
//            holder.noInsulinView.setVisibility(View.VISIBLE);
//        }
        if(insulinData.getIsCompleted() == 0){//未注射
            holder.inject.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            if (insulinData.getStock() <= insulinData.getInventoryAlarm()){
                holder.noInsulinView.setVisibility(View.VISIBLE);
            }else {
                holder.noInsulinView.setVisibility(View.GONE);
            }
        }else{//已注射
            holder.timeView.setVisibility(View.VISIBLE);
            holder.timeDiv.setVisibility(View.VISIBLE);
            holder.time.setText(insulinData.getInjectionTime());
            loadInsulinDetail();
            holder.in_1.setEnabled(false);
            holder.in_2.setEnabled(false);
            holder.in_3.setEnabled(false);
            holder.in_4.setEnabled(false);
            holder.in_5.setEnabled(false);
            holder.in_6.setEnabled(false);
        }
    }


    private void loadInsulinDetail() {
        holder.progress.setMessage("加载数据中...");
        holder.progress.show();
        TreeMap<String,String> map = new TreeMap<>();
        map.put("RecordId",insulinData.getRecordId()+"");
        map.put("sign", GetSign.GetSign(map,token));
        HttpUtils.getInstance().getRetrofitApi().getInsulinDetail("basic "+ token,map).enqueue(
            new Callback<InsulinDetail>() {

                @Override
                public void onResponse(Call<InsulinDetail> call, Response<InsulinDetail> response) {
                    holder.progress.dismiss();
                    InsulinDetail body = response.body();
                    if(body != null && CONTACT.DataSuccess.equals(body.getCode())){
                        data = body.getData();
                        holder.amount.setText(data.getInjectionDosage()+"");
                        clickPart(data.getParts());
                    }else{
                        Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override public void onFailure(Call<InsulinDetail> call, Throwable t) {
                    holder.progress.dismiss();
                    if(NetWorkUtils.isNetworkConnected(mContext)){
                        Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override public void onClick(View view) {
            switch (view.getId()){
                case R.id.insulin_back:
                    finish();
                    break;
                case R.id.insulin_1:
                    clickPart(1);
                    break;
                case R.id.insulin_2:
                    clickPart(2);
                    break;
                case R.id.insulin_3:
                    clickPart(3);
                    break;
                case R.id.insulin_4:
                    clickPart(4);
                    break;
                case R.id.insulin_5:
                    clickPart(5);
                    break;
                case R.id.insulin_6:
                    clickPart(6);
                    break;
                case R.id.insulin_inject:
                    inject();
                    break;
            }
        }
    };


    /**
     * 提交注射信息
     */
    private void inject() {
        if(currentId == 0){
            Toast.makeText(mContext, "请选择注射部位", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            if(TextUtils.isEmpty(holder.edit.getText().toString()) || Double.parseDouble(holder.edit.getText().toString()) <=0){
                Toast.makeText(mContext, "请输入正确的剂量", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(mContext, "请输入正确的剂量", Toast.LENGTH_SHORT).show();
        }
        holder.progress.setMessage("提交注射信息中...");
        holder.progress.show();
        TreeMap<String,String> map = new TreeMap<>();
        map.put("ElderID",drugElders.getElderId()+"");
        map.put("DrugID",insulinData.getDrugId()+"");
        map.put("TimeName",insulinData.getTimeName());
        map.put("TimeID",insulinData.getTimeId()+"");
        map.put("injectionTime",getTime());
        map.put("Dose",holder.edit.getText().toString());
        map.put("parts",currentId+"");
        map.put("sign",GetSign.GetSign(map,token));
        HttpUtils.getInstance().getRetrofitApi().inject("basic " + token,map).enqueue(
            new Callback<Code>() {
                @Override public void onResponse(Call<Code> call, Response<Code> response) {
                    holder.progress.dismiss();
                    Code body = response.body();
                    if(body != null && CONTACT.DataSuccess.equals(body.getCode())){
                        Toast.makeText(mContext, "注射成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override public void onFailure(Call<Code> call, Throwable t) {
                    holder.progress.dismiss();
                    if(NetWorkUtils.isNetworkConnected(mContext)){
                        Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private String getTime(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }


    private void clickPart(int i){
        if(i != currentId){
            View view = null;
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.OVAL);
            gradientDrawable.setColor(Color.parseColor("#ffa626"));
            switch (i){
                case 1:
                    view = holder.in_1;
                    break;
                case 2:
                    view = holder.in_2;
                    break;
                case 3:
                    view = holder.in_3;
                    break;
                case 4:
                    view = holder.in_4;
                    break;
                case 5:
                    view = holder.in_5;
                    break;
                case 6:
                    view = holder.in_6;
                    break;
            }
            view.setBackgroundDrawable(gradientDrawable);
            if(currentView != null){
                GradientDrawable gradientDrawable1 = new GradientDrawable();
                gradientDrawable1.setShape(GradientDrawable.OVAL);
                gradientDrawable1.setColor(Color.parseColor("#52bf3a"));
                currentView.setBackgroundDrawable(gradientDrawable1);
            }
            currentId = i;
            currentView = view;
        }
    }

    class Holder{
        private  ImageView back;
        private  SimpleDraweeView photo;
        private  TextView name;
        private  TextView message;
        private  TextView unit;
        private  TextView time;
        private  TextView amount;
        private  EditText edit;
        private  TextView in_1,in_2,in_3,in_4,in_5,in_6;
        private  TextView inject;
        private  View timeView;
        private  View timeDiv;
        private  TextView noInsulinView;
        private ProgressDialog progress;


        public Holder() {
            back = ((ImageView) findViewById(R.id.insulin_back));
            photo = ((SimpleDraweeView) findViewById(R.id.insulin_photo));
            name = ((TextView) findViewById(R.id.insulin_name));
            message = ((TextView) findViewById(R.id.insulin_message));
            unit = ((TextView) findViewById(R.id.insulin_unit));
            time = ((TextView) findViewById(R.id.insulin_time));
            amount = ((TextView) findViewById(R.id.insulin_amount));
            inject = ((TextView) findViewById(R.id.insulin_inject));
            edit = ((EditText) findViewById(R.id.insulin_edit));
            timeView = findViewById(R.id.insulin_time_view);
            timeDiv = findViewById(R.id.insulin_time_div);
            in_1 = ((TextView) findViewById(R.id.insulin_1));
            in_2 = ((TextView) findViewById(R.id.insulin_2));
            in_3 = ((TextView) findViewById(R.id.insulin_3));
            in_4 = ((TextView) findViewById(R.id.insulin_4));
            in_5 = ((TextView) findViewById(R.id.insulin_5));
            in_6 = ((TextView) findViewById(R.id.insulin_6));
            noInsulinView = ((TextView) findViewById(R.id.insulin_noinsulin));

            back.setOnClickListener(onClickListener);
            inject.setOnClickListener(onClickListener);
            in_1.setOnClickListener(onClickListener);
            in_2.setOnClickListener(onClickListener);
            in_3.setOnClickListener(onClickListener);
            in_4.setOnClickListener(onClickListener);
            in_5.setOnClickListener(onClickListener);
            in_6.setOnClickListener(onClickListener);

            progress = new ProgressDialog(mContext);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
        }
    }
}
