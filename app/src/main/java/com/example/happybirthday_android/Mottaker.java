package com.example.happybirthday_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Mottaker extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SettPeriodiskService.class);
        context.startService(i);
    }
}
