package com.jhzy.receptionevaluation.utils;

import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.jhzy.receptionevaluation.ui.bean.STSModel;
import java.util.UUID;

/**
 * Created by nakisaRen
 * on 17/3/7.
 */

public class OSSHelper {
    private String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    //private String videoPath = "companion_photo/video/";
    private String imageDir = "companion_photo/image/";
    private OSS oss;
    private PutObjectRequest put;
    private OSSProgress ossProgress;
    private OSSState ossState;

    private static int i;
    private String filename;
    private static OSSHelper ossHelper = null;
    private String bucketName = "";
    private STSModel stsModel;


    public static OSSHelper INSTANCE(Context context, final STSModel stsModel) {
        ossHelper = new OSSHelper(context, stsModel);
        return ossHelper;
    }


    public interface OSSState {
        void state(boolean state, String path);
    }


    public void setOKCallback(OSSState ossState) {
        this.ossState = ossState;
    }


    public interface OSSProgress {
        void progress(long currentSize, long totalSize);
    }


    public void setProgressCallback(OSSProgress ossProgress) {
        this.ossProgress = ossProgress;
    }


    private OSSHelper(Context context, final STSModel stsModel) {
        this.stsModel = stsModel;
        OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {

            @Override
            public OSSFederationToken getFederationToken() {
                // 您需要在这里实现获取一个FederationToken，并构造成OSSFederationToken对象返回
                // 如果因为某种原因获取失败，可直接返回nil

                OSSFederationToken token = new OSSFederationToken(stsModel.getId(),
                    stsModel.getSecret(), stsModel.getToken(), stsModel.getExpire());
                // 下面是一些获取token的代码，比如从您的server获取
                return token;
            }
        };
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(10); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(1); // 失败后最大重试次数，默认2次
        imageDir = stsModel.getDir();
        oss = new OSSClient(context.getApplicationContext(), endpoint, credentialProvider, conf);
    }


    public void uploadPhoto(String path) {
        filename = UUID.randomUUID().toString();
        put = new PutObjectRequest(stsModel.getBucketName(), "Photo/" + stsModel.getName() + "_" + filename + ".jpg",
            path);
        up(put);
    }


    //上传
    private void up(PutObjectRequest put) {
        oss.asyncPutObject(put,
            new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                    String path = "http://zhihuizunyang.oss-cn-shanghai.aliyuncs.com/" +
                        putObjectRequest.getObjectKey();
                    Log.e("rxf",putObjectRequest.getObjectKey());
                    Log.e("rxf", "uploadSuccess" + "//" + path);
                    if (ossState != null) {
                        ossState.state(true, putObjectRequest.getObjectKey());
                    }
                }


                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
                    if (ossState != null) {
                        ossState.state(false, e.getMessage());
                    }
                }
            });
    }


    /*public void switchPath(String path,int i) {
        filename = UUID.randomUUID().toString();
        switch (i) {
            *//*case 1:
                put = new PutObjectRequest("qszy", videoPath + filename + ".mp4",
                    path);
                put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                    @Override
                    public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                        Log.d("rxff", "currentSize: " + currentSize + " totalSize: " + totalSize);
                        if (ossProgress != null) {
                            ossProgress.progress(currentSize, totalSize);
                        }
                    }

                });
                break;*//*
            case 2:
                put = new PutObjectRequest(bucketName, "Org/"+ + filename + ".jpg",
                    path);
                break;
            case 3:
                put = new PutObjectRequest(bucketName,imagePath + filename + ".jpg",path);
                break;
        }
        up(put);
    }*/
}
