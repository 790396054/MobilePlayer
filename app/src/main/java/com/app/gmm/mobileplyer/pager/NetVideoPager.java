package com.app.gmm.mobileplyer.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.gmm.mobileplyer.AppConfig;
import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.activity.SystemVideoPlayer;
import com.app.gmm.mobileplyer.adapter.NetVideoPagerAdapter;
import com.app.gmm.mobileplyer.basepager.BasePager;
import com.app.gmm.mobileplyer.domain.MediaItem;
import com.app.gmm.mobileplyer.utils.Constants;
import com.app.gmm.mobileplyer.utils.LogUtil;
import com.app.gmm.mobileplyer.utils.SerializableUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by gmm on 2017/2/18.
 * 网络视频页面
 */

public class NetVideoPager extends BasePager {

    @ViewInject(R.id.listView)
    private ListView mListView;

    @ViewInject(R.id.tv_net_memo)
    private  TextView mTextView;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mProgressBar;

    /**
     * 装数据集合
     */
    private ArrayList<MediaItem> mediaItems;

    /**
     * 适配器
     */
    private NetVideoPagerAdapter adapter;

    /**
     * 是否已经加载更多了
     */
    private boolean isLoadMore = false;

    public NetVideoPager(Context context) {
        super(context);
    }

    /**
     * 初始化当前页面控件，由父类调用
     * @return
     */
    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.pager_net_video, null);
        x.view().inject(this, view);
        // 设置单击条目事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 1.传递列表数据
                Intent intent = new Intent(mContext, SystemVideoPlayer.class);
                intent.putExtra("videolist", SerializableUtil.toJson(mediaItems));
                intent.putExtra("position",position);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("网络视频的数据被初始化了。。。");
        RequestParams params = new RequestParams(Constants.NET_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("联网成功。。。" + result);
                //主线程
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("联网失败。。。" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("联网取消。。。" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                LogUtil.e("请求完成。。。");
            }
        });
    }

    /**
     * 处理服务端返回的数据
     * @param json
     */
    private void processData(String json) {
        mediaItems = parseJson(json);
        if (mediaItems != null && mediaItems.size() > 0) {
            adapter = new NetVideoPagerAdapter(mContext, mediaItems);
            mListView.setAdapter(adapter);

            mTextView.setVisibility(View.GONE);
        }else {

            mTextView.setVisibility(View.VISIBLE);
        }

        mProgressBar.setVisibility(View.GONE);

//        if(!isLoadMore){
//            mediaItems = parseJson(json);
//            //showData();
//        }else{
//            //加载更多
//            //要把得到更多的数据，添加到原来的集合中
//            ArrayList<MediaItem> moreDatas = parseJson(json);
//            isLoadMore = false;
//            mediaItems.addAll(parseJson(json));
//            //刷新适配器
//            //adapter.notifyDataSetChanged();
//        }
    }

    /**
     * 解决json数据：
     * 1.用系统接口解析json数据
     * 2.使用第三方解决工具（Gson,fastjson）
     * @param json
     * @return
     */
    private ArrayList<MediaItem> parseJson(String json) {
        ArrayList<MediaItem> mediaItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if(jsonArray!= null && jsonArray.length() >0){
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);
                    if(jsonObjectItem != null){
                        MediaItem mediaItem = new MediaItem();
                        mediaItem.name = jsonObjectItem.optString("movieName");//name
                        mediaItem.desc= jsonObjectItem.optString("videoTitle");//desc
                        mediaItem.imageUrl = jsonObjectItem.optString("coverImg");//imageUrl
                        mediaItem.data = jsonObjectItem.optString("hightUrl");//data
                        //把数据添加到集合
                        mediaItems.add(mediaItem);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaItems;
    }
}
