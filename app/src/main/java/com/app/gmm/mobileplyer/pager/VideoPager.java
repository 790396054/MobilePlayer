package com.app.gmm.mobileplyer.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.activity.SystemVideoPlayer;
import com.app.gmm.mobileplyer.adapter.VideoPagerAdapter;
import com.app.gmm.mobileplyer.basepager.BasePager;
import com.app.gmm.mobileplyer.domain.MediaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmm on 2017/2/18.
 * 视频页面
 */

public class VideoPager extends BasePager {

    private ListView mListView;
    private TextView mTextView;
    private ProgressBar mProgressBar;

    private VideoPagerAdapter mAdapter;

    /**
     * 视频集合
     */
    private List<MediaItem> mMediaItems;

    public VideoPager(Context context) {
        super(context);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mMediaItems != null && mMediaItems.size() > 0) { // 显示数据
                // 设置数据
                mAdapter = new VideoPagerAdapter(mContext, mMediaItems);
                mListView.setAdapter(mAdapter);
                mTextView.setVisibility(View.GONE);
            } else { //显示提示内容
                mTextView.setVisibility(View.VISIBLE);
            }

            mProgressBar.setVisibility(View.GONE);
        }
    };

    @Override
    protected View initView() {
        View view = View.inflate(mContext, R.layout.pager_video, null);
        mListView = (ListView) view.findViewById(R.id.listView);
        mTextView = (TextView) view.findViewById(R.id.tv_memo);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        // 设置单击条目事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(mContext, "" +  mMediaItems.get(position), Toast.LENGTH_SHORT).show();
                // 调用系统播放器播放视频--隐式意图
//                Intent intent = new Intent();
//                intent.setDataAndType(Uri.parse(mMediaItems.get(position).data), "video/*");
//                mContext.startActivity(intent);
                // 调用自己写的播放器--显示意图
                Intent intent = new Intent(mContext, SystemVideoPlayer.class);
                intent.setDataAndType(Uri.parse(mMediaItems.get(position).data), "video/*");
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        mMediaItems = new ArrayList<>();
        // 加载本地视频
        getDataFromLocal();
    }

    /**
     * 加载本地视频，两种方法
     * 1.遍历 sd 卡，后缀名
     * 2.从内容提供者里面获取视频
     */
    private void getDataFromLocal() {
        new Thread(){
            @Override
            public void run() {
                if(!requestPermission((Activity) mContext))
                    return;

                ContentResolver resolver = mContext.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Video.Media.DURATION,//视频总时长
                        MediaStore.Video.Media.SIZE,//视频的文件大小
                        MediaStore.Video.Media.DATA,//视频的绝对地址
                        MediaStore.Video.Media.ARTIST,//歌曲的演唱者
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        MediaItem item = new MediaItem();
                        mMediaItems.add(item);
                        item.name = cursor.getString(0);
                        item.duration = cursor.getLong(1);
                        item.size = cursor.getLong(2);
                        item.data = cursor.getString(3);
                        item.artist = cursor.getString(4);
                    }

                    cursor.close();
                }
                // 发消息
                mHandler.sendEmptyMessage(10);

            }
        }.start();
    }

    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean requestPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
            return false;
        }
        return true;
    }

}
