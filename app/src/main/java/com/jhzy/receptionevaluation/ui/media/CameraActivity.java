package com.jhzy.receptionevaluation.ui.media;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.utils.CONTACT;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraActivity extends AppCompatActivity {


    private Camera camera;
    private Camera.Parameters parameters;
    private String path = "";
    private ViewHolder vh;

    private boolean safeToTakePicture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        vh = new ViewHolder();
        vh.surfaceview.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        vh.surfaceview.getHolder().setFixedSize(176, 144); //设置Surface分辨率
        vh.surfaceview.getHolder().setKeepScreenOn(true);// 屏幕常亮
        vh.surfaceview.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数

        vh.paizhao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (camera != null) {
                    camera.takePicture(null, null, new MyPictureCallback());
                }*/
                if (camera != null && safeToTakePicture) {
                    camera.takePicture(null, null, new MyPictureCallback());
                    safeToTakePicture = false;
                }
            }
        });

        vh.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("path", path);
                Log.d("123456", path);
                CameraActivity.this.setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        vh.not.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.paizhao.setVisibility(View.VISIBLE);
                vh.cameraLinear.setVisibility(View.GONE);
                camera.startPreview();
            }
        });
        vh.iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回
                finish();
            }
        });
    }

    class ViewHolder {
        SurfaceView surfaceview;  //显示摄像头画面
        ImageView cameraBacground;
        ImageView paizhao;   //拍好操作按钮
        ImageView not;  //删除按钮
        ImageView sure;   //保存按钮
        LinearLayout cameraLinear;
        View iv_back;//返回按钮

        public ViewHolder() {
            surfaceview = (SurfaceView) findViewById(R.id.surfaceview);
            cameraBacground = (ImageView) findViewById(R.id.camera_bacground);
            paizhao = (ImageView) findViewById(R.id.paizhao);
            not = (ImageView) findViewById(R.id.not);
            sure = (ImageView) findViewById(R.id.sure);
            cameraLinear = (LinearLayout) findViewById(R.id.camera_linear);
            iv_back = findViewById(R.id.iv_back);
        }
    }

    private final class MyPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                //获取拍摄的图片，并顺时针旋转90度
                Bitmap bm0 = BitmapFactory.decodeByteArray(data, 0, data.length);
                Matrix m = new Matrix();
//                m.setRotate(90, (float) bm0.getWidth() / 2, (float) bm0.getHeight() / 2);
                m.setRotate(90, (float) bm0.getWidth(), (float) bm0.getHeight());
                Bitmap bm = Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), m, true);

                //把旋转后的照片再次转成字节数组
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] byteArray = stream.toByteArray();

                //设置图片保存路径
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
                String filename = format.format(date) + ".jpg";
                File fileFolder = new File(CONTACT.FilePathContact.PICTUREPATH);
                /**
                 * File fileFolder = new File("/mnt/sdcard"
                 + "/头像文件夹");
                 */
                if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                    fileFolder.mkdirs();
                }
                File jpgFile = new File(fileFolder, filename);
                path = jpgFile.getAbsolutePath();
                saveToSDCard(byteArray, jpgFile); // 保存图片到sd卡中
                Toast.makeText(getApplicationContext(), "拍照成功",
                        Toast.LENGTH_SHORT).show();

                vh.paizhao.setVisibility(View.GONE);
                vh.cameraLinear.setVisibility(View.VISIBLE);
                safeToTakePicture = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveToSDCard(byte[] data, File file) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file); // 文件输出流
        outputStream.write(data); // 写入sd卡中
        outputStream.close(); // 关闭输出流
    }


    private final class SurfaceCallback implements SurfaceHolder.Callback {

        // 拍照状态变化时调用该方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // 获取各项参数
            parameters = camera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(80); // 设置照片质量
        }

        // 开始拍照时调用该方法
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(90);
                camera.startPreview(); // 开始预览
                safeToTakePicture = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // 停止拍照时调用该方法
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent = new Intent();
        intent.putExtra("path", "");
        CameraActivity.this.setResult(Activity.RESULT_OK, intent);
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
