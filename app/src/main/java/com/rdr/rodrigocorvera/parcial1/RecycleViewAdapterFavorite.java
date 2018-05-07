package com.rdr.rodrigocorvera.parcial1;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

class RecyclerViewAdapterFavorite extends RecyclerView.Adapter<RecyclerViewAdapterFavorite.MyViewHolder>{

    Context context;
    //public static List<Contact> data;
    public RecyclerViewAdapterFavorite(Context context, List<Contact> data) {
        this.context = context;
        //this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        final MyViewHolder vwHolder = new MyViewHolder(v);
        final LinearLayout callingButton = v.findViewById(R.id.callAction);
        final LinearLayout favoriteButton = v.findViewById(R.id.favoriteAction);


        callingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + FragmentFavorite.favoriteData.get(vwHolder.getAdapterPosition()).getNumber()));
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
                //FragmentContact.sm.sendData(data.get(vwHolder.getAdapterPosition()).getName(),data.get(vwHolder.getAdapterPosition()).getNumber());
                //favoriteButton.setImageResource(R.drawable.ic_favorite_red);

                FragmentFavorite.removeItem(vwHolder.getAdapterPosition(),context);
            }
        });


        return vwHolder;
    }




    @Override
    public void onBindViewHolder(RecyclerViewAdapterFavorite.MyViewHolder holder, int position) {
        holder.name.setText(FragmentFavorite.favoriteData.get(position).getName());
        holder.phoneNumber.setText(FragmentFavorite.favoriteData.get(position).getNumber());
        holder.favoriteIcon.setImageResource(R.drawable.ic_favorite_red);
        //holder.name.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return FragmentFavorite.favoriteData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout item_contact;
        public TextView name;
        public TextView phoneNumber;
        public ImageView image;
        public LinearLayout favorite;
        public ImageView favoriteIcon;

        public MyViewHolder (View itemView){
            super(itemView);
            item_contact = itemView.findViewById(R.id.element);
            name = itemView.findViewById(R.id.nameContact);
            phoneNumber = itemView.findViewById(R.id.phoneContact);
            image = itemView.findViewById(R.id.contactImage);
            favorite = itemView.findViewById(R.id.favoriteAction);
            favoriteIcon = (ImageView) favorite.getChildAt(0);

        }



    }


}
