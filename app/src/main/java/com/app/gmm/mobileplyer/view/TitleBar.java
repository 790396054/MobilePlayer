package com.app.gmm.mobileplyer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.gmm.mobileplyer.R;

/**
 * Created by gmm on 2017/3/14.
 * 自定义 titleBar
 */

public class TitleBar extends LinearLayout implements View.OnClickListener{

    private View tv_search; // 搜索框

    private View rl_game; // 游戏

    private View iv_record; // 历史记录

    private Context mContext;

    /**
     * 在代码中实例化该类的时候，调用此方法
     * @param context
     */
    public TitleBar(Context context) {
        this(context, null);
    }

    /**
     * 当布局文件中使用该类的时候，Android 系统通过这个构造方法实例化该类
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当需要设置样式的时候，可以使用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    /**
     * 当布局文件加载完成的时候，回调该方法
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 实例化控件
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        iv_record = getChildAt(3);

        // 设置点击事件
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Toast.makeText(mContext, "搜索框", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(mContext, "游戏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(mContext, "历史", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
