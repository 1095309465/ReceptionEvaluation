package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxmd on 2017/2/28.
 */

public class EvaluationInfoAdapter extends BaseAdapter {

    private List<List<Elder>> mlist;
    private Context mContext;

    public EvaluationInfoAdapter(Context mContext) {
        mlist = new ArrayList<>();
        this.mContext = mContext;
    }

    public void setMlist(List<List<Elder>> mlists) {
        mlist.clear();
        mlist.addAll(mlists);
        Log.e("------", "setMlist: " + mlists.size());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlist == null ? 0 : mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.evaluation_info_item_1, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<Elder> letter = mlist.get(position);
        holder.tv_letter.setText(letter.get(0).getFirstLetter());
        EvaluationGridViewAdapter adapter = new EvaluationGridViewAdapter(mContext);
        holder.myGridView.setAdapter(adapter);
        adapter.setLetter(letter);

        return convertView;
    }


    public class ViewHolder {
        TextView tv_letter;
        MyGridView myGridView;

        public ViewHolder(View itemView) {
            tv_letter = (TextView) itemView.findViewById(R.id.tv_letter);
            myGridView = (MyGridView) itemView.findViewById(R.id.myGridView);
        }
    }
}
