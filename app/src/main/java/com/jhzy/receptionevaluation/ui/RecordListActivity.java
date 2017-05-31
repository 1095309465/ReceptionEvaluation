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
import com.jhzy.receptionevaluation.ui.adapter.MusicListAdapter;
import com.jhzy.receptionevaluation.ui.bean.MusicListBean;
import com.jhzy.receptionevaluation.utils.DeleteDialogUtil;
import com.jhzy.receptionevaluation.utils.Globars;
import com.jhzy.receptionevaluation.utils.PopWindowUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecordListActivity extends BaseActivity {
    private String name;
    private int type;
    private SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");
    private RecyclerView recycle;
    private TextView tv_bianji;
    private TextView btn;
    private List<MusicListBean> mList;
    private MusicListAdapter adapter;
    private String coursepath;

    @Override
    public int getContentView() {
        return R.layout.activity_music_list;
    }

    @Override
    public void init() {
        intiData();
        initView();
        checkIsExitsPic();
    }

    private void initView() {
        btn = (TextView) findViewById(R.id.btn);
        tv_bianji = (TextView) findViewById(R.id.tv_bianji);
        recycle = (RecyclerView) findViewById(R.id.recycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recycle.setLayoutManager(layoutManager);
        adapter = new MusicListAdapter(mList, this);
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                boolean flag = adapter.showButton;
                String path = mList.get(position).getPath();
                if (flag) {
                    mList.get(position).setDelete(!mList.get(position).isDelete());
                    adapter.notifyDataSetChanged();
                    checkIsExitsPic();
                    Log.e("123", "已经点击，开始刷新=" + mList.toString());
                } else {
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setDataAndType(Uri.parse("file://" + path), "audio/MP3");
                    startActivity(it);
                    Toast.makeText(RecordListActivity.this, "播放音乐", Toast.LENGTH_SHORT).show();
                }
            }
        });
        adapter.setOnPenItemClickListener(new MusicListAdapter.OnPenItemClickListener() {
            @Override
            public void onClick(final int position) {
                String info = mList.get(position).getInfo();
                PopWindowUtils.showVideoBianJiPop(RecordListActivity.this, findViewById(R.id.activity_lu_xiang_list), getWindow(), info, new PopWindowUtils.OnBackInfoListener() {
                    @Override
                    public void onBackInfo(String content) {//重命名
                        String path = mList.get(position).getPath();
                        String fileName = mList.get(position).getInfo();
                        File f = new File(path);
                        String newFilePath = path.replace(fileName, content);
                        File file = new File(newFilePath);
                        if (file.exists()) {
                            Toast.makeText(RecordListActivity.this, "重命名失败，已存在相同的文件", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (f.exists()) {
                            boolean flag = f.renameTo(file);
                            Log.e("123", "重命名文件夹=" + flag);
                            if (!flag) {
                                Toast.makeText(RecordListActivity.this, "重命名失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mList.get(position).setInfo(content);
                            mList.get(position).setPath(newFilePath);

                            adapter.notifyDataSetChanged();
                            checkIsExitsPic();
                            Toast.makeText(RecordListActivity.this, "重命名成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(RecordListActivity.this, "重命名成功,文件不存在", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });


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
        MusicListBean bean = new MusicListBean();

        Date date = new Date();
        String time = formatter.format(date);
        bean.setDelete(false);
        bean.setPath(path);
        bean.setTime(time);
        String[] paths = path.split("/");
        String name = paths[paths.length - 1];
        name = name.replace(".mp3", "");
        Log.e("123", "name=" + name);
        bean.setInfo(name);

        mList.add(bean);
        adapter.notifyDataSetChanged();
        checkIsExitsPic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("123", "onDestroy");
    }

    private void intiData() {
        //mList = new ArrayList<>();
        mList = Globars.newInstance().getMusicList();
        Log.e("123", "intiData=mList" + mList.toString());
        name = getIntent().getStringExtra("name");
        type = getIntent().getIntExtra("type", 1);
        coursepath = getIntent().getStringExtra("coursepath");

    }

    public void btn(View view) {
        switch (view.getId()) {
            case R.id.btn://添加录像按钮
            {
                String content = (String) btn.getText();
                if ("添加录音".equals(content)) {
                    Intent intent = new Intent(RecordListActivity.this, RecordActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("type", type);
                    intent.putExtra("coursepath",coursepath);
                    startActivityForResult(intent, 1);
                } else if ("删除".equals(content)) {
                    int num = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        boolean flag = mList.get(i).isDelete();
                        if (flag) {
                            num++;
                        }
                    }
                    if (num == 0) return;
                    new DeleteDialogUtil(RecordListActivity.this, "确认删除这" + num + "段录音吗？", new DeleteDialogUtil.OnDeleteClickListener() {
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
                    Toast.makeText(RecordListActivity.this, "点击编辑", Toast.LENGTH_SHORT).show();
                    adapter.setShowFlag(true);
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setDelete(false);
                        adapter.notifyDataSetChanged();
                    }
                    btn.setText("删除");
                    tv_bianji.setText("完成");
                } else if ("完成".equals(content)) {
                    tv_bianji.setText("编辑");
                    btn.setText("添加录音");
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
        adapter.notifyDataSetChanged();
        checkIsExitsPic();
        tv_bianji.performClick();
    }

    /**
     * 是否显示 无录音记录 的文字
     */
    private void checkIsExitsPic() {
//        无录音记录
        View noVideoText = findViewById(R.id.no_rec);
        if (mList == null || mList.size() == 0) {
            noVideoText.setVisibility(View.VISIBLE);
        }
        if (mList != null && mList.size() != 0) {
            noVideoText.setVisibility(View.GONE);
        }
    }

}
