package com.jhzy.receptionevaluation.ui.bean;

/**
 * Created by nakisaRen
 * on 16/9/24.
 */
public class STS {

    /**
     * code : A00000
     * data : {"host":"http://qszy.oss-cn-shanghai.aliyuncs.com","policy":"CAES8AIIARKAAUu2VUeX3y7Uo3tCkkCRHpQg4JUgKyePFolmU6jW5WEE1gAZ1P+G69aFoeG4WBe5TJJuAc8bQ8/frliaf6rjfKByYms/KIb2rkvP19v0TVpaCQ6rPHk5ZfreH1B0nkSv5z99LHoYlvDmqUeJInvTFn8JyIHL+pexbNvrrgaBtTMGGh1TVFMuRDFzb1lWMTh6TEVRRUNQWTZFaHhBTFZSaSISMzY4OTI0Nzc0ODgyNTQ2MjY3Kgd1c2VyMDAxMMX3/Nj2KjoGUnNhTUQ1QkoKATEaRQoFQWxsb3cSGwoMQWN0aW9uRXF1YWxzEgZBY3Rpb24aAwoBKhIfCg5SZXNvdXJjZUVxdWFscxIIUmVzb3VyY2UaAwoBKkoQMTU4Mjc3NDg3MjI1NzE3M1IFMjY4NDJaD0Fzc3VtZWRSb2xlVXNlcmAAahIzNjg5MjQ3NzQ4ODI1NDYyNjdyCW9ubHl3cml0ZXiV5ZTw5/DnAg==","accessid":"STS.D1soYV18zLEQECPY6EhxALVRi","signature":"4pzHtAjQCi2HWR7zftvC7FxAg6gJVnECDVHWh86C8uTf","expire":"2016/9/27 18:09:51","dir":""}
     * msg : null
     */

    private String code;
    /**
     * host : http://qszy.oss-cn-shanghai.aliyuncs.com
     * policy : CAES8AIIARKAAUu2VUeX3y7Uo3tCkkCRHpQg4JUgKyePFolmU6jW5WEE1gAZ1P+G69aFoeG4WBe5TJJuAc8bQ8/frliaf6rjfKByYms/KIb2rkvP19v0TVpaCQ6rPHk5ZfreH1B0nkSv5z99LHoYlvDmqUeJInvTFn8JyIHL+pexbNvrrgaBtTMGGh1TVFMuRDFzb1lWMTh6TEVRRUNQWTZFaHhBTFZSaSISMzY4OTI0Nzc0ODgyNTQ2MjY3Kgd1c2VyMDAxMMX3/Nj2KjoGUnNhTUQ1QkoKATEaRQoFQWxsb3cSGwoMQWN0aW9uRXF1YWxzEgZBY3Rpb24aAwoBKhIfCg5SZXNvdXJjZUVxdWFscxIIUmVzb3VyY2UaAwoBKkoQMTU4Mjc3NDg3MjI1NzE3M1IFMjY4NDJaD0Fzc3VtZWRSb2xlVXNlcmAAahIzNjg5MjQ3NzQ4ODI1NDYyNjdyCW9ubHl3cml0ZXiV5ZTw5/DnAg==
     * accessid : STS.D1soYV18zLEQECPY6EhxALVRi
     * signature : 4pzHtAjQCi2HWR7zftvC7FxAg6gJVnECDVHWh86C8uTf
     * expire : 2016/9/27 18:09:51
     * dir :
     * bucketName:
     */

    private DataBean data;
    private Object msg;


    public String getCode() { return code;}


    public void setCode(String code) { this.code = code;}


    public DataBean getData() { return data;}


    public void setData(DataBean data) { this.data = data;}


    public Object getMsg() { return msg;}


    public void setMsg(Object msg) { this.msg = msg;}


    public static class DataBean {
        private String ID;
        private String host;
        private String policy;
        private String accessid;
        private String signature;
        private String expire;
        private String bucketName;


        public String getID() {
            return ID;
        }


        public void setID(String ID) {
            this.ID = ID;
        }


        public String getHost() { return host;}


        public void setHost(String host) { this.host = host;}


        public String getPolicy() { return policy;}


        public void setPolicy(String policy) { this.policy = policy;}


        public String getAccessid() { return accessid;}


        public void setAccessid(String accessid) { this.accessid = accessid;}


        public String getSignature() { return signature;}


        public void setSignature(String signature) { this.signature = signature;}


        public String getExpire() { return expire;}


        public void setExpire(String expire) { this.expire = expire;}


        public String getBucketName() {
            return bucketName;
        }


        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }
    }
}
