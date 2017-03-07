package com.app.gmm.mobileplyer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.gmm.mobileplyer.pager.NetVideoPager;

/**
 * Created by gmm on 2017/3/7.
 */

public class NetVideoFragment extends Fragment {

    private NetVideoPager mNetVideoPager;

    public NetVideoFragment(NetVideoPager pager) {
        this.mNetVideoPager = pager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mNetVideoPager.initData();
        return mNetVideoPager.mRootView;
    }
}
