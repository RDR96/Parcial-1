package com.rdr.rodrigocorvera.parcial1.Fragmentos;

import android.content.Context;
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
import android.widget.Toast;

import com.rdr.rodrigocorvera.parcial1.Adaptadores.RecyclerViewAdapter;
import com.rdr.rodrigocorvera.parcial1.Clases.Contact;
import com.rdr.rodrigocorvera.parcial1.Actividades.MainActivity;
import com.rdr.rodrigocorvera.parcial1.R;
import com.rdr.rodrigocorvera.parcial1.Adaptadores.RecyclerViewAdapterFavorite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class FragmentFavorite extends Fragment {

    View v;
    private static RecyclerView rv;
    public static ArrayList<Contact> favoriteData;
    public static Context context;
    public static FragmentContact.sendMessage sm;

    public FragmentFavorite () {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        v = inflater.inflate(R.layout.fav_fragment, container, false);
        rv = v.findViewById(R.id.fav_recycleView);

        if (favoriteData != null) {

            RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), favoriteData);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(recyclerViewAdapter);

        }

        buscadorContactos();



        return v;
    }

    //Se encarga de buscar los contactos que se encuentren en el fragmento de favoritos
    public void buscadorContactos(){
        MainActivity.filterTextBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (favoriteData != null) {
                    ArrayList<Contact> filterList = new ArrayList<>();
                    for (Contact item : favoriteData) {
                        if (item.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                            filterList.add(item);
                        }
                    }
                    RecyclerViewAdapterFavorite rc;
                    if (!MainActivity.isContactFragment) {
                        if ( filterList.size() == 0) {
                            if (MainActivity.filterTextBox.getText().toString().equals("")) {
                                rc = new RecyclerViewAdapterFavorite(getContext(), favoriteData);
                            } else {
                                Toast.makeText(context,R.string.no_matches_contacts,Toast.LENGTH_SHORT).show();
                                List<Contact> listaVacia = new ArrayList<Contact>();
                                rc = new RecyclerViewAdapterFavorite(getContext(), listaVacia);
                            }

                        } else {
                            rc = new RecyclerViewAdapterFavorite(getContext(), filterList);
                        }

                        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rc.notifyDataSetChanged();
                        rv.setAdapter(rc);
                    }
                }
            }
        });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //Se ejecuta cuando existe un cambio en la lista de contactos principal. Si se marca como favorito en la pantalla
    // de contactos principal, esta función tendrá el trabajo de marcalo como favorito para el fragmento de favoritos.
    public void displayReceivedData(String name, String number, int option)
    {
        favoriteData = new ArrayList<Contact>();
        int counter = 0;
        for (Contact element : MainActivity.lstContact) {
            if (element.isFavorite()) {
            favoriteData.add(new Contact(element.getName(),element.getNumbers().get(0),true, element.getOriginalPosition(), element.getBitmap(),counter));
            MainActivity.lstContact.get(element.getOriginalPosition()).setFavoritePosition(counter);
            Log.d("Posicion y texto", String.valueOf(favoriteData.get(counter).getFavoritePosition()) + " " + favoriteData.get(counter).getName().toString());
            counter++;
            }
        }

        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), favoriteData);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);
     }

    //Se ejecuta cuando se elimina un elemento del fragmento de favoritos.
    public static void removeItem (int position, Context context) {
        MainActivity.lstContact.get(favoriteData.get(position).getOriginalPosition()).setFavorite(false);
        favoriteData.remove(favoriteData.get(position).getFavoritePosition());
        int counter = 0;
        for ( Contact element : FragmentFavorite.favoriteData) {
            element.setFavoritePosition(counter);
            counter++;
        }
        counter = 0;

        for (Contact element : MainActivity.lstContact) {
            if (element.isFavorite()) {
                MainActivity.lstContact.get(element.getOriginalPosition()).setFavoritePosition(counter);
                Log.d("Posicion y texto", String.valueOf(favoriteData.get(counter).getFavoritePosition()) + " " +favoriteData.get(counter).getName().toString());
                counter++;
            }
        }

        Log.d("mensaje ", String.valueOf(favoriteData.size()));
        //RecyclerViewAdapterFavorite recyclerViewAdapter2 = new RecyclerViewAdapterFavorite(FragmentContact.context, favoriteData);

        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(context, favoriteData);
        rv.removeViewAt(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        recyclerViewAdapter.notifyItemRangeChanged(position, favoriteData.size());
        recyclerViewAdapter.notifyDataSetChanged();
    }


    public interface sendDataDeleted{
        void sendDeleted(String name,String number, int option);
    }

    // Función que se encarga de actualizar la lista de contactos favoritos cuando el estado de estos cambian
    public static void refreshList() {
        /*RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, MainActivity.lstContact);
        rv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        rv.setAdapter(recyclerViewAdapter);*/

        if (favoriteData != null) {
            RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(context, favoriteData);
            rv.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            rv.setAdapter(recyclerViewAdapter);

        }
    }


    @Override
    public void onAttach(Context context) {
        Log.d("Mensaje", "Entro en el attach!!!!!!!!!!!");
        super.onAttach(context);
        try {
            sm = (FragmentContact.sendMessage) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }


}


