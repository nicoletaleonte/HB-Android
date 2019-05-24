package com.example.happybirthday_android;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class LinkListFragment extends ListFragment {
    private List<Contact> contacts;
    LinkListAdapter adapter;
    DatabaseHandler db;

    private ContactListListener listener;

    public interface ContactListListener{
        void editContact(int contactID);
    }

    public LinkListFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            listener = (ContactListListener) getActivity();
        }
        catch (ClassCastException e){
            throw new ClassCastException("Fail " + e.getMessage());
        }

    }

    @Override

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        final ListView listView = (ListView)v.findViewById(android.R.id.list);

        db = new DatabaseHandler(getActivity());
        contacts = db.findAllContacts();

        adapter = new LinkListAdapter(getActivity(), db.findAllContacts());

        setListAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int contactID =  contacts.get(position).get_ID();
                listener.editContact(contactID);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        contacts = db.findAllContacts();
        adapter.oppdaterListe(db.findAllContacts());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Contact contact = contacts.get(position);
        Toast.makeText(getActivity(),contact.getName(), Toast.LENGTH_SHORT).show();
    }
}
