package com.rdr.rodrigocorvera.parcial1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.icu.text.LocaleDisplayNames;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class FragmentFavorite extends Fragment {

    View v;
    private static RecyclerView rv;
    public static ArrayList<Contact> favoriteData;
    public static FragmentContact.sendMessage sm;

    public FragmentFavorite () {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fav_fragment, container, false);
        rv = v.findViewById(R.id.fav_recycleView);

        if (favoriteData != null) {

            RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), favoriteData);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(recyclerViewAdapter);
        }
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

                   if ( filterList.size() == 0) {
                       rc = new RecyclerViewAdapterFavorite(getContext(), favoriteData);
                   } else {
                       rc = new RecyclerViewAdapterFavorite(getContext(), filterList);
                   }

                   rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                   rc.notifyDataSetChanged();
                   rv.setAdapter(rc);
               }
               }
        });



        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    protected void displayReceivedData(String name,String number, int option)
    {

        favoriteData = new ArrayList<Contact>();

        for (Contact Element : MainActivity.lstContact) {
            if (Element.isFavorite()) {
            favoriteData.add(new Contact(Element.getName(),Element.getNumber(),true, Element.getOriginalPosition(), Element.getBitmap()));
            }
        }

        Log.d("El tamaño es: " , String.valueOf(favoriteData.size()));
        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), favoriteData);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);

     }

    public static void removeItem (int position, Context context) {
        MainActivity.lstContact.get(favoriteData.get(position).getOriginalPosition()).setFavorite(false);
        favoriteData.remove(position);

        //RecyclerViewAdapterFavorite recyclerViewAdapter2 = new RecyclerViewAdapterFavorite(FragmentContact.context, favoriteData);

        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(context, favoriteData);
        rv.removeViewAt(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        recyclerViewAdapter.notifyItemRangeChanged(position, favoriteData.size());
        recyclerViewAdapter.notifyDataSetChanged();
    }


    interface sendDataDeleted{
        void sendDeleted(String name,String number, int option);
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


