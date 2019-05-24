package com.example.happybirthday_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.Calendar;

public class SettPeriodiskService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar cal = Calendar.getInstance();
        int time;
        int minute;

        Log.d("PERIODISKSERVICE", "Timp: " + MainActivity.time);
        if(MainActivity.time == null) {
            // sette default time 08:00
            time = 8;
            minute = 0;
        }
        else
        {
            String timp = MainActivity.time;
            String timps = "";
            String minutes = "";

            // konvertere klokkeslett fra string til hh:mm
            for(int i = 0; i < timp.length(); i++){
                if(timp.charAt(i) != ':')
                    timps += timp.charAt(i);
                else
                    break;
            }

            for(int i = timp.length()-1; i >= 0; i--){
                if(timp.charAt(i) != ':')
                    minutes += timp.charAt(i);
                else
                    break;
            }

            time = Integer.parseInt(timps);
            minute = Integer.parseInt(minutes);
        }

        cal.set(Calendar.HOUR_OF_DAY, time);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, 0);
        cal = Calendar.getInstance();
        Intent i = new Intent(this, Warning.class);
        PendingIntent pintent = PendingIntent.getService(this,0,i,0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);

        return super.onStartCommand(intent, flags, startId);
    }
}

