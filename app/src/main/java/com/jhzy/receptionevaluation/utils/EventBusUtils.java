package com.jhzy.receptionevaluation.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by bigyu2012 on 2017/4/24.
 */

public class EventBusUtils {


    public  static  void sendStickMessage(Object o){
        EventBus.getDefault().postSticky(o);
    }

}
