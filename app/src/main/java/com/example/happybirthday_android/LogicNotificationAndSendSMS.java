package com.example.happybirthday_android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


import android.app.Notification;
import android.app.NotificationManager;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;
import java.util.Calendar;
import java.util.List;

public class LogicNotificationAndSendSMS extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DatabaseHandler db = new DatabaseHandler(this);

        Calendar calendar = Calendar.getInstance();
        String month = Integer.toString(calendar.get(Calendar.MONTH));
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

        List<Contact> contacts = db.getAllBirthdays(month,day);

        int all = contacts.size();

        if(all == 0){
            return super.onStartCommand(intent, flags, startId);
        }
        else {
            boolean isNotification = MainActivity.warning;
            boolean sendSMS = MainActivity.sendAllSMS;

            if (isNotification) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String showTitle = "Birthday";
                String showSummary;

                if (all == 1) {
                    Contact contact = contacts.get(0);
                    int age = calendar.get(Calendar.YEAR) - contact.getYear();
                    showSummary = contact.getName() + " împlineste " + age + " de ani.";
                } else {
                    showSummary = "La " + all + " multi ani tuturor...";
                }

                Notification notification = new Notification.Builder(this)
                        .setContentTitle(showTitle)
                        .setContentText(showSummary)
                        .setSmallIcon(R.drawable.birthday_logo).build();

                notification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, notification);
            }

            if (sendSMS){
                for (Contact k : contacts) {
                    String phoneNo = k.getPhoneNo();
                    String sms = MainActivity.SMS;

                    if(k.isSendSMS()) {
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, sms, null, null);
                            Toast.makeText(getApplicationContext(), "SMS trimis la " + k.getName(), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "SMS-ul nu s-a trimis la " + k.getName(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
