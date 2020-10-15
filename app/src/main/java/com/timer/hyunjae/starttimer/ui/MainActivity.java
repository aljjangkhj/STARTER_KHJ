package com.timer.hyunjae.starttimer.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.timer.hyunjae.starttimer.R;
import com.timer.hyunjae.starttimer.common.log;
import com.timer.hyunjae.starttimer.config.Config;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity {

    private Button play_Btn, pause_Btn, stop_Btn, scubtn;
    private long startTime, timeMS, timeSB, update;
    private TextView tv;
    private long backKeyPressedTime = 0;
    int min, s, ms;
    Handler h = new Handler();
    private LottieAnimationView lottieAnimationView;
    private ImageView ready_Img,gun_Img;
    private Thread thread;
    private Timer TimeOut_timer = null;
    private final android.os.Handler handler = new android.os.Handler();
    private boolean pauseTF = false;
    private boolean mainCheck = false;
    private boolean playing = false;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private SoundPool sp;
    private int i = 0;
    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            listViewAdapter.notifyDataSetChanged();
        }
    };
    private BillingManager billingManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_autoscreen(1);

        play_Btn = (Button)findViewById(R.id.play_btn);
        pause_Btn = (Button)findViewById(R.id.pause_btn);
        stop_Btn = (Button)findViewById(R.id.stop_btn);
        scubtn = (Button)findViewById(R.id.scubtn);
        tv = (TextView)findViewById(R.id.time_tv);
        lottieAnimationView = (LottieAnimationView)findViewById(R.id.watch_loti);
        ready_Img = (ImageView)findViewById(R.id.ready_iv);
        gun_Img = (ImageView)findViewById(R.id.gun_iv);
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.linear_frame);
        pauseTF = false;

        listViewAdapter = new ListViewAdapter();

        listView = (ListView)findViewById(R.id.listview_timelist);
        listView.setAdapter(listViewAdapter);

        billingManager = new BillingManager(this).init(new BillingManager.BillingCallback() {
            @Override
            public void onPurchased(String productId) {
                if (productId.equals("Config.SUBSCRIBE_SKU")) {
                    Toast.makeText(mContext, "구독 해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "구매 해 주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUpdatePrice(Pair<Double, Double> prices) {
                try {
                    Toast.makeText(mContext, "1"+prices.first.intValue(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(mContext, "2"+prices.second.intValue(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sp = new SoundPool(1, AudioManager.STREAM_ALARM, 0);
        play_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pauseTF) {
                    if (!playing) {//플레이중에 중복 클릭 안됨
                        mainCheck = true;
                        ready_Img.setVisibility(View.VISIBLE);
                        final int random = (int) (Math.random() * 5000) + 1000;
                        log.vlog(2, "random: " + random);

                        thread = new Thread() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Thread.sleep(random);

                                        } catch (Exception ex) {
                                            log.vlog(2, "Exception: " + ex);
                                        }
                                        ready_Img.setVisibility(View.INVISIBLE);
                                        gun_Img.setVisibility(View.VISIBLE);
                                        linearLayout.setVisibility(View.GONE);
                                        lottieAnimationView.setVisibility(View.VISIBLE);
                                        final int soundid = sp.load(mContext, R.raw.gun_sound, 1);
                                        sp.play(soundid, 1.0F, 1.0F, 0, -1, 1.0F);
                                        startTime = SystemClock.uptimeMillis();
                                        h.postDelayed(r, 0);

                                        TimeOut_StartTime = System.currentTimeMillis();
                                        TimerTask mm = new TimerTask() {
                                            @Override
                                            public void run() {
                                                Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        TimeOut_CurrentTime = System.currentTimeMillis();
                                                        long left_time = Config.POLLING_TIMEOUT - (TimeOut_CurrentTime - TimeOut_StartTime);

                                                        //타임아웃 체크
                                                        if (left_time <= 0) {
                                                            gun_Img.setVisibility(View.INVISIBLE);
                                                            sp.stop(soundid);
                                                            return;
                                                        }
                                                    }
                                                };
                                                handler.post(runnable);
                                            }
                                        };
                                        TimeOut_timer = new Timer();
                                        TimeOut_timer.schedule(mm, 0, 100);
                                    }
                                });
                            }
                        };
                        thread.start();
                        playing = true;
                    }
                }else {
                    mainCheck = true;
                    startTime = SystemClock.uptimeMillis();
                    h.postDelayed(r, 0);
                    playing = true;
                    linearLayout.setVisibility(View.GONE);
                }

            }
        });

        pause_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                timeSB += timeMS;
//                h.removeCallbacks(r);
                if(mainCheck) { // 00초일때 클릭막기
                    i = i += 1;
                    listViewAdapter.addItem(String.valueOf(i), tv.getText().toString());
                    runOnUiThread(updateUI);
                    listView.setSelection(listViewAdapter.getCount()-1);
                }
            }
        });

        stop_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                if (isAfter2Seconds()) {
                    backKeyPressedTime = System.currentTimeMillis();
                    timeSB += timeMS;
                    h.removeCallbacks(r);
                    linearLayout.setVisibility(View.VISIBLE);
                    mainCheck = false; //00초일때 일시정지 클릭안됨
                    pauseTF = true;
                    playing = false; //플레잉 초기화
                    return;
                }

                if (isBefore2Seconds()) {
                    startTime = 0;
                    timeMS = 0;
                    timeSB = 0;
                    s = 0;
                    min = 0;
                    ms = 0;
                    i = 0;
                    h.removeCallbacks(r);
                    tv.setText("0:00:000");
                    listViewAdapter.clearListview();
                    linearLayout.setVisibility(View.VISIBLE);
                    runOnUiThread(updateUI);

                    mainCheck = false; //00초일때 일시정지 클릭안됨
                    playing = false; //플레잉 초기화
                    pauseTF = false; //일시정지 버튼 초기화
                }
            }
        });

        scubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billingManager.subscribe();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(mainCheck) { // 00초일때 클릭막기
                    i = i += 1;
                    listViewAdapter.addItem(String.valueOf(i), tv.getText().toString());
                    runOnUiThread(updateUI);
                    listView.setSelection(listViewAdapter.getCount()-1);
                    pauseTF = true;
                }
        }
        super.onKeyDown(keyCode, event);
        return true;
    }
    private Boolean isAfter2Seconds() {
        return System.currentTimeMillis() > backKeyPressedTime + 2000;
        // 2초 지났을 경우
    }

    private Boolean isBefore2Seconds() {
        return System.currentTimeMillis() <= backKeyPressedTime + 2000;
        // 2초가 지나지 않았을 경우
    }

    public Runnable r = new Runnable() {
        @Override
        public void run() {
            timeMS = SystemClock.uptimeMillis() - startTime;
            update = timeSB + timeMS;
            s = (int) (update / 1000);
            min = s / 60;
            s = s % 60;
            ms = (int) (update % 1000);
            tv.setText("" + min + ":" + String.format("%02d", s) + ":" + String.format("%03d", ms));
            h.postDelayed(this, 0);
        }
    };

    @Override
    public void onResume() {
        billingManager.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        billingManager.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingManager.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
