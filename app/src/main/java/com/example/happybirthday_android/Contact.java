package com.example.happybirthday_android;

public class Contact {
    private int _ID;
    private String name;
    private String phoneNo;
    private int day;
    private int month;
    private int year;
    private boolean SendSMS;

    @Override
    public String toString() {
        return "Contact{" +
                "name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                '}';
    }

    public Contact() {
    }

    public Contact(String n, String p, int d, int m, int y, boolean sms) {
        name = n;
        phoneNo = p;
        day = d;
        month = m;
        year = y;
        SendSMS = sms;
    }

    public Contact(int id, String n, String p, int d, int m, int y, boolean sms) {
        _ID = id;
        name = n;
        phoneNo = p;
        day = d;
        month = m;
        year = y;
        SendSMS = sms;
    }


    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSendSMS() {
        return SendSMS;
    }

    public void setSendSMS(boolean sendSMS) {
        SendSMS = sendSMS;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
