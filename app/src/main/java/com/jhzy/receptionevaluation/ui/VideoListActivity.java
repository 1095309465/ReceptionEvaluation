package com.jhzy.receptionevaluation.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jhzy.receptionevaluation.BaseActivity;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.VideoListAdapter;
import com.jhzy.receptionevaluation.ui.bean.VideoListBean;
import com.jhzy.receptionevaluation.ui.media.VideoActivity;
import com.jhzy.receptionevaluation.utils.DeleteDialogUtil;
import com.jhzy.receptionevaluation.utils.Globars;
import com.jhzy.receptionevaluation.utils.PopWindowUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 评估录像界面
 */
public class VideoListActivity extends BaseActivity {
    private List<VideoListBean> mList;
    private VideoListAdapter adapter;
    private int type;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private RecyclerView recycle;
    private TextView tv_bianji;
    private TextView btn;
    private String name;
    private String coursepath;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lu_xiang_list);

    }*/

    @Override
    public int getContentView() {
        return R.layout.activity_lu_xiang_list;
    }

    @Override
    public void init() {
        initData();
        initView();
        checkIsExitsVideo();
    }

    private void initView() {
        btn = (TextView) findViewById(R.id.btn);
        tv_bianji = (TextView) findViewById(R.id.tv_bianji);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycle.setLayoutManager(layoutManager);
        adapter = new VideoListAdapter(mList, this);
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new VideoListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                boolean flag = adapter.showButton;
                String path = mList.get(position).getPath();
                if (flag) {//选择是否删除
                    mList.get(position).setDelete(!mList.get(position).isDelete());
                    adapter.notifyDataSetChanged();
                    checkIsExitsVideo();
                    Log.e("123", "已经点击，开始刷新=" + mList.toString());
                } else {//选择播放
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(path), "video/mp4");
                    startActivity(intent);

                }
            }
        });
        adapter.setOnPenItemClickListener(new VideoListAdapter.OnPenItemClickListener() {
            @Override
            public void onClick(final int position) {
                String info = mList.get(position).getInfo();
                PopWindowUtils.showVideoBianJiPop(VideoListActivity.this, findViewById(R.id.activity_lu_xiang_list), getWindow(), info, new PopWindowUtils.OnBackInfoListener() {
                    @Override
                    public void onBackInfo(String content) {//重命名
                        String path = mList.get(position).getPath();
                        String fileName = mList.get(position).getInfo();
                        File f = new File(path);
                        String newFilePath = path.replace(fileName, content);

                        File file = new File(newFilePath);
                        if (file.exists()) {
                            Toast.makeText(VideoListActivity.this, "重命名失败，已存在相同的文件", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (f.exists()) {
                            boolean flag = f.renameTo(file);
                            Log.e("123", "重命名文件夹=" + flag);
                            if (!flag) {
                                Toast.makeText(VideoListActivity.this, "重命名失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mList.get(position).setInfo(content);
                            mList.get(position).setPath(newFilePath);

                            adapter.notifyDataSetChanged();
                            checkIsExitsVideo();
                        } else {
                            Toast.makeText(VideoListActivity.this, "重命名成功,文件不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void initData() {
        //  mList = new ArrayList<>();

        type = getIntent().getIntExtra("type", 1);
        if (type != 4) {
            Globars.newInstance().setNull();
        }
        mList = Globars.newInstance().getVideoList();
        name = getIntent().getStringExtra("name");
        coursepath = getIntent().getStringExtra("coursepath");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        String path = data.getStringExtra("path");
        if (TextUtils.isEmpty(path)) {
            //Toast.makeText(VideoListActivity.this, "录制错误", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("123", "path=" + path);
        VideoListBean bean = new VideoListBean();

        Date date = new Date();
        String time = formatter.format(date);
        bean.setDelete(false);
        bean.setPath(path);
        bean.setTime(time);
        String[] paths = path.split("/");
        String name = paths[paths.length - 1];
        name = name.replace(".mp4", "");
        Log.e("123", "name=" + name);
        bean.setInfo(name);
        mList.add(bean);
        adapter.notifyDataSetChanged();
        checkIsExitsVideo();
    }

    public void btn(View view) {
        switch (view.getId()) {
            case R.id.btn://添加录像按钮
            {
                String content = (String) btn.getText();
                if ("添加录像".equals(content)) {
                    Intent intent = new Intent(VideoListActivity.this, VideoActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("name", name);
                    intent.putExtra("coursepath", coursepath);
                    startActivityForResult(intent, type);
                } else if ("删除".equals(content)) {
                    int num = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        boolean flag = mList.get(i).isDelete();
                        if (flag) {
                            num++;
                        }


                    }
                    if (num == 0) return;
                    new DeleteDialogUtil(VideoListActivity.this, "确认删除这" + num + "段录像吗？", new DeleteDialogUtil.OnDeleteClickListener() {
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
                    btn.setText("添加录像");
                    adapter.setShowFlag(false);
                    adapter.notifyDataSetChanged();
                }
                checkIsExitsVideo();

            }
            break;
        }

    }

    public void delete() {
        for (int i = 0; i < mList.size(); i++) {
            boolean isDelete = mList.get(i).isDelete();
            if (isDelete) {
                String path = mList.get(i).getPath();
                File f = new File(path);
                if (f.exists()) {
                    boolean flag = f.delete();
                    Log.e("123", "删除文件=" + flag);
                }
                mList.remove(i);
                delete();
                return;

            }
        }
        checkIsExitsVideo();
        adapter.notifyDataSetChanged();
        tv_bianji.performClick();
    }

    /**
     * 是否显示无录像记录的文字
     */
    private void checkIsExitsVideo() {
//        无录像记录
        View noVideoText = findViewById(R.id.no_video);
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
