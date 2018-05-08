package com.rdr.rodrigocorvera.parcial1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.rdr.rodrigocorvera.parcial1.MainActivity.addContact;
import static com.rdr.rodrigocorvera.parcial1.MainActivity.filterTextBox;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class FragmentContact extends Fragment {

    View v;
    private RecyclerView rv;
    public static int counter = 0;
    public static FragmentContact.sendMessage sm;
    public static Context context;
    public EditText filterTextBox;

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

        MainActivity.filterTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.filterTextBox.setFocusableInTouchMode(true);
            }
        });

        MainActivity.addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(context.getApplicationContext(), addContactActivity.class);
                context.startActivity(newIntent);
            }
        });


        MainActivity.filterTextBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ArrayList<Contact> filterList = new ArrayList<>();
                for (Contact item : MainActivity.lstContact) {
                    if (item.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterList.add(item);
                    }
                }
                RecyclerViewAdapter rc;

                if ( filterList.size() == 0) {
                    rc = new RecyclerViewAdapter(context, MainActivity.lstContact);
                } else {
                    rc = new RecyclerViewAdapter(context, filterList);
                }

                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                rc.notifyDataSetChanged();
                rv.setAdapter(rc);
            }
        });


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

        if (MainActivity.lstContact == null) {

            MainActivity.lstContact = new ArrayList<>();

            Cursor phones = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

            while (phones.moveToNext())
            {
                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                MainActivity.lstContact.add(new Contact(name,phoneNumber,false,counter, null));
                counter++;
            }
            phones.close();

            if (counter == 0) {
                Toast.makeText(getContext(), "No hay contactos", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(getContext(), MainActivity.lstContact.get(0).getName(), Toast.LENGTH_SHORT).show();
        }





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
