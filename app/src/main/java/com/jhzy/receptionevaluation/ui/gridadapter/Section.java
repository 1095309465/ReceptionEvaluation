package com.jhzy.receptionevaluation.ui.gridadapter;

/**
 * Created by welse on 2017/4/17.
 */

public class Section {
    private final String name;

    public boolean isExpanded;

    public Section(String name) {
        this.name = name;
        isExpanded = true;
    }

    public String getName() {
        return name;
    }
}
