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
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*if (MainActivity.lstContact  == null) {
            MainActivity.lstContact = new ArrayList<>();
        }*/
    }
    protected void displayReceivedData(String name,String number, int option)
    {
        favoriteData = new ArrayList<Contact>();
        for (Contact Element : MainActivity.lstContact) {
            if (Element.isFavorite()) {
            favoriteData.add(new Contact(Element.getName(),Element.getNumber(),true, Element.getOriginalPosition()));
            }
        }
        Log.d("El tamaño es: " , String.valueOf(favoriteData.size()));
        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), favoriteData);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);

            /*if(MainActivity.lstContact.size() == 0){
                Toast.makeText(getContext(),
                        "Se agrego", Toast.LENGTH_SHORT).show();
                MainActivity.lstContact.add(new Contact(name,number, true));
                RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), MainActivity.lstContact);
                rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv.setAdapter(recyclerViewAdapter);

            } else{
                for (Contact Element : MainActivity.lstContact) {

                    if(Element.getNumber() != number) {
                        MainActivity.lstContact.add(new Contact(name,number, true));
                        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), MainActivity.lstContact);
                        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv.setAdapter(recyclerViewAdapter);
                    }else {
                        MainActivity.lstContact.remove(MainActivity.lstContact.indexOf(Element));
                        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), MainActivity.lstContact);
                        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv.setAdapter(recyclerViewAdapter);
                    }
                }
            }*/




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


