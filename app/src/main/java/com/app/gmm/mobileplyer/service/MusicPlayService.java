package com.app.gmm.mobileplyer.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.app.gmm.mobileplyer.IPlayMusicService;
import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.activity.AudioPlayerActivity;
import com.app.gmm.mobileplyer.domain.MediaItem;
import com.app.gmm.mobileplyer.utils.CachesUtils;
import com.app.gmm.mobileplyer.utils.PermissionUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by gmm on 2017/3/31.
 */

public class MusicPlayService extends Service {
    public static final String OPENAUDIO = "com.atguigu.mobileplayer_OPENAUDIO";
    public static final String OPENAUDIO_RECEIVER = "com.atguigu.mobileplayer_OPENAUDIO_Receiver";
    public static final String PLAY_MODE = "PLAY_MODE";

    private ArrayList<MediaItem> mediaItems;
    private int position;
    private NotificationManager mManager;
    /**
     * 播放音乐
     */
    private MediaPlayer mediaPlayer;
    /**
     * 当前播放的音频文件对象
     */
    private MediaItem mediaItem;
    /**
     * 顺序播放
     */
    public static final int REPEAT_NORMAL = 1;
    /**
     * 单曲循环
     */
    public static final int REPEAT_SINGLE = 2;
    /**
     * 全部循环
     */
    public static final int REPEAT_ALL = 3;

    /**
     * 播放模式
     */
    private int playmode = REPEAT_NORMAL;

    @Override
    public void onCreate() {
        super.onCreate();
        playmode = CachesUtils.getInt(this, PLAY_MODE, REPEAT_NORMAL);
        getDataFromLocal();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    private IPlayMusicService.Stub mStub = new IPlayMusicService.Stub() {
        MusicPlayService service = MusicPlayService.this;
        @Override
        public void openAudio(int position) throws RemoteException {
            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {
            service.start();
        }

        @Override
        public void pause() throws RemoteException {
            service.pause();
        }

        @Override
        public void stop() throws RemoteException {
            service.start();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {
            service.next();
        }

        @Override
        public void pre() throws RemoteException {
            service.pre();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {
            service.setPlayMode(playmode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        /**
         拖动音频
         */
        public void seekTo(int position){
            service.seekTo(position);
        };

        /**
         * 是否正在播放
         */
        public boolean isPlaying(){
          return service.isPlaying();
        };
    };

    /**
     * 根据位置打开对应的音频文件
     * @param position
     */
    private void openAudio(int position){
        this.position = position;
        if (mediaItems == null || mediaItems.size() == 0) {
            Toast.makeText(MusicPlayService.this, "还没有数据", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaItem = mediaItems.get(position);
        if (mediaPlayer != null) {
            mediaPlayer.reset();
//            mediaPlayer.release();
        }
        try {
            mediaPlayer = new MediaPlayer();
            // 设置监听，播放出错，播放完成，准备好
            mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
            mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
            mediaPlayer.setOnErrorListener(new MyOnErrorListener());
            mediaPlayer.setDataSource(mediaItem.data);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public ContentResolver getContentResolver() {
        return super.getContentResolver();
    }

    /**
     * 播放音乐
     */
    private void start(){
        mediaPlayer.start();
        // 显示通知栏
        mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("321音乐")
                .setContentText("正在播放：" + mediaItem.name)
                .setContentIntent(pendingIntent)
                .build();
        mManager.notify(1, notification);
    }

    /**
     * 播暂停音乐
     */
    private void pause(){
        mediaPlayer.pause();
        mManager.cancel(1);
    }

    /**
     * 停止
     */
    private void stop(){
        mediaPlayer.stop();
    }

    /**
     * 得到当前的播放进度
     * @return
     */
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }


    /**
     * 得到当前音频的总时长
     * @return
     */
    private int getDuration(){
        return mediaPlayer.getDuration();
    }

    /**
     * 得到艺术家
     * @return
     */
    private String getArtist(){
        return mediaItem.artist;
    }

    /**
     * 得到歌曲名字
     * @return
     */
    private String getName(){
        return mediaItem.name;
    }


    /**
     * 得到歌曲播放的路径
     * @return
     */
    private String getAudioPath(){
        return mediaItem.data;
    }

    /**
     * 播放下一个音频
     */
    private void next(){
        position ++;
        if (position <= mediaItems.size()){
            openAudio(position);
        }else {
            openAudio(0);
        }
    }


    /**
     * 播放上一个视频
     */
    private void pre(){
        position--;
        if (position > 0) {
            openAudio(position);
        }
    }

    /**
     * 设置播放模式
     * @param playmode
     */
    private void setPlayMode(int playmode){
        this.playmode = playmode;
        CachesUtils.putInt(this, PLAY_MODE, playmode);
    }

    /**
     * 得到播放模式
     * @return
     */
    private int getPlayMode(){
        return playmode;
    }

    /**
     拖动音频
     */
    public void seekTo(int progress){
        mediaPlayer.seekTo(progress);
    };

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
//                PermissionUtils.isGrantExternalRW(AudioPlayerActivity.class);
                mediaItems = new ArrayList<MediaItem>();
                ContentResolver resolver = getContentResolver();

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
            }
        }.start();
    }

    /**
     * 是否正在播放
     */
    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mp) {
            // 通知 activity 来获取消息--广播
            notifyChanged(OPENAUDIO_RECEIVER);
            start();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 发送通知
     * @param action
     */
    private void notifyChanged(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }


}
