package com.timer.hyunjae.starttimer.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.timer.hyunjae.starttimer.common.log;
import com.timer.hyunjae.starttimer.config.AppPosPreferenceManager;

/**
 * SKO (2018. 04. 19)
 * FCM 관련해서 추가된 service 처음 App을 실행하게되면 호출되어 Token을 발급받게 된다.
 */

public class AppPosFirebaseInstanceIdService extends FirebaseInstanceIdService {

        @Override
        public void onTokenRefresh()
        {
                try
                {
                        String fcmToken = FirebaseInstanceId.getInstance().getToken();

                        AppPosPreferenceManager.setFcmToken(AppPosFirebaseInstanceIdService.this, fcmToken);
                        log.vlog(1, "AppPosFirebaseInstanceIdService onTokenRefresh fcmToken : " + fcmToken);
                }
                catch (Exception e)
                {
                        log.vlog(2, "AppPosFirebaseInstanceIdService onTokenRefresh Error : " + e.getMessage());
                }

        }
}
