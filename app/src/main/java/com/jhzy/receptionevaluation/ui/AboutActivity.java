package com.jhzy.receptionevaluation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.AppUtils;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.update.UpdateBean;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.UpdateDialogUtils;

import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * bigyu
 * 关于 的界面
 */
public class AboutActivity extends BaseActivity {

    private ViewHolder vh;
    private HttpUtils httpUtils;
    private String url;
    private String app_MD5;
    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return (R.layout.activity_about);
    }

    @Override
    public void init() {
        super.init();
        initUtils();
        initView();
        initListener();
    }

    private void initListener() {
        vh.versionLayout.setOnClickListener(onClickListener);
        vh.back.setOnClickListener(onClickListener);
    }

    private void initView() {
        vh.versionName.setText("版本号: V" + AppUtils.getAppVersionName(mContext));
    }

    @Override
    protected void onResume() {
        super.onResume();
        netWork();
    }

    /**
     * APP
     * 1		评估
     * 2		护理
     * 3		上门
     * 4		TV
     * 5		关爱
     * 6		管理
     */
    private void netWork() {
        vh.versionLayout.setEnabled(false);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Platform", "android");
        map.put("Version", AppUtils.getAppVersionName(mContext));
        //treeMap.put("Version", "0.0");
        map.put("App", "1");
        map.put("sign", GetSign.GetSign(map, null));
        httpUtils.getRetrofitApi().getUpdateInfo(map).enqueue(new Callback<UpdateBean>() {
            @Override
            public void onResponse(Call<UpdateBean> call, Response<UpdateBean> response) {
                UpdateBean body = response.body();
                if (body != null) {
                    if (CONTACT.DataSuccess.equals(body.getCode())) {
                        vh.versionLayout.setEnabled(true);
                        setVersionContent(R.mipmap.gantanhao, 0, R.mipmap.icon_daily_right, 0, "检测到新版本");
                        url = body.getData().getURL();
                        app_MD5 = body.getData().getMD5();
                    } else {
                        setVersionContent(0, 0, 0, 0, (String) body.getMsg());
                    }
                } else {
                    setVersionContent(0, 0, 0, 0, "未检测到版本信息");
                }
            }

            @Override
            public void onFailure(Call<UpdateBean> call, Throwable t) {
                t.printStackTrace();
                setVersionContent(0, 0, 0, 0, "未检测到版本信息");
            }
        });

    }

    private void initUtils() {
        vh = new ViewHolder();
        httpUtils = HttpUtils.getInstance();
    }


    class ViewHolder {
        private final ImageView back;
        private TextView versionName;
        private TextView versionConent;
        private View versionLayout;

        public ViewHolder() {
            versionName = ((TextView) findViewById(R.id.version_name));
            versionConent = ((TextView) findViewById(R.id.version_content));
            versionLayout = findViewById(R.id.version_layout);
            back = ((ImageView) findViewById(R.id.back));
        }
    }

    /**
     * 回调
     */
    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id){
                case R.id.version_layout:
                    new UpdateDialogUtils(mContext , url,app_MD5).show();
                    break;
                case R.id.back:
                    finish();
                    break;
            }
        }
    };
    /**
     * 版本更新文字
     * @param left drawableLeft
     * @param top
     * @param right
     * @param bottom
     * @param content 文本说明
     */
    private void setVersionContent(int left, int top, int right, int bottom, String content) {
        vh.versionConent.setText(content);
        vh.versionConent.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
    }
}
