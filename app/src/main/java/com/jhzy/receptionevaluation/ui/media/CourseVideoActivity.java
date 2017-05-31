package com.jhzy.receptionevaluation.ui.media;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.jhzy.receptionevaluation.R;


public class CourseVideoActivity extends AppCompatActivity {

    VideoView courseVideoVideoview;
    ImageView courseVideoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_video);
        initView();
        initData();
    }

    private void initView() {
        courseVideoVideoview = (VideoView) findViewById(R.id.course_video_videoview);
        courseVideoBack = (ImageView) findViewById(R.id.course_video_back);

    }

    private void initData() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        Log.d("123456", path);
        courseVideoVideoview.setMediaController(new MediaController(this));
        courseVideoVideoview.setVideoURI(Uri.parse(path));
        courseVideoVideoview.start();
        courseVideoVideoview.requestFocus();

        courseVideoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
