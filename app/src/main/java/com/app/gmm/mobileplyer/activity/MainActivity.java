package com.app.gmm.mobileplyer.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.basepager.BasePager;
import com.app.gmm.mobileplyer.fragment.AudioFragment;
import com.app.gmm.mobileplyer.fragment.NetAudioFragment;
import com.app.gmm.mobileplyer.fragment.NetVideoFragment;
import com.app.gmm.mobileplyer.fragment.VideoFragment;
import com.app.gmm.mobileplyer.pager.AudioPager;
import com.app.gmm.mobileplyer.pager.NetAudioPager;
import com.app.gmm.mobileplyer.pager.NetVideoPager;
import com.app.gmm.mobileplyer.pager.VideoPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmm on 2017/2/16.
 * 主页面
 */

public class MainActivity extends FragmentActivity{
    private FrameLayout fl_main_content;

    private RadioGroup mRadioGroup;

    private List<Fragment> mPagers;

    private int position; // 选中的位置
    private BasePager mBasePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_bottom_tag);


        mPagers = new ArrayList<>();
        mPagers.add(new VideoFragment(new VideoPager(this)));
        mPagers.add(new AudioFragment(new AudioPager(this)));
        mPagers.add(new NetVideoFragment(new NetVideoPager(this)));
        mPagers.add(new NetAudioFragment(new NetAudioPager(this)));

        // 设置radioGroup的监听
        mRadioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        mRadioGroup.check(R.id.rb_video); // 默认选中页面
    }

    /**
     * 返回pager页面
     * @return
     */
//    private BasePager getBasePager() {
//        BasePager pager = mPagers.get(position);
//        if (pager != null) {
//            pager.initData();
//            return pager;
//        }
//        return null;
//    }


    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_video:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_netvideo:
                    position = 2;
                    break;
                case R.id.rb_netaudio:
                    position = 3;
                    break;
            }
            // 设置Fragment
            setFragment();
        }
    }

    /**
     * 设置Fragment
     */
    private void setFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main_content, mPagers.get(position));
        transaction.commit();
    }

}
