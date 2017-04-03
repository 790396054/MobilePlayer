package com.app.gmm.mobileplyer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gmm on 2017/4/2.
 */

public class BaseVisualizerView extends View {
    public BaseVisualizerView(Context context) {
        super(context);
    }

    public BaseVisualizerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseVisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BaseVisualizerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
