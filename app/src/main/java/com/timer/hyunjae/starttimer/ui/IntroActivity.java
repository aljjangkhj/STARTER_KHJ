package com.timer.hyunjae.starttimer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;
import com.timer.hyunjae.starttimer.R;

public class IntroActivity extends BaseActivity {

    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        init_autoscreen(1);

        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                }

                // Run next activity
                Intent intent = new Intent(mContext, MainActivity.class);
                MoveToActivity(intent);
                finish();
            }
        };
        thread.start();
    }
    @Override public void onBackPressed() {

    }
}
