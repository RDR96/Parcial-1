package com.rdr.rodrigocorvera.parcial1.Actividades;

import android.Manifest;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rdr.rodrigocorvera.parcial1.Clases.Contact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentContact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentFavorite;
import com.rdr.rodrigocorvera.parcial1.R;
import com.rdr.rodrigocorvera.parcial1.Adaptadores.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FragmentContact.sendMessage, FragmentFavorite.sendDataDeleted{

    private TabLayout tab;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    Button getContactsButton;
    //public static List<Contact> lstContactFav;
    public  static List<Contact> lstContact;
    public static EditText filterTextBox;
    public static Button addContact;
    public static boolean isContactFragment;
    public boolean isGranted;
    public SharedPreferences prefs;
    public static Application application;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = getApplication();
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean("locked", false)) {
            isGranted = prefs.getBoolean("locked", true);
        } else {
            isGranted = prefs.getBoolean("locked", false);
        }

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_CONTACTS},2);

        }*/

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},2);
        }


        getContactsButton = findViewById(R.id.button_get_all_contacts);
        isContactFragment = true;
        filterTextBox = findViewById(R.id.filterBoxText);
        addContact = findViewById(R.id.addButton);
        tab = findViewById(R.id.tabLayout_id);
        viewPager = findViewById(R.id.viewPager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (isGranted) {
            getContactsButton.setVisibility(View.GONE);
        } else {
            getContactsButton.setVisibility(View.VISIBLE);
        }

        getContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissionAndContinue();
            }
        });


        // adding Fragments
        viewPagerAdapter.AddFragment(new FragmentContact(), "");
        viewPagerAdapter.AddFragment(new FragmentFavorite(), "");
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    isContactFragment = true;
                } else {
                    isContactFragment = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == viewPager.SCROLL_STATE_DRAGGING) {
                    filterTextBox.setText("");
                    FragmentContact.refreshList();
                    FragmentFavorite.refreshList();
                }
            }
        });

        tab.setupWithViewPager(viewPager);

        tab.getTabAt(0).setIcon(R.drawable.ic_contacts);
        tab.getTabAt(1).setIcon(R.drawable.ic_favorite);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }


    //Función que pide permisos al usuario, en este caso: READ_CONTACTS. De esta manera es posible recuperar los contactos del cel.
    private void requestPermissionAndContinue(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                Log.e("texto", "permission denied, show dialog");
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 2);
            }
        }else{
            accessContacts();
        }
    }

    //Esta función se ejecuta justamente después de que se ha llamado a la funcion requestPermissions(). Se verifica
    //el permiso que llamó a la función identificandolo con un código
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                accessContacts();
            }else{
                //redirect to settings page or ask permission again
            }
        }

    }

    //Función que accede a los contactos. Unicamente se llama si los permisos son dados por el usuario.

    public void accessContacts () {
        prefs.edit().putBoolean("locked", true).commit();
        lstContact = new ArrayList<Contact>();
        getContactsButton.setVisibility(View.GONE);
        Cursor phones = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        int counter = 0;
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            MainActivity.lstContact.add(new Contact(name,phoneNumber,false,counter, null));
            counter++;
        }
        phones.close();

        if (counter == 0) {
            Toast.makeText(getApplicationContext(), "No hay contactos", Toast.LENGTH_SHORT).show();
        }
        FragmentContact.refreshList();
    }

    //Esta función sirve de enlace para poder comunicar información entre fragmentos
    @Override
    public void sendData(String name, String number, int option) {
        String tag = "android:switcher:" + R.id.viewPager_id + ":" + 1;
        FragmentFavorite f = (FragmentFavorite) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(name, number, option);
    }



    //Funciona para enviar un mensaje de que el contacto se ha eliminado. El mensaje lo recibe uno de los fragmentos.
    @Override
    public void sendDeleted(String name, String number, int option){

        String tag = "android:switcher:" + R.id.viewPager_id + ":" + 1;
        FragmentContact f = (FragmentContact) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(name, number, option);
    }

    //Botont que establece lo que se hará cuando se presione el boton de "regreso" del teléfono.
    @Override
    public void onBackPressed() {
        if (!filterTextBox.getText().toString().equals("")) {
            filterTextBox.setText("");
        } else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
