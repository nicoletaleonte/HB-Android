package com.example.happybirthday_android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;

public class SetPeriodicService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //seteaza timplul la care sa se trimita mesaj
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar calendar = Calendar.getInstance();
        int time;
        int minute;

        Log.d("PERIODICSERVICE", "Timp: " + MainActivity.time);
        if (MainActivity.time == null) {
            time = 12;
            minute = 0;
        } else {
            String timp = MainActivity.time;
            String timps = "";
            String minutes = "";

            for (int i = 0; i < timp.length(); i++) {
                if (timp.charAt(i) != ':')
                    timps += timp.charAt(i);
                else
                    break;
            }

            for (int i = timp.length() - 1; i >= 0; i--) {
                if (timp.charAt(i) != ':')
                    minutes += timp.charAt(i);
                else
                    break;
            }

            time = Integer.parseInt(timps);
            minute = Integer.parseInt(minutes);
        }

        calendar.set(Calendar.HOUR_OF_DAY, time);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar = Calendar.getInstance();
        Intent i = new Intent(this, LogicNotificationAndSendSMS.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);

        return super.onStartCommand(intent, flags, startId);
    }
}

