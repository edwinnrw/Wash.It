package com.project.edn.washit.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.project.edn.washit.FirebaseApplication;
import com.project.edn.washit.R;
import com.project.edn.washit.activity.MainActivity;


import org.greenrobot.eventbus.EventBus;

import java.util.Map;


/**
 * Created by EDN on 5/6/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            if (!FirebaseApplication.getInstance().IsMainActivityOpen()){
                messageHandler(remoteMessage);

            }else {
                parseAndSendMessage(remoteMessage.getData());
            }
        }

    }

    private void parseAndSendMessage(Map<String, String> mapResponse) {
        int code = Integer.parseInt(mapResponse.get("response"));
        Log.e("PUBLISH", mapResponse.toString());
        EventBus.getDefault().post(mapResponse.get("notif"));

    }



    private void messageHandler(RemoteMessage remoteMessage){
        Log.e("FCM DATA", remoteMessage.getData().toString());
        notificationBuilder(remoteMessage.getData().get("notif"));

    }


    private void orderHandler(RemoteMessage remoteMessage){
        String code = remoteMessage.getData().get("status");
        Bundle data = new Bundle();
        data.putString("code", code);

    }

    private void notificationBuilder(String nama){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("WashIT Notifikasi")
                .setContentText(nama)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }


}
