package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.assess.ElderInfoActivity;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxmd on 2017/2/28.
 * 显示老人列表的适配器
 */

public class EvaluationGridViewAdapter extends BaseAdapter {
    private List<Elder> letter;
    private Context mContext;

    public EvaluationGridViewAdapter(Context mContext) {
        this.mContext = mContext;
        letter = new ArrayList<>();
    }

    public void setLetter(List<Elder> letters) {
        letter.clear();
        letter.addAll(letters);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return letter != null ? letter.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return letter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.evaluation_info_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Elder person = letter.get(position);
        holder.tv_name.setText(person.getElderName());
        String gender = person.getGender();
        int age = person.getAge();
        String bedTitle = person.getBedTitle();
        if (TextUtils.isEmpty(gender)) {
            gender = "不明";
        }
        if (TextUtils.isEmpty(bedTitle)) {
            bedTitle = "无";
        }
        holder.tv_info.setText(gender + "/" + age + "岁/" + bedTitle);
        //点击查看老人的详情
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ElderInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putParcelable("elder", person);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        ImageLoaderUtils.load(holder.iv_photo, person.getPhotoUrl());
        return convertView;
    }


    public class ViewHolder {
        SimpleDraweeView iv_photo;
        TextView tv_name;
        TextView tv_info;

        public ViewHolder(View itemView) {
            iv_photo = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
        }
    }


}
