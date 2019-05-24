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

    public static boolean dbEdit = false;
    public static String SMS = "Felicitari pentru aceasta zi!";
    public static Boolean sendAllSMS = true;
    public static Boolean warning = true;
    public static String time = "8:0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);

        SMS = PreferenceManager.getDefaultSharedPreferences(this).getString("edittext_preference", "Felicitari pentru aceasta zi!");
        sendAllSMS = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_preference", true);
        warning = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("warningCheckBox", true);
        time = PreferenceManager.getDefaultSharedPreferences(this).getString("preferences_time", "8:0");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.addContact:
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

        if(dbEdit){
            LinkListFragment contactListFragment = (LinkListFragment)getFragmentManager().findFragmentById(R.id.linkListFragment);

            if(contactListFragment == null){
                contactListFragment = new LinkListFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.linkListFragment, contactListFragment);
                ft.commit();
            }
            dbEdit = false;
        }

        SMS = PreferenceManager.getDefaultSharedPreferences(this).getString("edittext_preference", "La multi ani pentru această zi!");
        sendAllSMS = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_preference", true);
        warning = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("warningCheckBox", true);
        time = PreferenceManager.getDefaultSharedPreferences(this).getString("preferences_time", "8:0");


        Intent i = new Intent();
        i.setAction("package com.example.happybirthday_android;");
        sendBroadcast(i);

    }

    @Override
    public void editContact(int contactID) {
        Intent intent = new Intent(this, EditContact.class);
        intent.putExtra("contactID", contactID);
        startActivity(intent);
    }
}


