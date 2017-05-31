package com.jhzy.receptionevaluation.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/2/21.
 */

public class MyCheckPermissionUtils {

    private MyCheckPermissionUtils() {

    }

    public static MyCheckPermissionUtils myCheckPermissionUtils;

    public static synchronized MyCheckPermissionUtils newInstance() {
        if (myCheckPermissionUtils == null) {
            myCheckPermissionUtils = new MyCheckPermissionUtils();
        }
        return myCheckPermissionUtils;
    }

    public boolean requestPermission(Context context, String permission, int requestCode) {
        if (!isGranted(context, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(((Activity) context), permission)) {
                ActivityCompat.requestPermissions(((Activity) context), new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(((Activity) context), new String[]{permission}, requestCode);
            }
            return false;
        } else {
            //直接执行相应操作了
            return true;
        }
    }

    private boolean isGranted(Context context, String permission) {
        return !isMarshmallow() || isGranted_(context, permission);
    }

    private boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    private boolean isGranted_(Context context, String permission) {
        int checkSelfPermission = ActivityCompat.checkSelfPermission(context, permission);
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED;
    }


    public boolean checkResult(Context context, int requestCode, int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                // Permission Denied
                Toast.makeText(context, "您没有授权该权限，请在设置中打开授权", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return false;
    }

}
