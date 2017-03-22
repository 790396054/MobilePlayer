package com.app.gmm.mobileplyer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.domain.MediaItem;
import com.app.gmm.mobileplyer.utils.SerializableUtil;
import com.app.gmm.mobileplyer.utils.Utils;
import com.app.gmm.mobileplyer.view.VideoView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gmm on 2017/3/17.
 * 自己写的视频播放器
 */

public class SystemVideoPlayer extends Activity implements View.OnClickListener{

    /**
     * 播放进度
     */
    private static final int PROGRESS_INT = 1;
    /**
     * 隐藏控制面板的消息
     */
    private static final int HIDE_MEDIACONTROLLER = 2;

    /**
     * 控制面板默认显示时长
     */
    private static final int SHOW_TIME = 4000;

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
    private RelativeLayout rlControlView;

    /**
     * 手势识别器
     */
    private GestureDetector mDetector;

    private Uri uri;
    private Utils mUtils;
    private BatteryBroadCastRecevier mBatteryRecevier; // 电量变化的广播接收器
    /**
     * 传入进来的视频列表
     */
    private List<MediaItem> mediaItems;
    /**
     * 要播放的列表中的具体位置
     */
    int position;

    private boolean isVisibleControl = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        findViews();

        initData();

        getData();

        setData();

        setBtnState();
        // 设置控制面板
//        mVideoView.setMediaController(new MediaController(this));
    }

    // 得到上一个页面传递过来的数据
    private void getData() {
        //得到播放地址
        uri = getIntent().getData();//文件夹，图片浏览器，QQ空间
        position = getIntent().getIntExtra("position", 0);
        mediaItems = SerializableUtil.fromJson(getIntent().getStringExtra("videolist"),
                new TypeToken<List<MediaItem>>(){}.getType());
     }

    private void setData(){
        if (mediaItems != null && mediaItems.size() > 0) {
            MediaItem mediaItem = mediaItems.get(position);
            tvVideoName.setText(mediaItem.name); // 设置视频的名称
            mVideoView.setVideoPath(mediaItem.data);
        } else if (uri != null) {
            tvVideoName.setText(uri.toString());
            mVideoView.setVideoURI(uri);
        } else {
            Toast.makeText(this, "没有播放的数据", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        mUtils = new Utils();

        mBatteryRecevier = new BatteryBroadCastRecevier();
        // 注册电量变化的广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryRecevier, filter);
        // 实例化手势识别器
        mDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) { // 长按
                startAndPause();
                super.onLongPress(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) { // 双击
                startAndPause();
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) { // 单击
                if (isVisibleControl) { // 显示状态,隐藏控制面板
                    isVisibleControl = false;
                    rlControlView.setVisibility(View.GONE);
                    // 取消发消息
                    mHandler.removeMessages(HIDE_MEDIACONTROLLER);
                }else { // 隐藏状态，显示控制面板
                    isVisibleControl = true;
                    rlControlView.setVisibility(View.VISIBLE);
                    // 发消息隐藏
                    mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, SHOW_TIME);
                }
                return super.onSingleTapConfirmed(e);
            }
        });
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
        rlControlView = (RelativeLayout) findViewById(R.id.rl_control_view);
        setListener();


        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnPreVideo.setOnClickListener( this );
        btnVideoPlayPause.setOnClickListener( this );
        btnNextVideo.setOnClickListener( this );
        btnSwitchScreen.setOnClickListener( this );
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
                    // 设置系统时间
                    tvTime.setText(getSysTime());

                    // 取消消息
                    mHandler.removeMessages(PROGRESS_INT);
                    // 发送延迟消息，1秒钟刷新
                    mHandler.sendEmptyMessageDelayed(PROGRESS_INT, 1000);
                    break;
                case HIDE_MEDIACONTROLLER:
                    isVisibleControl = true;
                    rlControlView.setVisibility(View.GONE);
                    break;
            }
        }
    };

    /**
     * 得到系统时间
     * @return
     */
    private String getSysTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }

    private void setListener() {
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
                // 默认隐藏控制面板
                isVisibleControl = false;
                rlControlView.setVisibility(View.GONE);
                //
                mVideoView.setVideoSize(mp.getVideoWidth(), mp.getVideoHeight());

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
                mp.stop();
                nextVideo();
            }
        });

        // seekBar 的监听
        seekbarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 当手指滑动的时候，会引起 seekBar 的进度变化，会回调这个方法
             * @param seekBar
             * @param progress
             * @param fromUser 如果是用户引起的为 true， 否则是 false
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mVideoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeMessages(HIDE_MEDIACONTROLLER);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, SHOW_TIME);
            }
        });
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
            finish();
        } else if ( v == btnPreVideo ) { // 上一个按钮
            preVideo();
        } else if ( v == btnVideoPlayPause ) { //播放视频按钮
            startAndPause();
        } else if ( v == btnNextVideo ) { // 下一个按钮
            nextVideo();
        } else if ( v == btnSwitchScreen ) {
            // Handle clicks for btnSwitchScreen
        }

        mHandler.removeMessages(HIDE_MEDIACONTROLLER);
        mHandler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, SHOW_TIME);
    }

    private void startAndPause() {
        if (mVideoView.isPlaying()) { // 播放视频
            mVideoView.pause();
            btnVideoPlayPause.setBackgroundResource(R.drawable.btn_video_play_selector);
        } else { // 暂停视频
            mVideoView.start();
            btnVideoPlayPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
        }
    }

    // 播放下一个视频
    private void nextVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position++;
            if (position < mediaItems.size()) {
                MediaItem item = mediaItems.get(position);
                tvVideoName.setText(item.name);
                mVideoView.setVideoPath(item.data);
                btnVideoPlayPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
                setBtnState();
            }
        } else if (uri != null) {
            setBtnState();
        }
    }

    // 播放上一个视频
    private void preVideo() {
        if (mediaItems != null && mediaItems.size() > 0) {
            position--;
            if (position < mediaItems.size() && position >= 0) {
                MediaItem item = mediaItems.get(position);
                tvVideoName.setText(item.name);
                mVideoView.setVideoPath(item.data);
                setBtnState();
            }
        }else if (uri != null){
            setBtnState();
        }
    }

    // 设置按钮的状态
    private void setBtnState() {
        if (mediaItems != null && mediaItems.size() > 0) {
            if (mediaItems.size() == 1) {
                setBtnEnabled(false);
            } else if (mediaItems.size() == 2) {
                setBtnEnabled(true);
                if (position == 0) {
                    btnPreVideo.setEnabled(false);
                    btnPreVideo.setBackgroundResource(R.drawable.btn_pre_gray);
                } else if (position == mediaItems.size() - 1){
                    btnNextVideo.setEnabled(false);
                    btnNextVideo.setBackgroundResource(R.drawable.btn_next_gray);
                }
            } else {
                if (position == 0) {
                    btnPreVideo.setEnabled(false);
                    btnPreVideo.setBackgroundResource(R.drawable.btn_pre_gray);
                } else if (position == mediaItems.size() - 1) {
                    btnNextVideo.setEnabled(false);
                    btnNextVideo.setBackgroundResource(R.drawable.btn_next_gray);
                } else {
                    setBtnEnabled(true);
                }
            }
        } else if (uri != null) {
            setBtnEnabled(false);
        }
    }

    private void setBtnEnabled(boolean enabled){
        btnPreVideo.setEnabled(enabled);
        btnNextVideo.setEnabled(enabled);
        if (!enabled) {
            btnPreVideo.setBackgroundResource(R.drawable.btn_pre_gray);
            btnNextVideo.setBackgroundResource(R.drawable.btn_next_gray);
        }else {
            btnPreVideo.setBackgroundResource(R.drawable.btn_pre_normal);
            btnNextVideo.setBackgroundResource(R.drawable.btn_next_normal);
        }
    }

    @Override
    protected void onDestroy() {
        if (mBatteryRecevier != null) {
            unregisterReceiver(mBatteryRecevier);
            mBatteryRecevier = null;
        }
        super.onDestroy();
    }

    class BatteryBroadCastRecevier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0); // 0 -- 100
            setBattery(level);
        }
    }

    private void setBattery(int level) {
        if (level <= 0) {
            ivBattry.setImageResource(R.drawable.ic_battery_0);
        } else if (level <= 10) {
            ivBattry.setImageResource(R.drawable.ic_battery_10);
        } else if (level <= 20) {
            ivBattry.setImageResource(R.drawable.ic_battery_20);
        } else if (level <= 40) {
            ivBattry.setImageResource(R.drawable.ic_battery_40);
        }else if (level <= 60) {
            ivBattry.setImageResource(R.drawable.ic_battery_60);
        } else if (level <= 80) {
            ivBattry.setImageResource(R.drawable.ic_battery_80);
        } else if (level <= 90) {
            ivBattry.setImageResource(R.drawable.ic_battery_100);
        } else {
            ivBattry.setImageResource(R.drawable.ic_battery_100);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 把事件传递给手势识别器
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
