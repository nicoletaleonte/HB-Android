package com.example.happybirthday_android;


public class Contact {
    private int _ID;
    private String Navn;
    private String Telefonnr;
    private int dag;
    private int maned;
    private int ar;
    private boolean SendSMS;

    // konstruktors
    public Contact(){}

    public Contact(String n, String t, int d, int m, int a, boolean sms){
        Navn = n;
        Telefonnr = t;
        dag = d;
        maned = m;
        ar = a;
        SendSMS = sms;
    }

    public Contact(int id, String n, String t, int d, int m, int a, boolean sms){
        _ID = id;
        Navn = n;
        Telefonnr = t;
        dag = d;
        maned = m;
        ar = a;
        SendSMS = sms;
    }


    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public int getAr() {
        return ar;
    }

    public void setAr(int ar) {
        this.ar = ar;
    }

    public int getDag() {
        return dag;
    }

    public void setDag(int dag) {
        this.dag = dag;
    }

    public int getManed() {
        return maned;
    }

    public void setManed(int maned) {
        this.maned = maned;
    }

    public String getNavn() {
        return Navn;
    }

    public void setNavn(String navn) {
        Navn = navn;
    }

    public boolean isSendSMS() {
        return SendSMS;
    }

    public void setSendSMS(boolean sendSMS) {
        SendSMS = sendSMS;
    }

    public String getTelefonnr() {
        return Telefonnr;
    }

    public void setTelefonnr(String telefonnr) {
        Telefonnr = telefonnr;
    }
}
