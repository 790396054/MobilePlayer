package com.app.gmm.mobileplyer;

import android.app.Application;

import org.xutils.x;

/**
 * Created by gmm on 2017/3/28.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 xUtil
        x.Ext.init(this);
        x.Ext.setDebug(AppConfig.DEBUG);
    }
}
