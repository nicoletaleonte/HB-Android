package com.example.happybirthday_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements LinkListFragment.ContactListListener{

    // sette default verdier
    public static boolean dbEndret = false;
    public static String smsMelding = "Gratulerer med dagen!";
    public static Boolean sendAlleSMS = true;
    public static Boolean varsling = true;
    public static String tidspunkt = "8:0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // variabler
        DatabaseHandler db = new DatabaseHandler(this);

        // sjekk hvis preferanser har blitt endret
        smsMelding = PreferenceManager.getDefaultSharedPreferences(this).getString("edittext_preference", "Gratulerer med dagen!");
        sendAlleSMS = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_preference", true);
        varsling = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("varslingCheckBox", true);
        tidspunkt = PreferenceManager.getDefaultSharedPreferences(this).getString("preferences_tidspunkt", "8:0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.leggTilKontakt:
                Intent intent1 = new Intent(this,MyContact.class);
                startActivity(intent1);
                return true;

            case R.id.action_settings:
                Intent intent2 = new Intent(this, SetPreferencesActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // oppdater list på forsiden hvis den har blitt endret
//        if(dbEndret){
//            LinkListFragment kontaktListFragment = (LinkListFragment) getFragmentManager().findFragmentById(R.id.linkListFragment);
//
//            if(kontaktListFragment == null){
//                kontaktListFragment = new LinkListFragment();
//                FragmentManager fm = getFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.linkListFragment,kontaktListFragment);
//                ft.commit();
//            }
//            dbEndret = false;
//        }

        // sjekk hvis preferanser har blitt endret
        smsMelding = PreferenceManager.getDefaultSharedPreferences(this).getString("edittext_preference", "Gratulerer med dagen!");
        sendAlleSMS = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_preference", true);
        varsling = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("varslingCheckBox", true);
        tidspunkt = PreferenceManager.getDefaultSharedPreferences(this).getString("preferences_tidspunkt", "8:0");


        Intent i = new Intent();
        i.setAction("com.example.s198586_mappe2.startVarsling");
        sendBroadcast(i);

    }

    @Override
    public void endreKontakt(int kontaktID) {
        Intent intent = new Intent(this, EndreKontakt.class);
        intent.putExtra("kontaktID", kontaktID);
        startActivity(intent);
    }
}


