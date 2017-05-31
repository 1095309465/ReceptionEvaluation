package com.jhzy.receptionevaluation.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.adapter.PhysicalHistoryAdapter;
import com.jhzy.receptionevaluation.ui.bean.physical.PhysicalExamination;
import com.jhzy.receptionevaluation.ui.bean.physical.PhysicalExaminationCode;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.utils.TimeUtils;

import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 展示体检的历史记录的界面
 */
public class PhysicalHistoryActivity extends BaseActivity {

    private ViewHolder vh;
    private PhysicalHistoryAdapter physicalHistoryAdapter;
    private String type;
    private String elderID;
    private HttpUtils httpUtils;
    private String token;
    private String typeText;


    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, PhysicalHistoryActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return (R.layout.activity_physical_history);
    }

    @Override
    public void init() {
        super.init();
        initParams();
        initUtils();
        bindView();
        initListener();
        network();
    }

    /**
     * 获取网络数据
     */
    private void network() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Type", String.valueOf(type));
        map.put("ElderID", elderID);
        map.put("QueryType", "1");
        map.put("sign", GetSign.GetSign(map, token));
        httpUtils.getRetrofitApi().getPhysicalExaminationList("basic " + token, map).enqueue(new Callback<PhysicalExaminationCode>() {
            @Override
            public void onResponse(Call<PhysicalExaminationCode> call, Response<PhysicalExaminationCode> response) {
                PhysicalExaminationCode body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    List<PhysicalExamination> physicalExaminations = body.getData();
                    String lastData = "1970";
                    for (int i = 0; i < physicalExaminations.size(); i++) {
                        PhysicalExamination examination = physicalExaminations.get(i);
                        String date = examination.getExamDate();
                        String yearMonth = TimeUtils.dateToYearMonth(date);
                        if (!lastData.equals(yearMonth)) {
                            examination.setYearmonth(yearMonth);
                            lastData = yearMonth;
                        }
                    }
                    physicalHistoryAdapter.setDatas(physicalExaminations, type, typeText);
                }
            }


            @Override
            public void onFailure(Call<PhysicalExaminationCode> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void initParams() {
        Bundle extras = getIntent().getExtras();
        type = extras.getString("type");
        elderID = extras.getString("elderID");
        typeText = extras.getString("typeText");
        token = SPUtils.find(CONTACT.TOKEN);
        if (TextUtils.isEmpty(type) || TextUtils.isEmpty(elderID)) {
            Toast.makeText(mContext, "数据异常", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void initUtils() {
        vh = new ViewHolder();
        httpUtils = HttpUtils.getInstance();
    }

    private void bindView() {
        //初始化列表
        vh.listView = (ListView) findViewById(R.id.list_view);
        vh.back = findViewById(R.id.back);
        vh.title = (TextView) findViewById(R.id.title);

        vh.title.setText("历史" + typeText + "检查");

        physicalHistoryAdapter = new PhysicalHistoryAdapter(mContext);
        vh.listView.setAdapter(physicalHistoryAdapter);
    }

    private void initListener() {
        vh.back.setOnClickListener(onClickListener);
    }

    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.back: //、返回
                    finish();
                    break;
            }
        }
    };

    class ViewHolder {
        private ListView listView; // 列表
        private View back; // 返回

        private TextView title;
    }

}
