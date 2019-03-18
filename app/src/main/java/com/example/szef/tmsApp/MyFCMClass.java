package com.example.szef.tmsApp;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFCMClass extends FirebaseMessagingService {
    public static final String CHANNEL_ID = "TmsApp";
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        notificationManagerCompat = NotificationManagerCompat.from(this);
        System.out.println("started");

        if(remoteMessage.getNotification() != null) {
            notification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());

        }
        if (remoteMessage.getData().size() > 0) {
            System.out.println("Data: " + remoteMessage.getData().entrySet());

        }
    }

    public void notification(String title, String text) {
        Notification notifications = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_email_black_24dp)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .build();
        notificationManagerCompat.notify(1, notifications);
    }


}
