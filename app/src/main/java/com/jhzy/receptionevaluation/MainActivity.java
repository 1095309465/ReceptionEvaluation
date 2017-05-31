package com.jhzy.receptionevaluation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.NewElderInfoActivity;
import com.jhzy.receptionevaluation.ui.adapter.MainFragemntAdapter;
import com.jhzy.receptionevaluation.ui.event.LoginEventMessage;
import com.jhzy.receptionevaluation.ui.fragment.AssessmentInfoFragment;
import com.jhzy.receptionevaluation.ui.fragment.DrugFragment;
import com.jhzy.receptionevaluation.ui.fragment.MineFragment;
import com.jhzy.receptionevaluation.ui.login.LoginActivity;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewHolder vh;
    private int currentPosition = 0;
    private long exitTime = 0;

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return (R.layout.activity_main);
    }

    @Override
    public void init() {
        super.init();
        initUtils();
        bindView();
        initViewData();
        initListener();
    }

    class ViewHolder {
        ViewPager viewPager;
        MainFragemntAdapter mainFragemntAdapter; // 主页的fragment的适配器
        List<Fragment> fragmentList;
        RadioButton btn_1, btn_2, btn_3;
        RadioGroup rg;
        TextView title; //标题
        TextView newInfo; // 新建长者资料 按钮
    }

    private void initUtils() {
        vh = new ViewHolder();
    }

    private void bindView() {
        vh.viewPager = (ViewPager) findViewById(R.id.vpDispense);
        vh.btn_1 = (RadioButton) findViewById(R.id.btn_1);
        vh.btn_2 = (RadioButton) findViewById(R.id.btn_2);
        vh.btn_3 = (RadioButton) findViewById(R.id.btn_3);
        vh.title = (TextView) findViewById(R.id.title);
        vh.rg = (RadioGroup) findViewById(R.id.rg);
        vh.newInfo = (TextView) findViewById(R.id.new_info);
    }


    private void initViewData() {
        //初始化fragment集合
        vh.fragmentList = new ArrayList<>();
        //初始化framgent对象
        AssessmentInfoFragment assessmentFragment = AssessmentInfoFragment.newInstance("", "");
        //PhysicalExaminationFragment physicalExaminationFragment = PhysicalExaminationFragment.newInstance();
        DrugFragment drugFragment = DrugFragment.newInstance();
        MineFragment mineFragment = MineFragment.newInstance();
        //fragment对象加入集合中
        vh.fragmentList.add(assessmentFragment);
        vh.fragmentList.add(drugFragment);
        vh.fragmentList.add(mineFragment);
        // 初始化viewpager适配器
        vh.mainFragemntAdapter = new MainFragemntAdapter(getSupportFragmentManager(), vh.fragmentList);
        //适配器放入viewpager中
        vh.viewPager.setAdapter(vh.mainFragemntAdapter);
        //设置当前的fragment
        vh.viewPager.setCurrentItem(currentPosition);
        vh.viewPager.setOffscreenPageLimit(3);
        chooseTitle(currentPosition);
    }

    private void initListener() {
        //viewpager 的监听
        vh.viewPager.addOnPageChangeListener(onPageChangeListener);
        //RadioGroup 监听
        vh.rg.setOnCheckedChangeListener(onCheckedChangeListener);
        //新建长者资料
        vh.newInfo.setOnClickListener(onClickListener);
    }


    /**
     * viewpager 的监听
     */
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int childCount = vh.rg.getChildCount();
            int fragmentSize = vh.fragmentList.size();
            if (position <= childCount) {
                ((RadioButton) vh.rg.getChildAt(position)).setChecked(true);
            }
            currentPosition = position;
            chooseTitle(currentPosition);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * RadioGroup 监听
     */
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            switch (i) {
                case R.id.btn_1:
                    vh.viewPager.setCurrentItem(0);
                    currentPosition = 0;
                    break;

                case R.id.btn_2:
                    vh.viewPager.setCurrentItem(1);
                    currentPosition = 1;
                    break;

                case R.id.btn_3:
                    vh.viewPager.setCurrentItem(2);
                    currentPosition = 2;
                    break;
            }
            chooseTitle(currentPosition);
        }
    };

    /**
     * 监听回调
     */
    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.new_info: // 新建长者资料
                    NewElderInfoActivity.start(mContext, null);
                    break;
            }

        }
    };

    private void chooseTitle(int position) {
        if (position == 0) {
            initAssessTitle();
        } else if (position == 1) {
            initPhyExaTitle();
        } else if (position == 2) {
            initMineTitle();
        }
    }

    /**
     * 评估界面的标题
     */
    private void initAssessTitle() {
        vh.title.setText("评估信息");
        vh.newInfo.setVisibility(View.VISIBLE);
    }

    /**
     * 体检的标题
     */
    private void initPhyExaTitle() {
        vh.newInfo.setVisibility(View.GONE);
        vh.title.setText("日常检查");
    }

    /**
     * 个人中心的标题
     */
    private void initMineTitle() {
        vh.newInfo.setVisibility(View.GONE);
        vh.title.setText("我的");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDataSynEvent(LoginEventMessage message) {
        if (message == null) {
            Log.e("---------", "请初始化对象 ");
            return;
        }
        boolean loginSuccess = message.isLoginSuccess();
        if (loginSuccess) {
            Log.e("------------", "自动登录成功");
        } else {
            Toast.makeText(mContext, "登录过期请重新登陆", Toast.LENGTH_SHORT).show();
            //移除广播
            EventBus.getDefault().removeStickyEvent(message);
            SPUtils.updateOrSaveBoolean(CONTACT.LoginContact.AUTOMATIC, false);
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

}
