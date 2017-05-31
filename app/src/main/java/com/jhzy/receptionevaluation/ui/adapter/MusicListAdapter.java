package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.MusicListBean;
import com.jhzy.receptionevaluation.ui.bean.VideoListBean;

import java.util.List;

/**
 * Created by sxmd on 2017/3/2.
 */

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    private List<MusicListBean> mList;
    private Context mContext;
    public boolean showButton;

    public void setShowFlag(boolean flag) {
        showButton = flag;
    }

    public MusicListAdapter(List<MusicListBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MusicListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mucis_list_item, parent, false);
        return new MusicListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MusicListBean bean = mList.get(position);
        holder.tv_info.setText(bean.getInfo());
        holder.tv_time.setText(bean.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(holder.itemView, position);
                }
            }
        });

        if (showButton) {
            holder.iv_dot.setVisibility(View.VISIBLE);
            boolean flag = bean.isDelete();
            if (flag) {
                Log.e("123", "1");
                holder.iv_dot.setImageResource(R.mipmap.icon_dot_blue);
            } else {
                Log.e("123", "2");
                holder.iv_dot.setImageResource(R.mipmap.icon_dot_nor);
            }

        } else {
            holder.iv_dot.setVisibility(View.GONE);

        }

        holder.iv_pen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPenItemClickListener != null) {
                    onPenItemClickListener.onClick(position);
                }
            }
        });

    }

    public OnPenItemClickListener onPenItemClickListener;

    public void setOnPenItemClickListener(OnPenItemClickListener onPenItemClickListener) {
        this.onPenItemClickListener = onPenItemClickListener;
    }

    public interface OnPenItemClickListener {
        void onClick(int position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_info;
        TextView tv_time;
        ImageView iv_dot;
        ImageView iv_pen;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_info = (TextView) itemView.findViewById(R.id.tv_info);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_dot = (ImageView) itemView.findViewById(R.id.iv_dot);
            iv_pen = (ImageView) itemView.findViewById(R.id.iv_bi);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
