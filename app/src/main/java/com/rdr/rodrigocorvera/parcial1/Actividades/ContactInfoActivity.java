package com.rdr.rodrigocorvera.parcial1.Actividades;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rdr.rodrigocorvera.parcial1.Clases.Contact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentContact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentFavorite;
import com.rdr.rodrigocorvera.parcial1.R;

import java.util.ArrayList;

import static com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentContact.context;

/**
 * Created by Rodrigo Corvera on 6/5/2018.
 */

public class ContactInfoActivity extends AppCompatActivity {

    EditText name;
    EditText number;
    ImageView editButton;
    int originalPosition;
    ImageView shareButton;
    ImageView imageReview;
    ImageView callButton;
    ImageView deleteElement;
    int decision;
    int favoritePosition;
    Intent callingIntent;
    CoordinatorLayout coordinatorLayout;
    String intentAction;
    String intentType;
    Spinner numbersSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_main);
        getViews();
        setConfiguration();

    }

 //***Obtengo los elementos de la vista para que puedan ser manipulados en el código***
    public void getViews() {
        name = findViewById(R.id.nameText);
        //number = findViewById(R.id.numberText);
        editButton = findViewById(R.id.editButton);
        imageReview = findViewById(R.id.imageReview);
        name.clearFocus();
        //number.clearFocus();
        shareButton = findViewById(R.id.shareIntent);
        shareButton = findViewById(R.id.shareIntent);
        callButton = findViewById(R.id.callIntent);
        deleteElement = findViewById(R.id.deleteIntent);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        numbersSpinner = findViewById(R.id.numbers_spinner);

        //Obtengo el intent que vienen de actividad que llamó a esta.
        callingIntent = getIntent();
        intentAction = callingIntent.getAction();
        intentType = callingIntent.getType();
    }

//*** Se configuran aspectos necesarios para el funcionamiento de la pantalla, listeners, intents, etc.
    public void setConfiguration(){
        //Con este método se asegura que el teclado permanece oculto cuando se abra la vista actual
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Es posible colocar un boton para regresar a la pantalla en la que estabamos.
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        //Obtengo, en este caso, la información que viene en forma de texto desde la actividad que llamó a esta.
        if (Intent.ACTION_SEND.equals(intentAction) && intentType != null){
            if (intentType.equals("text/plain")){
                handleReceivedText(callingIntent);
            }
        }

        //Llena con numeros de teléfono el Spinner
        fillNumberSpinner();


        //****Comienzan los métodos de escucha para ejecutar funciones.****

        //Muestra un mensaje indicando que acción toma el botón.
        callButton.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.call_function),
                        Snackbar.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        //Muestra un mensaje indicando que acción toma el botón.
        deleteElement.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.delete_function),
                        Snackbar.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        //Muestra un mensaje indicando que acción toma el botón.
        shareButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Snackbar.make(coordinatorLayout, getResources().getString(R.string.share_function),
                        Snackbar.LENGTH_SHORT)
                        .show();
                return true;
            }
        });

        //Abre ventana para borrar contacto, un dialog en el cual confirmar decisión
        deleteElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactInfoActivity.this);
                View dialogElements = getLayoutInflater().inflate(R.layout.dialog_confirmacion_eliminar, null);
                Button yesButton = dialogElements.findViewById(R.id.button_yes_answer);
                Button noButton = dialogElements.findViewById(R.id.button_no_answer);
                final AlertDialog alert;
                mBuilder.setView(dialogElements);
                alert = mBuilder.create();
                alert.show();

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (decision == 0) {

                            if (MainActivity.lstContact.get(originalPosition).isFavorite()) {
                                for (Contact Element : FragmentFavorite.favoriteData) {
                                    if (Element.getOriginalPosition() == MainActivity.lstContact.get(originalPosition).getOriginalPosition()) {
                                        FragmentFavorite.favoriteData.remove(Element.getFavoritePosition());
                                    }
                                }
                            }

                            Toast.makeText(getApplicationContext(),name.getText().toString() + " eliminado",Toast.LENGTH_SHORT).show();
                            MainActivity.lstContact.remove(originalPosition);

                            int counter = 0;

                            for ( Contact Element : MainActivity.lstContact) {
                                Element.setOriginalPosition(counter);
                                counter++;
                            }

                            Intent backTo = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(backTo);
                            onBackPressed();
                        } else {

                            MainActivity.lstContact.remove(originalPosition);
                            FragmentFavorite.favoriteData.remove(favoritePosition);
                            int counter = 0;
                            for ( Contact Element : FragmentFavorite.favoriteData) {
                                Element.setFavoritePosition(counter);
                                counter++;
                            }

                            Intent backTo = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(backTo);
                            onBackPressed();
                        }

                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.hide();
                        alert.dismiss();
                    }
                });
            }


        });

        //Es posible llamar al contacto seleccionado actualmente.
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + numbersSpinner.getSelectedItem().toString()));
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                }else{
                    context.startActivity(intent);
                }
            }
        });


        //Abre ventana por defecto para poder compartir información a otra aplicación.
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


        //Abre dialogo para poder editar información del usuario actual
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<String> auxNumbers = new ArrayList<>();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ContactInfoActivity.this);
                View dialogElements = getLayoutInflater().inflate(R.layout.dialog_edit_info, null);
                TextView currentNameText = dialogElements.findViewById(R.id.text_box_name);
                TextView currentNumberText = dialogElements.findViewById(R.id.text_box_number);
                TextView currentAlternativeText = dialogElements.findViewById(R.id.text_box_number_2);
                currentNameText.setText(name.getText());
                currentNumberText.setText(MainActivity.lstContact.get(originalPosition).getNumbers().get(0));

                if (MainActivity.lstContact.get(originalPosition).getNumbers().size() > 1) {
                    currentAlternativeText.setText(MainActivity.lstContact.get(originalPosition).getNumbers().get(1));
                }else{
                    currentAlternativeText.setText("-");
                }

                //currentNumberText.setText(number.getText());
                final EditText editableTextboxName = dialogElements.findViewById(R.id.editable_textbox_name);
                final EditText editableTextboxNumber = dialogElements.findViewById(R.id.editable_textbox_number);
                final EditText editableTextboxNumber2 = dialogElements.findViewById(R.id.editable_textbox_number2);
                Button updateButton = dialogElements.findViewById(R.id.button_update_answer);
                Button cancelButton = dialogElements.findViewById(R.id.button_cancel_answer);
                final AlertDialog alert;
                mBuilder.setView(dialogElements);
                alert = mBuilder.create();
                alert.show();

                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nameTextbox = editableTextboxName.getText().toString();
                        String numberTextbox = editableTextboxNumber.getText().toString();
                        String alternativeNumber = editableTextboxNumber2.getText().toString();
                        Log.d("Valor real", MainActivity.lstContact.get(originalPosition).getName());
                        Log.d("Valor que viene", name.getText().toString());
                        if (!nameTextbox.equals("") || !numberTextbox.equals("") || !alternativeNumber.equals("")) {
                            if (!nameTextbox.equals("") && numberTextbox.equals("")) {
                                auxNumbers.add(MainActivity.lstContact.get(originalPosition).getNumbers().get(0));

                                if (!alternativeNumber.equals("")) {
                                    auxNumbers.add(alternativeNumber);
                                }
                                MainActivity.lstContact.get(originalPosition).setNumbers(auxNumbers);
                                MainActivity.lstContact.get(originalPosition).setName(nameTextbox);
                                name.setText(nameTextbox);
                            } else if (nameTextbox.equals("") && !numberTextbox.equals("")) {

                                auxNumbers.add(numberTextbox);
                                if (!alternativeNumber.equals("")) {
                                    auxNumbers.add(alternativeNumber);
                                } else if (MainActivity.lstContact.get(originalPosition).getNumbers().size() > 1) {
                                    auxNumbers.add(MainActivity.lstContact.get(originalPosition).getNumbers().get(1));
                                }

                                MainActivity.lstContact.get(originalPosition).setNumbers(auxNumbers);
                            } else if (nameTextbox.equals("") && numberTextbox.equals("") && !alternativeNumber.equals("")) {
                                auxNumbers.add(MainActivity.lstContact.get(originalPosition).getNumbers().get(0));
                                auxNumbers.add(alternativeNumber);
                                MainActivity.lstContact.get(originalPosition).setNumbers(auxNumbers);
                            }

                            else {
                                auxNumbers.add(numberTextbox);
                                MainActivity.lstContact.get(originalPosition).setName(nameTextbox);
                                MainActivity.lstContact.get(originalPosition).setNumbers(auxNumbers);
                                if (!alternativeNumber.equals("")) {
                                    auxNumbers.add(alternativeNumber);
                                }
                                MainActivity.lstContact.get(originalPosition).setNumbers(auxNumbers);
                                name.setText(nameTextbox);
                            }

                            if (FragmentFavorite.favoriteData != null) {
                                if (FragmentFavorite.favoriteData.size() != 0) {
                                    FragmentFavorite.favoriteData.get(MainActivity.lstContact.get(originalPosition).getFavoritePosition()).setName(name.getText().toString());
                                    if(auxNumbers.size() != 0) {
                                        FragmentFavorite.favoriteData.get(MainActivity.lstContact.get(originalPosition).getFavoritePosition()).setNumbers(auxNumbers);
                                    }else {
                                        FragmentFavorite.favoriteData.get(MainActivity.lstContact.get(originalPosition).getFavoritePosition()).setNumbers(MainActivity.lstContact.get(originalPosition).getNumbers());
                                    }

                                }

                            }
                            fillNumberSpinner();
                            Toast.makeText(getApplicationContext(),R.string.successful_update_info,Toast.LENGTH_SHORT).show();
                            alert.hide();
                            alert.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(),R.string.fill_some_field,Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.hide();
                        alert.dismiss();
                    }
                });

            }
        });



    }

    //Rellena el spinner con los números del contacto actual
    public void fillNumberSpinner(){
        numbersSpinner.setAdapter(null);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.spinner_item, MainActivity.lstContact.get(originalPosition).getNumbers());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numbersSpinner.setAdapter(spinnerAdapter);
    }

    //Es posible regresar a la pantalla principal de contactos cuando se presiona el icono superior izquierdo
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;
    }
    // Se obtiene la infomación de la actividad que levantó esta ventana. Se recibe con Intent
    public void handleReceivedText (Intent intent) {
        Bitmap bmp;
        byte[] byteArray = intent.getByteArrayExtra("image");
        String values [] = intent.getStringExtra(Intent.EXTRA_TEXT).split("'");
        name.setText(values[0]);
        originalPosition = Integer.parseInt(values[2]);
        decision = Integer.parseInt(values[3]);
        favoritePosition = Integer.parseInt(values[4]);
        if (MainActivity.lstContact.get(originalPosition).getBitmap() != null) {
            imageReview.setImageBitmap(bitmapRotation(MainActivity.lstContact.get(originalPosition).getBitmap()));
        }

    }

    //Obtiene el nombre y el numero principal para ser compartido con otras aplicaciones.
    public String getAllValues () {
        return "Nombre: " + name.getText().toString() + "\n" +
                "Numero: " + numbersSpinner.getSelectedItem().toString();
    }

    //Al presionar el botón de regreso es    posible actualizar la lista de contactos y favoritos cuando regremos
    //a la pantalla que llamó esta actividad.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ContactInfoActivity.this, MainActivity.class);
        FragmentContact.refreshList();
        FragmentFavorite.refreshList();
        startActivity(intent);
        finish();
    }

    //Función que gira un bitmap dado.
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

}
