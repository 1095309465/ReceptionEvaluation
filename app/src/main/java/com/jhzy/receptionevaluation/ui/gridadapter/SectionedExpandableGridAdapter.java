package com.jhzy.receptionevaluation.ui.gridadapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugElders;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;

import java.util.ArrayList;

/**
 * Created by welse on 2017/4/17.
 */

public class SectionedExpandableGridAdapter extends RecyclerView.Adapter<SectionedExpandableGridAdapter.ViewHolder> {

    //data array
    private ArrayList<Object> mDataArrayList;

    //context
    private final Context mContext;

    //listeners
    private ItemClickListener mItemClickListener;
    private SectionStateChangeListener mSectionStateChangeListener;

    //view type
    private static final int VIEW_TYPE_SECTION = R.layout.layout_section;
    private static final int VIEW_TYPE_ITEM = R.layout.evaluation_info_item; //TODO : change this

    private int layoutType;
    private boolean complete;

    public SectionedExpandableGridAdapter(Context context, ArrayList<Object> dataArrayList,
                                          final GridLayoutManager gridLayoutManager, ItemClickListener itemClickListener,
                                          SectionStateChangeListener sectionStateChangeListener, int layoutType, boolean complete) {
        mContext = context;
        mItemClickListener = itemClickListener;
        mSectionStateChangeListener = sectionStateChangeListener;
        mDataArrayList = dataArrayList;

        this.complete = complete;
        this.layoutType = layoutType;
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return isSection(position) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
    }

    private boolean isSection(int position) {
        return mDataArrayList.get(position) instanceof Section;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(viewType, parent, false), viewType);
    }

    /**
     * 生成长老的综合信息(性别、年龄和床位)
     *
     * @param elder 长者资料类
     * @return String
     */
    private String generate_info(DrugElders elder) {
        String gender = elder.getGender();
        int age = elder.getAge();
        String bedTitle = elder.getBedCode();
        if (TextUtils.isEmpty(gender)) {
            gender = "不明";
        }
        if (TextUtils.isEmpty(bedTitle)) {
            bedTitle = "无";
        }
        return gender + "/" + age + "岁/" + bedTitle;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.viewType) {
            case VIEW_TYPE_ITEM: //老人列表
                final DrugElders item = (DrugElders) mDataArrayList.get(position);
                if (TextUtils.isEmpty(item.getPhotoUrl())) {
                    Uri uri = Uri.parse("error");
                    holder.iv_photo.setImageURI(uri);
                } else {
                    ImageLoaderUtils.load(holder.iv_photo, item.getPhotoUrl());
                }
                holder.tv_name.setText(item.getName());
                holder.tv_info.setText(generate_info(item));
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(item);
                    }
                });
                if (layoutType == 1) {

                } else if (layoutType == 2) {
                    if (complete) {
                        holder.check.setVisibility(View.GONE);
                    } else {
                        if (item.isShowSelectedView()) {
                            holder.check.setVisibility(View.VISIBLE);
                        } else {
                            holder.check.setVisibility(View.GONE);
                        }
                        if (item.isSelected()) {
                            holder.layout.setBackgroundResource(R.drawable.selector_card_bg);
                            holder.check.setBackgroundResource(R.mipmap.icon_check_drug);
                        } else {
                            holder.layout.setBackgroundResource(R.color.color_white);
                            holder.check.setBackgroundResource(R.mipmap.icon_check_un_drug);
                        }
                    }
                } else if (layoutType == 3) {

                }
                break;
            case VIEW_TYPE_SECTION: // A  B  C D E F G
                final Section section = (Section) mDataArrayList.get(position);
                holder.sectionTextView.setText(section.getName());
                holder.sectionTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.itemClicked(section);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isSection(position))
            return VIEW_TYPE_SECTION;
        else return VIEW_TYPE_ITEM;
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        //common
        View view;
        int viewType;

        //for section
        TextView sectionTextView;
        //ToggleButton sectionToggleButton;

        //for item
        SimpleDraweeView iv_photo;
        TextView tv_name;
        TextView tv_info;

        ImageView check;
        private LinearLayout layout;

        public ViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;
            this.view = view;
            if (viewType == VIEW_TYPE_ITEM) {
                layout = ((LinearLayout) view.findViewById(R.id.layout));
                iv_photo = (SimpleDraweeView) view.findViewById(R.id.iv_photo);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_info = (TextView) view.findViewById(R.id.tv_info);
                check = (ImageView) view.findViewById(R.id.drug_check);
            } else {
                sectionTextView = (TextView) view.findViewById(R.id.text_section);
                //sectionToggleButton = (ToggleButton) view.findViewById(R.id.toggle_button_section);
            }
        }
    }
}
