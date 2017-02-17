package com.app.gmm.mobileplyer.activity.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.app.gmm.mobileplyer.activity.basepager.BasePager;

/**
 * Created by gmm on 2017/2/18.
 */

public class AudioPager extends BasePager {

    private  TextView textView;

    public AudioPager(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        textView = new TextView(mContext);
        textView.setTextColor(Color.GREEN);
        textView.setTextSize(22);
        return textView;
    }

    @Override
    public void initData() {
        textView.setText("音频页面");
    }
}
