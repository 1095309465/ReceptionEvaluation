package com.jhzy.receptionevaluation.api;

import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.CourseRecordBean;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugCode;
import com.jhzy.receptionevaluation.ui.bean.drugnext.DrugNext;
import com.jhzy.receptionevaluation.ui.bean.drugnext.Insulin;
import com.jhzy.receptionevaluation.ui.bean.drugnext.InsulinDetail;
import com.jhzy.receptionevaluation.ui.bean.drugtime.DrugTime;
import com.jhzy.receptionevaluation.ui.bean.NewCode;
import com.jhzy.receptionevaluation.ui.bean.STS;
import com.jhzy.receptionevaluation.ui.bean.assess.RenRelate;
import com.jhzy.receptionevaluation.ui.bean.assess.Zhuyi;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.EldersInfoCode;
import com.jhzy.receptionevaluation.ui.bean.login.LoginCode;
import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import com.jhzy.receptionevaluation.ui.bean.physical.PhysicalExaminationCode;
import com.jhzy.receptionevaluation.ui.bean.update.UpdateBean;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by bigyu on 2017/2/15.
 */

public interface RetrofitApi {


    /**
     * 登录到健康评估app
     *
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Account/OrgLogin")
    Call<LoginCode> login(@FieldMap Map<String, String> map);

    //    评估界面的老人列表
    @FormUrlEncoded
    @POST("v1_1/Health/AllElders")
    Call<EldersInfoCode> getEldersInfo(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 新增长者
     *
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/AddElder")
    Call<NewCode> newElder(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 获取长者信息
     *
     * @param auth_token
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/ElderList")
    Call<RenElderInfo> getElderInfo(@Header("Authorization") String auth_token, @Field("Elderid") String id);

    /**
     * 修改长者信息
     *
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/ElderEdit")
    Call<Code> editEdler(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);


    /**
     * 获取长者家属信息
     *
     * @param auth_token
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/Guardianin")
    Call<RenRelate> getElderRelate(@Header("Authorization") String auth_token, @Field("ElderID") String id);

    /**
     * 编辑家属资料
     *
     * @param auth_token
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/GuardianEdit")
    Call<Code> editElderRelate(@Header("Authorization") String auth_token, @Field("Guardianjson") String id);


    /**
     * http://192.168.0.38/HomeCareWebApi/v1_1/Health/ProgressNotes
     * 查看病程记录
     */
    @FormUrlEncoded
    @POST("v1_1/Health/ProgressNotes")
    Call<CourseRecordBean> getCourseRecord(@Header("Authorization") String auth_token, @Field("ElderID") String id);


    /**
     * 获取体检资料数据
     *
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/PhysicalExaminationList")
    Call<PhysicalExaminationCode> getPhysicalExaminationList(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 提交体检的数据
     *
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/PhysicalExamination")
    Call<Code> submitPhysicalData(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);
    /**
     * 获取注意事项
     * @param auth_token
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/Carefulmatter")
    Call<Zhuyi> getZhuYi(@Header("Authorization") String auth_token, @Field("ElderID") String id);

    //http://192.168.0.38/HomeCareWebApi/v1_1/Health/EditProgressNotes
    //新建病程记录
    @FormUrlEncoded
    @POST("v1_1/Health/EditProgressNotes")
    Call<Code> addCourseRecord(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 获取OSS凭证
     */
    @POST("/Public/GetPolicy")
    Call<STS> getPolicy(@Header("Authorization") String auth_token);


    /**
     * 版本更新
     * @return
     */
    @FormUrlEncoded
    @POST("public/AppVersion")
    Call<UpdateBean> getUpdateInfo(@FieldMap Map<String, String> map);

    @Streaming//大文件时要加不然会OOM
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    /**
     *提交注意事项
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/ElderAttention")
    Call<Code> submitAttention(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 获取配药发药的长者列表
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/AllElderUsageDrugInfo")
    Call<DrugCode> getAllElderUsageDrugInfo(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 获取配药时间
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/DragUsagetimeList")
    Call<DrugTime> getDrugUseTimeList(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 获得发药配药的数据
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/Dispensing")
    Call<DrugNext> getDrugNext(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 一键发药以及单个老人发药
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/DispensingAll")
    Call<Code> updateDispensingAll(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 配药
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/ElderDosage")
    Call<Code> updateDosage(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 获取长者胰岛素的记录
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/ElderInsulinRecord")
    Call<Insulin> getInsulin(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 修改胰岛素剂量
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/UpdateInSulinRecord")
    Call<Code> updateInsulinDosage(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 获取注射的详情
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/InSulinRecordDtail")
    Call<InsulinDetail> getInsulinDetail(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);

    /**
     * 注射胰岛素
     * @param auth_token
     * @param map
     * @return
     */
    @FormUrlEncoded
    @POST("v1_1/Health/InsulinRecord")
    Call<Code> inject(@Header("Authorization") String auth_token, @FieldMap Map<String, String> map);
}

