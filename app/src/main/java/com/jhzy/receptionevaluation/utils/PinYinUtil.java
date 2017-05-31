package com.jhzy.receptionevaluation.utils;

import android.text.TextUtils;

/**
 * Created by sxmd on 2017/2/27.
 */

public class PinYinUtil {
    /**
     * 获取一段中文所有的拼字大写字母
     */
    public static String getAllSpell(String s, PinyinTool tool) {
        String str = tool.toPinYin(s).toUpperCase();
        return str;
    }

    /**
     * 获取一段中文第一个文字的首字母
     */
    public static String getFirstSell(String s, PinyinTool tool) {
        if(!TextUtils.isEmpty(s)){
            char c = s.toCharArray()[0];
            return tool.toPinYin(c + "").toCharArray()[0] + "";
        }else{
            return "";
        }
    }

    /**
     * 根据一段中文获取每个字的首字母拼音
     */
    public static String getAllSell(String s, PinyinTool tool) {
        try {
            char[] h = s.toCharArray();//丰台区
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < h.length; j++) {
                char oneChar = tool.toPinYin(h[j] + "").toCharArray()[0];
                sb.append(oneChar + "");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "#";
    }
}