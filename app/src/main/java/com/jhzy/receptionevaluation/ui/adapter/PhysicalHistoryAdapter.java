package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.physical.PhysicalExamination;
import com.jhzy.receptionevaluation.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/28.
 * 体检的历史记录的适配器
 */

public class PhysicalHistoryAdapter extends BaseAdapter {

    private List<PhysicalExamination> datas;

    private Context mContext;

    private String lastDate;

    private String type;

    public PhysicalHistoryAdapter(Context mContext) {
        datas = new ArrayList<>();
        this.mContext = mContext;
    }

    private String typeText;

    public void setDatas(List<PhysicalExamination> data, String type, String typeText) {
        datas.clear();
        datas.addAll(data);
        this.type = type;
        this.typeText = typeText;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_physical_history, null);
            vh = new ViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) view.getTag();
        }
        int size = datas.size();
        if (datas != null && size != 0) {
            if (i == size - 1) {
                vh.bg.setBackgroundResource(R.mipmap.icon_history_half);
            } else {
                vh.bg.setBackgroundResource(R.mipmap.icon_history);
            }
        }

        PhysicalExamination examination = datas.get(i);
        String time = examination.getExamDate();
        String yearMonth = TimeUtils.dateToYearMonth(time);
        String dateHours = TimeUtils.dateHours(time);
        vh.date.setText(yearMonth);
        vh.time.setText(dateHours);
        if ("1".equals(type)) { // 血压
            String result = examination.getResult();
            if (!TextUtils.isEmpty(result)) {
                try {
                    String[] split = result.split(",");
                    vh.history.setText("收缩压 " + split[0] + "/舒张压 " + split[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            vh.history.setText(typeText + examination.getResult());
        }
        if (!TextUtils.isEmpty(examination.getYearmonth())) {
            vh.date.setText(yearMonth);
            vh.date.setVisibility(View.VISIBLE);
        } else {
            vh.date.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    class ViewHolder {

        private TextView history;
        private TextView date;
        private TextView time;
        private ImageView bg;

        public ViewHolder(View view) {
            time = ((TextView) view.findViewById(R.id.time));
            history = ((TextView) view.findViewById(R.id.history));
            date = ((TextView) view.findViewById(R.id.date));
            bg = ((ImageView) view.findViewById(R.id.bg));
        }
    }
}
