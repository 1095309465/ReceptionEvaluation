package com.jhzy.receptionevaluation.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;


/**
 * 配药界面
 */
public class MedicineActivity extends BaseActivity {


    @Override
    public int getContentView() {
        return (R.layout.activity_medicine);
    }


    @Override
    public void init() {
        super.init();

    }


    class ViewHolder{

        private View back;
        private TextView eat_1,eat_2,eat_3;

        public ViewHolder() {
            back = findViewById(R.id.back);
            eat_1 = ((TextView) findViewById(R.id.eat_1));
            eat_2 = ((TextView) findViewById(R.id.eat_2));
            eat_3 = ((TextView) findViewById(R.id.eat_3));
        }
    }
}
