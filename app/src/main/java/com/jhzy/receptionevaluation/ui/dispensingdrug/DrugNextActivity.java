package com.jhzy.receptionevaluation.ui.dispensingdrug;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.DrugNextAdapter;
import com.jhzy.receptionevaluation.ui.adapter.InsulinAdapter;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugElders;
import com.jhzy.receptionevaluation.ui.bean.drugnext.DrugNext;
import com.jhzy.receptionevaluation.ui.bean.drugnext.DrugNextData;
import com.jhzy.receptionevaluation.ui.bean.drugnext.Insulin;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.Globars;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.widget.InsulinDialogFragment;

import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 三级界面（胰岛素，发药，配药）
 */
public class DrugNextActivity extends BaseActivity {
    private DrugElders drugElders;
    /**
     * 1：配药 2：发药 3:胰岛素
     */
    private int type;
    private String sex;
    private String age;
    private String bedCode;

    private Holder holder;
    //发药配药adapter
    private DrugNextAdapter nextAdapter;
    private InsulinAdapter insulinAdapter;
    private String token;

    private String time;
    private List<DrugNextData> drugNextDataList;
    private List<Insulin.DataBean> insulinData;
    private InsulinDialogFragment dialogFragment;


    @Override
    public int getContentView() {
        return R.layout.activity_drug_next;
    }

    @Override
    public void init() {
        initParms();
        holder = new Holder();
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }


    private void initParms() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            drugElders = extras.getParcelable("elder");
            type = extras.getInt("type", 1);
        }
    }


    private void initData() {
        token = SPUtils.find(CONTACT.TOKEN);
        Globars globars = Globars.newInstance();
        time = globars.getYear() + "-" + globars.getMonth() + "-" + globars.getDay();
        if (type == 3) {//请求胰岛素信息
            dialogFragment = new InsulinDialogFragment();
            dialogFragment.setListener(new InsulinDialogFragment.InsulinDialogListener() {
                @Override
                public void onDialogPositiveClick(DialogFragment dialogFragment) {
                    String amount = ((InsulinDialogFragment) dialogFragment).getAmount();
                    int i = Integer.parseInt(dialogFragment.getTag());
                    updateInsulinDosage(amount, i);
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    dialogFragment.dismiss();
                }


                @Override
                public void onDialogNegativeClick(DialogFragment dialogFragment) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    dialogFragment.dismiss();
                }
            });
            loadInsulin();
        } else {
            loadData();//请求发药配药信息
        }
        holder.name.setText(drugElders.getName());
        if (TextUtils.isEmpty(drugElders.getGender())) {
            sex = "暂无数据";
        } else {
            sex = drugElders.getGender();
        }
        if (TextUtils.isEmpty(drugElders.getAge() + "")) {
            age = "暂无数据";
        } else {
            age = drugElders.getAge() + "";
        }
        if (TextUtils.isEmpty(drugElders.getBedCode())) {
            bedCode = "暂无数据";
        } else {
            bedCode = drugElders.getBedCode();
        }
        holder.message.setText(sex + " | " + age + " | " + bedCode);
        ImageLoaderUtils.load(holder.photo, drugElders.getPhotoUrl());
    }


    /**
     * 修改胰岛素剂量
     */
    private void updateInsulinDosage(final String amount, final int i) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("ElderId", drugElders.getElderId() + "");
        map.put("timeId", insulinData.get(i).getTimeId() + "");
        map.put("Dosage", amount);
        map.put("DrugId", insulinData.get(i).getDrugId() + "");
        map.put("sign", GetSign.GetSign(map, token));
        HttpUtils.getInstance().getRetrofitApi().updateInsulinDosage("basic " + token, map).enqueue(
                new Callback<Code>() {
                    @Override
                    public void onResponse(Call<Code> call, Response<Code> response) {
                        Code body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            insulinData.get(i).setDosage(Double.parseDouble(amount));
                            insulinAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Code> call, Throwable t) {
                        if (NetWorkUtils.isNetworkConnected(mContext)) {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 加载胰岛素
     */
    private void loadInsulin() {
        holder.progress.setMessage("加载中...");
        holder.progress.show();
        TreeMap<String, String> map = new TreeMap<>();
        map.put("cType", 2 + "");
        map.put("ElderId", drugElders.getElderId() + "");
        map.put("UsageTime", time);
        map.put("sign", GetSign.GetSign(map, token));
        HttpUtils.getInstance().getRetrofitApi().getInsulin("basic " + token, map).enqueue(
                new Callback<Insulin>() {

                    @Override
                    public void onResponse(Call<Insulin> call, Response<Insulin> response) {
                        holder.progress.dismiss();
                        Insulin body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            insulinData = body.getData();
                            insulinAdapter.setData(body.getData());
                        } else {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Insulin> call, Throwable t) {
                        holder.progress.dismiss();
                        if (NetWorkUtils.isNetworkConnected(mContext)) {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    /**
     * 加载 配药 和 发药的数据
     */
    private void loadData() {
        holder.progress.setMessage("加载中...");
        holder.progress.show();
        TreeMap<String, String> map = new TreeMap<>();
        map.put("UsageTime", time);
        map.put("ElderId", drugElders.getElderId() + "");
        map.put("ctype", type + "");
        if (type == 1) { // 配药
            map.put("TimeTypeId", Globars.newInstance().getTid() + "");
        } else if (type == 2) {//发药
            map.put("TimeID", "0");
        }
        map.put("sign", GetSign.GetSign(map, token));
        HttpUtils.getInstance().getRetrofitApi().getDrugNext("basic " + token, map).enqueue(
                new Callback<DrugNext>() {
                    @Override
                    public void onResponse(Call<DrugNext> call, Response<DrugNext> response) {
                        holder.progress.dismiss();
                        DrugNext body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode()) &&
                                body.getData() != null) {
                            drugNextDataList = body.getData();
                            if (drugNextDataList.size() == 0) {
                                Toast.makeText(mContext, "该长者无需要配的药品信息", Toast.LENGTH_SHORT).show();
                            } else {
                                nextAdapter.setData(drugNextDataList);
                                if (type == 1) {
                                    holder.peiyaoView.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Toast.makeText(mContext, "服务异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<DrugNext> call, Throwable t) {
                        holder.progress.dismiss();
                        if (NetWorkUtils.isNetworkConnected(mContext)) {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.drug_back:
                    /*if(!dialogFragment.isAdded()){
                        dialogFragment.show(getSupportFragmentManager(),"dialog");
                    }*/

                    finish();
                    break;
                case R.id.drug_peiyao:
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("确认进行配药")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    updateDosage();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).create().show();
                    break;
            }
        }
    };


    /**
     * 配药
     */
    private void updateDosage() {
        holder.progress.setMessage("配药中...");
        holder.progress.show();
        TreeMap<String, String> map = new TreeMap<>();
        map.put("ElderId", drugElders.getElderId() + "");
        map.put("UsageTime", time);
        map.put("TimeTypeId",type+"");
        map.put("sign", GetSign.GetSign(map, token));
        HttpUtils.getInstance().getRetrofitApi().updateDosage("basic " + token, map).enqueue(
                new Callback<Code>() {
                    @Override
                    public void onResponse(Call<Code> call, Response<Code> response) {
                        holder.progress.dismiss();
                        Code body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            Toast.makeText(mContext, "配药成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Code> call, Throwable t) {
                        holder.progress.dismiss();
                        if (NetWorkUtils.isNetworkConnected(mContext)) {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    class Holder {
        private final ImageView back;
        private final TextView title;
        private final SimpleDraweeView photo;
        private final TextView name;
        private final TextView message;
        private final TextView peiyao;
        private final RecyclerView rv;
        private final ProgressDialog progress;
        private final View peiyaoView;


        public Holder() {
            back = ((ImageView) findViewById(R.id.drug_back));
            title = ((TextView) findViewById(R.id.drug_title));
            photo = ((SimpleDraweeView) findViewById(R.id.drug_elder_photo));
            name = ((TextView) findViewById(R.id.drug_elder_name));
            message = ((TextView) findViewById(R.id.drug_elder_message));
            peiyao = ((TextView) findViewById(R.id.drug_peiyao));
            rv = ((RecyclerView) findViewById(R.id.drug_rv));
            peiyaoView = findViewById(R.id.drug_peiyao_view);

            back.setOnClickListener(onClickListener);
            peiyao.setOnClickListener(onClickListener);
            rv.setLayoutManager(
                    new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            progress = new ProgressDialog(mContext);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            switch (type) {
                case 1:
                    title.setText("长者配药");
                    nextAdapter = new DrugNextAdapter(1);
                    rv.setAdapter(nextAdapter);
                    break;
                case 2:
                    title.setText("长者发药");
                    nextAdapter = new DrugNextAdapter(2);
                    nextAdapter.setFayaoClick(new DrugNextAdapter.OnFayaoClickListener() {
                        @Override
                        public void onFayaoClick(final int position) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setMessage("确认进行发药")
                                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            updateDispenis(position);
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    }).create().show();

                        }
                    });
                    rv.setAdapter(nextAdapter);
                    break;
                case 3:
                    title.setText("长者胰岛素");
                    insulinAdapter = new InsulinAdapter();
                    insulinAdapter.setInsulin(new InsulinAdapter.InsulinListener() {
                        @Override
                        public void inject(int position) {
                            Intent intent = new Intent(mContext, InsulinActivity.class);
                            intent.putExtra(InsulinActivity.STATETAG, insulinData.get(position));
                            intent.putExtra(InsulinActivity.ELDERTAG, drugElders);
                            startActivity(intent);
                        }


                        @Override
                        public void edit(int position) {
                            //dialogFragment.show(getSupportFragmentManager(),position+"");
                            if (!dialogFragment.isAdded()) {
                                dialogFragment.show(getSupportFragmentManager(), position + "");
                            }
                        }
                    });
                    rv.setAdapter(insulinAdapter);
                    break;
            }
        }
    }


    /**
     * 发药
     */
    private void updateDispenis(int position) {
        holder.progress.setMessage("发药中...");
        holder.progress.show();
        final DrugNextData drugNextData = drugNextDataList.get(position);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("TimeId", drugNextData.getTimeId() + "");
        map.put("ElderIds", drugElders.getElderId() + "");
        map.put("UsageTime", time);
        map.put("sign", GetSign.GetSign(map, token));
        HttpUtils.getInstance().getRetrofitApi().updateDispensingAll("basic " + token, map).enqueue(
                new Callback<Code>() {
                    @Override
                    public void onResponse(Call<Code> call, Response<Code> response) {
                        holder.progress.dismiss();
                        Code body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            drugNextData.setFYDatetime("time");
                            nextAdapter.notifyDataSetChanged();
                            Toast.makeText(mContext, "发药成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onFailure(Call<Code> call, Throwable t) {
                        holder.progress.dismiss();
                        if (NetWorkUtils.isNetworkConnected(mContext)) {
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
