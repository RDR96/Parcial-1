package com.rdr.rodrigocorvera.parcial1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.drm.DrmStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements FragmentContact.sendMessage, FragmentFavorite.sendDataDeleted{

    private TabLayout tab;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    //public static List<Contact> lstContactFav;
    public  static List<Contact> lstContact;
    public static EditText filterTextBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CONTACTS},2);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},2);
        }

        filterTextBox = findViewById(R.id.filterBoxText);

        tab = findViewById(R.id.tabLayout_id);
        viewPager = findViewById(R.id.viewPager_id);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // adding Fragments
        viewPagerAdapter.AddFragment(new FragmentContact(), "");
        viewPagerAdapter.AddFragment(new FragmentFavorite(), "");
        viewPager.setAdapter(viewPagerAdapter);
        tab.setupWithViewPager(viewPager);


        tab.getTabAt(0).setIcon(R.drawable.ic_contacts);
        tab.getTabAt(1).setIcon(R.drawable.ic_favorite);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);

    }


    @Override
    public void sendData(String name, String number, int option) {
        String tag = "android:switcher:" + R.id.viewPager_id + ":" + 1;
        FragmentFavorite f = (FragmentFavorite) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(name, number, option);
    }

    @Override
    public void sendDeleted(String name, String number, int option){
        String tag = "android:switcher:" + R.id.viewPager_id + ":" + 1;
        FragmentContact f = (FragmentContact) getSupportFragmentManager().findFragmentByTag(tag);
        f.displayReceivedData(name, number, option);
    }

}
