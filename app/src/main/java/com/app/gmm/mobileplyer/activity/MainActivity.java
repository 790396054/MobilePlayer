package com.app.gmm.mobileplyer.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.gmm.mobileplyer.R;

/**
 * Created by gmm on 2017/2/16.
 * 主页面
 */

public class MainActivity extends Activity{
    private FrameLayout fl_main_content;

    private RadioGroup rg_bottom_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);

        rg_bottom_tag.check(R.id.rb_video);
    }
}
