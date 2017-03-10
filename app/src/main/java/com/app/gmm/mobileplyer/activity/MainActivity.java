package com.app.gmm.mobileplyer.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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

public class MainActivity extends FragmentActivity {
    private FrameLayout fl_main_content;

    private RadioGroup mRadioGroup;

    private List<Fragment> mPagers;

    private int position; // 选中的位置
    private BasePager mBasePager;

    /**
     * 上次切换的Fragment
     */
    private Fragment mContent;

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

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (checkedId) {
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
//            setFragment();
            //根据位置得到对应的Fragment
            Fragment to = getFragment();
            //替换
            switchFrament(mContent, to);
        }
    }

    /**
     * 根据位置得到对应的Fragment
     *
     * @return
     */
    private Fragment getFragment() {
        Fragment fragment = mPagers.get(position);
        return fragment;
    }

    /**
     * 设置Fragment
     */
   /* private void setFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fl_main_content, mPagers.get(position));
        transaction.commit();
    }*/


    /**
     * @param from 刚显示的Fragment,马上就要被隐藏了
     * @param to   马上要切换到的Fragment，一会要显示
     */
    private void switchFrament(Fragment from, Fragment to) {
        if (from != to) {
            mContent = to;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //才切换
            //判断有没有被添加
            if (!to.isAdded()) {
                //to没有被添加
                //from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //添加to
                if (to != null) {
                    ft.add(R.id.fl_main_content, to).commit();
                }
            } else {
                //to已经被添加
                // from隐藏
                if (from != null) {
                    ft.hide(from);
                }
                //显示to
                if (to != null) {
                    ft.show(to).commit();
                }
            }
        }
    }
}
