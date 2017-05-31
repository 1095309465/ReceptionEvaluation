package com.jhzy.receptionevaluation.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/2/22.
 */

public class SDCarkUtils {

    public static boolean checkDirExists(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return file.exists() || file.mkdirs();
    }

    /**
     * 是否有内存卡
     * @return
     */
    public static boolean isExitsSDCard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

}
