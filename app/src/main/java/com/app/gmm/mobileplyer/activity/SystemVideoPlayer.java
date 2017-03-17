package com.app.gmm.mobileplyer.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.gmm.mobileplyer.R;

/**
 * Created by gmm on 2017/3/17.
 * 自己写的视频播放器
 */

public class SystemVideoPlayer extends Activity{

    private VideoView mVideoView;

    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        mVideoView = (VideoView) findViewById(R.id.videoView);

        // 设置监听 准备好了的监听
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                 mp.start();
            }
        });

        // 播放出错的监听
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(SystemVideoPlayer.this, "播放错误", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 播放完毕的监听
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(SystemVideoPlayer.this, "播放完毕", Toast.LENGTH_SHORT).show();
                mp.stop();
            }
        });
        // 得到 URI 准备好数据
        uri = getIntent().getData();
        if (uri != null) {
            mVideoView.setVideoURI(uri);
        }

        // 设置控制面板
        mVideoView.setMediaController(new MediaController(this));
    }
}
