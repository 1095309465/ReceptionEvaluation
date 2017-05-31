package com.jhzy.receptionevaluation.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.kubility.demo.MP3Recorder;

import java.io.File;
import java.text.SimpleDateFormat;


public class RecordActivity extends AppCompatActivity implements MP3Recorder.OnMp3Listener {
    ImageView view;//音量显示
    TextView tvSecond;//录音时长
    View tvClose;//返回键按钮
    TextView tvState;//录音状态
    /* @Bind(R.id.btn_start_record)
     Button btnStartRecord;//录音按钮*/
    ImageView ivBack;//返回按钮的图标
    ImageView iv_start;

    ImageView iv_delete;
    ImageView iv_play;
    ImageView iv_sure;

    private View lin;
    /*  @Bind(R.id.btn_paush)
      Button btnPaush;*/
    private String FileName = null;//录音保存的路径
    //语音操作对象
    private long startTime;//开始的时间
    private long paushTime;//结束的时间
    private long restartTime;//重新开始的时间
    private MP3Recorder mp3Recorder;
    private boolean isFirst;//是否第一次按开始键
    private File file;//录音文件
    private Handler handler = new Handler();
    private int second;//秒
    private int minute;//分钟
    private int hour;//小时
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long difference = (System.currentTimeMillis() - startTime) / 1000;
            hour = (int) (difference / 3600);
            minute = (int) ((difference - hour * 3600) / 60);
            second = (int) (difference - hour * 3600 - minute * 60);
            String time = addZero(hour) + ":" + addZero(minute) + ":" + addZero(second);
            tvSecond.setText(time);
            handler.postDelayed(runnable, 1000);
        }
    };
    private String coursepath;

    /**
     * 时间格式：不足两位则前面加0 显示
     *
     * @param num
     * @return
     */
    public String addZero(int num) {
        if (num < 10) {
            return "0" + num;
        }
        return num + "";
    }

    private int type = 1;
    private String name = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lu_yin);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(getResources().getColor(R.color.color_black));
        initView();
        initData();
        init();
    }

    private void initData() {
        type = getIntent().getIntExtra("type", 1);
        name = getIntent().getStringExtra("name");
        coursepath = getIntent().getStringExtra("coursepath");
    }

    private void initView() {
        lin = findViewById(R.id.lin);
        lin.setVisibility(View.GONE);
        view = (ImageView) findViewById(R.id.view);
        tvSecond = (TextView) findViewById(R.id.tv_second);
        tvClose = findViewById(R.id.tv_close);
        tvState = (TextView) findViewById(R.id.tv_state);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        iv_start = (ImageView) findViewById(R.id.iv_start);

        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_sure = (ImageView) findViewById(R.id.iv_sure);

    }


    /**
     * 更新音量状态
     *
     * @param voice
     */
    private void updateMicStatus(int voice) {
        if (mp3Recorder != null && view != null) {
            int img = 0;
            switch (voice / 200) {
                case 0:
                    img = R.mipmap.signal;
                    break;
                case 1:
                    img = R.mipmap.signal0;
                    break;
                case 2:
                    img = R.mipmap.signal1;
                    break;
                case 3:
                    img = R.mipmap.signal2;
                    break;
                case 4:
                    img = R.mipmap.signal3;
                    break;
                case 5:
                    img = R.mipmap.signal4;
                    break;
                case 6:
                    img = R.mipmap.signal5;
                    break;
                case 7:
                    img = R.mipmap.signal6;
                    break;
                default:
                    break;
            }
            final int finalImg = img;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setImageResource(finalImg);
                }
            });

        }
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    /**
     * 初始化保存数据
     */
    public void init() {

        FileName = coursepath+ "/录音/";
        file = new File(FileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(FileName, System.currentTimeMillis() + ".mp3");
        Log.e("123", "file=" + file);
        mp3Recorder = new MP3Recorder(file);
        mp3Recorder.setOnCallVolumeBack(this);
    }


    /**
     * 结束录音
     */
    public void stopRecord() {
        if (mp3Recorder != null) {
            mp3Recorder.stop();
        }
        tvState.setText("暂未录音");
        //  nothing();
    }

    private void saveToScad() {
        //   String distanceString = decimalFormat.format(distanceValue)
        String path = file.getAbsolutePath();
        String durtion = tvSecond.getText().toString().trim();
        String size = getFileSize(file.length());
        Intent intent = new Intent();
        intent.putExtra("path", path);
        intent.putExtra("durtion", durtion);
        intent.putExtra("size", size);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /* *
      * 无操作以及按返回键的处理
      */
    public void nothing() {
        boolean fileFlag = file.exists();
        if (!fileFlag) {
            backNull();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(RecordActivity.this);
            dialog.setTitle("是否保存该录音");
            dialog.setCancelable(false);
            dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {//点击确认返回录音的路径
                    saveToScad();

                }
            });
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {//点击取消不返回路径，
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    backNull();

                }
            });
            dialog.show();
        }
    }

    /**
     * 保留两位小数
     */
    public String getFileSize(long length) {//B
        double kb = length * 1.0 / 1024;
        if (kb > 0 && kb < 1) {//B
            return Math.round(length) + "B";
        }
        if (kb > 1 && kb < 1024) {//KB

            return Math.round(kb) + "KB";
        }
        double mb = kb / 1024;

        return Math.round(mb) + "MB";

    }

    /**
     * 返回空路径
     */
    public void backNull() {
        Intent intent = new Intent();
        intent.putExtra("path", "");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            nothing();
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        stopRecord();
        super.onStop();
    }

    @Override
    public void getVolume(int voice) {//获取音量大小的回调接口
        Log.e("123", "音量=" + voice);

        updateMicStatus(voice);

    }


    @Override
    public void startMp3() {
        Log.e("123", "已开始录音");
        startCalculateTime();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvState.setTextColor(getResources().getColor(R.color.color_white));
                tvState.setText("正在录音");
            }
        });


    }

    @Override
    public void stopMp3() {
        Log.e("123", "已停止录音");
        stopCalculateTime();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvState.setText("录音完成");
            }
        });


    }

    @Override
    public void paushMp3() {
        Log.e("123", "已暂停录音");

    }

    @Override
    public void reStoreMp3() {
        Log.e("123", "已继续录音");

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_close://返回
                nothing();
                break;
           /* case R.id.btn_start://开始
                if (!isFirst) {//第一次按开始录音
                    startTime = System.currentTimeMillis();
                    mp3Recorder.start();
                    handler.postDelayed(runnable, 1000);
                    tvState.setText("正在录音");
                    ivBack.setImageResource(R.mipmap.icon_close);
                    btnStop.setBackgroundResource(R.mipmap.stop_record_green);
                    btnStart.setBackgroundResource(R.mipmap.paush_record);
                    isFirst = true;
                } else {

                    if (mp3Recorder.isRecording()) {
                        if (mp3Recorder.isPaus()) {//继续开始录音
                            mp3Recorder.restore();
                            restartTime = System.currentTimeMillis();
                            paushAllTime += restartTime - paushTime;
                            handler.post(runnable);
                            btnStart.setBackgroundResource(R.mipmap.paush_record);

                        } else {//暂停录音
                            mp3Recorder.pause();
                            paushTime = System.currentTimeMillis();
                            handler.removeCallbacks(runnable);
                            btnStart.setBackgroundResource(R.mipmap.start_record);
                            updateMicStatus(0);
                        }
                    }


                }
                break;*/

            case R.id.iv_start://开始和停止按钮
                boolean isRecording = mp3Recorder.isRecording();
                if (isRecording) {//正在录音中
                    Log.e("123", "正在录音中");
                    stopRecordMethod();
                } else {//未在录音
                    Log.e("123", "未在录音");
                    startRecordMethod();
                }


                break;

            case R.id.iv_delete://删除按钮，删除后，重新录制
                delete();

                break;

            case R.id.iv_play://播放按钮
                play();
                break;


            case R.id.iv_sure://保存按钮
                saveToScad();
                break;
        }
    }

    /**
     * 录音的图标为录音时，进行录音
     */
    private void startRecordMethod() {

        if (file.exists()) file.delete();//每次录音前，如果文件已经存在就删除
        mp3Recorder.start();
        iv_start.setImageResource(R.mipmap.stop_record_green);//设置图标为停止按钮图标

    }

    /**
     * 录音的图标为停止时，停止录音
     */
    private void stopRecordMethod() {
        mp3Recorder.stop();
        iv_start.setImageResource(R.mipmap.start_record);//设置图标为录音按钮图标
        view.setImageResource(R.mipmap.signal);//音量图标设置为无

        lin.setVisibility(View.VISIBLE);
        iv_start.setVisibility(View.GONE);


    }

    /**
     * 开始计算时间，规整为0
     */
    private void startCalculateTime() {
        startTime = System.currentTimeMillis();
        handler.postDelayed(runnable, 1000);

    }

    /**
     * 停止计算
     */
    private void stopCalculateTime() {
        handler.removeCallbacks(runnable);
    }

    /**
     * 删除操作
     */
    private void delete() {
        lin.setVisibility(View.GONE);
        iv_start.setVisibility(View.VISIBLE);
        tvState.setTextColor(getResources().getColor(R.color.text_gray));
        tvState.setText("暂未录音");

    }

    /**
     * 播放录音
     */
    private void play() {

        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "audio/MP3");
        startActivity(it);

    }

    /**
     * 保存操作
     */
    private void save() {
        nothing();

    }


    @Override
    protected void onDestroy() {
        if (mp3Recorder != null) mp3Recorder.stop();
        super.onDestroy();
    }
}
