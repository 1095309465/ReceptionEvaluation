package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.ui.bean.drugtime.DrugDataBean;
import java.util.ArrayList;
import java.util.List;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.ImageText;

/**
 * 配药菜单条界面的适配器，尽管菜单项是静态确定的，但用列表形式显示扩展性好
 * Created by welse on 2017/4/13.
 */

public class DispenseAdapter extends RecyclerView.Adapter<DispenseAdapter.ViewHolder> {
    private DispenseAdapter.OnItemClickListener onItemClickListener;
    private Context context;
    private LayoutInflater layoutInflater;
    public List<DrugDataBean> datas;


    public interface OnItemClickListener {
        void onItemIconClick(View view, int position);
    }


    public void setOnItemClick(DispenseAdapter.OnItemClickListener onItemClick) {
        this.onItemClickListener = onItemClick;
    }


    public void setDatas(List<DrugDataBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    public DispenseAdapter(Context context) {
        super();
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public DispenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_dispense, parent, false);
        DispenseAdapter.ViewHolder viewHolder = new DispenseAdapter.ViewHolder(view);
        viewHolder.txtDrugUseName = (TextView) view.findViewById(R.id.txtDrugUseName);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(DispenseAdapter.ViewHolder holder, final int position) {
        holder.txtDrugUseName.setText(datas.get(position).getTimeTypeName());
        //Resources res = this.context.getResources();
        //holder.imDrugUseName.setImageBitmap(BitmapFactory.decodeResource(res,datas.get(position)._resId));
        if (onItemClickListener != null) {
            holder.txtDrugUseName.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    onItemClickListener.onItemIconClick(view, position);
                    notifyDataSetChanged();
                }
            });
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDrugUseName;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
