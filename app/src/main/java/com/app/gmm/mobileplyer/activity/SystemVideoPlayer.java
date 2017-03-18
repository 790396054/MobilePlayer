package com.app.gmm.mobileplyer.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.utils.Utils;

/**
 * Created by gmm on 2017/3/17.
 * 自己写的视频播放器
 */

public class SystemVideoPlayer extends Activity implements View.OnClickListener{

    /**
     * 播放进度
     */
    private static final int PROGRESS_INT = 1;
    private VideoView mVideoView;
    private LinearLayout llTop;
    private TextView tvVideoName;
    private ImageView ivBattry;
    private TextView tvTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvVideoTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnPreVideo;
    private Button btnVideoPlayPause;
    private Button btnNextVideo;
    private Button btnSwitchScreen;

    private Uri uri;
    private Utils mUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        findViews();

        mUtils = new Utils();

        // 得到 URI 准备好数据
        uri = getIntent().getData();
        if (uri != null) {
            mVideoView.setVideoURI(uri);
        }

        // 设置控制面板
//        mVideoView.setMediaController(new MediaController(this));
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-03-18 22:58:34 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvVideoName = (TextView)findViewById( R.id.tv_video_name );
        ivBattry = (ImageView)findViewById( R.id.iv_battry );
        tvTime = (TextView)findViewById( R.id.tv_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvVideoTime = (TextView)findViewById( R.id.tv_video_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnPreVideo = (Button)findViewById( R.id.btn_pre_video );
        btnVideoPlayPause = (Button)findViewById( R.id.btn_video_play_pause );
        btnNextVideo = (Button)findViewById( R.id.btn_next_video );
        btnSwitchScreen = (Button)findViewById( R.id.btn_switch_screen );

        // 设置监听 准备好了的监听
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // 当底层解码准备好的时候
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                // 设置进度
                int duration = mVideoView.getDuration();
                seekbarVideo.setMax(duration);
                tvDuration.setText(mUtils.stringForTime(duration));
                // 发送消息
                mHandler.sendEmptyMessage(PROGRESS_INT);
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

        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnPreVideo.setOnClickListener( this );
        btnVideoPlayPause.setOnClickListener( this );
        btnNextVideo.setOnClickListener( this );
        btnSwitchScreen.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-03-18 22:58:34 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
        } else if ( v == btnPreVideo ) {
            // Handle clicks for btnPreVideo
        } else if ( v == btnVideoPlayPause ) {
            // Handle clicks for btnVideoPlayPause
            if (mVideoView.isPlaying()) { // 播放视频
                mVideoView.pause();
                btnVideoPlayPause.setBackgroundResource(R.drawable.btn_video_play_selector);
            } else { // 暂停视频
                mVideoView.start();
                btnVideoPlayPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            }
        } else if ( v == btnNextVideo ) {
            // Handle clicks for btnNextVideo
        } else if ( v == btnSwitchScreen ) {
            // Handle clicks for btnSwitchScreen
        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS_INT:
                    // 设置进度
                    int duration = mVideoView.getCurrentPosition();
                    seekbarVideo.setProgress(duration);
                    tvVideoTime.setText(mUtils.stringForTime(duration));
                    // 取消消息
                    mHandler.removeMessages(PROGRESS_INT);
                    // 发送延迟消息，1秒钟刷新
                    mHandler.sendEmptyMessageDelayed(PROGRESS_INT, 1000);
                    break;
            }
        }
    };
}
