package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxmd on 2017/2/27.
 */

public class NameListAdapter extends BaseAdapter {
    private List<Elder> mList;
    private Context mContext;

    private boolean isShowLetter  = true;

    public NameListAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<Elder> mLists , boolean isShowLetter) {
        this.isShowLetter = isShowLetter;
        mList.clear();
        mList.addAll(mLists);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.name_list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Elder bean = mList.get(position);
        holder.tv_name.setText(bean.getElderName());
        holder.tv_letter.setText(bean.getFirstLetter());
        boolean showLetter = bean.isShowLetter();
        if (showLetter) {
            holder.tv_letter.setVisibility(View.VISIBLE);
            holder.view_black.setVisibility(View.VISIBLE);
        } else {
            holder.view_black.setVisibility(View.GONE);
            holder.tv_letter.setVisibility(View.GONE);
        }
        if (!isShowLetter){
            holder.tv_letter.setVisibility(View.GONE);
        }
        return convertView;
    }


    public class ViewHolder {
        TextView tv_name;
        TextView tv_age;
        TextView tv_gender;
        TextView tv_bed;
        View view;
        TextView tv_letter;
        View lin;
        View view_black;

        public ViewHolder(View itemView) {
            this.view = itemView;
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_age = (TextView) itemView.findViewById(R.id.tv_age);
            tv_gender = (TextView) itemView.findViewById(R.id.tv_gender);
            tv_bed = (TextView) itemView.findViewById(R.id.tv_bed);
            tv_letter = (TextView) itemView.findViewById(R.id.tv_letter);
            lin = itemView.findViewById(R.id.lin);
            view_black = itemView.findViewById(R.id.view_black);
        }
    }

}
