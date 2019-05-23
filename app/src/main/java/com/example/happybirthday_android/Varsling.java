package com.example.happybirthday_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;
import java.util.Calendar;
import java.util.List;

public class Varsling extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DatabaseHandler db = new DatabaseHandler(this);

        Calendar calendar = Calendar.getInstance();
        String maned = Integer.toString(calendar.get(Calendar.MONTH));
        String dag = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        List<Contact> contacts = db.hentAlleSomHarBursdag(maned,dag);

        int antall = contacts.size();

        if(antall == 0){
            return super.onStartCommand(intent, flags, startId);
        }
        else {
            boolean faVarsling = MainActivity.varsling;
            boolean sendSMS = MainActivity.sendAlleSMS;

            if (faVarsling) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String utskriftTittel = "Bursdag";
                String utskriftSummary;

                if (antall == 1) {
                    Contact contact = contacts.get(0);
                    int alder = calendar.get(Calendar.YEAR) - contact.getAr();
                    utskriftSummary = contact.getNavn() + " blir " + alder + " år gammel";
                } else {
                    utskriftSummary = "hos " + antall + " av dine kontakter";
                }

                Notification notification = new Notification.Builder(this)
                        .setContentTitle(utskriftTittel)
                        .setContentText(utskriftSummary)
                        .setSmallIcon(R.drawable.bursdag_logo).build();

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, notification);
            }

            // sjekk hvis bruker vil bruke sms funksjonen generelt
            if (sendSMS){

                for (Contact k : contacts) {
                    String tlf = k.getTelefonnr();
                    String melding = MainActivity.smsMelding;

                    // sjekk om kontakt skal få sms
                    if(k.isSendSMS()) {
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(tlf, null, melding, null, null);
                            Toast.makeText(getApplicationContext(), "SMS sent til " + k.getNavn(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Feil i sending av SMS til " + k.getNavn(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
