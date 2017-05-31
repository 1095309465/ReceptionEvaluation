package com.jhzy.receptionevaluation;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/10.
 */

public class AppUtils {

    /**
     * 收起键盘
     *
     * @param v
     */
    public static void hideKeyBoard(View v, Activity activity) {
        InputMethodManager imm = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    //定义判别用户身份证号的正则表达式（要么是15位，要么是18位，最后一位可以为字母）
    public static boolean compileIdent(String ident) {
        if (TextUtils.isEmpty(ident)) {
            return false;
        }
        Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
        Matcher matcher = idNumPattern.matcher(ident);
        return matcher.matches();
    }

    /**
     * 验证手机号码
     * <p>
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     *
     * @return
     */
    public static boolean compilePhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        Pattern phonePattern = Pattern.compile(regex);
        Matcher matcher = phonePattern.matcher(phoneNum);
        return matcher.matches();
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("VersionInfo", "Exception", e);
        }
        Log.e("123", "versionName=" + versionName);
        return versionName;
    }

}
