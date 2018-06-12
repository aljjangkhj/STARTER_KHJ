package com.timer.hyunjae.starttimer.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * SKO (2018. 04. 19)
 * FCM 관련해서 추가된 Service Push message가 오게되면 onMessageReceived가 호출 됨.
 */

public class AppPosFirebaseMessagingService extends FirebaseMessagingService {

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> pushDataMap = remoteMessage.getData();
        sendNotification(pushDataMap);
    }

    private void sendNotification(Map<String, String> dataMap) {

    }
}
