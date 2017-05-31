package com.jhzy.receptionevaluation.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.bean.eldersInfo.Elder;
import com.jhzy.receptionevaluation.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by welse on 2017/4/15.
 */

public class RVDispenseAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    public List<ElderExt> elderExts = new ArrayList<>();    //长者适配器列表
    private List<String> characterList = new ArrayList<>(); // 字母List
    private int width;
    public enum ITEM_TYPE{
        TYPE_ITEM,          //数据项
        TYPE_FOOTER,        //列表尾部
        TYPE_CHARACTER      //组标题
    }

    public RVDispenseAdapter(Context context, int screen_width) {
        this.context = context;
        this.width = screen_width;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 将长者资料按字母分组存入适配器列表中
     * @param elders 长者资料
     */
    public void classify_elder(List<Elder> elders){
        ElderExt elderExt;
        elderExts.clear();
        characterList.clear();;
        for (Elder elder:elders) {//循环所有长者
            if (!characterList.contains(elder.getFirstLetter())) {//当前首字母未包含在列表中
                if(Character.isUpperCase(elder.getFirstLetter().charAt(0))){
                    characterList.add(elder.getFirstLetter());
                    elderExt = new ElderExt(null, ITEM_TYPE.TYPE_CHARACTER, elder.getFirstLetter());
                    elderExts.add(elderExt);
                }else {
                    if (!characterList.contains("#")) {
                        characterList.add("#");
                        elderExt = new ElderExt(null, ITEM_TYPE.TYPE_CHARACTER, "#");
                        elderExts.add(elderExt);
                    }
                }
            }
            elderExt = new ElderExt(elder, ITEM_TYPE.TYPE_ITEM, elder.getFirstLetter());
            elderExts.add(elderExt);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return elderExts.size() == 0 ? 0 : elderExts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return ITEM_TYPE.TYPE_FOOTER.ordinal();
        } else {
            return elderExts.get(position)._itemType.ordinal();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TYPE_ITEM.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.evaluation_info_item, parent, false);
            int height = view.getLayoutParams().height;
            view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
            return new ItemViewHolder(view);
        } else if (viewType == ITEM_TYPE.TYPE_FOOTER.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_footer, parent, false);
            return new FootViewHolder(view);
        } else if (viewType == ITEM_TYPE.TYPE_CHARACTER.ordinal()) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_character, parent, false);
            return new CharacterHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Elder elder;
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            elder = elderExts.get(position)._elder;
            ImageLoaderUtils.load(viewHolder.iv_photo, elder.getPhotoUrl());
            ((ItemViewHolder) holder).tv_name.setText(elder.getElderName());
            String gender = elder.getGender();
            int age = elder.getAge();
            String bedTitle = elder.getBedTitle();
            if (TextUtils.isEmpty(gender)) {
                gender = "不明";
            }
            if (TextUtils.isEmpty(bedTitle)) {
                bedTitle = "无";
            }
            viewHolder.tv_info.setText(gender + "/" + age + "岁/" + bedTitle);
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(holder.itemView, position);
                        return false;
                    }
                });
            }
        }
        else if(holder instanceof CharacterHolder){
            ((CharacterHolder) holder).mTextView.setText(elderExts.get(position)._letter);
        }
    }

    /**
     * 根据首字母返回对应的列表项序号
     * @param character 首字母
     * @return int
     */
    public int getScrollPosition(String character) {
        if (characterList.contains(character)) {
            for (int i = 0; i < elderExts.size(); i++) {
                if (elderExts.get(i)._letter.equals(character)) {
                    return i;
                }
            }
        }
        return -1; // -1不会滑动
    }

    static class CharacterHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public CharacterHolder(View view) {
            super(view);
            mTextView = (TextView) view.findViewById(R.id.character);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView iv_photo;
        TextView tv_name;
        TextView tv_info;

        public ItemViewHolder(View view) {
            super(view);
            iv_photo = (SimpleDraweeView) view.findViewById(R.id.iv_photo);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_info = (TextView) view.findViewById(R.id.tv_info);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    class ElderExt{
        public ITEM_TYPE _itemType;   //数据项类别，见ITEM_TYPE定义
        public String _letter;  //首字母
        public Elder _elder;    //长者资料，对于组标题项是null
        public ElderExt(Elder elder,ITEM_TYPE itemType,String letter){
            _elder = elder;
            _itemType = itemType;
            _letter = letter;
        }
    }
}
