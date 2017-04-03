package com.app.gmm.mobileplyer.activity;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.gmm.mobileplyer.IPlayMusicService;
import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.service.MusicPlayService;
import com.app.gmm.mobileplyer.utils.Utils;

/**
 * Created by gmm on 2017/4/2.
 */

public class AudioPlayerActivity extends Activity implements View.OnClickListener{

    private int position;

    /**
     * 进度更新
     */
    private static final int PROGRESS = 1;
    /**
     * 显示歌词
     */
    private static final int SHOW_LYRIC = 2;

    private boolean notification;
    private IPlayMusicService service;//服务的代理类，通过它可以调用服务的方法

    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvName;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnLyrc;
   // private ShowLyricView showLyricView;
   // private BaseVisualizerView baseVisualizerView;

    private Utils utils;

    private MyReceiver receiver;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PROGRESS:
                    try {
                        // 得到当前进度
                        int currentProgress = service.getCurrentPosition();
                        // 设置进度
                        seekbarAudio.setProgress(currentProgress);
                        // 设置时间文本
                        tvTime.setText(utils.stringForTime(currentProgress) + "/" + utils.stringForTime(service.getDuration()));
                        // 每秒发一个消息
                        mHandler.removeMessages(PROGRESS);
                        mHandler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        bindAndStartService();
    }

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        intent.setAction(MusicPlayService.OPENAUDIO);
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent); // 不至于实例化多个服务
    }

    private void getData() {
        notification = getIntent().getBooleanExtra("notification", false);
        if (!notification) {
            position = getIntent().getIntExtra("position", 0);
        }
    }

    private void findViews() {
        setContentView(R.layout.activity_audioplayer);

        ivIcon = (ImageView)findViewById( R.id.iv_icon );
        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable rocketAnimation = (AnimationDrawable) ivIcon.getBackground();
        rocketAnimation.start();
        tvArtist = (TextView)findViewById( R.id.tv_artist );
        tvName = (TextView)findViewById( R.id.tv_name );
        tvTime = (TextView)findViewById( R.id.tv_time );
        seekbarAudio = (SeekBar)findViewById( R.id.seekbar_audio );
        btnAudioPlaymode = (Button)findViewById( R.id.btn_audio_playmode );
        btnAudioPre = (Button)findViewById( R.id.btn_audio_pre );
        btnAudioStartPause = (Button)findViewById( R.id.btn_audio_start_pause );
        btnAudioNext = (Button)findViewById( R.id.btn_audio_next );
//        btnLyrc = (Button)findViewById( R.id.btn_lyrc );
       // showLyricView = (ShowLyricView) findViewById(R.id.showLyricView);
       // baseVisualizerView = (BaseVisualizerView)    findViewById(R.id.baseVisualizerView);

        btnAudioPlaymode.setOnClickListener( this );
        btnAudioPre.setOnClickListener( this );
        btnAudioStartPause.setOnClickListener( this );
        btnAudioNext.setOnClickListener( this );
//        btnLyrc.setOnClickListener( this );

        //设置视频的拖动
        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    private ServiceConnection con = new ServiceConnection() {

        /**
         * 当连接成功的时候回调这个方法
         * @param name
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IPlayMusicService.Stub.asInterface(iBinder);
            if(service != null){
                try {
                    if (notification) {
                        showData();
                    }else {
                        service.openAudio(position);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 当断开连接的时候回调这个方法
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                if(service != null){
                    service.stop();
                    service = null;
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                //拖动进度
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private void initData() {
        utils = new Utils();

        // 注册广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter(MusicPlayService.OPENAUDIO_RECEIVER);
        registerReceiver(receiver, intentFilter);
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-07-22 16:52:50 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnAudioPlaymode ) {
            // Handle clicks for btnAudioPlaymode
            setPlayMode();
        } else if ( v == btnAudioPre ) {
            // Handle clicks for btnAudioPre
            if(service != null){
                try {
                    service.pre();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if ( v == btnAudioStartPause ) {
            if(service != null){
                try {
                    if(service.isPlaying()){
                        //暂停
                        service.pause();
                        //按钮-播放
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    }else{
                        //播放
                        service.start();
                        //按钮-暂停
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            // Handle clicks for btnAudioStartPause
        } else if ( v == btnAudioNext ) {
            // Handle clicks for btnAudioNext
            if(service != null){
                try {
                    service.next();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if ( v == btnLyrc ) {
            // Handle clicks for btnLyrc
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            showData();
            checkPlaymode();
        }
    }

    /**
     * 设置播放模式
     */
    private void setPlayMode() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayService.REPEAT_NORMAL) {
                playmode = MusicPlayService.REPEAT_SINGLE;
            } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
                playmode = MusicPlayService.REPEAT_ALL;
            }else if(playmode == MusicPlayService.REPEAT_NORMAL){
                playmode = MusicPlayService.REPEAT_NORMAL;
            } else {
                playmode = MusicPlayService.REPEAT_NORMAL;
            }
            service.setPlayMode(playmode);
            showPlaymode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showPlaymode() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayService.REPEAT_NORMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(this, "顺序播放", Toast.LENGTH_SHORT).show();
            } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                Toast.makeText(this, "单曲播放", Toast.LENGTH_SHORT).show();
            } else if (playmode == MusicPlayService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(this, "全部播放", Toast.LENGTH_SHORT).show();
            }else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(this, "全部播放", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void checkPlaymode() {
        try {
            int playmode = service.getPlayMode();
            if (playmode == MusicPlayService.REPEAT_NORMAL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            } else if (playmode == MusicPlayService.REPEAT_SINGLE) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            } else if (playmode == MusicPlayService.REPEAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            }else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    // 显示数据
    public void showData() {
        try {
            tvArtist.setText(service.getArtist());
            tvName.setText(service.getName());
            seekbarAudio.setMax(service.getDuration());
            // 发消息，更新进度
            mHandler.sendEmptyMessage(PROGRESS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(con);
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        if (con != null) {
            mHandler.removeCallbacksAndMessages(null);
            con = null;
        }
        super.onDestroy();
    }
}
