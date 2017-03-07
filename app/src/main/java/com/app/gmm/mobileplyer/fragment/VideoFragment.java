package com.app.gmm.mobileplyer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.gmm.mobileplyer.pager.AudioPager;
import com.app.gmm.mobileplyer.pager.VideoPager;

/**
 * Created by gmm on 2017/3/7.
 */

public class VideoFragment extends Fragment {

    private VideoPager mVideoPager;

    public VideoFragment(VideoPager pager){
        this.mVideoPager = pager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mVideoPager.initData();
        return mVideoPager.mRootView;
    }
}
