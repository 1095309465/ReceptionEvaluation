package com.jhzy.receptionevaluation.ui.media;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.utils.CONTACT;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    //返回上一级
    ImageView recordingBack;
    //显示录制时间
    TextView recordingTime;
    //小红点
    ImageView recordingRedpoit;
    //录像按钮
    ImageView recordingVideo;
    //直接退出
    ImageView recordingDelete;
    ImageView recordingVideoDelete;
    ImageView recordingVideoPlay;
    ImageView recordingVideoSure;
    LinearLayout recordingVideoLinear;
    //是否正在录制
    private boolean isRecording;
    private MediaRecorder mediaRecorder;
    //显示摄像头画面
    SurfaceView surfaceview;
    //是否正在录制
    private boolean isvideoing = false;
    private boolean isvideo = false;
    //存储地址
    private String path;
    private Camera mCamera;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private int type;
    private String name;
    //控件点击监听
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.recording_video:
                    if (!isvideoing) {
                        start();
                        startTime = System.currentTimeMillis();
                        handler.postDelayed(runnable, 1000);
                        recordingVideo.setImageResource(R.mipmap.recording_videoing);//停止
                        isvideoing = true;
                        setFlickerAnimation(recordingRedpoit);
                    } else {
                        Log.d("12345678", "执行到了111");
                        Log.d("12345678", "执行到了111" + isRecording);
                        recordingVideo.setVisibility(View.GONE);
                        handler.removeCallbacks(runnable);
                        stop();
                    }
                    break;
                case R.id.recording_back:
                    onBackMethod();
                /*    *//*File file = new File(path);
                    file.delete();*//*
                    mCamera.release();
//                    mediaRecorder.stop();
//                    mediaRecorder.release();
                    Intent intent = new Intent();
                    intent.putExtra("path", "");
                    setResult(Activity.RESULT_OK, intent);
                    finish();*/
                    break;
//                case R.id.recording_delete:
//                    File file = new File(path);
//                    file.delete();
//                    if (isRecording) {
//                    // 如果正在录制，停止并释放资源
//                    mediaRecorder.stop();
//                    mediaRecorder.reset();
//                    isRecording = false;
//                    mCamera.lock();
//                    recordingRedpoit.clearAnimation();
//                    Log.d("12345678", "执行到了");
//                    }
//                    finish();
//                    break;
                case R.id.recording_video_delete:
                    if (!TextUtils.isEmpty(path)) {
                        File files = new File(path);
                        files.delete();
                    }
                    recordingTime.setText("00:00:00");
                    recordingVideoLinear.setVisibility(View.GONE);
                    recordingVideo.setVisibility(View.VISIBLE);
                    recordingVideo.setImageResource(R.mipmap.recording_video);
                    isvideoing = false;
                    isRecording = false;
                    isvideo = false;

                    break;
                case R.id.recording_video_sure:
                    btnSure();
                    break;
                case R.id.recording_video_play:
                    Intent intent2 = new Intent(VideoActivity.this, CourseVideoActivity.class);
                    intent2.putExtra("path", path);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
        }
    };
    private String coursepath;

    private void btnSure() {
        if (mCamera != null) {
            mCamera.release();
        }
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
        // Toast.makeText(VideoActivity.this, "停止录像，并保存文件", Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent();
        intent1.putExtra("path", path);
        setResult(Activity.RESULT_OK, intent1);
        finish();

    }

    //开始录制时间
    private long startTime;
    private String courseid;
    private Handler handler = new Handler();
    private int second;//秒
    private int minute;//分钟
    private int hour;//小时
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            long difference = (now - startTime) / 1000;
            hour = (int) (difference / 3600);
            minute = (int) ((difference - hour * 3600) / 60);
            second = (int) (difference - hour * 3600 - minute * 60);
            Log.e("123", "时间=" + hour + "," + minute + ",=" + second);
            String time = addZero(hour) + ":" + addZero(minute) + ":" + addZero(second);
            /*tvHour.setText(addZero(hour));
            tvMinute.setText(addZero(minute));*/
            recordingTime.setText(time);
            handler.postDelayed(runnable, 1000);
        }
    };

    public String addZero(int num) {
        String str = null;
        if (num < 10) {
            return "0" + num;
        }
        return num + "";
    }

    private SurfaceHolder surfaceHolder;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        initView();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setStatusBarColor(getResources().getColor(R.color.color_black));
        initData();
    }

    private void initView() {
        recordingBack = (ImageView) findViewById(R.id.recording_back);
        recordingTime = (TextView) findViewById(R.id.recording_time);
        recordingRedpoit = (ImageView) findViewById(R.id.recording_redpoit);
        recordingVideo = (ImageView) findViewById(R.id.recording_video);
        recordingDelete = (ImageView) findViewById(R.id.recording_delete);
        recordingVideoDelete = (ImageView) findViewById(R.id.recording_video_delete);
        recordingVideoPlay = (ImageView) findViewById(R.id.recording_video_play);
        recordingVideoSure = (ImageView) findViewById(R.id.recording_video_sure);
        recordingVideoLinear = (LinearLayout) findViewById(R.id.recording_video_linear);
        surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
    }

    /**
     * 初始化
     */
    private void initData() {
        mediainitdata();
        Intent intent = getIntent();
        courseid = intent.getStringExtra("id");
        type = intent.getIntExtra("type", 1);
        name = intent.getStringExtra("name");
        coursepath = intent.getStringExtra("coursepath");
        recordingBack.setOnClickListener(click);
        recordingVideo.setOnClickListener(click);
        recordingDelete.setOnClickListener(click);
        recordingVideoSure.setOnClickListener(click);
        recordingVideoPlay.setOnClickListener(click);
        recordingVideoDelete.setOnClickListener(click);
        SurfaceHolder holder = surfaceview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        recordingVideo.setVisibility(View.GONE);
        handler.removeCallbacks(runnable);
        stop();
    }

    private void mediainitdata() {
//        isvideo = true;
//        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".mp4";//
//        File file = new File("/mnt/sdcard/" + "ZHZY/video/");
//
//
//        Toast.makeText(VideoActivity.this, file.toString() + "sssss", Toast.LENGTH_SHORT).show();
//        if (!file.exists()) {
//            file.mkdirs();
//            Toast.makeText(VideoActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(VideoActivity.this, file.toString() + "dddddd", Toast.LENGTH_SHORT).show();
//        }
//        path = "/mnt/sdcard/" + "ZHZY/video/" + fileName;
        mediaRecorder = new MediaRecorder();
//        //设置播放时旋转
//        mediaRecorder.setOrientationHint(90);
//        mediaRecorder.reset();
        try {
            mCamera = Camera.open();

        } catch (Exception e) {
            Toast.makeText(this, "请不要频繁开启摄像头", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Camera.Parameters params = mCamera.getParameters();
        mCamera.setParameters(params);
        mCamera.stopPreview();
//        mCamera.setDisplayOrientation(90);//旋转了90度,最好先判断下JDK的版本号，再决定旋转不
//        mCamera.unlock();//解锁
//        mCamera.unlock();//解锁
//        mediaRecorder.setCamera(mCamera);
//        // 设置音频录入源
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        // 设置视频图像的录入源
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
////            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_CIF));
////            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));
////            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
////            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
////            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
////            // 设置录入媒体的输出格式
//        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//        mediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);
////            // 设置音频的编码格式
//        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
////             //设置视频的编码格式
//        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//        mediaRecorder.setOutputFile(path);
        // 设置捕获视频图像的预览界面
//        mediaRecorder.setPreviewDisplay(surfaceview.getHolder().getSurface());
//        mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
//            @Override
//            public void onError(MediaRecorder mr, int what, int extra) {
//                // 发生错误，停止录制
//                mediaRecorder.stop();
//                mediaRecorder.release();
//                mediaRecorder = null;
//                isRecording = false;
//                Toast.makeText(VideoActivity.this, "录制出错", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //小红点闪烁动画
    private void setFlickerAnimation(ImageView iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        iv_chat_head.setAnimation(animation);
    }

    private void normalVideo(String typeStr) {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".mp4";//
        String personFileName = dateFormat.format(new Date()) + name;
        String filesPath = Environment.getExternalStorageDirectory() + typeStr + personFileName + "/录像/";
        File file = new File(filesPath);
        if (!file.exists()) {
            file.mkdirs();
            //   Toast.makeText(VideoActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
        } else {
            //   Toast.makeText(VideoActivity.this, file.toString() + "dddddd", Toast.LENGTH_SHORT).show();
        }
        path = filesPath + "/" + fileName;
    }

    private void courseVideo() {
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".mp4";//
        String filesPath = coursepath + "/录像/";
        File file = new File(filesPath);
        if (!file.exists()) {
            file.mkdirs();
            //   Toast.makeText(VideoActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
        } else {
            //   Toast.makeText(VideoActivity.this, file.toString() + "dddddd", Toast.LENGTH_SHORT).show();
        }
        path = filesPath + "/" + fileName;
    }

    /**
     * 开始录制
     */
    protected void start() {
        try {
            isvideo = true;
            String typeStr = "";
            switch (type) {
                case 1://日常评估
                    typeStr = CONTACT.FilePathContact.RICHANGPATH;
                    //  typeStr = "评估文件夹/日常评估/";
                    normalVideo(typeStr);
                    break;
                case 2://入院评估
                    typeStr = CONTACT.FilePathContact.RUYUANPATH;
                    //  typeStr = "评估文件夹/入院评估/";
                    normalVideo(typeStr);
                    break;
                case 3://出院评估
                    typeStr = CONTACT.FilePathContact.CHUYUANPATH;
                    // typeStr = "评估文件夹/出院评估/";
                    normalVideo(typeStr);
                    break;
                case 4://病程记录
                    courseVideo();
                    break;
            }

            Log.e("123", "path=" + path);
            mediaRecorder = new MediaRecorder();
            //设置播放时旋转

            mediaRecorder.reset();
//            Camera mCamera = Camera.open();
//            Camera.Parameters params = mCamera.getParameters();
            mCamera.setDisplayOrientation(90);//旋转了90度,最好先判断下JDK的版本号，再决定旋转不
            mediaRecorder.setOrientationHint(90);
//            mCamera.setParameters(params);
//            mCamera.stopPreview();
            mCamera.unlock();//解锁
            mediaRecorder.setCamera(mCamera);
            // 设置音频录入源
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置视频图像的录入源
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_CIF));
//            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QCIF));
//            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
//            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
//            mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
//            // 设置录入媒体的输出格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);
//            // 设置音频的编码格式
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
//             //设置视频的编码格式
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // 设置视频的采样率，每秒20帧
//            mediaRecorder.setVideoFrameRate(20);
            // 设置录制视频文件的输出路径
            mediaRecorder.setOutputFile(path);
            // 设置捕获视频图像的预览界面
            mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {

                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    // 发生错误，停止录制
                    mediaRecorder.setOnErrorListener(null);
                    mediaRecorder.setOnInfoListener(null);
                    mediaRecorder.setPreviewDisplay(null);
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording = false;

                    Toast.makeText(VideoActivity.this, "录制出错", Toast.LENGTH_SHORT).show();
                }
            });
            // 准备、开始
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch (Exception e) {
            Log.e("123", "录像异常=" + e.toString());
            Toast.makeText(VideoActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 停止录制
     */
    protected void stop() {
        if (isRecording) {
            // 如果正在录制，停止并释放资源
            if (mediaRecorder != null) {
                try {
                    mCamera.stopPreview();
                    mediaRecorder.setOnErrorListener(null);
                    mediaRecorder.setOnInfoListener(null);
                    mediaRecorder.setPreviewDisplay(null);
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                    mediaRecorder = null;
                    isRecording = false;

                } catch (Exception e) {
                    Toast.makeText(this, "关闭录像异常", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
//            mCamera.lock();
            recordingRedpoit.clearAnimation();
            Log.d("12345678", "执行到了");
//            Toast.makeText(VideoActivity.this, "停止录像，并保存文件", Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(this, LocalVideoActivity.class);
//            intent.putExtra("path", path);
//            this.setResult(2, intent);
//            finish();
            recordingVideoLinear.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        mCamera.release();
        if (isRecording) {
            if (mediaRecorder != null) {
                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mCamera.release();
                    mediaRecorder = null;
                } catch (Exception e) {
                    finish();
                }
            }
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        onBackMethod();
        return super.onKeyDown(keyCode, event);
    }


    private void onBackMethod() {
        if (isvideo) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("是否保存录像并退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                 /*   stop();
                    mCamera.release();*/
                    btnSure();

                }
            }).setNegativeButton("取消", null).show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("path", "");
            this.setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        surfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceview = null;
        surfaceHolder = null;
        mediaRecorder = null;
    }
}

