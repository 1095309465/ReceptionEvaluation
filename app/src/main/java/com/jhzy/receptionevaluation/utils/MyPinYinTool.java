package com.jhzy.receptionevaluation.utils;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * Created by welse on 2017/4/27.
 */

public class MyPinYinTool {
    private String[] pinyin;

    public MyPinYinTool()
    {
        pinyin = null;
    }

    //转换单个字符
    public String getCharacterPinYin(char c)
    {
        String pinyinStr;
        if (!Pinyin.isChinese(c))
            return c+"";
        else {
            pinyinStr = Pinyin.toPinyin(c);
            if (pinyinStr != null)
                return pinyinStr;
            else
                return "#";
        }
    }

    //转换一个字符串
    public String getAllSpell(String str)
    {
        char c;
        String pinyinStr;
        StringBuilder sb = new StringBuilder();
        str=  str.toUpperCase();
        for(int i = 0; i < str.length(); ++i)
        {
            c = str.charAt(i);
            if (!Pinyin.isChinese(c))
                sb.append(c);
            else {
                pinyinStr =  Pinyin.toPinyin(c);
                if(pinyinStr == null)
                {
                    // 如果str.charAt(i)非汉字，则保持原样
                    sb.append(c);
                }
                else
                {
                    sb.append(pinyinStr);
                }
            }
        }
        return sb.toString();
    }

    public String getAllSell(String str){
        char c;
        String pinyinStr;
        StringBuilder sb = new StringBuilder();
        str=  str.toUpperCase();
        for(int i = 0; i < str.length(); ++i)
        {
            c = str.charAt(i);
            if (!Pinyin.isChinese(c))//如果不是中文
                sb.append(c);
            else {//是中文
                pinyinStr =  Pinyin.toPinyin(c);
                if(pinyinStr == null)
                {
                    // 如果str.charAt(i)非汉字，则保持原样
                    sb.append(c);
                }
                else
                {
                    sb.append(pinyinStr.charAt(0));
                }
            }
        }
        return sb.toString();
    }
}
