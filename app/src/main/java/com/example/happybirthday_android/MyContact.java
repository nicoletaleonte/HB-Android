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

//public class MyContact extends AppCompatActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {
public class MyContact extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    EditText editTextNavn;
    EditText editTextTlf;
    TextView editTextDato;
    Switch knappSendSMS;
    int dd, mm, aa;
    boolean sendSMS;

    DatabaseHandler db;
    Calendar calendar;
    Calendar tempCalendar;

    //private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;

    // brukt for Toast hvis regex ikke stemmer
    String regexFeil = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact);

        db = new DatabaseHandler(this);

        // View elementer
        editTextNavn = (EditText) findViewById(R.id.tekstfelt_navn);
        editTextTlf = (EditText) findViewById(R.id.tekstfelt_tlf);
        editTextDato = (EditText) findViewById(R.id.tekstfelt_dato);
        knappSendSMS = (Switch) findViewById(R.id.knapp_sendSMS);

        // datepicker elementet
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        calendar = Calendar.getInstance();
        tempCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener dato = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                tempCalendar.set(year, monthOfYear, dayOfMonth);
                editTextDato.setText(dateFormat.format(tempCalendar.getTime()));
                aa = tempCalendar.get(Calendar.YEAR);
                mm = tempCalendar.get(Calendar.MONTH);
                dd = tempCalendar.get(Calendar.DAY_OF_MONTH);
            }
        };

        editTextDato.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editTextDato.getText().toString().matches("")) {
                    aa = tempCalendar.get(Calendar.YEAR);
                    mm = tempCalendar.get(Calendar.MONTH);
                    dd = tempCalendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    String na = editTextDato.getText().toString();
                    dd = Integer.parseInt(na.substring(0, 2));
                    mm = Integer.parseInt(na.substring(3, 5)) - 1;
                    aa = Integer.parseInt(na.substring(6, 10));
                }

                if (hasFocus) {
                    new DatePickerDialog(MyContact.this, dato, aa, mm, dd).show();
                } else {
                    new DatePickerDialog(MyContact.this, dato, aa, mm, dd).hide();
                }
            }
        });

        editTextDato.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDato.getText().equals("")) {
                    aa = calendar.get(Calendar.YEAR);
                    mm = calendar.get(Calendar.MONTH);
                    dd = calendar.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(MyContact.this, dato, aa, mm, dd).show();
                } else {
                    String na = editTextDato.getText().toString();
                    dd = Integer.parseInt(na.substring(0, 2));
                    mm = Integer.parseInt(na.substring(3, 5)) - 1;
                    aa = Integer.parseInt(na.substring(6, 10));
                    new DatePickerDialog(MyContact.this, dato, aa, mm, dd).show();
                }
            }
        });

        // sett default checked
        knappSendSMS.setChecked(true);

        // sett listener til å hente verdi fra Switch knapp
        knappSendSMS.setOnCheckedChangeListener(this);

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
                if(sjekkFelter()) {
                    Contact innKontakt = new Contact();
                    innKontakt.setNavn(editTextNavn.getText().toString());
                    innKontakt.setTelefonnr(editTextTlf.getText().toString());
                    innKontakt.setDag(dd);
                    innKontakt.setManed(mm);
                    innKontakt.setAr(aa);
                    innKontakt.setSendSMS(sendSMS);
                    if(db.leggTilKontakt(innKontakt)) {
                        MainActivity.dbEndret = true;
                        Toast.makeText(getApplicationContext(), innKontakt.getNavn() + " lagt til", Toast.LENGTH_SHORT).show();
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

            case R.id.action_avbryt :
                finish();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    // sjekk for lovlige felter
    private boolean sjekkFelter(){
        // sjekk for tomme felter
        if(editTextNavn.getText().toString().equals("") ||
                editTextTlf.getText().toString().equals("") ||
                editTextDato.getText().toString().equals("")) {
            regexFeil = "Feil - du må fylle ut alle felter";
            Toast.makeText(this, regexFeil, Toast.LENGTH_LONG).show();
            regexFeil = "";
            return false;
        }
        // sjekk for regex patterns
        else {
            Pattern patternNavn = Pattern.compile("^[\\p{L} .'-]+$");
            Pattern patternTlf = Pattern.compile("^\\+?[0-9. ()-]{5,20}$");
            Pattern patternDato = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

            Matcher matcherNavn = patternNavn.matcher(editTextNavn.getText().toString());
            Matcher matcherTlf = patternTlf.matcher(editTextTlf.getText().toString());
            Matcher matcherDato = patternDato.matcher(editTextDato.getText().toString());

            if (!matcherNavn.find()) {
                regexFeil += "Feil navn\n";
            }

            if (!matcherTlf.find()) {
                regexFeil += "Feil telefonnummer\n";
            }

            if (!matcherDato.find()) {
                regexFeil += "Feil dato\n";
            }

            if (!regexFeil.equals("")){
                Toast.makeText(this, regexFeil, Toast.LENGTH_LONG).show();
                regexFeil = "";
                return false;
            }
            else{
                return true;
            }
        }
    }

    // brukt til å få verdi fra Switch knapp (send sms)
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sendSMS = isChecked;
    }
}
