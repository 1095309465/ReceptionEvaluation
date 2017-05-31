package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.api.OnClickListenerNoDouble;
import com.jhzy.receptionevaluation.api.PhysicalAdapterListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */
@Deprecated
public class PhysicalAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflate;

    private PhysicalAdapterListener physicalAdapterListener;

    private List<String> list;

    public PhysicalAdapter(Context mContext) {
        this.mContext = mContext;
        inflate = LayoutInflater.from(mContext);
        list = new ArrayList<>();
    }

    public void setPhysicalAdapterListener(PhysicalAdapterListener physicalAdapterListener) {
        this.physicalAdapterListener = physicalAdapterListener;
    }

    public void setData(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
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
    public View getView(final int i, View convertview, ViewGroup viewGroup) {

        ViewHolder vh;

        if (convertview == null) {
            convertview = this.inflate.inflate(R.layout.adapter_physical, null);
            vh = new ViewHolder(convertview);
            convertview.setTag(vh);
        } else {
            vh = (ViewHolder) convertview.getTag();
        }
        vh.item.setText(list.get(i));

        if (physicalAdapterListener != null) {
            convertview.setOnClickListener(new OnClickListenerNoDouble() {
                @Override
                public void myOnClick(View view) {
                    physicalAdapterListener.physicalAdapterListener(i);
                }
            });
        }
        return convertview;
    }


    class ViewHolder {
        private TextView item;

        public ViewHolder(View view) {
            item = ((TextView) view.findViewById(R.id.item));
        }
    }
}
