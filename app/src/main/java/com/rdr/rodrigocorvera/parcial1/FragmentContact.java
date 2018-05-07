package com.rdr.rodrigocorvera.parcial1;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class FragmentContact extends Fragment {

    View v;
    private RecyclerView rv;
    private int counter = 0;
    public static FragmentContact.sendMessage sm;
    public static Context context;
    public FragmentContact () {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        v = inflater.inflate(R.layout.contacts_fragment,container ,false);
        rv = v.findViewById(R.id.contact_recycleView);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), MainActivity.lstContact);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);

        return rv;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if (MainActivity.lstContact  == null) {
            MainActivity.lstContact = new ArrayList<>();
        }*/
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.lstContact = new ArrayList<>();

        Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            MainActivity.lstContact.add(new Contact(name,phoneNumber,false,counter));
            counter++;
        }

        phones.close();

    }

    interface sendMessage{
        void sendData(String name,String number, int option);
    }

    @Override
    public void onAttach(Context context) {
        Log.d("Mensaje", "Entro en el attach!!!!!!!!!!!");
        super.onAttach(context);
        try {
            sm = (sendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }


    }

    public void displayReceivedData (String name, String number, int i) {

    }

}
