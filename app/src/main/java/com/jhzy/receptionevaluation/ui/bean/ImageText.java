package com.jhzy.receptionevaluation.ui.bean;

import android.graphics.drawable.BitmapDrawable;

/**
 * 菜单条类，用于Drawable图片资源
 * Created by welse on 2017/4/13.
 */

public class ImageText {
    /** 图片资源Id */
    public int _resId;
    /** 菜单项名字 */
    public String _menuItem;
    /** 构造函数 */
    public  ImageText(){
        _resId = 0;
        _menuItem = "";
    }

    /** 带参数的构造函数 */
    public ImageText(int resId,String item){
        _resId = resId;
        _menuItem = item;
    }
}
