package com.jhzy.receptionevaluation.ui.fragment.elderInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.VideoListActivity;
import com.jhzy.receptionevaluation.ui.PictureListActivity;

/**
 * 评估录像
 */
public class LuxiangFragment extends Fragment {
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "param2";

    private String mParam2;
    private View dailyLuxiang;
    private View dailyPhoto;
    private View inLuxiang;
    private View inPhoto;
    private View outLuxiang;
    private View outPhoto;
    private View dailyTitle;
    private TextView inTitle;
    private TextView outTitle;
    private String name;


    public LuxiangFragment() {
        // Required empty public constructor
    }


    public static LuxiangFragment newInstance(String param1, String param2) {
        LuxiangFragment fragment = new LuxiangFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
            if (TextUtils.isEmpty(name)) name = "name";
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_luxiang, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        View daily = view.findViewById(R.id.daily);
        View in = view.findViewById(R.id.in);
        View out = view.findViewById(R.id.out);
        dailyLuxiang = daily.findViewById(R.id.luxiang_luxiang);
        dailyLuxiang.setTag(1);
        dailyPhoto = daily.findViewById(R.id.luxiang_photo);
        dailyPhoto.setTag(2);
        inLuxiang = in.findViewById(R.id.luxiang_luxiang);
        inLuxiang.setTag(3);
        inPhoto = in.findViewById(R.id.luxiang_photo);
        inPhoto.setTag(4);
        outLuxiang = out.findViewById(R.id.luxiang_luxiang);
        outLuxiang.setTag(5);
        outPhoto = out.findViewById(R.id.luxiang_photo);
        outPhoto.setTag(6);
        initListener();
        dailyTitle = ((TextView) daily.findViewById(R.id.luxiang_title));
        inTitle = ((TextView) in.findViewById(R.id.luxiang_title));
        inTitle.setText("入院评估");
        outTitle = ((TextView) out.findViewById(R.id.luxiang_title));
        outTitle.setText("出院评估");
    }


    private void initListener() {
        dailyLuxiang.setOnClickListener(noDouble);
        dailyPhoto.setOnClickListener(noDouble);
        inLuxiang.setOnClickListener(noDouble);
        inPhoto.setOnClickListener(noDouble);
        outLuxiang.setOnClickListener(noDouble);
        outPhoto.setOnClickListener(noDouble);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.e("123", "返回错误--Fragment");
            return;
        }
        switch (requestCode) {//日常评估录像
            case 2:
                String path = data.getStringExtra("path");
                if (TextUtils.isEmpty(path)) {
                    return;
                }

                Log.e("123", "日常评估录像的返回文件" + path);

                break;
        }
    }

    OnClickListenerNoDouble noDouble = new OnClickListenerNoDouble() {
        @Override
        public void myOnClick(View view) {
            switch ((int) view.getTag()) {
                case 1://日常评估录像
                {
                    Intent intent = new Intent(getActivity(), VideoListActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                   /* Toast.makeText(getContext(), "开始录像", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity().getApplicationContext(), VideoActivity.class);
                    startActivityForResult(intent, 2);*/
                break;
                case 2://日常拍照
                {
                    Intent intent = new Intent(getActivity(), PictureListActivity.class);
                    intent.putExtra("type", 1);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }


                break;
                case 3://入院评估录像
                {
                    Intent intent = new Intent(getActivity(), VideoListActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                break;
                case 4://入院拍照
                {
                    Intent intent = new Intent(getActivity(), PictureListActivity.class);
                    intent.putExtra("type", 2);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                break;
                case 5://出院评估录像
                {
                    Intent intent = new Intent(getActivity(), VideoListActivity.class);
                    intent.putExtra("type", 3);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                break;
                case 6://出院拍照
                {
                    Intent intent = new Intent(getActivity(), PictureListActivity.class);
                    intent.putExtra("type", 3);
                    intent.putExtra("name", name);
                    startActivity(intent);
                }
                break;
            }
        }
    };
}
