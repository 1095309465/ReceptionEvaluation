package com.jhzy.receptionevaluation.ui.bean;

import java.io.Serializable;

/**
 * Created by nakisaRen
 * on 16/9/27.
 */
public class STSModel implements Serializable {
    private String id;
    private String secret;
    private String token;
    private String expire;
    private String dir;
    private String subDir;
    private String host;
    private String bucketName;
    private String name;


    public STSModel(String id, String secret, String token, String expire, String dir, String subDir, String host, String bucketName,String name) {
        this.id = id;
        this.secret = secret;
        this.token = token;
        this.expire = expire;
        this.dir = dir;
        this.subDir = subDir;
        this.host = host;
        this.bucketName = bucketName;
        this.name = name;
    }


    public String getId() {
        return id;
    }


    public String getSecret() {
        return secret;
    }


    public String getToken() {
        return token;
    }


    public String getExpire() {
        return expire;
    }


    public String getDir() {
        return dir;
    }


    public String getSubDir() {
        return subDir;
    }


    public String getHost() {
        return host;
    }


    public String getBucketName() {
        return bucketName;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
