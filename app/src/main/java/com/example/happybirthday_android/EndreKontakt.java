package com.example.happybirthday_android;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
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

public class EndreKontakt extends AppCompatActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener{

    Context context = this;

    EditText editTextNavn;
    EditText editTextTlf;
    TextView editTextDato;
    Switch knappSendSMS;
    int dd, mm, aa;
    boolean sendSMS;

    DatabaseHandler db;
    Contact endreKontakt;
    Calendar fodselsdag;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;

    // brukt for Toast hvis regex ikke stemmer
    String regexFeil = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        db = new DatabaseHandler(this);

        // henter kunden som skal endres
        int kundeID = getIntent().getIntExtra("kontaktID", 0);
        endreKontakt = db.finnKontakt(kundeID);

        // View elementer
        editTextNavn = (EditText) findViewById(R.id.tekstfelt_navn);
        editTextTlf = (EditText) findViewById(R.id.tekstfelt_tlf);
        editTextDato = (EditText) findViewById(R.id.tekstfelt_dato);
        knappSendSMS = (Switch) findViewById(R.id.knapp_sendSMS);

        // datepicker elementet
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        editTextDato.setInputType(InputType.TYPE_NULL);
        editTextDato.setOnClickListener(this);

        // fylle ut felter
        editTextNavn.setText(endreKontakt.getNavn());
        editTextTlf.setText(endreKontakt.getTelefonnr());
        fodselsdag = Calendar.getInstance();
        fodselsdag.set(endreKontakt.getAr(), endreKontakt.getManed(), endreKontakt.getDag());
        editTextDato.setText(dateFormat.format(fodselsdag.getTime()));
        knappSendSMS.setChecked(endreKontakt.isSendSMS() ? true : false);
        sendSMS = endreKontakt.isSendSMS();

        // datepicker elementet
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        final DatePickerDialog.OnDateSetListener dato = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar tempDato = fodselsdag;
                tempDato.set(year, monthOfYear, dayOfMonth);
                aa = tempDato.get(Calendar.YEAR);
                mm = tempDato.get(Calendar.MONTH);
                dd = tempDato.get(Calendar.DAY_OF_MONTH);
                editTextDato.setText(dateFormat.format(tempDato.getTime()));
            }
        };

        editTextDato.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String na = editTextDato.getText().toString();
                dd = Integer.parseInt(na.substring(0, 2));
                mm = Integer.parseInt(na.substring(3, 5)) - 1;
                aa = Integer.parseInt(na.substring(6, 10));
                if(hasFocus){
                    new DatePickerDialog(EndreKontakt.this,dato,aa,mm,dd).show();
                }
                else{
                    new DatePickerDialog(EndreKontakt.this,dato,aa,mm,dd).hide();
                }
            }
        });

        editTextDato.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextDato.getText().equals("")) {
                    aa = fodselsdag.get(Calendar.YEAR);
                    mm = fodselsdag.get(Calendar.MONTH);
                    dd = fodselsdag.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(EndreKontakt.this,dato,aa,mm,dd).show();
                }
                else{
                    String na = editTextDato.getText().toString();
                    dd = Integer.parseInt(na.substring(0,2));
                    mm = Integer.parseInt(na.substring(3, 5)) - 1;
                    aa = Integer.parseInt(na.substring(6,10));

                    new DatePickerDialog(EndreKontakt.this, dato,aa,mm,dd).show();
                }
            }
        });

        // hent verdi fra Switch knapp
        knappSendSMS.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_lagre :
                if(sjekkFelter()) {

                    if(dd == 0 && mm == 0 && aa == 0)
                    {
                        dd = endreKontakt.getDag();
                        mm = endreKontakt.getManed();
                        aa = endreKontakt.getAr();
                    }
                    endreKontakt.setNavn(editTextNavn.getText().toString());
                    endreKontakt.setTelefonnr(editTextTlf.getText().toString());
                    endreKontakt.setDag(dd);
                    endreKontakt.setManed(mm);
                    endreKontakt.setAr(aa);
                    endreKontakt.setSendSMS(sendSMS);

                    if(db.oppdaterKontakt(endreKontakt) != -1){
                        MainActivity.dbEndret = true;
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

            case R.id.action_slette :

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                // set title
                alertDialogBuilder.setTitle(context.getResources().getString(R.string.sletteDialogTittel));

                // set dialog message
                alertDialogBuilder
                        .setMessage(context.getResources().getString(R.string.sletteDialogMelding))
                        //.setCancelable(false)
                        .setPositiveButton(context.getResources().getString(R.string.sletteDialogJa),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // bruker trykker på "slett"
                                        db.slettKontakt(endreKontakt);
                                        MainActivity.dbEndret = true;
                                        Toast.makeText(context, endreKontakt.getNavn() + " slettet", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                        .setNegativeButton(context.getResources().getString(R.string.sletteDialogNei),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // if this button is clicked, just close
                                        // the dialog box and do nothing
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return  true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == editTextDato){
            datePickerDialog.show();
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
