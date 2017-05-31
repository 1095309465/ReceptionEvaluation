package com.jhzy.receptionevaluation.utils;

import android.os.Environment;

/**
 * Created by Administrator on 2017/2/22.
 */

public class CONTACT {

    //文件存放位置
    public static final String APP_DIR = Environment.getExternalStorageDirectory() + "/zhzy/";
    public final static String DataSuccess = "A00000";
    public final static String TOKEN = "token";
    public final static String ACCOUNT = "ACCOUNT";
    public final static String ORGNAME = "ORGNAME";
    //编辑长者目录,家属，职工
    public static final String subDirElder = "Elder/";
    public static final String subDirGrardian = "Guardian/";
    public static final String subDirStaff = "Staff/";
    public static boolean isCanDownLoad = false;
    public final class LoginContact {//机构账号登陆用的flag

        public final static String AUTOMATIC = "automatic";
        public final static String NAME = "name";
        public final static String PASSWORD = "password";
    }

    public final static class FilePathContact {
        public final static String PICTUREPATH = Environment.getExternalStorageDirectory().getPath() + "/头像文件夹";//头像拍照的文件存放地址
        public final static String VIDEOPATH = Environment.getExternalStorageDirectory().getPath() + "";//存放录像的文件路径
        public final static String MUSICPATH = Environment.getExternalStorageDirectory().getPath() + "";//存放录音的文件路径
        public final static String RICHANGPATH = "/家和评估/评估资料/日常评估/";  //日常评估文件路径
        public final static String RUYUANPATH = "/家和评估/评估资料/入院评估/";  //入院评估文件路径
        public final static String CHUYUANPATH = "/家和评估/评估资料/出院评估/"; //出院评估文件路径
        public final static String BINGCHENGPATH = "/家和评估/病程资料/";   //病程文件路径
    }

    public final static class DispenseDrugContact{
        public final  static String TYPE = "type";// 配药
        public final  static String TYPE_1 = "type_1";// 配药
        public final  static String TYPE_2 = "type_2";// 配药(子类
        public final  static String TYPE_NAME_2 = "type_name_2";// 配药(子类 文字
        public final  static String COMPLETED = "completed";// 配药
        public final  static int TYPE_DISPENSE = 0;// 配药
        public final  static int TYPE_DRUG = 1; // 发药
        public final  static int TYPE_INSULIN = 2; // 胰岛素
    }

}
