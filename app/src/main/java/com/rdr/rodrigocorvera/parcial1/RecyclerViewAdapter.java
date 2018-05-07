package com.rdr.rodrigocorvera.parcial1;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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

import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context context;
    
    public RecyclerViewAdapter(Context context, List<Contact> data) {
        this.context = context;
        //Log.d("data", data.get(0).getNumber());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
       final MyViewHolder vwHolder = new MyViewHolder(v);
        final LinearLayout callingButton = v.findViewById(R.id.callAction);
        final LinearLayout favoriteButton = v.findViewById(R.id.favoriteAction);
        final LinearLayout container = v.findViewById(R.id.element);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Nombre: " + MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getName(),Toast.LENGTH_SHORT).show();
                Intent newIntent = new Intent(context.getApplicationContext(), contactInfoActivity.class);
                newIntent.setAction(Intent.ACTION_SEND);
                newIntent.setType("text/plain");
                newIntent.putExtra(Intent.EXTRA_TEXT, MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getName()
                + "'" + MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getNumber() + "'" + MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getOriginalPosition());
                context.startActivity(newIntent);
            }
        });

        callingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getNumber()));
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

                if (!MainActivity.lstContact.get(vwHolder.getAdapterPosition()).isFavorite()) {
                    MainActivity.lstContact.get(vwHolder.getAdapterPosition()).setFavorite(true);
                    FragmentContact.sm.sendData(MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getName(),MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getNumber(),0);
                    ImageView imae = (ImageView) favoriteButton.getChildAt(0);
                    imae.setImageResource(R.drawable.ic_favorite_red);

                } else {
                    MainActivity.lstContact.get(vwHolder.getAdapterPosition()).setFavorite(false);
                    FragmentContact.sm.sendData(MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getName(),MainActivity.lstContact.get(vwHolder.getAdapterPosition()).getNumber(),1);
                    ImageView imae = (ImageView) favoriteButton.getChildAt(0);
                    imae.setImageResource(R.drawable.ic_favorite);
                }



                //favoriteButton.setImageResource(R.drawable.ic_favorite_red);
            }
        });

       /* vwHolder.item_contact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
            }

        });*/

       /* vwHolder.item_contact.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentContact.sm.sendData(data.get(vwHolder.getAdapterPosition()).getName(),data.get(vwHolder.getAdapterPosition()).getNumber());
                return true;
            }

        });*/



        return vwHolder;
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(MainActivity.lstContact.get(position).getName());
        holder.phoneNumber.setText(MainActivity.lstContact.get(position).getNumber());

        //holder.name.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return MainActivity.lstContact.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_contact;
        private TextView name;
        private TextView phoneNumber;
        private ImageView image;


        public MyViewHolder (View itemView){
            super(itemView);
            item_contact = itemView.findViewById(R.id.element);
            name = itemView.findViewById(R.id.nameContact);
            phoneNumber = itemView.findViewById(R.id.phoneContact);
            image = itemView.findViewById(R.id.contactImage);
        }


    }
}
