package com.jhzy.receptionevaluation.ui;

import android.content.res.Resources;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.TimeAdapter;
import com.jhzy.receptionevaluation.utils.PopWindowUtils;

import java.util.ArrayList;
import java.util.List;

public class FayaoActivity extends BaseActivity {

    private TextView time;
    private ImageView arrow;
    private View choose;
    private PopWindowUtils popWindowUtils;
    private PopWindowUtils.Holder holder;
    TimeAdapter timeAdapter;
    private List<String> datas;
    private int width;
    private int height;
    private int naviHeight;
    private View chooseTop;
    private ImageView arrowTop;


    @Override public int getContentView() {
        return R.layout.activity_fayao;
    }


    @Override public void init() {
        datas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            datas.add("chifan " + i);
        }
        popWindowUtils = new PopWindowUtils(mContext);
        choose = findViewById(R.id.drug_choose);
//        timeAdapter = new TimeAdapter(mContext,datas);
        time = ((TextView) findViewById(R.id.drug_time));
        arrow = ((ImageView) findViewById(R.id.drug_arrow));
        choose.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                show(false,choose,arrow);
            }
        });

        chooseTop = findViewById(R.id.drug_choose_top);
        arrowTop = ((ImageView) findViewById(R.id.drug_arrow_top));
        chooseTop.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                show(true,chooseTop,arrowTop);
            }
        });
    }




    private void show(boolean isTop,View view,ImageView arrow) {
        holder = popWindowUtils.showEldersInfo(view,arrow,width,height,naviHeight,isTop);
        holder.listView.setAdapter(timeAdapter);
        holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }


    @Override public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            width = choose.getWidth();
            height = choose.getHeight();
            Resources resources = mContext.getResources();
            int test = resources.getIdentifier("navigation_bar_height","dimen", "android");
            naviHeight = resources.getDimensionPixelSize(test);
        }



    }
}
