package com.app.gmm.mobileplyer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.app.gmm.mobileplyer.IPlayMusicService;

/**
 * Created by gmm on 2017/3/31.
 */

public class MusicPlayService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mStub;
    }

    private IPlayMusicService.Stub mStub = new IPlayMusicService.Stub() {
        @Override
        public void openAudio(int position) throws RemoteException {

        }

        @Override
        public void start() throws RemoteException {

        }

        @Override
        public void pause() throws RemoteException {

        }

        @Override
        public void stop() throws RemoteException {

        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return 0;
        }

        @Override
        public int getDuration() throws RemoteException {
            return 0;
        }

        @Override
        public String getArtist() throws RemoteException {
            return null;
        }

        @Override
        public String getName() throws RemoteException {
            return null;
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return null;
        }

        @Override
        public void next() throws RemoteException {

        }

        @Override
        public void pre() throws RemoteException {

        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {

        }

        @Override
        public int getPlayMode() throws RemoteException {
            return 0;
        }
    }

    /**
     * 根据位置打开对应的音频文件
     * @param position
     */
    private void openAudio(int position){

    }

    /**
     * 播放音乐
     */
    private void start(){

    }

    /**
     * 播暂停音乐
     */
    private void pause(){

    }

    /**
     * 停止
     */
    private void stop(){

    }

    /**
     * 得到当前的播放进度
     * @return
     */
    private int getCurrentPosition(){
        return 0;
    }


    /**
     * 得到当前音频的总时长
     * @return
     */
    private int getDuration(){
        return 0;
    }

    /**
     * 得到艺术家
     * @return
     */
    private String getArtist(){
        return "";
    }

    /**
     * 得到歌曲名字
     * @return
     */
    private String getName(){
        return "";
    }


    /**
     * 得到歌曲播放的路径
     * @return
     */
    private String getAudioPath(){
        return "";
    }

    /**
     * 播放下一个视频
     */
    private void next(){

    }


    /**
     * 播放上一个视频
     */
    private void pre(){

    }

    /**
     * 设置播放模式
     * @param playmode
     */
    private void setPlayMode(int playmode){

    }

    /**
     * 得到播放模式
     * @return
     */
    private int getPlayMode(){
        return 0;
    }
}
