package com.jhzy.receptionevaluation.ui.dispensingdrug;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jhzy.receptionevaluation.R;
import com.jhzy.receptionevaluation.ui.adapter.RVDispenseAdapter;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugElders;
import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.TagsBean;
import com.jhzy.receptionevaluation.ui.gridadapter.ItemClickListener;
import com.jhzy.receptionevaluation.ui.gridadapter.Section;
import com.jhzy.receptionevaluation.ui.gridadapter.SectionedExpandableLayoutHelper;
import com.jhzy.receptionevaluation.utils.CONTACT;
import com.jhzy.receptionevaluation.utils.PinyinTool;
import com.jhzy.receptionevaluation.widget.MyView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * 是菜单选择界面，菜单项是餐前药，餐中药，餐后药，睡前药，其他药。
 */
public class DispensedFragment extends Fragment implements ItemClickListener {
    private static final String TAG = "-----------------";
    public List<List<DrugElders>> mList = new ArrayList<>();

    public List<List<DrugElders>> mListFilter = new ArrayList<>();
    private List<String> characterList = new ArrayList<>(); // 字母List
    private SectionedExpandableLayoutHelper<DrugElders> sectionedExpandableLayoutHelper;
    private PinyinTool tool;

    //界面控件
    private RecyclerView recyclViewDispense;
    private MyView sidebarDispense;
    private RVDispenseAdapter rvDispenseAdapter;    //
    private TextView txtDialog;
    // 当前对应的模块  配药 发药  胰岛素
    private int layoutType;
    //已完成 未完成
    private boolean isCompleted;
    private SwipeRefreshLayout refresh;
    //是否能选择多项 （发药内容）
    private boolean canSelectedItems;


    public DispensedFragment() {
        tool = new PinyinTool();
    }

    public static DispensedFragment newInstance(boolean complete, int type) {
        DispensedFragment fragment = new DispensedFragment();
        Bundle args = new Bundle();
        args.putBoolean(CONTACT.DispenseDrugContact.COMPLETED, complete);
        args.putInt(CONTACT.DispenseDrugContact.TYPE_1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dispensed, container, false);
        sidebarDispense = (MyView) view.findViewById(R.id.sidebarDispense2);
        recyclViewDispense = (RecyclerView) view.findViewById(R.id.recyclViewDispense2);
        refresh = ((SwipeRefreshLayout) view.findViewById(R.id.iv_refresh));
        txtDialog = (TextView) view.findViewById(R.id.txtDialog2);
        Bundle arguments = getArguments();
        layoutType = arguments.getInt(CONTACT.DispenseDrugContact.TYPE_1, 0);
        isCompleted = arguments.getBoolean(CONTACT.DispenseDrugContact.COMPLETED);
        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper<DrugElders>(getActivity(), recyclViewDispense, this, 4, layoutType, isCompleted);
        initListener();
        return view;
    }

    private void initListener() {
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh.setRefreshing(true);
                refreshLishtener.refresh();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, "onCreateView: 4");
        sidebarDispense.setOnTouchLetterListener(new MyView.OnTouchLetterListener() {
            @Override
            public void onActionDownAndMove(String c, int percent) {
                proc_ActionDownAndMove(c);
            }

            @Override
            public void onActionUp() {
                txtDialog.setVisibility(View.GONE);

            }
        });
    }

    /**
     * 根据字母计算滚动位置，并更新显示
     *
     * @param c 字母
     */
    private void proc_ActionDownAndMove(String c) {
        int iPos = 0;
        txtDialog.setVisibility(View.VISIBLE);
        txtDialog.setText(c);

        for (int i = 0; i < mList.size(); i++) {
            String letter = mList.get(i).get(0).getFirstLetter();
            if (c.compareTo(letter) <= 0) {
                break;
            } else {
                iPos += mList.get(i).size() + 1;
            }
        }
        sectionedExpandableLayoutHelper.scrollToPositionWithOffset(iPos, 0);
    }


    /**
     * 初始化数据
     *
     * @param mList
     * @param characterList
     */
    public void show(List<List<DrugElders>> mList, List<String> characterList) {
        this.mList.clear();
        this.characterList.clear();
        this.mListFilter.clear();
        this.mList.addAll(mList);
        this.mListFilter.addAll(mList);
        this.characterList.addAll(characterList);
        displayAll(); //控件显示长者资料
    }

    public void cancelRefresh() {
        refresh.setRefreshing(false);
    }


    /**
     * 根据姓名过滤出对应的长者
     *
     * @param content 姓名串
     */
    public void filterElder(String content) {
        List<DrugElders> elderList = null;
        List<List<DrugElders>> mList2 = new ArrayList<>();
        //过滤包含姓名串的长者资料存入mList2
        for (List<DrugElders> elders : mListFilter) {
            elderList = new ArrayList<>();
            for (DrugElders elder : elders) {
                if (elder.getName().contains(content)) {
                    elderList.add(elder);
                } else if (elder.getAllLetter().contains(content)) {
                    elderList.add(elder);
                } else if (elder.getSell().contains(content)) {
                    elderList.add(elder);
                }
            }
            if (!elderList.isEmpty())
                mList2.add(elderList);
        }
        //将mList2送控件显示
        if (!mList2.isEmpty()) {
            sectionedExpandableLayoutHelper.clearItems();
            for (List<DrugElders> elders : mList2) {
                String firstLetter = elders.get(0).getFirstLetter();

                sectionedExpandableLayoutHelper.addSection(firstLetter, ((ArrayList<DrugElders>) elders));
            }
        } else {
            sectionedExpandableLayoutHelper.clearItems();
        }
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    public void filterKind(String kindName) {
        List<DrugElders> elderList = null;
        mListFilter.clear();
        if (TextUtils.isEmpty(kindName)) {
            mListFilter.addAll(mList);
        } else {
            //过滤包含姓名串的长者资料存入mList2
            for (List<DrugElders> elders : mList) {
                elderList = new ArrayList<>();
                for (DrugElders elder : elders) {
                    List<TagsBean> tags = elder.getTags();
                    for (int i = 0; i < tags.size(); i++) {
                        if (kindName.equals(tags.get(i).getType()) && 0 == tags.get(i).getIsCompleted()){
                            elderList.add(elder);
                            break;
                        }
                    }
                }
                if (!elderList.isEmpty())
                    mListFilter.add(elderList);
            }
        }
        if (sectionedExpandableLayoutHelper != null) {
            //将mList2送控件显示
            if (!mListFilter.isEmpty()) {
                sectionedExpandableLayoutHelper.clearItems();
                for (List<DrugElders> elders : mListFilter) {
                    String firstLetter = elders.get(0).getFirstLetter();

                    sectionedExpandableLayoutHelper.addSection(firstLetter, ((ArrayList<DrugElders>) elders));
                }
            } else {
                sectionedExpandableLayoutHelper.clearItems();
            }
            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }

    public String submit() {
        List<Integer> subList = new ArrayList<>();
        for (int i = 0; i < mListFilter.size(); i++) {
            List<DrugElders> drugElderses = mListFilter.get(i);
            for (int j = 0; j < drugElderses.size(); j++) {
                DrugElders drugElders = drugElderses.get(j);
                int elderId = drugElders.getElderId();
                boolean selected = drugElders.isSelected();
                if (selected) {
                    subList.add(elderId);
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < subList.size(); s++) {
            sb.append(subList.get(s));
            if (s != subList.size() - 1){
                sb.append(",");
            }
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        displayAll();
    }

    /**
     * 显示全部长者资料
     */
    public void displayAll() {
        if (sectionedExpandableLayoutHelper != null) {
            sectionedExpandableLayoutHelper.clearItems();
            for (List<DrugElders> elders : mListFilter) {
                String firstLetter = elders.get(0).getFirstLetter();
                sectionedExpandableLayoutHelper.addSection(firstLetter, ((ArrayList<DrugElders>) elders));
            }
            sectionedExpandableLayoutHelper.notifyDataSetChanged();
        }
    }


    @Override
    public void itemClicked(DrugElders item) {
        //todo 配药的界面逻辑
        if (layoutType == 1) {
            if (isCompleted) {
            } else {
                //跳转到配界面
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("elder", item);
                bundle.putInt("type", layoutType);
                intent.putExtras(bundle);
                intent.setClass(getContext(), DrugNextActivity.class);
                startActivity(intent);
            }
        } else if (layoutType == 2) { //todo 发药界面逻辑
            if (isCompleted) {
            } else {
                if (canSelectedItems) {
                    item.setSelected(!item.isSelected());
                    sectionedExpandableLayoutHelper.notifyDataSetChanged();
                } else {
                    //跳转到发药界面
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("elder", item);
                    bundle.putInt("type", layoutType);
                    intent.putExtras(bundle);
                    intent.setClass(getContext(), DrugNextActivity.class);
                    startActivity(intent);
                }
            }
        } else if (layoutType == 3) {//  胰岛素界面逻辑
            if (isCompleted){

            }else {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable("elder", item);
                bundle.putInt("type", layoutType);
                intent.putExtras(bundle);
                intent.setClass(getContext(), DrugNextActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void itemClicked(Section section) {
    }


    /**
     * (发药界面内容)
     * 一键发药  或  取消
     *
     * @param canSelectedItems
     */
    public void setCanSelectedItems(boolean canSelectedItems, boolean selectAll) {
        this.canSelectedItems = canSelectedItems;
        //初始化选项 是否显示选中的样式
        for (int i = 0; i < mList.size(); i++) {
            List<DrugElders> drugElderses = mList.get(i);
            for (int j = 0; j < drugElderses.size(); j++) {
                drugElderses.get(j).setSelected(selectAll);
                drugElderses.get(j).setShowSelectedView(canSelectedItems);
            }
        }
        sectionedExpandableLayoutHelper.notifyDataSetChanged();
    }

    public interface RefreshListener {
        void refresh();
    }

    private RefreshListener refreshLishtener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            refreshLishtener = ((RefreshListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("类型异常");
        }
    }
}
