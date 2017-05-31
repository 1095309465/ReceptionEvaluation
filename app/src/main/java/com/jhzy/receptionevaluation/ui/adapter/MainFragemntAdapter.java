package com.jhzy.receptionevaluation.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by bigyu on 2017/2/17.
 */

public class MainFragemntAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public MainFragemntAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragmentList = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0;
    }
}
