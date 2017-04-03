package com.app.gmm.mobileplyer.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.activity.AudioPlayerActivity;
import com.app.gmm.mobileplyer.adapter.VideoPagerAdapter;
import com.app.gmm.mobileplyer.basepager.BasePager;
import com.app.gmm.mobileplyer.domain.MediaItem;
import com.app.gmm.mobileplyer.utils.LogUtil;
import com.app.gmm.mobileplyer.utils.PermissionUtils;

import java.util.ArrayList;

/**
 * Created by gmm on 2017/2/18.
 */

public class AudioPager extends BasePager {

    private  TextView mTextView;
    private ListView mListView;
    private ProgressBar mProgressBar;

    private VideoPagerAdapter videoPagerAdapter;

    /**
     * 装数据集合
     */
    private ArrayList<MediaItem> mediaItems;

    public AudioPager(Context context) {
        super(context);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10: // 得到本地音频数据
                    if (mediaItems != null && mediaItems.size() > 0) {
                        // 设置适配器
                        videoPagerAdapter = new VideoPagerAdapter(mContext, mediaItems);
                        mListView.setAdapter(videoPagerAdapter);
                        // 隐藏文本
                        mTextView.setVisibility(View.GONE);
                    }else {
                        mTextView.setVisibility(View.VISIBLE);
                        mTextView.setText("没有发现音频。。。");
                    }
                    mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    /**
     * 初始化当前页面，由父类调用
     * @return
     */
    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.pager_video, null);
        mListView = (ListView) view.findViewById(R.id.listView);
        mTextView = (TextView) view.findViewById(R.id.tv_memo);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        // 设置 listView 的 item 点击事件
        mListView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地音频数据被初始化了");
        getDataFromLocal();
    }

    /**
     * 从本地的sdcard得到数据
     * //1.遍历sdcard,后缀名
     * //2.从内容提供者里面获取视频
     * //3.如果是6.0的系统，动态获取读取sdcard的权限
     */
    private void getDataFromLocal() {
        new Thread(){
            @Override
            public void run() {
                super.run();
                PermissionUtils.isGrantExternalRW((Activity) mContext);
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = mContext.getContentResolver();

                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视频总时长
                        MediaStore.Audio.Media.SIZE,//视频的文件大小
                        MediaStore.Audio.Media.DATA,//视频的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor == null)
                    return;
                while (cursor.moveToNext()) {
                    MediaItem mediaItem = new MediaItem();
                    mediaItems.add(mediaItem);
                    mediaItem.name = cursor.getString(0);
                    mediaItem.duration = cursor.getLong(1);
                    mediaItem.size = cursor.getLong(2);
                    mediaItem.data = cursor.getString(3);
                    mediaItem.artist = cursor.getString(4);
                }
                cursor.close();

                // handler 发送消息
                mHandler.sendEmptyMessage(10);

            }
        }.start();
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //3.传递列表数据-对象-序列化
            Intent intent = new Intent(mContext,AudioPlayerActivity.class);
            intent.putExtra("position",position);
            mContext.startActivity(intent);
        }
    }
}
