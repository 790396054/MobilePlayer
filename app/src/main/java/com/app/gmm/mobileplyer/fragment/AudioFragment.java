package com.app.gmm.mobileplyer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.gmm.mobileplyer.pager.AudioPager;
import com.app.gmm.mobileplyer.utils.LogUtil;

/**
 * Created by gmm on 2017/3/7.
 */

public class AudioFragment extends Fragment {

    private AudioPager mAudioPager;

    public AudioFragment(AudioPager pager){
        this.mAudioPager = pager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAudioPager.initData();
        LogUtil.d("AudioFragment onCreateView ... ");
        return mAudioPager.mRootView;
    }
}
