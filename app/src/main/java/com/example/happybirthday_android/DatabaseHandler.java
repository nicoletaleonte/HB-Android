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

    // headers
    private static final String TABELLNAVN = "Kontakter";
    private static final String KEY_ID = "_ID";       // INT PK
    private static final String KEY_NAME = "Navn";    // TEXT
    private static final String KEY_TLF = "Tlf";      // TEXT
    private static final String KEY_DAG = "Dag";      // INT
    private static final String KEY_MANED = "Maned";  // INT
    private static final String KEY_AR = "Ar";        // INT
    private static final String KEY_SMS = "Sms";      // INT

    static int DATABASE_VERSION = 1;
    static String DATABASE_NAVN = "DB_Kontakter";

    // konstruktors
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAVN, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String lagTabell = "CREATE TABLE " + TABELLNAVN + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_NAME + " TEXT, " + KEY_TLF + " TEXT, " + KEY_DAG + " INTEGER, " +
                KEY_MANED + " INTEGER, " +  KEY_AR + " INTEGER, " + KEY_SMS + " INTEGER" + ")";
        db.execSQL(lagTabell);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try{
            db.execSQL("DROP TABLE IF EXISTS " + TABELLNAVN);
        }
        catch (Exception e){
            Log.d("DATABASE","onUpgrade feil "+ e.toString());
        }
    }

    // legg til kontakt
    public boolean leggTilKontakt(Contact contact){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();     // nøkkel-verdi par lagres her

            values.put(KEY_NAME, contact.getNavn());
            values.put(KEY_TLF, contact.getTelefonnr());
            values.put(KEY_DAG, contact.getDag());
            values.put(KEY_MANED, contact.getManed());
            values.put(KEY_AR, contact.getAr());
            values.put(KEY_SMS, contact.isSendSMS() ? 1 : 0);

            db.insert(TABELLNAVN, null, values);

            db.close();
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    // finn alle kontakter
    public List<Contact> finnAlleKontakter(){
        List<Contact> kontaktList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABELLNAVN;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.set_ID(Integer.parseInt(cursor.getString(0)));
                    contact.setNavn(cursor.getString(1));
                    contact.setTelefonnr(cursor.getString(2));
                    contact.setDag(Integer.parseInt(cursor.getString(3)));
                    contact.setManed(Integer.parseInt(cursor.getString(4)));
                    contact.setAr(Integer.parseInt(cursor.getString(5)));
                    contact.setSendSMS(Integer.parseInt(cursor.getString(6)) == 0 ? false : true);
                    kontaktList.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return kontaktList;
        }
        catch (Exception e){
            Log.d("DATABASE","Feil "+ e.toString());
            return null;
        }
    }

    // oppdatere kontakt
    public int oppdaterKontakt(Contact contact){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, contact.getNavn());
            values.put(KEY_TLF, contact.getTelefonnr());
            values.put(KEY_DAG, contact.getDag());
            values.put(KEY_MANED, contact.getManed());
            values.put(KEY_AR, contact.getAr());
            if (contact.isSendSMS())
                values.put(KEY_SMS, 1);
            else
                values.put(KEY_SMS, 0);

            int endret = db.update(TABELLNAVN, values, KEY_ID + "=?", new String[]{String.valueOf(contact.get_ID())});
            db.close();
            return endret;
        }
        catch (Exception e){
            Log.d("DATABASE","Feil "+ e.toString());
            return -1;
        }
    }

    // slette kontakt
    public void slettKontakt(Contact contact){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABELLNAVN, KEY_ID + "=?", new String[]{String.valueOf(contact.get_ID())});
            db.close();
        }
        catch (Exception e){
            Log.d("DATABASE","Feil "+ e.toString());
        }
    }

    // finn kontakt
    public Contact finnKontakt(int id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.query(TABELLNAVN, new String[]{KEY_ID, KEY_NAME, KEY_TLF, KEY_DAG, KEY_MANED, KEY_AR, KEY_SMS}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)), Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)), Integer.parseInt(cursor.getString(6)) == 0 ? false : true);
            cursor.close();
            db.close();
            return contact;
        }
        catch (Exception e){
            Log.d("DATABASE","Feil "+ e.toString());
            return null;
        }
    }

    public List<Contact> hentAlleSomHarBursdag(String maned, String dag){
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM " + TABELLNAVN + " WHERE " + KEY_MANED + " = " + maned + " AND " + KEY_DAG + " = " + dag;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.moveToFirst()) {
                do {
                    Contact contact = new Contact();
                    contact.set_ID(Integer.parseInt(cursor.getString(0)));
                    contact.setNavn(cursor.getString(1));
                    contact.setTelefonnr(cursor.getString(2));
                    contact.setDag(Integer.parseInt(cursor.getString(3)));
                    contact.setManed(Integer.parseInt(cursor.getString(4)));
                    contact.setAr(Integer.parseInt(cursor.getString(5)));
                    contact.setSendSMS(Integer.parseInt(cursor.getString(6)) == 0 ? false : true);
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return contacts;
        }
        catch (Exception e){
            Log.d("DATABASE","Feil "+ e.getMessage());
            return null;
        }
    }
}
