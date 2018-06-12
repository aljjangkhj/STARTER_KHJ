package com.timer.hyunjae.starttimer.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;
import com.timer.hyunjae.starttimer.common.log;

/**
 * SKO (2018. 03. 21)
 * App이 종료된 상태에서도 data가 유지되어야 하는경우 사용.
 */

public class AppPosPreferenceManager
{
    private final static String PREF_NAME = "kr.co.seeroo.apppos";

    private static final String FCM_TOKEN = "fcm_token";    // fcm token을 저장하는 key 값.
    private static final String APP_KEY_ID = "app_key_id";    // appkeyid 저장하는 key 값.
    private static final String PUB_KEY_MOD = "pub_key_mod";        // pubkey를 저장하는 key 값.
    private static final String POS_SEQ = "pos_seq";     // posSeq를 저장하는 key 값.
    private static final String CHECK_BOX_STATE = "check_box_state";    // login 화면 check box on/off 값을 저장하는 key 값.
    private static final String SAVE_ID = "save_id";    // id를 저장하는 key 값.
    private static final String AUTO_LOGIN = "auto_login";
    private static final String FIXED_CODE = "fixed_code";
    private static final String PAGE_CODE = "page_code";

//    public void synchronized

    // fcm push token read/write.
    public static synchronized String getFcmToken(Context context)
    {
        String result = "";

        try
        {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            result = pref.getString(FCM_TOKEN, "");

            // 토큰이 없는 경우 다시 받아온다.
            if(result.equals(""))
            {
                try
                {
                    String fcmToken = FirebaseInstanceId.getInstance().getToken();

                    AppPosPreferenceManager.setFcmToken(context, fcmToken);
                    log.vlog(1, "AppPosPreferenceManager getFcmToken fcmToken : " + fcmToken);
                }
                catch (Exception e)
                {
                    log.vlog(2, "AppPosPreferenceManager getFcmToken Error : " + e.getMessage());
                }
            }
        }
        catch (Exception e)
        {
            log.vlog(2, "Error getFcmToken : " + e);
        }

        return result;
    }
    public static synchronized void setFcmToken(Context context, String fcmToken)
    {
        try
        {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME,Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString(FCM_TOKEN, fcmToken);
            editor.commit();
        }
        catch (Exception e)
        {
            log.vlog(2, "Error setFcmToken : " + e);
        }
    }

    public static synchronized String getAppKeyId(Context context)
    {
        String result = "";
        try
        {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            result = pref.getString(APP_KEY_ID, "");
        }catch (Exception e)
            {
                log.vlog(2, "Error getAppKeyId : " + e);
            }
        return result;
    }

    public static synchronized void setAppKeyId(Context context, String appkeyid)
    {
        try
        {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString(APP_KEY_ID, appkeyid);
            editor.commit();
        }catch (Exception e)
        {
            log.vlog(2, "Error setAppKeyId : " + e);
        }
    }

    public static synchronized String getPubKeyMod(Context context)
    {
        String result = "";

        try
        {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            result = pref.getString(PUB_KEY_MOD, "");
        }catch (Exception e)
        {
            log.vlog(2, "Error getPubKeyMod : " + e);
        }
        return result;
    }

    public static synchronized void setPubKey(Context context, String pubKey)
    {
        try
        {
            SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString(PUB_KEY_MOD, pubKey);
            editor.commit();
        }catch (Exception e)
        {
            log.vlog(2, "Error setPubKey : " + e);
        }

    }

    public static synchronized int getPosSeq(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getInt(POS_SEQ, 0);
    }

    public static synchronized void setPosSeq(Context context, int posSeq)
    {
        try
        {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(POS_SEQ, posSeq);
        editor.commit();
        }catch (Exception e)
        {
            log.vlog(2, "Error setPubKey : " + e);
        }
    }

    public static synchronized String getId(Context context)
    {
        String result = "";

        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        result = pref.getString(SAVE_ID, "");

        return result;
    }

    public static synchronized void setId(Context context, String id)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(SAVE_ID, id);
        editor.commit();
    }

    public static synchronized boolean getCheckBoxState(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getBoolean(CHECK_BOX_STATE, false);
    }

    public static synchronized void setCheckBoxState(Context context, boolean checkBoxState)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(CHECK_BOX_STATE, checkBoxState);
        editor.commit();
    }

    public static synchronized boolean getAutoLogin(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getBoolean(AUTO_LOGIN, false);
    }

    public static synchronized void setAutoLogin(Context context, boolean autoLogin)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(AUTO_LOGIN, autoLogin);
        editor.commit();
    }

    public static synchronized long getFixedCode(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getLong(FIXED_CODE, 0);
    }

    public static synchronized void setFixedCode(Context context, long fixedCode)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(FIXED_CODE, fixedCode);
        editor.commit();
    }

    public static synchronized int getMainPage(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getInt(PAGE_CODE, 0);
    }

    public static synchronized void setMainPage(Context context, int pageCode)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(PAGE_CODE, pageCode);
        editor.commit();
    }

    public static synchronized long getPollingTimeOut(Context context)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        return pref.getLong(PAGE_CODE, 0);
    }

    public static synchronized void setPollingTimeOut(Context context, long pageCode)
    {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(PAGE_CODE, pageCode);
        editor.commit();
    }
}
