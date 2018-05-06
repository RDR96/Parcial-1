package com.rdr.rodrigocorvera.parcial1;

import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class FragmentCall extends Fragment {

    View v;
    private RecyclerView rv;
    private static List<Contact> lstContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.call_fragment, container, false);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public FragmentCall (){

    }
}
