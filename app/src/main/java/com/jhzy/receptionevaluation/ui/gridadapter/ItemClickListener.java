package com.jhzy.receptionevaluation.ui.gridadapter;

import com.jhzy.receptionevaluation.ui.bean.dispensingdrug.DrugElders;

/**
 * Created by welse on 2017/4/17.
 */

public interface ItemClickListener {
    void itemClicked(DrugElders item);
    void itemClicked(Section section);
}
