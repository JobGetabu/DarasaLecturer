package com.job.darasalecturer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.job.darasalecturer.R;

import java.util.Map;

/**
 * Created by Job on Monday : 10/15/2018.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static int count = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "Notification Message TITLE: " + remoteMessage.getNotification().getTitle());
        Log.d(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message DATA: " + remoteMessage.getData().toString());

        String clickaction = remoteMessage.getNotification().getClickAction();

        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), remoteMessage.getData(),clickaction);
    }

    //This method is only generating push notification
    private void sendNotification(String messageTitle, String messageBody, Map<String, String> row, String clickaction ) {

        //click intent action
        Intent intent = new Intent(clickaction);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) (Math.random() * 100), intent, 0);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            currentapiVersion = R.mipmap.ic_launcher;
        } else{
            currentapiVersion = R.mipmap.ic_launcher;
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(currentapiVersion)
                .setContentTitle(messageTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }
}
