package com.jhzy.receptionevaluation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.CourseRecordBean;
import com.jhzy.receptionevaluation.ui.bean.MusicListBean;
import com.jhzy.receptionevaluation.ui.bean.PictureListBean;
import com.jhzy.receptionevaluation.ui.bean.VideoListBean;
import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.Globars;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.widget.MyTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 病程记录的界面
 */
public class CourseRecordActivity extends BaseActivity {
    private boolean isShowFunc = false;  // 是否显示功能键
    private ViewHolder vh;
    private int type = 4;//病程记录
    private String name;
    private RenElderInfo.DataBean elderBean;
    private CourseRecordBean.DataBean recordBean;
    private static final String ARG_PARAM1 = "RenElderInfo.DrugElders";
    private static final String ARG_PARAM2 = "CourseRecordBean.DrugElders";
    private CustomDialogutils customDialogutils;
    private boolean newAssess; // 是否新建一个病程
    private List<MusicListBean> mListMusic;//录音的数据源
    private List<VideoListBean> mListVideo;//录像的数据源
    private List<PictureListBean> mListPicture;//照片的数据源
    private String coursepath; // 病程文件的路径
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, CourseRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return (R.layout.activity_record_next);
    }

    @Override
    public void init() {
        super.init();
        initParams();
        initUtils();
        initListener();
        initFuncLayoutState();
        initData();
    }

    private void initParams() {
        elderBean = (RenElderInfo.DataBean) getIntent().getSerializableExtra(ARG_PARAM1);
        name = elderBean.getElderName();
        recordBean = (CourseRecordBean.DataBean) getIntent().getSerializableExtra(ARG_PARAM2);
        Bundle extras = getIntent().getExtras();
        newAssess = extras.getBoolean("newAssess");
        isShowFunc = newAssess;
    }

    /**
     * 加载数据
     */
    private void initData() {
        mListMusic = new ArrayList<>();
        mListVideo = new ArrayList<>();
        mListPicture = new ArrayList<>();
        if (elderBean != null) {
            ImageLoaderUtils.load(vh.head_icon, elderBean.getPhotoUrl());//加载头像
            vh.name.setText(elderBean.getElderName());
            vh.tv_gender.setText(elderBean.getGender());
            vh.tv_age.setText(elderBean.getAges() + "岁");
            String bedTitle = elderBean.getBedTitle();
            if (TextUtils.isEmpty(bedTitle)) {
                bedTitle = "暂无";
            }
            vh.tv_bed.setText(bedTitle);
            vh.address.setText(elderBean.getAddress());
        }
        if (recordBean != null) {
            vh.tv_time.setText(recordBean.getInspectionTime());
            vh.input.setText(recordBean.getTreatSituation());
        }

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    /**
     * 初始化对象
     */
    private void initUtils() {
        vh = new ViewHolder();
        String date = format.format(new Date());
        coursepath = Environment.getExternalStorageDirectory() + CONTACT.FilePathContact.BINGCHENGPATH + date + name;
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        vh.submit.setOnClickListener(onClickListener);
        vh.back.setOnClickListener(onClickListener);
        vh.tvRecordMusic.setOnClickListener(onClickListener);
        vh.tv_record_video.setOnClickListener(onClickListener);
        vh.tv_record_picture.setOnClickListener(onClickListener);
    }

    class ViewHolder {
        private TextView submit; // 编辑记录、完成 按钮
        private LinearLayout funcLayout; // 功能键布局
        private EditText input; // 输入框
        private View back; // 返回
        private MyTextView tvRecordMusic;//录音按钮
        private MyTextView tv_record_video;//录像按钮
        private MyTextView tv_record_picture;//拍照按钮
        private SimpleDraweeView head_icon;//头像
        private TextView name;
        private TextView tv_gender;
        private TextView tv_age;//
        private TextView tv_bed;//
        private TextView address;
        private TextView tv_time;//

        public ViewHolder() {
            tv_time = (TextView) findViewById(R.id.tv_time);
            address = (TextView) findViewById(R.id.address);
            tv_bed = (TextView) findViewById(R.id.bed);
            tv_age = (TextView) findViewById(R.id.age);
            tv_gender = (TextView) findViewById(R.id.sex);
            name = (TextView) findViewById(R.id.name);
            head_icon = (SimpleDraweeView) findViewById(R.id.head_icon);
            submit = ((TextView) findViewById(R.id.submit));
            funcLayout = ((LinearLayout) findViewById(R.id.func_layout));
            input = ((EditText) findViewById(R.id.input));
            back = findViewById(R.id.back);
            tvRecordMusic = (MyTextView) findViewById(R.id.tv_record_music);
            tv_record_video = (MyTextView) findViewById(R.id.tv_record_video);
            tv_record_picture = (MyTextView) findViewById(R.id.tv_record_picture);
        }
    }

    /**
     * 修改或者新建病程记录
     */
    private void networkEditNotes() {
        String content = vh.input.getText().toString();
        String id = "";
        if (recordBean != null) {
            id = recordBean.getId() + "";
        }
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "病情描述不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        customDialogutils = new CustomDialogutils(this);
        customDialogutils.networkRefreshDiallog("正在上传，请稍后...");
        String token = SPUtils.find(CONTACT.TOKEN);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("ID", id);
        map.put("ElderID", elderBean.getID() + "");
        map.put("TreatSituation", content);
        HttpUtils.getInstance().getRetrofitApi().addCourseRecord("basic " + token, map).enqueue(new Callback<Code>() {
            @Override
            public void onResponse(Call<Code> call, Response<Code> response) {
                Code body = response.body();
                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                    customDialogutils.cancelNetworkDialog("上传成功", true);
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    customDialogutils.cancelNetworkDialog("上传失败", false);
                }
            }

            @Override
            public void onFailure(Call<Code> call, Throwable t) {
                if(NetWorkUtils.isNetworkConnected(mContext)){
                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                }else{
                    customDialogutils.cancelNetworkDialog("网络异常", false);
                }

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mListMusic = Globars.newInstance().getMusicList();
        int musicSize = mListMusic.size();
        if (musicSize > 0)
            vh.tvRecordMusic.setText("录音(" + musicSize + ")");

        mListVideo = Globars.newInstance().getVideoList();
        int videoSize = mListVideo.size();
        if (videoSize > 0)
            vh.tv_record_video.setText("录像（" + videoSize + "）");

        mListPicture = Globars.newInstance().getPictureList();
        int pictureSize = mListPicture.size();
        if (pictureSize > 0)
            vh.tv_record_picture.setText("拍照(" + pictureSize + ")");
    }

    @Override
    protected void onDestroy() {
        Globars.newInstance().setNull();
        super.onDestroy();
    }

    /**
     * 监听回调
     */
    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.submit: // 编辑记录 、 完成
                    String content = vh.submit.getText().toString();
                    if ("完成".equals(content)) {
                        networkEditNotes();
                    } else {
                        isShowFunc = !isShowFunc;
                        initFuncLayoutState();
                    }
                    break;
                case R.id.back: // 退出界面
                    setResult(Activity.RESULT_OK);
                    finish();
                    break;
                case R.id.tv_record_music://录音按钮
                    Intent intent = new Intent(CourseRecordActivity.this, RecordListActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("coursepath" , coursepath);
                    intent.putExtra("type", type);
                    startActivity(intent);
                    break;
                case R.id.tv_record_video://录像
                    Intent intent2 = new Intent(CourseRecordActivity.this, VideoListActivity.class);
                    intent2.putExtra("name", name);
                    intent2.putExtra("type", type);
                    intent2.putExtra("coursepath" , coursepath);
                    startActivity(intent2);
                    break;
                case R.id.tv_record_picture://拍照
                    Intent intent3 = new Intent(CourseRecordActivity.this, PictureListActivity.class);
                    intent3.putExtra("name", name);
                    intent3.putExtra("type", type);
                    intent3.putExtra("coursepath" , coursepath);
                    startActivity(intent3);

                    break;
            }
        }
    };

    // 是否显示功能键 录像 录音
    private void initFuncLayoutState() {
        //是否可编辑
        vh.input.setEnabled(isShowFunc);
        if (isShowFunc) {
            // 设置功能是否显示
            vh.funcLayout.setVisibility(View.VISIBLE);
            vh.submit.setText("完成");
            //vh.input.setTextColor(mContext.getResources().getColor(R.color.color_blue));
            String content = vh.input.getText().toString().trim();
            //光标放在最后
            if (!TextUtils.isEmpty(content)) {
                vh.input.setSelection(content.length());
            }
        } else {
            // 设置功能是否显示
            vh.funcLayout.setVisibility(View.INVISIBLE);
            vh.submit.setText("编辑记录");
            //vh.input.setTextColor(mContext.getResources().getColor(R.color.color_black));
        }
    }
}
