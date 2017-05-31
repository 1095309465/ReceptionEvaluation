package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.PictureListBean;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by sxmd on 2017/3/2.
 */

public class PictureListAdapter extends RecyclerView.Adapter<PictureListAdapter.ViewHolder> {

    private List<PictureListBean> mList;
    private Context mContext;

    public boolean showButton;

    public void setShowFlag(boolean flag) {
        showButton = flag;
    }

    public PictureListAdapter(List<PictureListBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public PictureListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.picture_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PictureListAdapter.ViewHolder holder, final int position) {
        PictureListBean bean = mList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onClick(holder.itemView, position);
                }
            }
        });
        //setImageURI(Uri.parse("file://"+list.get(position).getPath()));
        String path = bean.getPath();
        if (!TextUtils.isEmpty(path)) {
            Log.e("123", "加载图片=" + mList.get(position).getPath());
            Log.e("123", "加载图片位置=" + position);
            ImageLoaderUtils.loadLocalImage(holder.iv , Uri.parse("file://" + mList.get(position).getPath()));
//            holder.iv.setImageURI(Uri.parse("file://" + mList.get(position).getPath()));
        }
        if (showButton) {
            holder.iv_dot.setVisibility(View.VISIBLE);
            boolean flag = bean.isDelete();
            if (flag) {
                Log.e("123", "1");
                holder.iv_dot.setImageResource(R.mipmap.icon_dot_orange_click);
            } else {
                Log.e("123", "2");
                holder.iv_dot.setImageResource(R.mipmap.icon_dot_orange);
            }

        } else {
            holder.iv_dot.setVisibility(View.GONE);

        }


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv;
        ImageView iv_dot;


        public ViewHolder(View itemView) {
            super(itemView);
            iv = (SimpleDraweeView) itemView.findViewById(R.id.iv);
            iv_dot = (ImageView) itemView.findViewById(R.id.iv_dot);
        }
    }

    private VideoListAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(VideoListAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
