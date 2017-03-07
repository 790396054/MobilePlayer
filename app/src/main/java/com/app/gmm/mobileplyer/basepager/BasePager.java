package com.app.gmm.mobileplyer.basepager;

import android.content.Context;
import android.view.View;

/**
 * Created by gmm on 2017/2/18.
 * 页面的基类
 */

public abstract class BasePager {

    protected Context mContext; // 上下文

    public View mRootView;

    public BasePager(Context context){
        this.mContext = context;
        mRootView = initView();
    }

    protected abstract View initView();

    public void initData(){

    }
}
