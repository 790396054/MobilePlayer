package com.app.gmm.mobileplyer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import com.app.gmm.mobileplyer.domain.Lyric;

import org.xutils.common.util.DensityUtil;

import java.util.ArrayList;

/**
 * Created by gmm on 2017/4/3.
 * 自定义 view
 */

public class ShowLyricView extends AppCompatTextView {

    private ArrayList<Lyric> mLyrics;

    private int width;
    private int height;
    private Paint paint;

    public ShowLyricView(Context context) {
        this(context, null);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        //创建画笔
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(DensityUtil.dip2px(16));
        paint.setAntiAlias(true);
        //设置居中对齐
        paint.setTextAlign(Paint.Align.CENTER);
        mLyrics = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Lyric lyric = new Lyric();
            lyric.setContent("aaaaaaaaaa"+i);
            lyric.setSleepTime(10000 * i);
            lyric.setTimePoint(1000);
            mLyrics.add(lyric);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLyrics == null || mLyrics.size() == 0) {
            canvas.drawText("没有歌词", width / 2, height / 2, paint);
            return;
        }
    }
}
