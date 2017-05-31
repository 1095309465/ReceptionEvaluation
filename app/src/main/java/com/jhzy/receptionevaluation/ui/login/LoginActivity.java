package com.jhzy.receptionevaluation.ui.login;

import android.Manifest;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.MainActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.login.LoginCode;
import com.jhzy.receptionevaluation.ui.event.LoginEventMessage;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.EventBusUtils;
import com.jhzy.receptionevaluation.utils.GetSign;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.MyCheckPermissionUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;

import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jhzy.receptionevaluation.R.id.login;

public class LoginActivity extends BaseActivity {

    private ViewHolder vh;
    private MyCheckPermissionUtils myCheckPermissionUtils;
    private CustomDialogutils customDialogutils;
    private HttpUtils instance;

    //    初始化布局
    @Override
    public int getContentView() {
        return (R.layout.activity_login);
    }


    /**
     * 初始化
     */
    @Override
    public void init() {
        super.init();
        initUtils();
        bindView();
        initListener();
        checkLoginFlag();
    }


    /**
     * 检查是否自动登陆
     */
    public void checkLoginFlag() {
        boolean flag = SPUtils.findBoolean(CONTACT.LoginContact.AUTOMATIC);
        if (flag) {
            jumpActivity();
            loginToService(false);
        }
    }


    private void initListener() {
        vh.login.setOnClickListener(onClickListener);
        vh.iv.setOnClickListener(onClickListener);
    }


    private void initUtils() {
        vh = new ViewHolder();
        myCheckPermissionUtils = MyCheckPermissionUtils.newInstance();
        customDialogutils = new CustomDialogutils(this);
        instance = HttpUtils.getInstance();
    }


    private void bindView() {
        vh.login = (TextView) findViewById(login);
        vh.password = (EditText) findViewById(R.id.password);
        vh.name = (EditText) findViewById(R.id.name);
        vh.iv = (ImageView) findViewById(R.id.iv);
    }


    class ViewHolder {
        TextView login; // 登录按钮
        EditText name, password; // 用户名 、密码
        ImageView iv;//图标按钮
    }


    /**
     * 监听回调
     */
    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id) {
                case login: // 登录按钮
                    loginToService(true);
                    break;
                case R.id.iv://测试使用，暂时跳转到老人列表界面
                    //                    startActivity(new Intent(LoginActivity.this, NameListActivity.class));

                    break;
            }

        }
    };

    private String passwordContent, nameContent;


    /**
     * 登录到系统
     */
    private void loginToService(final boolean showUI) {
        if (showUI) {
            passwordContent = vh.password.getText().toString().trim();
            nameContent = vh.name.getText().toString().trim();
            if (TextUtils.isEmpty(nameContent)) {
                Toast.makeText(mContext, "用户名不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passwordContent)) {
                Toast.makeText(mContext, "密码不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }

            customDialogutils.networkRefreshDiallog("正在登录，请稍后...");
        } else {
            passwordContent = SPUtils.find("password");
            nameContent = SPUtils.find(CONTACT.ACCOUNT);
        }
        TreeMap<String, String> map = new TreeMap<>();
        map.put("Account", nameContent);
        map.put("PassWord", passwordContent);
        map.put("sign", GetSign.GetSign(map, null));
        instance.getRetrofitApi().login(map).enqueue(new Callback<LoginCode>() {
            @Override
            public void onResponse(Call<LoginCode> call, Response<LoginCode> response) {
                LoginCode body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    customDialogutils.dismissDialog();
                    String auth_token = body.getData().getAuth_token();
                    String orgName = body.getData().getOrgName();
                    //把token保存到本地
                    SPUtils.updateOrSave(CONTACT.TOKEN, auth_token);
                    SPUtils.updateOrSave(CONTACT.ORGNAME, orgName);
                    SPUtils.updateOrSave(CONTACT.ACCOUNT, nameContent);
                    SPUtils.updateOrSave("password", passwordContent);
                    SPUtils.updateOrSaveBoolean(CONTACT.LoginContact.AUTOMATIC, true);
                 /*   SPUtils.updateOrSave(CONTACT.LoginContact.NAME, name);
                    SPUtils.updateOrSave(CONTACT.LoginContact.PASSWORD, password);*/
                    if (showUI) {
                        jumpActivity();
                    } else {
                        EventBusUtils.sendStickMessage(new LoginEventMessage(true));
                    }
                }else  if (body != null){
                    customDialogutils.cancelNetworkDialog(body.getMsg(), false);
                    if (!showUI) {
                        EventBusUtils.sendStickMessage(new LoginEventMessage(false));
                    }
                }else {  customDialogutils.cancelNetworkDialog("登录失败，服务器异常", false);
                    if (!showUI) {
                        EventBusUtils.sendStickMessage(new LoginEventMessage(false));
                    }
                }
            }


            @Override
            public void onFailure(Call<LoginCode> call, Throwable t) {
                t.printStackTrace();
                if(!NetWorkUtils.isNetworkConnected(mContext)){
                    customDialogutils.cancelNetworkDialog("登录失败，网络异常", false);
                }else{
                    customDialogutils.cancelNetworkDialog("登录失败，服务器异常", false);
                }
                if (!showUI) {
                    EventBusUtils.sendStickMessage(new LoginEventMessage(false));
                }
            }
        });

    }


    private void jumpActivity() {
        boolean requestPermission = myCheckPermissionUtils.requestPermission(mContext,
            Manifest.permission.CAMERA, 100);
        if (requestPermission) {
            MainActivity.start(mContext, null);
            finish();
        }
    }

}
