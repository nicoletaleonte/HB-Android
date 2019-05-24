package com.example.happybirthday_android;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class LinkListAdapter extends BaseAdapter {

    List<Contact> contacts;
    Context context;
    LayoutInflater layoutInflater;

    public LinkListAdapter(Context context, List<Contact> contacts){
        super();
        layoutInflater = LayoutInflater.from(context);
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.linklist_contact,parent,false);

            // initialisere
            viewHolder = new ViewHolder();
            viewHolder.contact_name = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.contact_varsta = (TextView) convertView.findViewById(R.id.contact_varsta);
            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder)convertView.getTag();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Contact contact = (Contact) getItem(position);
        Calendar iDay = GregorianCalendar.getInstance();
        Calendar birthday = GregorianCalendar.getInstance();
        Calendar isBirthday = GregorianCalendar.getInstance();
        birthday.set(Calendar.YEAR,iDay.get(Calendar.YEAR));
        birthday.set(Calendar.MONTH,contact.getMonth());
        birthday.set(Calendar.DAY_OF_MONTH, contact.getDay());
        isBirthday.set(Calendar.YEAR, contact.getYear());
        isBirthday.set(Calendar.MONTH, contact.getMonth());
        isBirthday.set(Calendar.DAY_OF_MONTH, contact.getDay());

        int age;

        if(iDay.after(birthday)) {
            age = iDay.get(Calendar.YEAR) - isBirthday.get(Calendar.YEAR);
            birthday.add(Calendar.YEAR,1);
        }
        else {
            age = iDay.get(Calendar.YEAR) - isBirthday.get(Calendar.YEAR)-1;
        }

        viewHolder.contact_name.setText(contact.getName());

        viewHolder.contact_varsta.setText("Are" + " " +
                (age+1)+ " " + "ani pe" + " " +
                birthday.get(Calendar.DAY_OF_MONTH) + "." +
                (birthday.get(Calendar.MONTH)+1) + "." +
                birthday.get(Calendar.YEAR));

        if(position % 2 == 0){
            convertView.setBackgroundColor(Color.parseColor("#E6E6E6"));
        }


        return convertView;
    }

    private static class ViewHolder{
        TextView contact_name;
        TextView contact_varsta;
    }

    public void oppdaterListe(List<Contact> kontakter){
        Log.d("ADAPTER", "este in lista de actualizari");
        this.contacts = kontakter;
        notifyDataSetChanged();
    }
}
