package com.rdr.rodrigocorvera.parcial1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class FragmentFavorite extends Fragment {

    View v;
    private static RecyclerView rv;


    public FragmentFavorite () {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fav_fragment, container, false);
        rv = v.findViewById(R.id.fav_recycleView);

        if (MainActivity.lstContactFav != null) {
            RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), MainActivity.lstContactFav);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(recyclerViewAdapter);
        }

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (MainActivity.lstContactFav  == null) {
            MainActivity.lstContactFav = new ArrayList<>();
        }

    }
    protected void displayReceivedData(String name,String number)
    {
        //txtData.setText("Data received: "+message);
        MainActivity.lstContactFav.add(new Contact(name,number));
        Log.d("Posicion 1:", MainActivity.lstContactFav.get(0).getName());
        Log.d("Posicion 1:", MainActivity.lstContactFav.get(0).getNumber());
        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(getContext(), MainActivity.lstContactFav);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(recyclerViewAdapter);
     }

    public static void removeItem (int position, Context context) {
        MainActivity.lstContactFav.remove(position);
        RecyclerViewAdapterFavorite recyclerViewAdapter = new RecyclerViewAdapterFavorite(context, MainActivity.lstContactFav);
        rv.removeViewAt(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        recyclerViewAdapter.notifyItemRangeChanged(position, MainActivity.lstContactFav.size());
        recyclerViewAdapter.notifyDataSetChanged();
    }


}


