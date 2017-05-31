package com.jhzy.receptionevaluation.ui.assess;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.FayaoActivity;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import com.jhzy.receptionevaluation.ui.fragment.PhysicalExaminationFragment;
import com.jhzy.receptionevaluation.ui.fragment.elderInfo.JiluFragment;
import com.jhzy.receptionevaluation.ui.fragment.elderInfo.LuxiangFragment;
import com.jhzy.receptionevaluation.ui.fragment.elderInfo.PingguFragment;
import com.jhzy.receptionevaluation.ui.fragment.elderInfo.ZhuyiFragment;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.Globars;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * z长者信息
 */
public class ElderInfoActivity extends BaseActivity {
    private Holder holder;
    private FragmentManager fragmentManager;
    private LuxiangFragment luxiangFragment;
    private JiluFragment jiluFragment;
    private ZhuyiFragment zhuyiFragment;
    private PingguFragment pingguFragment;
    private PhysicalExaminationFragment examinationFragment;
    private int lastPosition = -1;
    private RenElderInfo.DataBean elderData; // 老人的数据
    private Elder elder;
    private CustomDialogutils customDialogutils;


    @Override
    public int getContentView() {
        return R.layout.activity_elder_info;
    }


    @Override
    public void init() {
        initParams();
        initView();
        initListener();
        loadEldersData();
    }


    /**
     * 加载老人信息
     */
    private void loadEldersData() {
        customDialogutils.setResfreshDialog("正在加载");
        HttpUtils.getInstance()
            .getRetrofitApi()
            .getElderInfo("basic " + SPUtils.find(CONTACT.TOKEN), elder.getElderID() + "")
            .enqueue(
                new Callback<RenElderInfo>() {
                    @Override
                    public void onResponse(Call<RenElderInfo> call, Response<RenElderInfo> response) {
                        RenElderInfo body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            if (body.getData().size() > 0) {
                                elderData = body.getData().get(0);
                                toDo();
                                setTabSelection(0);
                            }
                            customDialogutils.dismissDialogWithoutAnim();
                        } else {
                            customDialogutils.cancelNetworkDialog("服务器异常", false);
                        }
                    }


                    @Override
                    public void onFailure(Call<RenElderInfo> call, Throwable t) {
                        if(NetWorkUtils.isNetworkConnected(mContext)){
                            customDialogutils.cancelNetworkDialog("服务器异常", false);
                        }else{
                            customDialogutils.cancelNetworkDialog("网络异常", false);
                        }
                    }
                });

    }


    private void toDo() {
        //姓名
        String elderName = elderData.getElderName();
        holder.name.setText(elderName);
        //性别
        String gender = elderData.getGender();
        holder.sex.setText(gender);
        //年龄
        int age = elderData.getAges();
        holder.age.setText(age + "岁");
        //房间号
        String bedTitle = elderData.getBedTitle();
        if (TextUtils.isEmpty(bedTitle)) {
            bedTitle = "暂无床位信息";
        }
        holder.bed.setText(bedTitle);
        //地址
        String address = elderData.getAddress();
        holder.address.setText(address);
        //加载头像
        String photoUrl = elderData.getPhotoUrl();
        ImageLoaderUtils.load(holder.head, photoUrl);
    }


    //获取数据
    private void initParams() {
        Bundle extras = getIntent().getExtras();
        elder = extras.getParcelable("elder");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == 99) {
            loadEldersData();
            return;
        }
        if (jiluFragment != null) {
            jiluFragment.netWork();
        }
        switch (requestCode) {//日常评估录像
            case 2:
                String path = data.getStringExtra("path");
                if (TextUtils.isEmpty(path)) {
                    return;
                }
                Log.e("123", "日常评估录像的返回文件" + path);
                break;
            case 22:
                zhuyiFragment.loadData();
                break;
        }
    }


    View.OnClickListener noDouble = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.info_back:
                    /*Intent intent2 = new Intent(mContext, FayaoActivity.class);
                    startActivity(intent2);*/
                    finish();
                    break;
                case R.id.info_relation:
                    if(elderData != null){
                        Intent intent = new Intent(mContext, RelateActivity.class);
                        intent.putExtra(RelateActivity.TAG, elderData.getID());
                        startActivity(intent);
                    }
                    break;
                case R.id.info_edit:
                    if (elderData != null) {
                        Intent intent1 = new Intent(mContext, EditElderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(EditElderActivity.TAG, elderData);
                        intent1.putExtras(bundle);
                        startActivityForResult(intent1, 99);
                    }
                    break;
                case R.id.layout_0:
                    setTabSelection(0);
                    break;
                case R.id.layout_1:
                    setTabSelection(1);
                    break;
                case R.id.layout_2:
                    setTabSelection(2);
                    break;
                case R.id.layout_3:
                    setTabSelection(3);
                    break;
                case R.id.layout_4:
                    setTabSelection(4);
                    break;

            }
        }
    };


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index 每个tab页对应的下标。0表示录像，1表示记录，2表示注意，3表示评估。
     */
    private void setTabSelection(int index) {
        Globars.newInstance().setNull();
        if (elderData == null) {
            return;
        }
        // 每次选中之前先清楚掉上次的选中状态
        if (index != lastPosition) {
            clearSelection(lastPosition);
            // 开启一个Fragment事务
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
            hideFragments(transaction);
            switch (index) {
                case 1:
                    doWithView(holder.tab1, holder.luxiang, R.mipmap.icon_info_001);
                    if (luxiangFragment == null) {
                        luxiangFragment = LuxiangFragment.newInstance(elderData.getElderName(),
                            null);
                        transaction.add(R.id.info_main, luxiangFragment);
                    } else {
                        transaction.show(luxiangFragment);
                    }
                    break;
                case 2:
                    doWithView(holder.tab2, holder.jilu, R.mipmap.icon_info_002);
                    if (jiluFragment == null) {
                        jiluFragment = JiluFragment.newInstance(elderData);
                        transaction.add(R.id.info_main, jiluFragment);
                    } else {
                        transaction.show(jiluFragment);
                    }

                    break;
                case 3:
                    doWithView(holder.tab3, holder.zhuyi, R.mipmap.icon_info_003);
                    if (zhuyiFragment == null) {
                        if (elderData != null) {
                            zhuyiFragment = ZhuyiFragment.newInstance(elderData.getID(), null);
                            transaction.add(R.id.info_main, zhuyiFragment);
                        } else {
                            Toast.makeText(mContext, "老人信息不完整,请退出重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        transaction.show(zhuyiFragment);
                    }
                    break;
                case 4:
                    doWithView(holder.tab4, holder.test, R.mipmap.icon_jiancha_click);
                    if (examinationFragment == null) {
                        if (elderData != null) {
                            examinationFragment = PhysicalExaminationFragment.newInstance(elderData);
                            transaction.add(R.id.info_main, examinationFragment);
                        } else {
                            Toast.makeText(mContext, "老人信息不完整,请退出重试", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        transaction.show(examinationFragment);
                    }
                    break;
                case 0:
                default:
                    doWithView(holder.tab0, holder.pinggu, R.mipmap.icon_info_004);

                    if (pingguFragment == null) {
                        pingguFragment = PingguFragment.newInstance(elderData.getID() + "", null);
                        transaction.add(R.id.info_main, pingguFragment);
                    } else {
                        transaction.show(pingguFragment);
                    }

                    break;
            }
            transaction.commitAllowingStateLoss();
            lastPosition = index;
        }

    }


    private void doWithView(View view, TextView tv, int id) {
        Drawable drawable = getResources().getDrawable(id);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
        view.setVisibility(View.VISIBLE);
        tv.setTextColor(getResources().getColor(R.color.editgreen));
    }


    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection(int lastPosition) {
        switch (lastPosition) {
            case 0:
                doWithView(holder.tab0, holder.pinggu, R.mipmap.icon_info_004_2);
                holder.tab0.setVisibility(View.INVISIBLE);
                holder.pinggu.setTextColor(getResources().getColor(R.color.color_black_font));
                break;
            case 1:
                doWithView(holder.tab1, holder.luxiang, R.mipmap.icon_info_001_2);
                holder.tab1.setVisibility(View.INVISIBLE);
                holder.luxiang.setTextColor(getResources().getColor(R.color.color_black_font));
                break;
            case 2:
                doWithView(holder.tab2, holder.jilu, R.mipmap.icon_info_002_2);
                holder.tab2.setVisibility(View.INVISIBLE);
                holder.jilu.setTextColor(getResources().getColor(R.color.color_black_font));
                break;
            case 3:
                doWithView(holder.tab3, holder.zhuyi, R.mipmap.icon_info_003_2);
                holder.tab3.setVisibility(View.INVISIBLE);
                holder.zhuyi.setTextColor(getResources().getColor(R.color.color_black_font));
                break;
            case 4:
                doWithView(holder.tab4, holder.test, R.mipmap.icon_test);
                holder.tab4.setVisibility(View.INVISIBLE);
                holder.test.setTextColor(getResources().getColor(R.color.color_black_font));
                break;
        }
    }


    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (luxiangFragment != null) {
            transaction.hide(luxiangFragment);
        }
        if (jiluFragment != null) {
            transaction.hide(jiluFragment);
        }
        if (zhuyiFragment != null) {
            transaction.hide(zhuyiFragment);
        }
        if (pingguFragment != null) {
            transaction.hide(pingguFragment);
        }
        if(examinationFragment != null){
            transaction.hide(examinationFragment);
        }
    }


    private void initListener() {
        holder.back.setOnClickListener(noDouble);
        holder.relation.setOnClickListener(noDouble);
        holder.edit.setOnClickListener(noDouble);
        holder.layout0.setOnClickListener(noDouble);
        holder.layout1.setOnClickListener(noDouble);
        holder.layout2.setOnClickListener(noDouble);
        holder.layout3.setOnClickListener(noDouble);
        holder.layout4.setOnClickListener(noDouble);
    }


    private void initView() {
        fragmentManager = getSupportFragmentManager();
        customDialogutils = new CustomDialogutils(this);
        holder = new Holder();

    }


    class Holder {
        View layout0, layout1, layout2, layout3,layout4;
        SimpleDraweeView head;
        ImageView back;
        TextView relation;
        TextView name, detail, address, edit, bed, sex, age;
        TextView luxiang, jilu, zhuyi, pinggu,test;
        View tab0, tab1, tab2, tab3,tab4;
        FrameLayout main;


        public Holder() {
            back = ((ImageView) findViewById(R.id.info_back));
            relation = ((TextView) findViewById(R.id.info_relation));
            head = ((SimpleDraweeView) findViewById(R.id.head_icon));
            name = ((TextView) findViewById(R.id.name));
            address = ((TextView) findViewById(R.id.address));
            edit = ((TextView) findViewById(R.id.info_edit));
            pinggu = ((TextView) findViewById(R.id.info_0));
            test = ((TextView) findViewById(R.id.info_4));
            luxiang = ((TextView) findViewById(R.id.info_1));
            jilu = ((TextView) findViewById(R.id.info_2));
            zhuyi = ((TextView) findViewById(R.id.info_3));
            main = ((FrameLayout) findViewById(R.id.info_main));
            age = (TextView) findViewById(R.id.age);
            sex = (TextView) findViewById(R.id.sex);
            bed = (TextView) findViewById(R.id.bed);
            tab0 = findViewById(R.id.tab_0);
            tab1 = findViewById(R.id.tab_1);
            tab2 = findViewById(R.id.tab_2);
            tab3 = findViewById(R.id.tab_3);
            tab4 = findViewById(R.id.tab_4);
            layout0 = findViewById(R.id.layout_0);
            layout1 = findViewById(R.id.layout_1);
            layout2 = findViewById(R.id.layout_2);
            layout3 = findViewById(R.id.layout_3);
            layout4 = findViewById(R.id.layout_4);
        }
    }

}
