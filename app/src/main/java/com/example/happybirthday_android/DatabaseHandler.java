package com.example.happybirthday_android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{


    private static final String TABELNAME = "Contacts";
    private static final String KEY_ID = "_ID";       // INT PK
    private static final String KEY_NAME = "Name";    // TEXT
    private static final String KEY_PHONENO = "PhoneNo";      // TEXT
    private static final String KEY_DAY = "Day";      // INT
    private static final String KEY_MONTH = "Month";  // INT
    private static final String KEY_YEAR = "Year";        // INT
    private static final String KEY_SMS = "Sms";      // INT

    static int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "DB_Contacts";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String teamTable = "CREATE TABLE " + TABELNAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " + KEY_PHONENO + " TEXT, " + KEY_DAY + " INTEGER, " +
                KEY_MONTH + " INTEGER, " + KEY_YEAR + " INTEGER, " + KEY_SMS + " INTEGER" + ")";
        db.execSQL(teamTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABELNAME);
        }
        catch (Exception e){
            Log.d("DATABASE","onUpgrade fail "+ e.toString());
        }
    }


    public boolean addContact(Contact contact){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(KEY_NAME, contact.getName());
            values.put(KEY_PHONENO, contact.getPhoneNo());
            values.put(KEY_DAY, contact.getDay());
            values.put(KEY_MONTH, contact.getMonth());
            values.put(KEY_YEAR, contact.getYear());
            values.put(KEY_SMS, contact.isSendSMS() ? 1 : 0);

            db.insert(TABELNAME, null, values);

            db.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<Contact> findAllContacts(){
        List<Contact> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABELNAME;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.set_ID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNo(cursor.getString(2));
                    contact.setDay(Integer.parseInt(cursor.getString(3)));
                    contact.setMonth(Integer.parseInt(cursor.getString(4)));
                    contact.setYear(Integer.parseInt(cursor.getString(5)));
                    contact.setSendSMS(Integer.parseInt(cursor.getString(6)) == 0 ? false : true);
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return contacts;
        }
        catch (Exception e){
            Log.d("DATABASE","Fail "+ e.toString());
            return null;
        }
    }


    public int updateContact(Contact contact){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getName());
            values.put(KEY_PHONENO, contact.getPhoneNo());
            values.put(KEY_DAY, contact.getDay());
            values.put(KEY_MONTH, contact.getMonth());
            values.put(KEY_YEAR, contact.getYear());
            if (contact.isSendSMS())
                values.put(KEY_SMS, 1);
            else
                values.put(KEY_SMS, 0);

            int endret = db.update(TABELNAME, values, KEY_ID + "=?", new String[]{String.valueOf(contact.get_ID())});
            db.close();
            return endret;
        }
        catch (Exception e){
            Log.d("DATABASE","Feil "+ e.toString());
            return -1;
        }
    }


    public void deleteContact(Contact contact){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABELNAME, KEY_ID + "=?", new String[]{String.valueOf(contact.get_ID())});
            db.close();
        }
        catch (Exception e){
            Log.d("DATABASE","Fail "+ e.toString());
        }
    }


    public Contact findContactById(int id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.query(TABELNAME, new String[]{KEY_ID, KEY_NAME, KEY_PHONENO, KEY_DAY, KEY_MONTH, KEY_YEAR, KEY_SMS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)) == 0 ? false : true);
            cursor.close();
            db.close();
            return contact;
        }
        catch (Exception e){
            Log.d("DATABASE","Fail "+ e.toString());
            return null;
        }
    }

    public List<Contact> getAllBirthdays(String maned, String dag){
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM " + TABELNAME + " WHERE " + KEY_MONTH + " = " + maned + " AND " + KEY_DAY + " = " + dag;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.set_ID(Integer.parseInt(cursor.getString(0)));
                    contact.setName(cursor.getString(1));
                    contact.setPhoneNo(cursor.getString(2));
                    contact.setDay(Integer.parseInt(cursor.getString(3)));
                    contact.setMonth(Integer.parseInt(cursor.getString(4)));
                    contact.setYear(Integer.parseInt(cursor.getString(5)));
                    contact.setSendSMS(Integer.parseInt(cursor.getString(6)) == 0 ? false : true);
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return contacts;
        }
        catch (Exception e){
            Log.d("DATABASE","Fail "+ e.getMessage());
            return null;
        }
    }
}
