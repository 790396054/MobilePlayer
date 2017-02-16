package com.app.gmm.mobileplyer.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.app.gmm.mobileplyer.R;

/**
 * 闪屏页面
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 2秒后跳到主页面
                // 执行在主线程中
                startMainActivity();
                Log.i(TAG, "线程名称="+Thread.currentThread().getName());
            }
        }, 4000);
    }

    /**
     * 打开主页面
     */
    private void startMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        // 关闭当前页面
        finish();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        startMainActivity();
        Log.i(TAG, "onTouchEvent=="+event.getAction());
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
