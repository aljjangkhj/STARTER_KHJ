package com.timer.hyunjae.starttimer.config;

import android.app.Activity;

import java.util.ArrayList;

/**
 * 프로젝트 설정값.
 */
public class Config
{
    static public String TAG = "";
    // PROJECT Default
    final static public boolean RELEASE = false; // 프로젝트 동작모드 true : 배포용, false : 개발용
//    final static public int SERVERMODE = 2; // 1 : 운영, 2 : 개발, 3 : 스테이징

    static public int DEVICE_SCREEN_HEIGHT = 2560; //
    static public int DEVICE_SCREEN_WIDTH = 1440; //


    final static public String FCM_TOPIC = "/topics/push_all";  // FCM topic
    static private String FCM_TOKEN = "";  // FCM

    public static ArrayList<Activity> actList = new ArrayList<Activity>();

    // 서버정보
    static public String SERVER_IP =  "";

    // 폴링 만료 시간
    final static public long POLLING_TIMEOUT =  500;

    static public boolean FUNC_TEST_MODE = false;
    static public int FUNC_TEST_POLLING_STATE = 1;

    public static void InitConfig()
    {
//        if(SERVERMODE == 1)
//        {
//            SERVER_IP = URL_DEF.REAL_SEVER;
//        }
//        else
//        {
//            SERVER_IP = URL_DEF.DEV_SERVER;
//        }
    }
}