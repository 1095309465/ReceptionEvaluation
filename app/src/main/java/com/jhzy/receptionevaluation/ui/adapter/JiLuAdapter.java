package com.jhzy.receptionevaluation.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.CourseRecordBean;
import com.jhzy.receptionevaluation.ui.bean.InfoJilu;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/2/28.
 */

public class JiLuAdapter extends RecyclerView.Adapter<JiLuAdapter.Holder> {

    private List<CourseRecordBean.DataBean> infoJiluList;

    public JiLuAdapter(List<CourseRecordBean.DataBean> infoJiluList) {
        this.infoJiluList = infoJiluList;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jilu, null);
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        CourseRecordBean.DataBean jilu = infoJiluList.get(position);
        holder.time.setText(getTime(jilu.getInspectionTime()));
        holder.detail.setText(jilu.getTreatSituation());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(position);
                }

            }
        });
    }

    private String getTime(String time){
        if(time.length() > 10){
            return time.substring(0,11);
        }else{
            return "暂无时间";
        }
    }


    @Override
    public int getItemCount() {
        return infoJiluList.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        private final TextView time;
        private final TextView detail;
        private final ImageView next;


        public Holder(View view) {
            super(view);
            time = ((TextView) view.findViewById(R.id.jilu_time));
            detail = ((TextView) view.findViewById(R.id.jilu_detail));
            next = ((ImageView) view.findViewById(R.id.jilu_next));
        }
    }

    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
