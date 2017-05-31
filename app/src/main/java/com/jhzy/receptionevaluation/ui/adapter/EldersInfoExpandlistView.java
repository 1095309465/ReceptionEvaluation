package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.Person;

import java.util.List;

/**
 * Created by bigyu on 2017/3/1.
 */
@Deprecated
public class EldersInfoExpandlistView extends BaseExpandableListAdapter {

    private List<List<Person>> mlist;
    private Context mContext;
    LayoutInflater layoutInflater;


    public EldersInfoExpandlistView(Context mContext, List<List<Person>> mlist) {
        this.mContext = mContext;
        this.mlist = mlist;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return mlist.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mlist.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return mlist.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mlist.get(i).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolderGroup vhGroup;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.evaluation_info_item_1, viewGroup, false);
            vhGroup = new ViewHolderGroup(view);
            view.setTag(vhGroup);
        } else {
            vhGroup = (ViewHolderGroup) view.getTag();
        }
        List<Person> letter = mlist.get(i);
        vhGroup.tv_letter.setText(letter.get(0).getFirstLetter());
        return view;
    }

    public class ViewHolderGroup {
        TextView tv_letter;
        GridLayout gridLayout;

        public ViewHolderGroup(View itemView) {
            tv_letter = (TextView) itemView.findViewById(R.id.tv_letter);
            gridLayout = (GridLayout) itemView.findViewById(R.id.grid_layout);
        }
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolderChild vhChild;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_child, viewGroup, false);
            vhChild = new ViewHolderChild(view);
            view.setTag(vhChild);
        } else {
            vhChild = (ViewHolderChild) view.getTag();
        }
        vhChild.gridLayout.removeAllViews();
        int size = mlist.get(i).size();

        List<Person> persons = mlist.get(i);
        for (int j = 0; j < size; j++) {
            View inflate = layoutInflater.inflate(R.layout.evaluation_info_item, null);
            TextView name = (TextView) inflate.findViewById(R.id.tv_name);
            name.setText(persons.get(j).getName());
            vhChild.gridLayout.addView(inflate);
        }
        return view;
    }

    public class ViewHolderChild {
        SimpleDraweeView iv_photo;
        TextView tv_name;
        TextView tv_age;
        TextView tv_gender;
        TextView tv_bed;
        private GridLayout gridLayout;

        public ViewHolderChild(View itemView) {
            gridLayout = ((GridLayout) itemView.findViewById(R.id.grid_layout));
//            iv_photo = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
//            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
//            tv_age = (TextView) itemView.findViewById(R.id.tv_age);
//            tv_gender = (TextView) itemView.findViewById(R.id.tv_gender);
//            tv_bed = (TextView) itemView.findViewById(R.id.tv_bed);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
