package com.example.coen357_final_sound;

import java.security.Provider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyForegroundService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.d("Tag", "foreground running");
                            try{
                                Thread.sleep(2000);
                            }
                            catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                        }

                    }
                }
        ).start();
        final String CHANNEL_ID = "Foreground Service";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);

            String foregrondMsg = "Foreground Service running";
            String title = "Sound detector message";
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                    .setContentText(foregrondMsg)
                    .setContentTitle(title);
            startForeground(1001,notification.build());

        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
