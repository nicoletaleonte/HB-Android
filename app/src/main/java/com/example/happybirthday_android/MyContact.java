package com.example.happybirthday_android;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyContact extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    EditText editTextName;
    EditText editTextphoneNo;
    TextView editTextDate;
    Switch buttonSendSMS;
    int dd, mm, yy;
    boolean sendSMS;

    DatabaseHandler db;
    Calendar calendar;
    Calendar tempCalendar;

    private SimpleDateFormat dateFormat;

    String regexFail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);

        db = new DatabaseHandler(this);


        editTextName = (EditText) findViewById(R.id.text_name);
        editTextphoneNo = (EditText) findViewById(R.id.text_phoneNo);
        editTextDate = (EditText) findViewById(R.id.text_date);
        buttonSendSMS = (Switch) findViewById(R.id.button_sendSMS);

        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        calendar = Calendar.getInstance();
        tempCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                tempCalendar.set(year, monthOfYear, dayOfMonth);
                editTextDate.setText(dateFormat.format(tempCalendar.getTime()));
                yy = tempCalendar.get(Calendar.YEAR);
                mm = tempCalendar.get(Calendar.MONTH);
                dd = tempCalendar.get(Calendar.DAY_OF_MONTH);
            }
        };

        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextDate.getText().toString().matches("")) {
                    yy = tempCalendar.get(Calendar.YEAR);
                    mm = tempCalendar.get(Calendar.MONTH);
                    dd = tempCalendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    String na = editTextDate.getText().toString();
                    dd = Integer.parseInt(na.substring(0, 2));
                    mm = Integer.parseInt(na.substring(3, 5)) - 1;
                    yy = Integer.parseInt(na.substring(6, 10));
                }

                if (hasFocus) {
                    new DatePickerDialog(MyContact.this, date, yy, mm, dd).show();
                } else {
                    new DatePickerDialog(MyContact.this, date, yy, mm, dd).hide();
                }
            }
        });

        editTextDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDate.getText().equals("")) {
                    yy = calendar.get(Calendar.YEAR);
                    mm = calendar.get(Calendar.MONTH);
                    dd = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(MyContact.this, date, yy, mm, dd).show();
                } else {
                    String na = editTextDate.getText().toString();
                    dd = Integer.parseInt(na.substring(0, 2));
                    mm = Integer.parseInt(na.substring(3, 5)) - 1;
                    yy = Integer.parseInt(na.substring(6, 10));
                    new DatePickerDialog(MyContact.this, date, yy, mm, dd).show();
                }
            }
        });

        buttonSendSMS.setChecked(true);

        buttonSendSMS.setOnCheckedChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id){
            case R.id.action_lagre :
                if(checkFilds()) {
                    Contact innKontakt = new Contact();
                    innKontakt.setName(editTextName.getText().toString());
                    innKontakt.setPhoneNo(editTextphoneNo.getText().toString());
                    innKontakt.setDay(dd);
                    innKontakt.setMonth(mm);
                    innKontakt.setYear(yy);
                    innKontakt.setSendSMS(sendSMS);
                    if(db.addContact(innKontakt)) {
                        MainActivity.dbEdit = true;
                        Toast.makeText(getApplicationContext(), innKontakt.getName() + " added", Toast.LENGTH_SHORT).show();
                        finish();
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else {
                    return false;
                }

            case R.id.action_cancel:
                finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean checkFilds(){

        if(editTextName.getText().toString().equals("") ||
                editTextphoneNo.getText().toString().equals("") ||
                editTextDate.getText().toString().equals("")) {
            regexFail = "Gresit - trebuie sa completezi toate campurile.";
            Toast.makeText(this, regexFail, Toast.LENGTH_LONG).show();
            regexFail = "";
            return false;
        }

        else {
            Pattern patternName = Pattern.compile("^[\\p{L} .'-]+$");
            Pattern patternPhoneNo = Pattern.compile("^\\+?[0-9. ()-]{5,20}$");
            Pattern patternDate = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

            Matcher matcherName = patternName.matcher(editTextName.getText().toString());
            Matcher matcherPhoneNo = patternPhoneNo.matcher(editTextphoneNo.getText().toString());
            Matcher matcherDate = patternDate.matcher(editTextDate.getText().toString());

            if (!matcherName.find()) {
                regexFail += "Nume gresit\n";
            }

            if (!matcherPhoneNo.find()) {
                regexFail += "Numar de telefon gresit\n";
            }

            if (!matcherDate.find()) {
                regexFail += "Data gresita\n";
            }

            if (!regexFail.equals("")){
                Toast.makeText(this, regexFail, Toast.LENGTH_LONG).show();
                regexFail = "";
                return false;
            }
            else{
                return true;
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sendSMS = isChecked;
    }
}
