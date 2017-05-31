package com.jhzy.receptionevaluation;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jhzy.receptionevaluation.utils.MyCheckPermissionUtils;

import java.lang.reflect.Field;

/**
 * bigyu  2017/2/15  14:56
 */
public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private MyCheckPermissionUtils myCheckPermissionUtils;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        mContext = this;
        initPermission();
        init();

    }

    private void initPermission() {
        myCheckPermissionUtils = MyCheckPermissionUtils.newInstance();
        //照相机权限
        myCheckPermissionUtils.requestPermission(mContext, Manifest.permission.CAMERA, 100);
        // 存储权限
        myCheckPermissionUtils.requestPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, 100);
        myCheckPermissionUtils.requestPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE, 100);
    }

    ;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!myCheckPermissionUtils.checkResult(mContext, requestCode, grantResults)) {
            finish();
        }
    }

    public abstract int getContentView();


    public void init() {
    }
}
