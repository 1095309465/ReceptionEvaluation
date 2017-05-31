package com.jhzy.receptionevaluation.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.SDCarkUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


/**
 * Created by Administrator on 2017/2/21.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Camera.AutoFocusCallback {


    private final String TAG = "---------";

    private Context mContext;
    private SurfaceHolder holder;
    private Camera mCamera;


    private String path = "";


    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public String getPath() {
        return path;
    }

    public void takePic() {
//        setCameraParams(w, h);
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
                    //旋转图片
                    private Bitmap roatePic(byte[] bytes) {
                        //获取拍摄的图片，并顺时针旋转90度
                        Bitmap bm0 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        Matrix m = new Matrix();
                        m.setRotate(90, (float) bm0.getWidth(), (float) bm0.getHeight());
                        return Bitmap.createBitmap(bm0, 0, 0, bm0.getWidth(), bm0.getHeight(), m, true);
                    }

                    @Override
                    public void onPictureTaken(byte[] bytes, Camera camera) {
                        BufferedOutputStream bos = null;
                        Bitmap bm = null;
                        try {
                            //旋转图片
                            bm = roatePic(bytes);
                            //判断是否存在sd卡
                            boolean exitsSDCard = SDCarkUtils.isExitsSDCard();
                            if (exitsSDCard) {
                                //创建文件夹
                                boolean checkDirExists = SDCarkUtils.checkDirExists(CONTACT.APP_DIR);
                                path = CONTACT.APP_DIR + System.currentTimeMillis() + ".jpg";//照片保存路径
                                File file = new File(path);
                                bos = new BufferedOutputStream(new FileOutputStream(file));
                                if (bytes.length >= 1024 * 1024) {
                                    bm.compress(Bitmap.CompressFormat.JPEG, 30, bos);//将图片压缩到流中
                                } else {
                                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中
                                }
                            } else {
                                Toast.makeText(mContext, "没有检测到内存卡", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(mContext, "拍照成功", Toast.LENGTH_SHORT).show();
                            mCamera.setPreviewDisplay(null);
                        } catch (Exception e) {
                            path = "";
                            e.printStackTrace();
                        } finally {
                            try {
                                if (bos != null) {
                                    bos.flush();//输出
                                    bos.close();//关闭
                                }
                                if (bm != null) {
                                    bm.recycle();// 回收bitmap空间
                                }
                                mCamera.stopPreview();// 关闭预览
                                mCamera.startPreview();// 开启预览
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    /**
     * 将图片的旋转角度置为0, 剪切图片会有问题
     *
     * @param path
     * @return void
     * @Title: setPictureDegreeZero
     * @date 2012-12-10 上午10:54:46
     */
    private void setPictureDegreeZero(String path) throws IOException {
        ExifInterface exifInterface = new ExifInterface(path);
        // 修正图片的旋转角度，设置其不旋转。这里也可以设置其旋转的角度，可以传值过去，
        // 例如旋转90度，传值ExifInterface.ORIENTATION_ROTATE_90，需要将这个值转换为String类型的
        exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "" + ExifInterface.ORIENTATION_ROTATE_90);
        exifInterface.saveAttributes();
    }


    private void initView() {
        holder = getHolder();//获得surfaceHolder引用
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);//设置类型
    }


    @Override
    public void onAutoFocus(boolean b, Camera camera) {
        if (b) {
            Log.e(TAG, "onAutoFocus: " + b);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceCreated: ");
        if (mCamera == null) {
            mCamera = Camera.open();//开启相机
            try {
                mCamera.setPreviewDisplay(holder);//摄像头画面显示在Surface上
                mCamera.setDisplayOrientation(90);
                Camera.Parameters parameters = mCamera.getParameters();
                if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
                }

                mCamera.cancelAutoFocus();//自动对焦。
                mCamera.setParameters(parameters);
                mCamera.startPreview(); // 开始预览
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int holder, int width, int height) {
        Log.e(TAG, "surfaceChanged: " + width + height);
//        setCameraParams(width, height);
//        mCamera.startPreview();
        // 获取各项参数
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPreviewSize(width, height); // 设置预览大小
        parameters.setPictureSize(width, height);
        parameters.setJpegQuality(100); // 设置照片质量
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Log.e(TAG, "surfaceDestroyed: ");
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        holder = null;
    }

//************************************************以下无效*****************************************************************************

    /**
     * 设置一些参数
     */
    private void setCameraParams(int width, int height) {
        Camera.Parameters parameters = mCamera.getParameters();
        // 获取摄像头支持的PictureSize列表
//        List<Camera.Size> pictureSizeList = parameters.getSupportedPictureSizes();
//        for (Camera.Size size : pictureSizeList) {
//            Log.i(TAG, "pictureSizeList size.width=" + size.width + "  size.height=" + size.height);
//        }
//        /**从列表中选取合适的分辨率*/
////        Camera.Size picSize = getProperSize(pictureSizeList, ((float) width / height));
//
//        Camera.Size picSize = pictureSizeList.get(pictureSizeList.size() - 1);
//        if (null == picSize) {
//            Log.i(TAG, "null == picSize");
//            picSize = parameters.getPictureSize();
//        }
//        Log.i(TAG, "picSize.width=" + picSize.width + "  picSize.height=" + picSize.height);
        // 根据选出的PictureSize重新设置SurfaceView大小
        parameters.setPictureSize(width, height);

//        // 获取摄像头支持的PreviewSize列表
//        List<Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
//
//        for (Camera.Size size : previewSizeList) {
//            Log.i(TAG, "previewSizeList size.width=" + size.width + "  size.height=" + size.height);
//        }
//        Camera.Size preSize = getProperSize(previewSizeList, ((float) width) / height);
//        if (null != preSize) {
//            Log.i(TAG, "preSize.width=" + preSize.width + "  preSize.height=" + preSize.height);
//            parameters.setPreviewSize(preSize.width, preSize.height);
//        }
//        Log.e(TAG, "setCameraParams: " + getMeasuredWidth() + "  " + getMeasuredHeight());
//        parameters.setJpegQuality(100); // 设置照片质量
//        if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
//            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
//        }

//        mCamera.cancelAutoFocus();//自动对焦。
//        mCamera.setDisplayOrientation(0);// 设置PreviewDisplay的方向，效果就是将捕获的画面旋转多少度显示
        mCamera.setParameters(parameters);
    }

    /**
     * 从列表中选取合适的分辨率
     * 默认w:h = 4:3
     * <p>注意：这里的w对应屏幕的height
     * h对应屏幕的width<p/>
     */
    private Camera.Size getProperSize(List<Camera.Size> pictureSizeList, float screenRatio) {
        Log.i(TAG, "screenRatio=" + screenRatio);
        Camera.Size result = null;
        for (Camera.Size size : pictureSizeList) {
            float currentRatio = ((float) size.width) / size.height;
            if (currentRatio - screenRatio == 0) {
                result = size;
                break;
            }
        }

        if (null == result) {
            for (Camera.Size size : pictureSizeList) {
                float curRatio = ((float) size.width) / size.height;
                if (curRatio == 4f / 3) {// 默认w:h = 4:3
                    result = size;
                    break;
                }
            }
        }

        return result;
    }

}







