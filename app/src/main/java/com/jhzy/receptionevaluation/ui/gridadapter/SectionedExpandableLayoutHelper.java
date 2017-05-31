package com.jhzy.receptionevaluation.ui.gridadapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by welse on 2017/4/17.
 */
public class SectionedExpandableLayoutHelper<T extends Object>  implements SectionStateChangeListener {

    //data list
    private LinkedHashMap<Section, ArrayList<T>> mSectionDataMap = new LinkedHashMap<>();
    private ArrayList<Object> mDataArrayList = new ArrayList<>();
    private GridLayoutManager gridLayoutManager;
    private RecyclerView mRecyclerView = null;

    //section map
    //TODO : look for a way to avoid this
    private HashMap<String, Section> mSectionMap = new HashMap<>();

    //adapter
    private SectionedExpandableGridAdapter  mSectionedExpandableGridAdapter;

    public SectionedExpandableLayoutHelper(Context context, RecyclerView recyclerView, ItemClickListener itemClickListener,
                                           int gridSpanCount , int layoutType , boolean complete) {

        //setting the recycler view
        mRecyclerView = recyclerView;
        gridLayoutManager = new GridLayoutManager(context, gridSpanCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        mSectionedExpandableGridAdapter = new SectionedExpandableGridAdapter(context, mDataArrayList,
                gridLayoutManager, itemClickListener, this , layoutType , complete);
        recyclerView.setAdapter(mSectionedExpandableGridAdapter);
    }

    public void notifyDataSetChanged() {
        //TODO : handle this condition such that these functions won't be called if the recycler view is on scroll
        generateDataList();
        if ((mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) && (!mRecyclerView.isComputingLayout())) {
            mSectionedExpandableGridAdapter.notifyDataSetChanged();
        }
    }

    public void addSection(String section, ArrayList<T> items) {
        Section newSection;
        mSectionMap.put(section, (newSection = new Section(section)));
        mSectionDataMap.put(newSection, items);
    }

    public void addItem(String section, T item) {
        mSectionDataMap.get(mSectionMap.get(section)).add(item);
    }

    public void removeItem(String section, T item) {
        mSectionDataMap.get(mSectionMap.get(section)).remove(item);
    }

    public void removeSection(String section) {
        mSectionDataMap.remove(mSectionMap.get(section));
        mSectionMap.remove(section);
    }

    public void clearItems(){
        mSectionDataMap.clear();
        mSectionMap.clear();
    }

    private void generateDataList () {
        mDataArrayList.clear();
        for (Map.Entry<Section, ArrayList<T>> entry : mSectionDataMap.entrySet()) {
            Section key;
            mDataArrayList.add((key = entry.getKey()));
            if (key.isExpanded)
                mDataArrayList.addAll(entry.getValue());
        }
    }

    public void scrollToPositionWithOffset(int position,int offset){
        gridLayoutManager.scrollToPositionWithOffset(position,offset);
    }

    @Override
    public void onSectionStateChanged(Section section, boolean isOpen) {
        //section.isExpanded = isOpen;
        //notifyDataSetChanged();
    }
}
