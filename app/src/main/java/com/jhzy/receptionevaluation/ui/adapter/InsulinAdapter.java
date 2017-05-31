package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.ui.bean.drugnext.Insulin;
import java.util.List;

/**
 * Created by nakisaRen
 * on 17/5/2.
 */

public class InsulinAdapter extends RecyclerView.Adapter<InsulinAdapter.Holder> {

    public interface InsulinListener{
        void inject(int position);
        void edit(int position);
    }

    private InsulinListener insulin;
    private List<Insulin.DataBean> data;
    private Context context;
    private GradientDrawable gradientDrawable;


    public void setInsulin(InsulinListener insulin){
        this.insulin = insulin;
    }

    public void setData(List<Insulin.DataBean> data){
        this.data = data;
        notifyDataSetChanged();
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context)
            .inflate(R.layout.item_insulin, null);
        gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.OVAL);
        gradientDrawable.setColor(Color.parseColor("#4AB034"));
        return new Holder(inflate);
    }


    @Override public void onBindViewHolder(Holder holder, final int position) {
        Insulin.DataBean insulinData = data.get(position);
        holder.typeView.setText(insulinData.getTimeName());
        if(insulinData.getIsCompleted() == 1){//1已注射 2未注射
            holder.state.setTextColor(context.getResources().getColor(R.color.color_gray_999999));
            holder.state.setText("已注射");
        }else{
            holder.state.setTextColor(context.getResources().getColor(R.color.color_red_e50000));
            holder.state.setText("未注射");
        }
        if(!TextUtils.isEmpty(insulinData.getDoseUnit())){
            holder.content.setText(insulinData.getDosage() +" "+ insulinData.getDoseUnit());
        }

        try {
            /*if(TextUtils.isEmpty(drugNextData.getTimeColor())){
                gradientDrawable.setColor(Color.parseColor("#4AB034"));
            }else{
                gradientDrawable.setColor(Color.parseColor(drugNextData.getTimeColor()));
            }*/
            holder.colorView.setBackgroundDrawable(gradientDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(this.insulin != null){
            holder.state.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    insulin.inject(position);
                }
            });
            holder.edit.setOnClickListener(new OnClickListenerNoDouble() {
                @Override public void myOnClick(View view) {
                    insulin.edit(position);
                }
            });
        }
    }


    @Override public int getItemCount() {
        return data == null ? 0:data.size();
    }


    class Holder extends RecyclerView.ViewHolder{

        private final View colorView;
        private final TextView state;
        private final TextView typeView;
        private final TextView content;
        private final TextView edit;


        public Holder(View itemView) {
            super(itemView);
            colorView = itemView.findViewById(R.id.item_insulin_color);
            state = ((TextView) itemView.findViewById(R.id.item_insulin_fayao));
            typeView = ((TextView) itemView.findViewById(R.id.item_insulin_type));
            content = ((TextView) itemView.findViewById(R.id.item_insulin_content));
            edit = ((TextView) itemView.findViewById(R.id.item_insulin_edit));
        }
    }
}
