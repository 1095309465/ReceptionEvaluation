package com.jhzy.receptionevaluation.ui.assess;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.jhzy.receptionevaluation.AppUtils;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.assess.RenRelate;
import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.NewElderInfoPopupwindowUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 家属资料界面
 */
public class RelateActivity extends BaseActivity{
    public static final String TAG = "relate";
    private LinearLayout content;
    private ImageView back;
    private TextView ok;
    private List<SB> sbList;
    // 弹出框对象
    private NewElderInfoPopupwindowUtils newElderInfoPopupwindowUtils;
    private CustomDialogutils customDialogutils;
    /**
     * 长者id
     */
    private int id;
    /**
     * 家属信息
     */
    private List<RenRelate.DataBean> data;



    @Override public int getContentView() {
        return R.layout.activity_relate;
    }


    @Override public void init() {
        initParms();
        initViewAndData();
        loadData();
    }


    private void loadData() {
        customDialogutils.setResfreshDialog("正在加载");
        HttpUtils.getInstance().getRetrofitApi().getElderRelate("basic " + SPUtils.find(CONTACT.TOKEN),id+"").enqueue(
            new Callback<RenRelate>() {
                @Override
                public void onResponse(Call<RenRelate> call, Response<RenRelate> response) {
                    RenRelate body = response.body();
                    if(body != null && CONTACT.DataSuccess.equals(body.getCode())){
                        customDialogutils.cancelNetworkDialog("加载完成",true);
                        data = body.getData();
                        if(data.size() > 0){
                            show();
                        }else{
                            content.addView(LayoutInflater.from(mContext).inflate(R.layout.item_no_relate,null));
                            ok.setVisibility(View.INVISIBLE);
                        }
                    }else{
                        customDialogutils.cancelNetworkDialog("服务器异常", false);
                    }
                }


                @Override public void onFailure(Call<RenRelate> call, Throwable t) {
                    if(NetWorkUtils.isNetworkConnected(mContext)){
                        customDialogutils.cancelNetworkDialog("服务器异常", false);
                    }else{
                        customDialogutils.cancelNetworkDialog("网络异常", false);
                    }
                }
            });
    }


    /**
     * 显示
     */
    private void show() {
        for (int i = 0; i < data.size(); i++) {
            RenRelate.DataBean dataBean = data.get(i);
            SB sb = new SB();
            sb.name.setText(dataBean.getGuardianName());
            sb.sex.setText(dataBean.getGender());
            sb.birth.setText(dataBean.getRelationship());
            sb.phone.setText(dataBean.getContactPhone());
            sb.setGuardianID(dataBean.getGuardianID());
            if(data.size() > 1){
                sb.title.setVisibility(View.VISIBLE);
                sb.title.setText("家属" + (i+1));
            }
            sbList.add(sb);
            content.addView(sb.view);
        }

    }


    private void initViewAndData() {
        sbList = new ArrayList<>();
        content = ((LinearLayout) findViewById(R.id.content));
        back = ((ImageView) findViewById(R.id.back));
        ok = ((TextView) findViewById(R.id.ok));
        back.setOnClickListener(noDouble);
        ok.setOnClickListener(noDouble);
        newElderInfoPopupwindowUtils = new NewElderInfoPopupwindowUtils(mContext);
        customDialogutils = new CustomDialogutils(this);
    }


    private void initParms() {
        Intent intent = getIntent();
        id = intent.getIntExtra(TAG,0);
    }


    OnClickListenerNoDouble noDouble = new OnClickListenerNoDouble() {
        @Override public void myOnClick(View view) {
            switch (view.getId()){
                case R.id.back:
                    finish();
                    break;
                case R.id.ok:
                    for (int i = 0; i < sbList.size(); i++) {
                        SB sb = sbList.get(i);
                        if("编辑".equals(ok.getText())){
                            sb.sexView.setEnabled(true);
                            sb.birth.setEnabled(true);
                            sb.name.setEnabled(true);
                            sb.phone.setEnabled(true);
                            sb.sexDown.setVisibility(View.VISIBLE);
                        }else {
                            sb.sexView.setEnabled(false);
                            sb.birth.setEnabled(false);
                            sb.name.setEnabled(false);
                            sb.phone.setEnabled(false);
                            sb.sexDown.setVisibility(View.GONE);
                        }
                    }
                    if("编辑".equals(ok.getText())){
                        ok.setText("完成");
                    }else{
                        submit();
                        ok.setText("编辑");
                    }
                    break;
            }
        }
    };


    /**
     * 家属资料修改提交
     */
    private void submit() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < sbList.size(); i++) {
            SB sb = sbList.get(i);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("GuardianName",sb.name.getText().toString());
                jsonObject.put("Gender",sb.sex.getText());
                jsonObject.put("ContactPhone",sb.phone.getText().toString());
                jsonObject.put("Relationship",sb.birth.getText().toString());
                jsonObject.put("ElderID",id);
                jsonObject.put("GuardianID",sb.GuardianID);
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        customDialogutils.setResfreshDialog("提交中");
        HttpUtils.getInstance().getRetrofitApi().editElderRelate("basic " + SPUtils.find(CONTACT.TOKEN),jsonArray.toString()).enqueue(
            new Callback<Code>() {
                @Override public void onResponse(Call<Code> call, Response<Code> response) {
                    Code body = response.body();
                    if(body != null && CONTACT.DataSuccess.equals(body.getCode())){
                        customDialogutils.cancelNetworkDialog("修改完成", true);
                    }else{
                        customDialogutils.cancelNetworkDialog("服务器异常，修改失败", false);
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


    class SB{
        private final EditText name;
        private final TextView sex;
        private final EditText birth;
        private final EditText phone;
        private final TextView title;
        private final ImageView sexDown;
        private final View sexView;
        private View view;
        private SB sb;
        private int GuardianID;
        public SB() {
            sb = this;
            view = LayoutInflater.from(mContext).inflate(R.layout.item_relate, null);
            name = ((EditText) view.findViewById(R.id.name));
            sex = ((TextView) view.findViewById(R.id.sex));
            sexDown = ((ImageView) view.findViewById(R.id.sex_down));
            sexView = view.findViewById(R.id.sex_layout);
            birth = ((EditText) view.findViewById(R.id.birth));
            phone = ((EditText) view.findViewById(R.id.phone));
            title = ((TextView) view.findViewById(R.id.title));
            sexView.setOnClickListener(noDouble);
            sexView.setEnabled(false);
        }

        public void setGuardianID(int id){
            GuardianID = id;
        }

        OnClickListenerNoDouble noDouble = new OnClickListenerNoDouble() {
            @Override public void myOnClick(View view) {
                switch (view.getId()){
                    case R.id.sex_layout:
                        chooseSex(view,sb);
                        break;
                }
            }
        };
    }

    //选择性别
    private void chooseSex(View view,final SB sb) {
        final NewElderInfoPopupwindowUtils.ViewHolder viewHolder = newElderInfoPopupwindowUtils.popChooseSex(view);
        OnClickListenerNoDouble listener = new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.female:
                        sb.sex.setText("女");
                        break;
                    case R.id.male:
                        sb.sex.setText("男");
                        break;
                }
                viewHolder.popupWindow.dismiss();
            }
        };
        viewHolder.male.setOnClickListener(listener);
        viewHolder.female.setOnClickListener(listener);

    }
}
