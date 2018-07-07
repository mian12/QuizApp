package com.solution.alnahar.quizapp.pushNotification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.solution.alnahar.quizapp.common.Common;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //ctrl+o

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        handleNotification(remoteMessage.getNotification().getBody());
    }

    private void handleNotification(String body) {
        Intent pushNotification=new Intent(Common.PUSH_NOTIFICATION);
        pushNotification.putExtra("message",body);

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

    }
}
