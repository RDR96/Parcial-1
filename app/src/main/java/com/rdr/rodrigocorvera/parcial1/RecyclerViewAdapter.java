package com.rdr.rodrigocorvera.parcial1;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.rdr.rodrigocorvera.parcial1.MainActivity.filterTextBox;
import static java.security.AccessController.getContext;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    public Context context;
    public List<Contact> data;
    public RecyclerViewAdapter(Context context, List<Contact> data) {
        this.context = context;
        this.data = data;
        //Log.d("data", data.get(0).getNumber());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
       final MyViewHolder vwHolder = new MyViewHolder(v);
        final LinearLayout callingButton = v.findViewById(R.id.callAction);
        final ImageView favoriteButton = v.findViewById(R.id.favoriteAction);
        final LinearLayout container = v.findViewById(R.id.element);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newIntent = new Intent(context.getApplicationContext(), contactInfoActivity.class);
                newIntent.setAction(Intent.ACTION_SEND);
                newIntent.setType("text/plain");

                if (data.get(vwHolder.getAdapterPosition()).getBitmap() != null) {
                    int position = vwHolder.getAdapterPosition();
                    newIntent.putExtra(Intent.EXTRA_TEXT, data.get(vwHolder.getAdapterPosition()).getName()
                            + "'" + data.get(vwHolder.getAdapterPosition()).getNumber() + "'" + data.get(vwHolder.getAdapterPosition()).getOriginalPosition() + "'" + 0 + "'" + data.get(vwHolder.getAdapterPosition()).getFavoritePosition() + "'" + position);
                } else {
                    newIntent.putExtra(Intent.EXTRA_TEXT, data.get(vwHolder.getAdapterPosition()).getName()
                            + "'" + data.get(vwHolder.getAdapterPosition()).getNumber() + "'" + data.get(vwHolder.getAdapterPosition()).getOriginalPosition() + "'" + 0 + "'" + data.get(vwHolder.getAdapterPosition()).getFavoritePosition() + "'" + 5000);
                }
                context.startActivity(newIntent);
            }
        });

        callingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + data.get(vwHolder.getAdapterPosition()).getNumber()));
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                }else{
                    context.startActivity(intent);
                }
            }
        });



        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!data.get(vwHolder.getAdapterPosition()).isFavorite()) {
                    data.get(vwHolder.getAdapterPosition()).setFavorite(true);
                    FragmentContact.sm.sendData(data.get(vwHolder.getAdapterPosition()).getName(),data.get(vwHolder.getAdapterPosition()).getNumber(),0);
                    favoriteButton.setImageResource(R.drawable.ic_favorite_red);
                    Log.d("Tamaño", "prueba");
                } else {
                    data.get(vwHolder.getAdapterPosition()).setFavorite(false);
                    FragmentContact.sm.sendData(data.get(vwHolder.getAdapterPosition()).getName(),data.get(vwHolder.getAdapterPosition()).getNumber(),1);
                    favoriteButton.setImageResource(R.drawable.ic_favorite);
                }
            }
        });

        return vwHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(data.get(position).getName());
        holder.phoneNumber.setText(data.get(position).getNumber());
        holder.image.setImageBitmap(data.get(position).getBitmap());

        /*holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (data.get(position).isFavorite()) {
                    holder.favorite.setImageResource(R.drawable.ic_favorite_red);
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_favorite);
                }
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_contact;
        private TextView name;
        private TextView phoneNumber;
        private ImageView image;
        private ImageView favorite;

        public MyViewHolder (View itemView){
            super(itemView);
            item_contact = itemView.findViewById(R.id.element);
            name = itemView.findViewById(R.id.nameContact);
            phoneNumber = itemView.findViewById(R.id.phoneContact);
            image = itemView.findViewById(R.id.contactImage);
            favorite = itemView.findViewById(R.id.favoriteAction);
        }


    }
}
