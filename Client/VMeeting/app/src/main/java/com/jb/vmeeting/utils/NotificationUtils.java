package com.jb.vmeeting.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public final class NotificationUtils {

    public static void showSimpleNotification(Context context, int id, int icon, String title, String text) {
        showSimpleNotification(context, id, icon, title, text, (PendingIntent) null);
    }

    public static void showSimpleNotification(Context context, int id, int icon, String title, String text, Class<?> jump2Clazz) {
        Intent intent = new Intent();
        if (jump2Clazz != null) {
            intent = new Intent(context, jump2Clazz);
        }
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        showSimpleNotification(context, id, icon, title, text, pi);
    }
    
    public static void showSimpleNotification(Context context, int id, int icon, String title, String text, PendingIntent pi) {
        NotificationManager mNotifyMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setAutoCancel(true)
        .setWhen(System.currentTimeMillis())
        .setDefaults(NotificationCompat.DEFAULT_ALL)
        .setOnlyAlertOnce(true)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText(text)
        .setTicker(title);

        if(pi!=null){
        	mBuilder.setContentIntent(pi);
        }
        Notification mNotification = mBuilder.build();
        mNotifyMan.notify(id, mNotification);
    }

    public static void cancelNotification(Context context, int id) {
        NotificationManager mNotifyMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMan.cancel(id);
    }
}
