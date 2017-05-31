package com.jhzy.receptionevaluation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.NewCode;
import com.jhzy.receptionevaluation.ui.bean.STS;
import com.jhzy.receptionevaluation.ui.bean.STSModel;
import com.jhzy.receptionevaluation.ui.bean.newelder.FamilyMember;
import com.jhzy.receptionevaluation.ui.bean.newelder.NewElderInfo;
import com.jhzy.receptionevaluation.ui.media.CameraActivity;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.Chooseimage_Dialog;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.NewElderInfoPopupwindowUtils;
import com.jhzy.receptionevaluation.utils.OSSHelper;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.widget.DatePicklayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 新建长者信息界面
 */
public class NewElderInfoActivity extends BaseActivity {

    // 控件初始化
    private ViewHolder vh;
    // 弹出框对象
    private NewElderInfoPopupwindowUtils newElderInfoPopupwindowUtils;
    private NewElderInfo newElderInfo;
    private FamilyMember familyMember;
    private boolean isShowOtherFamily = false; // 是否显示第二家属

    private String strImgPath;//照片绝对路径
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");//时间格式器
    private static final int FLAG_CHOOSE_IMG = 5;//选择相册图片的请求码
    private CustomDialogutils customDialogutils;
    private String photoUrl = "";

    private String birthDate = ""; //出生年月日

    private String localPhotoUrl = "";

    public static void start(Context context, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setClass(context, NewElderInfoActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int getContentView() {
        return (R.layout.activity_new_elder_info);
    }


    @Override
    public void init() {
        super.init();
        initUtils();
        initListener();
    }


    private void initUtils() {
        // 控件初始化
        vh = new ViewHolder();
        // 弹出框对象
        newElderInfoPopupwindowUtils = new NewElderInfoPopupwindowUtils(mContext);
        //老人数据对象
        newElderInfo = new NewElderInfo();
        //老人家属对象
        familyMember = new FamilyMember();

        customDialogutils = new CustomDialogutils(mContext);
    }


    private void initListener() {
        // 性别选择
        vh.sexLayout.setOnClickListener(onClickListener);
        //生日选择
        vh.birthLayout.setOnClickListener(onClickListener);
        //新增家属资料
        vh.addFamilyMember.setOnClickListener(onClickListener);
        //删除家属资料
        vh.deleteFamilyMember.setOnClickListener(onClickListener);
        //家属1性别选择
        vh.familySexLayout_1.setOnClickListener(onClickListener);
        vh.sex2.setOnClickListener(onClickListener);
        vh.back.setOnClickListener(onClickListener);
        vh.photo.setOnClickListener(onClickListener);
        vh.ok.setOnClickListener(onClickListener);
    }


    class ViewHolder {
        private final TextView ok;
        private final View sex2;
        private View sexLayout; // 性别选择
        private TextView sex; // 性别
        private View birthLayout; //生日选择
        private TextView birth; // 生日
        private EditText name; // 姓名
        //        private TextView age; // 年龄
        private EditText address; //地址
        private EditText ident; // 身份证

        private View familySexLayout_1; // 家属性别选择
        private TextView familySex_1; // 家属性别
        private View addFamilyMember; // 增加家属资料
        private EditText familyName_1; // 家属姓名
        private EditText familyRelation_1; //家属关系
        private EditText familyPhone_1; // 家属电话

        private View familyLayout_2;  // 家属资料2的布局
        private View deleteFamilyMember; // 删除家属资料
        private EditText familyname_2;
        private TextView familySex_2;
        private EditText familyRelation_2;
        private EditText familyPhone_2;
        private View back;
        private SimpleDraweeView photo;
        private EditText SicknessNote;


        public ViewHolder() {
            photo = (SimpleDraweeView) findViewById(R.id.photo);
            sexLayout = findViewById(R.id.sex_layout);
            sex = ((TextView) findViewById(R.id.sex));
            birthLayout = findViewById(R.id.birth_layout);
            birth = ((TextView) findViewById(R.id.birth));
            name = ((EditText) findViewById(R.id.name));
//            age = ((TextView) findViewById(age));
            familySexLayout_1 = findViewById(R.id.family_members_sex_layout_1);
            familySex_1 = ((TextView) findViewById(R.id.family_members_sex_1));
            familyLayout_2 = findViewById(R.id.family_members_layout_2);
            addFamilyMember = findViewById(R.id.add_family_member);
            deleteFamilyMember = findViewById(R.id.delete_family_member);
            ident = ((EditText) findViewById(R.id.ident));
            address = ((EditText) findViewById(R.id.address));
            familyName_1 = ((EditText) findViewById(R.id.family_members_name_1));
            familyRelation_1 = ((EditText) findViewById(R.id.family_members_relation_1));
            familyPhone_1 = (EditText) findViewById(R.id.family_members_phone_1);
            familyname_2 = ((EditText) findViewById(R.id.family_members_name_2));
            familySex_2 = ((TextView) findViewById(R.id.family_members_sex_2));
            familyRelation_2 = ((EditText) findViewById(R.id.family_members_relation_2));
            familyPhone_2 = ((EditText) findViewById(R.id.family_members_phone_2));
            back = findViewById(R.id.back);
            ok = ((TextView) findViewById(R.id.family_ok));
            sex2 = findViewById(R.id.family_sex_layout_2);
            SicknessNote = ((EditText) findViewById(R.id.SicknessNote));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLAG_CHOOSE_IMG://系统相册
                if (resultCode != Activity.RESULT_OK) {
                    return;
                }
                Uri uri1 = data.getData();
                Cursor cursor = getContentResolver().query(uri1, new String[]{"_data"}, null,
                        null, null);
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


    private final String imgType = "file://";


    /**
     * \
     * 上传头像和加载头像
     */
    private void updatePhoto(String strImgPath) {
        //photo
        Uri uri = Uri.parse(imgType + strImgPath);
        vh.photo.setImageURI(uri);
        //OSSHelper.INSTANCE()
        localPhotoUrl = strImgPath;
    }

    /**
     * 上传头像
     */
    private void getSTS(final String strImgPath) {
        HttpUtils.getInstance()
                .getRetrofitApi()
                .getPolicy("basic " + SPUtils.find(CONTACT.TOKEN))
                .enqueue(new Callback<STS>() {
                    @Override
                    public void onResponse(Call<STS> call, Response<STS> response) {
                        STS sts = response.body();
                        if (sts != null && CONTACT.DataSuccess.equals(sts.getCode())) {
                            String id = sts.getData().getAccessid();
                            String secret = sts.getData().getSignature();
                            String token = sts.getData().getPolicy();
                            String expire = sts.getData().getExpire();
                            String dir = sts.getData().getID();
                            String host = sts.getData().getHost();
                            String bucketName = sts.getData().getBucketName();
                            STSModel stsModel = new STSModel(id, secret, token, expire, dir, CONTACT.subDirElder,host,bucketName,vh.name.getText().toString().trim());
                            OSSHelper ossHelper = OSSHelper.INSTANCE(mContext, stsModel);
                            ossHelper.uploadPhoto(strImgPath);
                            ossHelper.setOKCallback(new OSSHelper.OSSState() {
                                @Override
                                public void state(boolean state, String path) {
                                    if (state) {
                                        photoUrl = path;
                                    } else {
                                        Toast.makeText(mContext, "服务器异常，头像上传失败", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    uploadInfo();
                                }
                            });
                        }

                    }


                    @Override
                    public void onFailure(Call<STS> call, Throwable t) {
                        if(NetWorkUtils.isNetworkConnected(mContext)){
                            Toast.makeText(mContext, "服务器异常", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(mContext, "网络异常", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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


    public static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTORESOULT = 0;

    OnClickListenerNoDouble onClickListener = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.sex_layout:  // 性别选择
                    view.setTag(vh.sex);
                    chooseSex(view);
                    break;
                case R.id.family_members_sex_layout_1: // 家属1性别选择
                    view.setTag(vh.familySex_1);
                    chooseSex(view);
                    break;
                case R.id.family_members_sex_layout_2:// 家属2性别选择
                    view.setTag(vh.familySex_2);
                    chooseSex(view);
                    break;

                case R.id.birth_layout:////生日选择
                    chooseBirth(view);
                    break;
                case R.id.add_family_member: // 新增家属资料
                    vh.familyLayout_2.setVisibility(View.VISIBLE);
                    vh.addFamilyMember.setVisibility(View.GONE);
                    isShowOtherFamily = true;
                    break;
                case R.id.delete_family_member:  // 删除家属资料
                    vh.familyLayout_2.setVisibility(View.GONE);
                    vh.addFamilyMember.setVisibility(View.VISIBLE);
                    isShowOtherFamily = false;
                    break;
                case R.id.back:
                    finish();
                    break;

                case R.id.photo://拍照
                    //  String parentFile = "/mnt/sdcard/头像文件夹";
                    String parentFile = CONTACT.FilePathContact.PICTUREPATH;
                    File f = new File(parentFile);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    strImgPath = parentFile + "/" + formatter.format(new Date()) + ".jpg";
                    Chooseimage_Dialog dialog = new Chooseimage_Dialog(NewElderInfoActivity.this,
                            R.style.mydialog) {
                        @Override
                        public void doGoToImg() {//相册
                            super.doGoToImg();
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, FLAG_CHOOSE_IMG);
                            this.dismiss();
                        }


                        @Override
                        public void doGoToPhone() {//拍照
                            super.doGoToPhone();
                            myCamera();
                            this.dismiss();

                        }
                    };
                    dialog.setTitle("请选择图片");
                    dialog.show();

                    break;

                case R.id.family_ok:
                    initEldersInfo();
                    break;
                case R.id.family_sex_layout_2:
                    view.setTag(vh.familySex_2);
                    chooseSex(view);
                    break;
            }

        }

    };
    private static final int myCamera = 2000;


    /**
     * 非系统自带的拍照
     */
    public void myCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, myCamera);
    }


    //生日选择
    private void chooseBirth(View view) {
        final NewElderInfoPopupwindowUtils.ViewHolder viewHolder
                = newElderInfoPopupwindowUtils.popChooseBirth(view);
        viewHolder.sure.setOnClickListener(new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                DatePicklayout picklayout = viewHolder.datePicklayout;
                int year = picklayout.getYear();
                int month = picklayout.getMonth();
                int day = picklayout.getDay();
                birthDate = year + "-" + month + "-" + day;
                viewHolder.popupWindow.dismiss();
                // 设置生日
                vh.birth.setText(year + "年" + month + "月" + day + "日");
                //设置年龄
//                int age = TimeUtils.year() - year;
//                vh.age.setText("" + age);
            }
        });

    }


    //选择性别
    private void chooseSex(final View v) {
        final NewElderInfoPopupwindowUtils.ViewHolder viewHolder
                = newElderInfoPopupwindowUtils.popChooseSex(v);
        final TextView textView = (TextView) v.getTag();
        OnClickListenerNoDouble listener = new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.female:
                        textView.setText("女");
                        break;
                    case R.id.male:
                        textView.setText("男");
                        break;
                }
                viewHolder.popupWindow.dismiss();
            }
        };
        viewHolder.male.setOnClickListener(listener);
        viewHolder.female.setOnClickListener(listener);
    }


    /**
     * 提交数据
     */
    private void initEldersInfo() {
        /**
         * 长者数据
         */
        // 长者姓名
        String name = vh.name.getText().toString().trim();
        if (!ok(name)) {
            Toast.makeText(mContext, "请填写姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        // 身份证号
        String ident = vh.ident.getText().toString().trim();
        if (!ok(ident)) {
            Toast.makeText(mContext, "请填写身份证号", Toast.LENGTH_SHORT).show();
            return;
        }
        //性别
        String sex = vh.sex.getText().toString().trim();
        if (!ok(sex)) {
            Toast.makeText(mContext, "请选择性别", Toast.LENGTH_SHORT).show();
            return;
        }
        //生日
        String birth = vh.birth.getText().toString().trim();
//        //年龄
//        String age = vh.age.getText().toString().trim();
        if (!ok(birth)) {
            Toast.makeText(mContext, "请选择出生年月", Toast.LENGTH_SHORT).show();
            return;
        }
        //住址
        String address = vh.address.getText().toString().trim();
        if (!ok(address)) {
            Toast.makeText(mContext, "请填写住址", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 家属资料1
         */
        String fName_1 = vh.familyName_1.getText().toString().trim();
        if (!ok(fName_1)) {
            Toast.makeText(mContext, "请填写家属姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        String fSex_1 = vh.familySex_1.getText().toString().trim();
        if (!ok(fSex_1)) {
            Toast.makeText(mContext, "请选择家属性别", Toast.LENGTH_SHORT).show();
            return;
        }
        String fRelation_1 = vh.familyRelation_1.getText().toString().trim();
        if (!ok(fRelation_1)) {
            Toast.makeText(mContext, "请填写家属与长者关系", Toast.LENGTH_SHORT).show();
            return;
        }
        String fPhone_1 = vh.familyPhone_1.getText().toString().trim();
        if (!ok(fPhone_1)) {
            Toast.makeText(mContext, "请填写家属联系电话", Toast.LENGTH_SHORT).show();
            return;
        }

        String fName_2 = "";
        String fSex_2 = "";
        String fRelation_2 = "";
        String fPhone_2 = "";

        /**
         * 家属资料2
         */
        if (vh.familyLayout_2.getVisibility() == View.VISIBLE) {
            fName_2 = vh.familyname_2.getText().toString().trim();
            if (!ok(fName_2)) {
                Toast.makeText(mContext, "请填写家属2姓名", Toast.LENGTH_SHORT).show();
                return;
            }
            fSex_2 = vh.familySex_2.getText().toString().trim();
            if (!ok(fSex_2)) {
                Toast.makeText(mContext, "请选择家属2性别", Toast.LENGTH_SHORT).show();
                return;
            }
            fRelation_2 = vh.familyRelation_2.getText().toString().trim();
            if (!ok(fRelation_2)) {
                Toast.makeText(mContext, "请填写家属2与长者关系", Toast.LENGTH_SHORT).show();
                return;
            }
            fPhone_2 = vh.familyPhone_2.getText().toString().trim();
            if (!ok(fPhone_2)) {
                Toast.makeText(mContext, "请填写家属2联系电话", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(TextUtils.isEmpty(localPhotoUrl)){
            uploadInfo();
        }else{
            getSTS(localPhotoUrl);
        }
    }

    private void uploadInfo(){
        TreeMap<String, String> map = new TreeMap<>();
        map.put("ElderName", vh.name.getText().toString().trim());
        map.put("PhotoUrl", photoUrl);
        map.put("Gender", vh.sex.getText().toString().trim());
        map.put("IDCard", vh.ident.getText().toString().trim());
        map.put("BirthDate", birthDate);
        map.put("Address", vh.address.getText().toString().trim());
        map.put("SicknessNote" , vh.SicknessNote.getText().toString().trim());
        JSONArray jsonArray = new JSONArray();
        if (vh.familyLayout_2.getVisibility() == View.VISIBLE) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("GuardianName", vh.familyname_2.getText().toString().trim());
                jsonObject.put("Gender", vh.familySex_2.getText().toString().trim());
                jsonObject.put("ContactPhone", vh.familyPhone_2.getText().toString().trim());
                jsonObject.put("Relationship", vh.familyRelation_2.getText().toString().trim());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("GuardianName", vh.familyName_1.getText().toString().trim());
            jsonObject.put("Gender", vh.familySex_1.getText().toString().trim());
            jsonObject.put("ContactPhone", vh.familyPhone_1.getText().toString().trim());
            jsonObject.put("Relationship", vh.familyRelation_1.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(jsonObject);
        map.put("json", jsonArray.toString());
        customDialogutils.networkRefreshDiallog("正在提交资料...");
        HttpUtils.getInstance()
            .getRetrofitApi()
            .newElder("basic " + SPUtils.find(CONTACT.TOKEN), map)
            .enqueue(
                new Callback<NewCode>() {
                    @Override
                    public void onResponse(Call<NewCode> call, Response<NewCode> response) {
                        NewCode body = response.body();
                        if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                            Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
                            customDialogutils.cancelNetworkDialog("提交资料成功", true);
                            finish();
                        } else {
                            Toast.makeText(mContext, "异常", Toast.LENGTH_SHORT).show();
                            customDialogutils.cancelNetworkDialog("提交资料失败", false);
                        }
                    }


                    @Override
                    public void onFailure(Call<NewCode> call, Throwable t) {
                        if(NetWorkUtils.isNetworkConnected(mContext)){
                            customDialogutils.cancelNetworkDialog("服务器异常,提交资料失败", false);
                        }else{
                            customDialogutils.cancelNetworkDialog("网络异常,提交资料失败", false);
                        }
                    }
                });
    }


    private boolean ok(String text) {
        if (!TextUtils.isEmpty(text)) {
            return true;
        } else {
            return false;
        }
    }
}
