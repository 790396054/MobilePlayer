package com.app.gmm.mobileplyer.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.gmm.mobileplyer.R;
import com.app.gmm.mobileplyer.domain.MediaItem;
import com.app.gmm.mobileplyer.utils.Utils;

import java.util.List;

/**
 * Created by gmm on 2017/3/14.
 * 视频列表的适配器
 */

public class VideoPagerAdapter extends BaseAdapter {

    private List<MediaItem> mMediaItems;

    private Utils mUtils;

    private Context mContext;

    public VideoPagerAdapter(Context context, List<MediaItem> mediaItems){
        this.mContext = context;
        this.mMediaItems = mediaItems;
        mUtils = new Utils();
    }

    @Override
    public int getCount() {
        return mMediaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mMediaItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_video_pager, null);
            holder = new ViewHolder();
            holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_size = (TextView) convertView.findViewById(R.id.tv_size);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //根据position得到列表中对应位置的数据
        MediaItem mediaItem = mMediaItems.get(position);
        holder.tv_name.setText(mediaItem.name);
        holder.tv_size.setText(Formatter.formatFileSize(mContext, mediaItem.size));
        holder.tv_time.setText(mUtils.stringForTime((int) mediaItem.duration));
        return convertView;
    }

    class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
