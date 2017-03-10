package com.app.gmm.mobileplyer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.gmm.mobileplyer.pager.NetAudioPager;
import com.app.gmm.mobileplyer.utils.LogUtil;

/**
 * Created by gmm on 2017/3/7.
 */

public class NetAudioFragment extends Fragment {

    private NetAudioPager mNetAudioPager;

    public NetAudioFragment(NetAudioPager pager) {
        this.mNetAudioPager = pager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mNetAudioPager.initData();
        LogUtil.d("NetAudioFragment onCreateView ... ");
        return mNetAudioPager.mRootView;
    }
}
