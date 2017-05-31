package com.jhzy.receptionevaluation.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.HttpCallBack;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sxmd on 2017/3/13.
 */

public class UpdateDialogUtils implements View.OnClickListener {

    private Dialog dialog;
    private Context mContext;
    private TextView tvTitle, btnOk, btnCancle;
    private TextView progressTxt;

    private String url;
    private View lien;

    private String app_MD5;


    public UpdateDialogUtils(Context mContext, String url, String app_MD5) {
        this.url = url;
        this.mContext = mContext;
        this.app_MD5 = app_MD5;
        View contentView = LayoutInflater.from(mContext)
            .inflate(R.layout.dialog_updat_layout, null);
        initView(contentView);
        dialog = new Dialog(mContext, R.style.mydialog);
        dialog.setContentView(contentView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }


    public void show() {
        if (dialog != null) {
            dialog.show();
        }
        CONTACT.isCanDownLoad = true;
    }


    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
        CONTACT.isCanDownLoad = false;
    }


    private void initView(View contentView) {
        tvTitle = (TextView) contentView.findViewById(R.id.tv_info);
        btnOk = (TextView) contentView.findViewById(R.id.btn_ok);
        btnCancle = (TextView) contentView.findViewById(R.id.btn_cancle);
        progressTxt = ((TextView) contentView.findViewById(R.id.progress));
        lien = contentView.findViewById(R.id.line);
        btnOk.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok://确认按钮
                startDownLoad(url);
                tvTitle.setVisibility(View.GONE);
                progressTxt.setVisibility(View.VISIBLE);
                lien.setVisibility(View.GONE);
                btnOk.setVisibility(View.GONE);
                break;
            case R.id.btn_cancle://取消按钮
                dismiss();
                break;
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String info = (String) msg.obj;
            progressTxt.setText(info);
        }
    };
    private int progress = 0;


    private void setProgress(int num) {
        if (progress == num) return;
        Message msg1 = mHandler.obtainMessage(0);
        msg1.obj = "更新中...(" + num + "%)";
        mHandler.sendMessage(msg1);
        progress = num;
    }


    private void startDownLoad(String url) {
        Log.e("rxf", "app_MD5:" + app_MD5);
        final String[] realMD5 = new String[1];
        HttpUtils.getInstance()
            .getRetrofitApi()
            .downloadFile(url)
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    final File file = new File(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.apk");
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //保存到本地
                            FileUtils.writeFile2Disk(response, file, new HttpCallBack() {
                                @Override
                                public void onLoading(final long current, final long total) {
                                    int now = (int) (current * 100 / total);
                                    setProgress(now);
                                    if (now == 100) {
                                        try {
                                            realMD5[0] = BinaryUtil.calculateBase64Md5(
                                                file.getAbsolutePath());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e("rxf", "realMD5:" + realMD5[0]);
                                        if (app_MD5.equals(realMD5[0])) {
                                            Log.e("123", "开始安装");
                                            install(file.getAbsolutePath());
                                        } else {

                                        }
                                    }
                                }
                            });

                        }
                    }.start();
                }


                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Message msg1 = mHandler.obtainMessage(0);
                    msg1.obj = "      更新失败\r\n请确认网络是否正常";
                    mHandler.sendMessage(msg1);

                }
            });

    }

    /*private void checkAndInstall(){
        try {
            realMD5[0] = BinaryUtil.calculateBase64Md5(
                file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("rxf", "realMD5:" + realMD5[0]);
        if (app_MD5.equals(realMD5[0])) {
            Log.e("123", "开始安装");
            install(file.getAbsolutePath());
        } else {

        }
    }*/


    public void install(String fileName) {
        //安装文件apk路径
        //创建URI
        Uri uri = Uri.fromFile(new File(fileName));
        //创建Intent意图
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//启动新的activity
        //设置Uri和类型
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //执行安装
        mContext.startActivity(intent);
        dismiss();

    }

}
