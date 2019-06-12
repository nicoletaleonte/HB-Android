package com.example.happybirthday_android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Receiver extends BroadcastReceiver {

    //folosit pentru sms-uri
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, SetPeriodicService.class);
        context.startService(i);
    }

}
