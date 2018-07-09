package com.rdr.rodrigocorvera.parcial1.Actividades;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rdr.rodrigocorvera.parcial1.Clases.Contact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentContact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentFavorite;
import com.rdr.rodrigocorvera.parcial1.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Rodrigo Corvera on 8/5/2018.
 */

public class AddContactActivity extends AppCompatActivity{

    EditText name;
    EditText number;
    TextView addButton;
    int originalPosition;
    ImageView imageButton;
    ImageView imageSection;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_contacts_layout);
        getViews();
        setConfiguration();

    }

    //***Obtengo los elementos de la vista para que puedan ser manipulados en el código***
    public void getViews(){
        name = findViewById(R.id.nameText);
        number = findViewById(R.id.numberText);
        addButton = findViewById(R.id.addButton);
        imageButton = findViewById(R.id.addImage);
        imageSection = findViewById(R.id.imageSection);

    }

    //*** Se configuran aspectos necesarios para el funcionamiento de la pantalla, listeners, intents, etc.
    public void setConfiguration() {
        //Es posible colocar un boton para regresar a la pantalla en la que estabamos.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //***El bloque siguiente establece los listeners

        //** Boton para cargar una imagen de perfil para el contacto a añadir
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        //Botón para añadir el contacto luego de que los datos fueron ingresados correctamente
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().equals("") && number.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(),R.string.insert_data, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),R.string.add_successful, Toast.LENGTH_SHORT).show();
                    MainActivity.lstContact.add(new Contact(name.getText().toString(), number.getText().toString(),false, MainActivity.lstContact.size(), bitmap));
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        });


    }

    public static final int PICK_IMAGE = 1;

    //Funciona para recibir la imagen que se seleccionó desde la pantalla de carga de Android
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            try {
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageSection.setImageBitmap(bitmapRotation(bitmap));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //Con esta función es posible girar un bitmap.
    public Bitmap bitmapRotation (Bitmap imageBitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90f);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(imageBitmap, imageBitmap.getWidth(), imageBitmap.getHeight(), true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        /*ImageView imageElement = imageView;
        imageElement.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate(90f, imageElement.getDrawable().getBounds().width()/2, imageElement.getDrawable().getBounds().height()/2);
        imageView.setImageMatrix(matrix);*/
        return rotatedBitmap;
    }

    //Es posible regresar a la pantalla principal de contactos cuando se presiona el icono superior izquierdo
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    //Botont que establece lo que se hará cuando se presione el boton de "regreso" del teléfono.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddContactActivity.this, MainActivity.class);
        FragmentContact.refreshList();
        FragmentFavorite.refreshList();
        startActivity(intent);
        finish();
    }
}
