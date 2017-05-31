package com.jhzy.receptionevaluation.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.PictureListAdapter;
import com.jhzy.receptionevaluation.ui.adapter.VideoListAdapter;
import com.jhzy.receptionevaluation.ui.bean.PictureListBean;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.DeleteDialogUtil;
import com.jhzy.receptionevaluation.utils.Globars;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PictureListActivity extends BaseActivity {

    private RecyclerView recycle;
    private TextView tv_bianji;
    private TextView btn;
    private int type;
    private List<PictureListBean> mList;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    private PictureListAdapter adapter;
    private String filePath = "";
    private String name;
    private String coursepath;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_list);
        initData();
        initView();
        checkIsExitsPic();
    }*/


    @Override
    public int getContentView() {
        return R.layout.activity_picture_list;
    }


    @Override
    public void init() {
        initData();
        initView();
        checkIsExitsPic();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Toast.makeText(PictureListActivity.this, "拍照完成", Toast.LENGTH_SHORT).show();
        PictureListBean bean = new PictureListBean();
        bean.setPath(filePath);
        Date date = new Date();
        String time = formatter.format(date);
        bean.setTime(time);
        mList.add(bean);
        adapter.notifyDataSetChanged();
        checkIsExitsPic();

    }

    private void initData() {
        type = getIntent().getIntExtra("type", 1);
        if (type != 4) {
            Globars.newInstance().setNull();
        }
        name = getIntent().getStringExtra("name");
        mList = Globars.newInstance().getPictureList();
        coursepath = getIntent().getStringExtra("coursepath");
    }

    private void initView() {
        btn = (TextView) findViewById(R.id.btn);
        tv_bianji = (TextView) findViewById(R.id.tv_bianji);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        recycle.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new PictureListAdapter(mList, this);
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                boolean flag = adapter.showButton;
                if (flag) {
                    mList.get(position).setDelete(!mList.get(position).isDelete());
                    adapter.notifyDataSetChanged();
                    checkIsExitsPic();
                    Log.e("123", "已经点击，开始刷新=" + mList.toString());
                }
            }
        });
    }

    private String typePath = "评估";

    private void normalPic(String dataName) {
        filePath = Environment.getExternalStorageDirectory() + typePath + dataName + "/图片/";
        File f = new File(filePath);
        if (!f.exists()) {
            boolean flag = f.mkdirs();
            Log.e("123", "创建文件夹=" + flag);
        }
        filePath = Environment.getExternalStorageDirectory() + typePath + dataName + "/图片/" + formatter.format(new Date()) + ".jpg";
        Log.e("123", filePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        startActivityForResult(intent, type);
    }

    private void coursePic() {
        filePath = coursepath + "/图片/";
        File f = new File(filePath);
        if (!f.exists()) {
            boolean flag = f.mkdirs();
            Log.e("123", "创建文件夹=" + flag);
        }
        filePath = coursepath + "/图片/" + formatter.format(new Date()) + ".jpg";
        Log.e("123", filePath);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        startActivityForResult(intent, type);
    }

    public void btn(View view) {
        switch (view.getId()) {
            case R.id.btn://添加照片按钮
            {
                String content = (String) btn.getText();
                if ("添加照片".equals(content)) {

                    String date = format.format(new Date());
                    String dataName = "";
                    dataName = date + name;
                    switch (type) {
                        case 1://日常评估
                            typePath = CONTACT.FilePathContact.RICHANGPATH;
                            //  typeStr = "评估文件夹/日常评估/";
                            normalPic(dataName);
                            break;
                        case 2://入院评估
                            typePath = CONTACT.FilePathContact.RUYUANPATH;
                            normalPic(dataName);
                            //  typeStr = "评估文件夹/入院评估/";
                            break;
                        case 3://出院评估
                            typePath = CONTACT.FilePathContact.CHUYUANPATH;
                            normalPic(dataName);
                            // typeStr = "评估文件夹/出院评估/";
                            break;
                        case 4://病程记录
                            typePath = CONTACT.FilePathContact.BINGCHENGPATH;
                            //    typeStr = "病程文件夹/";
                            coursePic();
                            break;
                    }

                } else if ("删除".equals(content)) {
                    int num = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        boolean flag = mList.get(i).isDelete();
                        if (flag) {
                            num++;
                        }
                    }
                    if (num == 0) return;
                    new DeleteDialogUtil(PictureListActivity.this, "确认删除这" + num + "张照片吗？", new DeleteDialogUtil.OnDeleteClickListener() {
                        @Override
                        public void onDeleteOk() {
                            delete();
                        }

                        @Override
                        public void onDeleteCancle() {

                        }
                    }).show();
                }

            }
            break;
            case R.id.iv_back://返回按键
                finish();
                break;
            case R.id.tv_bianji://编辑
            {

                String content = tv_bianji.getText().toString();
                if ("编辑".equals(content)) {
                    if (mList.size() == 0) return;
                    adapter.setShowFlag(true);
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setDelete(false);
                        adapter.notifyDataSetChanged();
                    }
                    btn.setText("删除");
                    tv_bianji.setText("完成");
                } else if ("完成".equals(content)) {
                    tv_bianji.setText("编辑");
                    btn.setText("添加照片");
                    adapter.setShowFlag(false);
                    adapter.notifyDataSetChanged();
                }
                checkIsExitsPic();

            }
            break;

        }

    }

    public void delete() {
        for (int i = 0; i < mList.size(); i++) {
            boolean isDelete = mList.get(i).isDelete();
            if (isDelete) {
                String path = mList.get(i).getPath();
                File file = new File(path);
                if (file.exists()) {
                    boolean flag = file.delete();
                }
                mList.remove(i);
                delete();
                return;

            }
        }
        adapter.notifyDataSetChanged();
        checkIsExitsPic();
        tv_bianji.performClick();
    }

    /**
     * 是否显示 无照片记录 的文字
     */
    private void checkIsExitsPic() {
//        无照片记录
        View noVideoText = findViewById(R.id.no_pic);
        if (mList == null || mList.size() == 0) {
            noVideoText.setVisibility(View.VISIBLE);
            tv_bianji.setVisibility(View.INVISIBLE);
        }
        if (mList != null && mList.size() != 0) {
            noVideoText.setVisibility(View.GONE);
            tv_bianji.setVisibility(View.VISIBLE);
        }
    }
}
