package com.app.gmm.mobileplyer.domain;

/**
 * Created by gmm on 2017/3/14.
 * 视频实体类
 */

public class MediaItem {

    /**
     * 名称
     */
    public String name;
    /**
     * 时长
     */
    public long duration;

    /**
     * 大小
     */
    public long size;
    /**
     * 艺术家
     */
    public String artist;
    /**
     * 播放地址
     */
    public String data;

    /**
     * 视频描述
     */
    public String desc;


    public String imageUrl;

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", artist='" + artist + '\'' +
                ", data='" + data + '\'' +
                ", desc='" + desc + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}

