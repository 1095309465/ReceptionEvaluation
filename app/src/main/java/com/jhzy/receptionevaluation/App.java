package com.jhzy.receptionevaluation;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.SDCarkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;

/**
 * Created by Administrator on 2017/2/15.
 * <p>
 * 接待评估APP
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //图片处理框架初始化
        Fresco.initialize(this);
        //检查目录是否存在 不存在则创建
        if (!SDCarkUtils.checkDirExists(CONTACT.APP_DIR)) {
            System.exit(0);
        }
        SPUtils.init(getSharedPreferences("JKPG", MODE_PRIVATE));
        //CrashReport.initCrashReport(getApplicationContext(), "f2eb512290", false);
    }
}
