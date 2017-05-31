package com.jhzy.receptionevaluation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.media.CameraActivity;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.Chooseimage_Dialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 查看长者资料界面
 */
public class AssessActivity extends BaseActivity {


    private ViewHolder vh;
    private String strImgPath;//照片绝对路径
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");//时间格式器
    private static final int FLAG_CHOOSE_IMG = 5;//选择相册图片的请求码

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, AssessActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getContentView() {
        return (R.layout.activity_assess);
    }

    @Override
    public void init() {
        super.init();
        initUtils();
        bindView();
        initListener();
    }


    private void initUtils() {
        vh = new ViewHolder();
    }

    private void bindView() {
        vh.headIcon = (SimpleDraweeView) findViewById(R.id.head_icon);
    }

    private void initListener() {
        vh.headIcon.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                cameraMethod();
            }
        });
    }

    class ViewHolder {
        SimpleDraweeView headIcon;
    }


    /**
     * 照相功能
     */
    private void cameraMethod() {
        // String parentFile = "/mnt/sdcard/头像文件夹";
        String parentFile = CONTACT.FilePathContact.PICTUREPATH;
        File f = new File(parentFile);
        if (!f.exists()) {
            f.mkdirs();
        }
        strImgPath = parentFile + "/" + formatter.format(new Date()) + ".jpg";
        Chooseimage_Dialog dialog = new Chooseimage_Dialog(this, R.style.mydialog) {
            @Override
            public void doGoToImg() {// 相册
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, FLAG_CHOOSE_IMG);
                this.dismiss();

            }

            @Override
            public void doGoToPhone() {// 拍照
                myCamera();
                this.dismiss();
            }
        };
        dialog.setTitle("请选择图片");
        dialog.show();
    }

    private static final int myCamera = 2000;

    /**
     * 非系统自带的拍照
     */
    public void myCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, myCamera);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG_CHOOSE_IMG://系统相册
                if (resultCode != Activity.RESULT_OK)
                    return;
                Uri uri1 = data.getData();
                Cursor cursor = getContentResolver().query(uri1, new String[]{"_data"}, null, null, null);
                if (cursor.moveToFirst()) {
                    String otherfile = cursor.getString(0);
                    File picture = new File(otherfile);
                    startPhotoZoom(Uri.fromFile(picture));
                    //  strImgPath = picture.getAbsolutePath();
                }

                break;

            case PHOTORESOULT://裁剪
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "裁剪失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(strImgPath);
                Log.e("123", "strImgPath=" + strImgPath);
                if (file.exists()) updatePhoto(strImgPath);
                break;

            case myCamera://自定义拍照
                if (resultCode != Activity.RESULT_OK) return;
                String imgUrl = data.getStringExtra("path");
                if (TextUtils.isEmpty(imgUrl)) return;
                File p = new File(imgUrl);
                startPhotoZoom(Uri.fromFile(p));
                break;
        }

    }

    public static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTORESOULT = 0;
    private final String imgType = "file://";

    /**
     * \
     * 上传头像和加载头像
     *
     * @param strImgPath
     */
    private void updatePhoto(String strImgPath) {
        //photo
        Uri uri = Uri.parse(imgType + strImgPath);
        vh.headIcon.setImageURI(uri);

    }

    /**
     * 剪辑照片
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX, outputY 是裁剪图片宽高
        intent.putExtra("output", Uri.fromFile(new File(strImgPath))); // 保存路径
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, PHOTORESOULT);
    }


}
