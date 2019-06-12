package com.example.happybirthday_android;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

public class EditContact extends AppCompatActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    Context context = this;

    EditText editTextName;
    EditText editTextPhoneNo;
    TextView editTextDate;
    Switch buttonSendSMS;
    int dd, mm, yy;
    boolean sendSMS;

    DatabaseHandler db;
    Contact editContact;
    Calendar birthday;

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat;

    String regexFail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        db = new DatabaseHandler(this);

        int clientID = getIntent().getIntExtra("contactID", 0);
        editContact = db.findContactById(clientID);


        editTextName = (EditText) findViewById(R.id.text_name);
        editTextPhoneNo = (EditText) findViewById(R.id.text_phoneNo);
        editTextDate = (EditText) findViewById(R.id.text_date);
        buttonSendSMS = (Switch) findViewById(R.id.button_sendSMS);


        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        editTextDate.setInputType(InputType.TYPE_NULL);
        editTextDate.setOnClickListener(this);


        editTextName.setText(editContact.getName());
        editTextPhoneNo.setText(editContact.getPhoneNo());
        birthday = Calendar.getInstance();
        birthday.set(editContact.getYear(), editContact.getMonth(), editContact.getDay());
        editTextDate.setText(dateFormat.format(birthday.getTime()));
        buttonSendSMS.setChecked(editContact.isSendSMS() ? true : false);
        sendSMS = editContact.isSendSMS();


        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar tempDate = birthday;
                tempDate.set(year, monthOfYear, dayOfMonth);
                yy = tempDate.get(Calendar.YEAR);
                mm = tempDate.get(Calendar.MONTH);
                dd = tempDate.get(Calendar.DAY_OF_MONTH);
                editTextDate.setText(dateFormat.format(tempDate.getTime()));
            }
        };

        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String na = editTextDate.getText().toString();
                dd = Integer.parseInt(na.substring(0, 2));
                mm = Integer.parseInt(na.substring(3, 5)) - 1;
                yy = Integer.parseInt(na.substring(6, 10));
                if (hasFocus) {
                    new DatePickerDialog(EditContact.this, date, yy, mm, dd).show();
                } else {
                    new DatePickerDialog(EditContact.this, date, yy, mm, dd).hide();
                }
            }
        });

        editTextDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextDate.getText().equals("")) {
                    yy = birthday.get(Calendar.YEAR);
                    mm = birthday.get(Calendar.MONTH);
                    dd = birthday.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(EditContact.this, date, yy, mm, dd).show();
                } else {
                    String na = editTextDate.getText().toString();
                    dd = Integer.parseInt(na.substring(0, 2));
                    mm = Integer.parseInt(na.substring(3, 5)) - 1;
                    yy = Integer.parseInt(na.substring(6, 10));

                    new DatePickerDialog(EditContact.this, date, yy, mm, dd).show();
                }
            }
        });


        buttonSendSMS.setOnCheckedChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_contact, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_lagre:
                if (checkFields()) {

                    if (dd == 0 && mm == 0 && yy == 0) {
                        dd = editContact.getDay();
                        mm = editContact.getMonth();
                        yy = editContact.getYear();
                    }
                    editContact.setName(editTextName.getText().toString());
                    editContact.setPhoneNo(editTextPhoneNo.getText().toString());
                    editContact.setDay(dd);
                    editContact.setMonth(mm);
                    editContact.setYear(yy);
                    editContact.setSendSMS(sendSMS);

                    if (db.updateContact(editContact) != -1) {
                        MainActivity.dbEdit = true;
                        finish();
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }

            case R.id.action_cancel:
                finish();
                return true;

            case R.id.action_delete:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle(context.getResources().getString(R.string.deleteDialogTitle));

                alertDialogBuilder
                        .setMessage(context.getResources().getString(R.string.deleteDialogMessage))
                        .setPositiveButton(context.getResources().getString(R.string.deleteDialogDa),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        db.deleteContact(editContact);
                                        MainActivity.dbEdit = true;
                                        Toast.makeText(context, editContact.getName() + " deleted", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                })
                        .setNegativeButton(context.getResources().getString(R.string.deleteDialogNu),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == editTextDate) {
            datePickerDialog.show();
        }
    }

    // check
    private boolean checkFields() {
        // sjekk for tomme felter
        if (editTextName.getText().toString().equals("") ||
                editTextPhoneNo.getText().toString().equals("") ||
                editTextDate.getText().toString().equals("")) {
            regexFail = "Greșit - trebuie sa completezi toate campurile";
            Toast.makeText(this, regexFail, Toast.LENGTH_LONG).show();
            regexFail = "";
            return false;
        } else {
            Pattern patternName = Pattern.compile("^[\\p{L} .'-]+$");
            Pattern patternPhoneNo = Pattern.compile("^\\+?[0-9. ()-]{5,20}$");
            Pattern patternDate = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

            Matcher matcherName = patternName.matcher(editTextName.getText().toString());
            Matcher matcherPhoneNo = patternPhoneNo.matcher(editTextPhoneNo.getText().toString());
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

            if (!regexFail.equals("")) {
                Toast.makeText(this, regexFail, Toast.LENGTH_LONG).show();
                regexFail = "";
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sendSMS = isChecked;
    }
}
