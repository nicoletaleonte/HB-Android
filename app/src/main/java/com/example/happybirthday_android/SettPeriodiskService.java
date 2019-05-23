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
        int minutt;

        Log.d("PERIODISKSERVICE", "Tidspunkt: " + MainActivity.tidspunkt);
        if(MainActivity.tidspunkt == null) {
            // sette default tidspunkt 08:00
            time = 8;
            minutt = 0;
        }
        else
        {
            String tidspunkt = MainActivity.tidspunkt;
            String timeStreng = "";
            String minuttStreng = "";

            // konvertere klokkeslett fra string til hh:mm
            for(int i = 0; i < tidspunkt.length(); i++){
                if(tidspunkt.charAt(i) != ':')
                    timeStreng += tidspunkt.charAt(i);
                else
                    break;
            }

            for(int i = tidspunkt.length()-1; i >= 0; i--){
                if(tidspunkt.charAt(i) != ':')
                    minuttStreng += tidspunkt.charAt(i);
                else
                    break;
            }

            time = Integer.parseInt(timeStreng);
            minutt = Integer.parseInt(minuttStreng);
        }

        cal.set(Calendar.HOUR_OF_DAY, time);
        cal.set(Calendar.MINUTE, minutt);
        cal.set(Calendar.SECOND, 0);
        cal = Calendar.getInstance();
        Intent i = new Intent(this, Varsling.class);
        PendingIntent pintent = PendingIntent.getService(this,0,i,0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pintent);

        return super.onStartCommand(intent, flags, startId);
    }
}

