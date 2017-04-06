package com.app.gmm.mobileplyer.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.basepager.BasePager;
import com.app.gmm.mobileplyer.utils.Constants;
import com.app.gmm.mobileplyer.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by gmm on 2017/2/18.
 */

public class NetAudioPager extends BasePager {

    private  TextView textView;

    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.pager_net_video, null);
        return view;
    }

    @Override
    public void initData() {
        getDataFromNet();

    }

    private void getDataFromNet() {
        RequestParams parameter = new RequestParams(Constants.ALL_RES_URL);

        x.http().get(parameter, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtil.e(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
