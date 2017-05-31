package com.jhzy.receptionevaluation.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jhzy.receptionevaluation.R;


public class Chooseimage_Dialog extends Dialog implements
        OnClickListener {

    private LayoutInflater factory;

    private Button mImg;

    private Button mPhone;
    private int screenWidth;
    private Button cancel;


    public Chooseimage_Dialog(Context context) {
        super(context);
        factory = LayoutInflater.from(context);
    }

    public Chooseimage_Dialog(Context context, int theme) {
        super(context, theme);
        screenWidth = ScreenUtils.getScreenWidth(context);
        factory = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        View inflate = factory.inflate(R.layout.myadt_tourist_chooseimage_dialog, null);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                (int) (screenWidth * 0.6),
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        this.setContentView(inflate, layoutParams);


        mImg = (Button) this.findViewById(R.id.gl_choose_img);
        mPhone = (Button) this.findViewById(R.id.gl_choose_phone);
        cancel = ((Button) findViewById(R.id.gl_choose_cancel));
        mImg.setOnClickListener(this);
        mPhone.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gl_choose_img:
                doGoToImg();
                break;
            case R.id.gl_choose_phone:
                doGoToPhone();
                break;
            case R.id.gl_choose_cancel:
                dismiss();
                break;
        }
    }

    public void doGoToImg() {

    }

    public void doGoToPhone() {
    }
}
