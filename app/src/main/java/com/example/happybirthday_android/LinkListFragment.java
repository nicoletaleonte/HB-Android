package com.example.happybirthday_android;

import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class LinkListFragment extends ListFragment {
    // liste
    private List<Contact> listKontakter;

    // fragment adapter
    LinkListAdapter adapter;

    // Database handler
    DatabaseHandler db;


    // interface som aktiviteten må kalle
    private ContactListListener listener;

    public interface ContactListListener{
        void endreKontakt(int kontaktID);
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
            throw new ClassCastException("Feil " + e.getMessage());
        }

    }

    @Override

    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        final ListView listView = (ListView)v.findViewById(android.R.id.list);

        db = new DatabaseHandler(getActivity());
        listKontakter = db.finnAlleKontakter();

        adapter = new LinkListAdapter(getActivity(), db.finnAlleKontakter());

        setListAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // hente ListView elementet
                int kontaktID =  listKontakter.get(position).get_ID();

                // sende kontaktID til main aktiviteten
                listener.endreKontakt(kontaktID);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        listKontakter = db.finnAlleKontakter();
        adapter.oppdaterListe(db.finnAlleKontakter());
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // hente ListView elementet
        Contact contact = listKontakter.get(position);

        // vis posisjonen
        Toast.makeText(getActivity(),contact.getNavn(), Toast.LENGTH_SHORT).show();
    }
}
