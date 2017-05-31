package com.jhzy.receptionevaluation.utils;


import com.jhzy.receptionevaluation.api.RetrofitApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/2/15.
 * 网络请求工具
 */
public class HttpUtils {
    private static HttpUtils httpUtils = null;
    private RetrofitApi retrofitApi;
    private String demingBaseUrl = "http://192.168.20.145:32650/";
    private  String base = "http://192.168.30.244/HomeCareWebApi/";
    private String baseUrl_38 = "http://192.168.0.38/HomeCareWebApi/";
    private String test_url = "http://test-api.91sxmd.com/";


    //网页地址
    public static  String BASEURL = "http://www.91sxmd.com/OrgWeb/";

    private String OfficialURl = "http://www.91sxmd.com/HomeCareWebApi/";

    private HttpUtils() {
        OkHttpClient httpClient = okHttpClient();
        retrofitApi = new Retrofit.Builder()
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(OfficialURl)
                .build()
                .create(RetrofitApi.class);
    }

    public static synchronized HttpUtils getInstance() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        return httpUtils;
    }

    public RetrofitApi getRetrofitApi() {
        return retrofitApi;
    }


    private OkHttpClient okHttpClient() {
        //手动创建一个OkHttpClient并设置超时时间
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request()
                                .newBuilder()
                                //                                .addHeader("appinfo", addAppInfo())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }
}