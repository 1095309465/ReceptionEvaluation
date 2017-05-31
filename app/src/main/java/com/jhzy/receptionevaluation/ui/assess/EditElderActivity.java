package com.jhzy.receptionevaluation.ui.assess;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.AppUtils;
import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.Code;
import com.jhzy.receptionevaluation.ui.bean.STS;
import com.jhzy.receptionevaluation.ui.bean.STSModel;
import com.jhzy.receptionevaluation.ui.bean.newelder.RenElderInfo;
import com.jhzy.receptionevaluation.ui.media.CameraActivity;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.Chooseimage_Dialog;
import com.jhzy.receptionevaluation.utils.CustomDialogutils;
import com.jhzy.receptionevaluation.utils.HttpUtils;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;
import com.jhzy.receptionevaluation.utils.NetWorkUtils;
import com.jhzy.receptionevaluation.utils.NewElderInfoPopupwindowUtils;
import com.jhzy.receptionevaluation.utils.OSSHelper;
import com.jhzy.receptionevaluation.utils.SPUtils;
import com.jhzy.receptionevaluation.utils.TimeUtils;
import com.jhzy.receptionevaluation.widget.DatePicklayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 编辑长者资料界面
 */
public class EditElderActivity extends BaseActivity {
    public static final String TAG = "editelder";
    private SB sb;
    // 弹出框对象
    private NewElderInfoPopupwindowUtils newElderInfoPopupwindowUtils;
    private String strImgPath;//照片绝对路径
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");//时间格式器
    private static final int FLAG_CHOOSE_IMG = 5;//选择相册图片的请求码
    private RenElderInfo.DataBean elderData;
    private String photoUrl = "";
    private CustomDialogutils customDialogutils;
    private String year;
    private boolean isChanged = false;
    private String birthString = "";
    private STSModel stsModel;
    private OSSHelper ossHelper;


    @Override
    public int getContentView() {
        return R.layout.activity_edit_elder;
    }


    @Override
    public void init() {
        Bundle bundle = getIntent().getExtras();
        elderData = (RenElderInfo.DataBean) bundle.getSerializable(TAG);
        customDialogutils = new CustomDialogutils(this);
        if (elderData != null) {
            sb = new SB();
            newElderInfoPopupwindowUtils = new NewElderInfoPopupwindowUtils(mContext);
            initListener();
            show();
        }

    }


    /**
     * 上传头像
     */
    private void getSTS(final String strImgPath) {
        Log.e("rxf", strImgPath);
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
                            String host = sts.getData().getHost();
                            String bucketName = sts.getData().getBucketName();
                            String dir = sts.getData().getID();
                            stsModel = new STSModel(id, secret, token, expire, dir, CONTACT.subDirElder,host,bucketName,sb.name.getText().toString().trim());
                            ossHelper = OSSHelper.INSTANCE(mContext, stsModel);
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
                                }
                            });
                        } else {
                            Toast.makeText(EditElderActivity.this, "获取凭证失败", Toast.LENGTH_SHORT).show();
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
     * 加载数据
     */
    private void show() {

        sb.name.setText(elderData.getElderName());
        sb.ident.setText(elderData.getIDCard());
        sb.sex.setText(elderData.getGender());
        sb.birth.setText(getBirth());
//        int i = Integer.parseInt(year);
//        int age = TimeUtils.year() - i;
//        sb.age.setText("" + age);
        sb.address.setText(elderData.getAddress());
        sb.des.setText(elderData.getSicknessNote());
        ImageLoaderUtils.load(sb.head, elderData.getPhotoUrl());
    }


    private void initListener() {
        sb.ok.setOnClickListener(noDouble);
        sb.back.setOnClickListener(noDouble);
        //sb.headClick.setOnClickListener(noDouble);
        sb.nameClick.setOnClickListener(noDouble);
        sb.idClick.setOnClickListener(noDouble);
        sb.sexClick.setOnClickListener(noDouble);
        sb.birthClick.setOnClickListener(noDouble);
        sb.addressClick.setOnClickListener(noDouble);
        sb.head.setOnClickListener(noDouble);
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
        sb.head.setImageURI(uri);
        getSTS(strImgPath);
    }


    public static final String IMAGE_UNSPECIFIED = "image/*";
    private static final int PHOTORESOULT = 0;


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


    private static final int myCamera = 2000;


    /**
     * 非系统自带的拍照
     */
    public void myCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, myCamera);
    }


    OnClickListenerNoDouble noDouble = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            switch (view.getId()) {
                case R.id.back:
                    finish();
                    break;
                case R.id.photo:
                    // String parentFile = "/mnt/sdcard/头像文件夹";
                    String parentFile = CONTACT.FilePathContact.PICTUREPATH;
                    File f = new File(parentFile);
                    if (!f.exists()) {
                        f.mkdirs();
                    }
                    strImgPath = parentFile + "/" + formatter.format(new Date()) + ".jpg";
                    Chooseimage_Dialog dialog = new Chooseimage_Dialog(EditElderActivity.this,
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
                case R.id.name_click:
                    //Toast.makeText(mContext, "点击名字", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.id_click:
                    //Toast.makeText(mContext, "点击身份证", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.sex_layout:
                    chooseSex(view);
                    break;
                case R.id.birth_layout:
                    chooseBirth(view);
                    break;
                case R.id.address_click:
                    //Toast.makeText(mContext, "点击了地址", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.ok:
                    submit();
                    break;
            }
        }
    };


    /**
     * 提交修改
     */
    private void submit() {
        if (!AppUtils.compileIdent(sb.ident.getText().toString().trim())) {
            Toast.makeText(mContext, "身份证格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        customDialogutils.setResfreshDialog("正在提交");
        TreeMap<String, String> map = new TreeMap<>();
        map.put("ElderID", elderData.getID() + "");
        map.put("PhotoUrl", photoUrl);
        map.put("ElderName", sb.name.getText().toString().trim());
        map.put("Gender", sb.sex.getText().toString().trim());
        map.put("IDCard", sb.ident.getText().toString().trim());
        map.put("Address", sb.address.getText().toString().trim());
        map.put("BirthDate", birthString);
        map.put("SicknessNote", sb.des.getText().toString().trim());
        map.put("BedID", elderData.getBedid() + "");
        Log.e("rxf", "basic " + SPUtils.find(CONTACT.TOKEN));
        HttpUtils.getInstance()
                .getRetrofitApi()
                .editEdler("basic " + SPUtils.find(CONTACT.TOKEN), map)
                .enqueue(
                        new Callback<Code>() {
                            @Override
                            public void onResponse(Call<Code> call, Response<Code> response) {
                                Code body = response.body();
                                if (body != null && CONTACT.DataSuccess.equals(body.getCode())) {
                                    customDialogutils.cancelNetworkDialog("提交成功", true);
                                    isChanged = true;
                                    backEvent();
                                } else {
                                    customDialogutils.cancelNetworkDialog("服务器异常，提交失败", false);
                                }
                            }


                            @Override
                            public void onFailure(Call<Code> call, Throwable t) {
                                if(NetWorkUtils.isNetworkConnected(mContext)){
                                    customDialogutils.cancelNetworkDialog("服务器异常", false);
                                }else{
                                    customDialogutils.cancelNetworkDialog("网络异常", false);
                                }
                            }
                        });
    }


    @Override
    public void onBackPressed() {
        backEvent();
    }


    /**
     * 返回改变状态
     */
    private void backEvent() {
        Intent intent = new Intent();
        if (isChanged) {
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED, intent);
        }
        finish();
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
                viewHolder.popupWindow.dismiss();
                // 设置生日
                sb.birth.setText(year + "年" + month + "月" + day + "日");
                //设置年龄
                int age = TimeUtils.year() - year;
//                sb.age.setText("" + age);
                birthString = year + "-" + month + "-" + day;
            }
        });
    }


    //选择性别
    private void chooseSex(View view) {
        final NewElderInfoPopupwindowUtils.ViewHolder viewHolder
                = newElderInfoPopupwindowUtils.popChooseSex(view);
        OnClickListenerNoDouble listener = new OnClickListenerNoDouble() {
            @Override
            public void myOnClick(View view) {
                int id = view.getId();
                switch (id) {
                    case R.id.female:
                        sb.sex.setText("女");
                        break;
                    case R.id.male:
                        sb.sex.setText("男");
                        break;
                }
                viewHolder.popupWindow.dismiss();
            }
        };
        viewHolder.male.setOnClickListener(listener);
        viewHolder.female.setOnClickListener(listener);

    }


    private String getBirth() {
        String birthDate = elderData.getBirthDate();
        if (!TextUtils.isEmpty(birthDate)) {
            String dateToYearMonth = TimeUtils.dateString(birthDate);
            birthString = TimeUtils.dateToYearMonth(birthDate);
            return dateToYearMonth;
        } else {
            return null;
        }
//        if (birthDate.length() > 10) {
//
//            birthString = elderData.getBirthDate().substring(0, 10);
//            year = birthDate.substring(0, 4);
//            String month = birthDate.substring(5, 7);
//            String day = birthDate.substring(8, 10);
//            if (month.startsWith("0")) {
//                month = month.substring(1, 2);
//            }
//            if (day.startsWith("0")) {
//                day = day.substring(1, 2);
//            }
//            Log.e("rxf", year + "//" + month + "//" + day);
//            return month + "月" + day + "日";
//        } else {
//            return "生日信息暂无";
//        }

    }


    class SB {
        private ImageView back;
        private SimpleDraweeView head;
        private EditText name;
        private EditText ident;
        private TextView sex;
        private ImageView sexDown;
        private TextView birth;
        private ImageView birthDown;
        //        privatal TextView age;
        private EditText address;
        private EditText des;
        private TextView ok;
        private View headClick;
        private View nameClick;
        private View idClick;
        private View sexClick;
        private View birthClick;
        private View addressClick;


        public SB() {
            back = ((ImageView) findViewById(R.id.back));
            head = ((SimpleDraweeView) findViewById(R.id.photo));
            name = ((EditText) findViewById(R.id.name));
            ident = ((EditText) findViewById(R.id.ident));
            sex = ((TextView) findViewById(R.id.sex));
            sexDown = ((ImageView) findViewById(R.id.sex_down));
            birth = ((TextView) findViewById(R.id.birth));
            birthDown = ((ImageView) findViewById(R.id.birth_down));
//            age = ((TextView) findViewById(age));
            address = ((EditText) findViewById(R.id.address));
            des = ((EditText) findViewById(R.id.des));
            ok = ((TextView) findViewById(R.id.ok));

            headClick = findViewById(R.id.head_click);
            nameClick = findViewById(R.id.name_click);
            idClick = findViewById(R.id.id_click);
            sexClick = findViewById(R.id.sex_layout);
            birthClick = findViewById(R.id.birth_layout);
            addressClick = findViewById(R.id.address_click);
        }
    }
}
