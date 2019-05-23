package com.example.happybirthday_android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    int ar, maned, dag;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // vi bruker dagens dag som default for date picker
        final Calendar calendar = Calendar.getInstance();
        ar = calendar.get(Calendar.YEAR);
        maned = calendar.get(Calendar.MONTH);
        dag = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(),this,ar,maned,dag);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

    }
}
