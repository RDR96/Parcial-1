package com.rdr.rodrigocorvera.parcial1.Fragmentos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import com.rdr.rodrigocorvera.parcial1.Clases.Contact;
import com.rdr.rodrigocorvera.parcial1.Actividades.MainActivity;
import com.rdr.rodrigocorvera.parcial1.R;
import com.rdr.rodrigocorvera.parcial1.Adaptadores.RecyclerViewAdapter;
import com.rdr.rodrigocorvera.parcial1.Actividades.AddContactActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class FragmentContact extends Fragment {

    View v;
    private static RecyclerView rv;
    public static int counter = 0;
    public static sendMessage sm;
    public static Context context;
    public EditText filterTextBox;
    public static boolean isGranted;

    public FragmentContact () {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        context = getContext();

        v = inflater.inflate(R.layout.contacts_fragment,container ,false);
        rv = v.findViewById(R.id.contact_recycleView);

        if (checkVersion5_1()) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                MainActivity.lstContact = new ArrayList<>();
                //System.out.println(i);
            } else {
                accessContacts();
                sortElements();
            }
        } else {
            accessContacts();
            sortElements();
        }


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
                Intent newIntent = new Intent(context.getApplicationContext(), AddContactActivity.class);
                context.startActivity(newIntent);
                getActivity().finish();
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
                int valueCounter = 0;
                for (Contact item : MainActivity.lstContact) {

                    if (item.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterList.add(item);
                        item.setFilterPosition(valueCounter);
                        valueCounter++;
                        item.setFilter(true);
                    } else {
                        item.setFilter(false);
                    }
                }
                RecyclerViewAdapter rc;
                if (MainActivity.isContactFragment) {
                    if ( filterList.size() == 0) {
                        if (MainActivity.filterTextBox.getText().toString().equals("")) {
                            rc = new RecyclerViewAdapter(context, MainActivity.lstContact);
                        } else {
                            Toast.makeText(context,R.string.no_matches_contacts,Toast.LENGTH_SHORT).show();
                            List<Contact> emptyList = new ArrayList<>();
                            rc = new RecyclerViewAdapter(context, emptyList);
                        }
                    } else {
                        rc = new RecyclerViewAdapter(context, filterList);
                    }
                    rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rc.notifyDataSetChanged();
                    rv.setAdapter(rc);
                }

            }
        });


        return v;
    }

    //Verifica la versión de Android con el propósito de pedir permisos o no.
    //De tener una versión de Android igual o menor a la 5.1, los permisos se otorgan cuando se instala la aplicación.
    public boolean checkVersion5_1(){
        double version = java.lang.Double.parseDouble(new String(Build.VERSION.RELEASE).replaceAll("(\\d+[.]\\d+)(.*)", "$1"));
        if (version >= 5.1) {
            return true;
        } else{
            return false;
        }
    }

    //Obtiene los contactos del usuario
    public void accessContacts () {

        if (MainActivity.lstContact == null) {
            MainActivity.lstContact = new ArrayList<Contact>();
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            int counter = 0;
            HashSet<String> mobileNoSet = new HashSet<String>();
            while (phones.moveToNext())
            {
                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneNumber = phoneNumber.replaceAll("\\s", "");
                if (phoneNumber.charAt(0) == '+') {
                    phoneNumber = phoneNumber.substring(4);
                }
                if (!mobileNoSet.contains(phoneNumber)) {
                    MainActivity.lstContact.add(new Contact(name,phoneNumber,false,counter, null));
                    mobileNoSet.add(phoneNumber);
                    counter++;
                }
            }
            phones.close();

            if (counter == 0) {
                Toast.makeText(context, "No hay contactos", Toast.LENGTH_SHORT).show();
            }
        }


    }

    //Ordena la lista de contactos
    public static void sortElements() {

        Collections.sort(MainActivity.lstContact, Contact.StuNameComparator);
        int counter = 0;
        for (Contact contactObject : MainActivity.lstContact) {
            contactObject.setOriginalPosition(counter);
            counter++;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if (MainActivity.lstContact  == null) {
            MainActivity.lstContact = new ArrayList<>();
        }*/
    }

    public static void refreshList() {
        sortElements();
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, MainActivity.lstContact);
        rv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        rv.setAdapter(recyclerViewAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (MainActivity.lstContact == null) {
            MainActivity.lstContact = new ArrayList<>();
        }*/
    }

    public void checkPermissions () {
        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                isGranted = true;
                getAllContacts();
            } else {
                isGranted = false;
           }
        }

    }

    public void getAllContacts () {


    }

    public interface sendMessage{
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
