package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.gridadapter.DrugSelectList;

import java.util.ArrayList;

/**
 * Created by nakisaRen
 * on 17/4/27.
 */

public class TimeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DrugSelectList> datas;


    public TimeAdapter(Context context, ArrayList<DrugSelectList> datas) {
        this.context = context;
        this.datas = datas;
    }


    @Override public int getCount() {
        return datas == null ? 0 : datas.size();
    }


    @Override public Object getItem(int i) {
        return i;
    }


    @Override public long getItemId(int i) {
        return i;
    }


    @Override public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pop_item_item,null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        DrugSelectList drugSelectList = datas.get(i);
        holder.tv.setText(drugSelectList.getTimeName());
        return view;
    }

    class ViewHolder{
        private final TextView tv;

        public ViewHolder(View view) {
            tv = ((TextView) view.findViewById(R.id.pop_item_time));
        }
    }


}
