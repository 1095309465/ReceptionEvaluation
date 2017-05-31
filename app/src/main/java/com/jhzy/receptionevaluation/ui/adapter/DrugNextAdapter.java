package com.jhzy.receptionevaluation.ui.adapter;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.drugnext.DrugNextData;
import com.jhzy.receptionevaluation.ui.bean.drugnext.DrugsBean;

import java.util.List;

/**
 * Created by nakisaRen
 * on 17/4/26.
 */

public class DrugNextAdapter extends RecyclerView.Adapter<DrugNextAdapter.Holder> {
    private List<DrugNextData> datas;
    private int type = 0;
    private OnFayaoClickListener onFayaoClickListener;
    private GradientDrawable gradientDrawable;


    public interface OnFayaoClickListener {
        void onFayaoClick(int position);
    }


    public void setFayaoClick(OnFayaoClickListener onFayaoClickListener) {
        this.onFayaoClickListener = onFayaoClickListener;
    }


    public void setData(List<DrugNextData> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    public DrugNextAdapter(int type) {
        this.type = type;
    }


    @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drugnext, null);
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        return new Holder(view);
    }


    @Override public void onBindViewHolder(Holder holder, final int position) {
        DrugNextData drugNextData = datas.get(position);
        List<DrugsBean> drugs = drugNextData.getDrugs();
        try {
            if (TextUtils.isEmpty(drugNextData.getTimeColor())) {
                gradientDrawable.setColor(Color.parseColor("#4AB034"));
            } else {
                gradientDrawable.setColor(Color.parseColor(drugNextData.getTimeColor()));
            }
        } catch (Exception e) {
            gradientDrawable.setColor(Color.parseColor("#4AB034"));
        }
        holder.colorView.setBackgroundDrawable(gradientDrawable);
        if (TextUtils.isEmpty(drugNextData.getTimeName())) {
            holder.typeView.setText("数据异常");
        } else {
            holder.typeView.setText(drugNextData.getTimeName());
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < drugs.size(); i++) {
            DrugsBean drugsBean = drugs.get(i);
            if (drugsBean != null) {
                if (TextUtils.isEmpty(drugsBean.getDrugName())) {
                    builder.append("药品名返回错误");
                } else {
                    builder.append(drugsBean.getDrugName());
                }
                try {
                    builder.append("\t")
                        .append(drugsBean.getDosage())
                        .append(drugsBean.getDoseUnit());
//                    if (type == 1 && drugsBean.getCurrentAmount() <= drugsBean.getMinimum()) {//库存预警
//                        builder.append(Html.fromHtml("< 1 color='#e50000'>(库存不足)</font>"));
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                builder.append(";");
                if (i < drugs.size() - 1) {
                    builder.append("\r\n");
                }
            }
        }
        holder.content.setText(builder.toString());
        if (type == 2 && onFayaoClickListener != null) {//发药
            if (TextUtils.isEmpty(drugNextData.getFYDatetime())) {
                holder.fayao.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View view) {
                        onFayaoClickListener.onFayaoClick(position);
                    }
                });
            } else {
                holder.fayao.setEnabled(false);
            }
        }

    }


    @Override public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class Holder extends RecyclerView.ViewHolder {

        private final View colorView;
        private final TextView typeView;
        private final TextView fayao;
        private final TextView content;


        public Holder(View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.item_drugnext_color);
            typeView = ((TextView) itemView.findViewById(R.id.item_drugnext_type));
            fayao = ((TextView) itemView.findViewById(R.id.item_drugnext_fayao));
            if (type == 2) {
                fayao.setVisibility(View.VISIBLE);
            }
            content = ((TextView) itemView.findViewById(R.id.item_drugnext_content));
        }
    }
}
