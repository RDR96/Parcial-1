package com.rdr.rodrigocorvera.parcial1;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Rodrigo Corvera on 6/5/2018.
 */

public class contactInfoActivity extends AppCompatActivity {

    EditText name;
    EditText number;
    ImageView editButton;
    int originalPosition;
    ImageView shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_main);
        Intent callingIntent = getIntent();
        final String intentAction = callingIntent.getAction();
        String intentType = callingIntent.getType();
        name = findViewById(R.id.nameText);
        number = findViewById(R.id.numberText);
        editButton = findViewById(R.id.editButton);
        name.clearFocus();
        number.clearFocus();
        shareButton = findViewById(R.id.shareIntent);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentshare = new Intent();
                intentshare.setAction(Intent.ACTION_SEND);
                intentshare.setType("text/plain");
                intentshare.putExtra(getIntent().EXTRA_TEXT, getAllValues());
                startActivity(Intent.createChooser(intentshare,"Comparte tu contacto"));
            }
        });

        /*name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!name.isFocusable()) {
                    name.clearFocus();
                    number.setFocusable(false);
                }
            }
        });*/

        number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!number.isFocusable()) {
                    number.setFocusable(true);
                    name.setFocusable(false);
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !name.isEnabled() && !number.isEnabled() ) {
                    //name.setFocusable(true); number.setFocusable(true);
                    name.setEnabled(true); number.setEnabled(true);
                } else{
                    //name.setFocusable(false); number.setFocusable(false);
                    name.setEnabled(false); number.setEnabled(false);
                    MainActivity.lstContact.get(originalPosition).setName(name.getText().toString());
                    Toast.makeText(getApplicationContext(),"Nombre: " + MainActivity.lstContact.get(0).getName(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Nombre: " + MainActivity.lstContact.get(originalPosition).getName(),Toast.LENGTH_SHORT).show();
                    MainActivity.lstContact.get(originalPosition).setNumber(number.getText().toString());
                }
            }
        });

        if (Intent.ACTION_SEND.equals(intentAction) && intentType != null){
            if (intentType.equals("text/plain")){
                handleReceivedText(callingIntent);

            }
        }

    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void handleReceivedText (Intent intent) {
        String values [] = intent.getStringExtra(Intent.EXTRA_TEXT).split("'");
        name.setText(values[0]);
        number.setText(values[1]);
        originalPosition = Integer.parseInt(values[2]);
    }

    public String getAllValues () {
        return "Nombre: " + name.getText().toString() + "\n" +
                "Numero: " + number.getText().toString();
    }

    public void getFocus (String tag) {

        if( tag.equals("0") ) {
            name.setFocusable(true);
        } else {
            number.setEnabled(true);
        }

    }


}
