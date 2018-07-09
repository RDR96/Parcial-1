package com.rdr.rodrigocorvera.parcial1.Adaptadores;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rdr.rodrigocorvera.parcial1.Actividades.ContactInfoActivity;
import com.rdr.rodrigocorvera.parcial1.Clases.Contact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentContact;
import com.rdr.rodrigocorvera.parcial1.Fragmentos.FragmentFavorite;
import com.rdr.rodrigocorvera.parcial1.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Rodrigo Corvera on 2/5/2018.
 */

public class RecyclerViewAdapterFavorite extends RecyclerView.Adapter<RecyclerViewAdapterFavorite.MyViewHolder>{

    Context context;
    public static List<Contact> data;
    public RecyclerViewAdapterFavorite(Context context, List<Contact> data) {
        this.context = context;
        this.data = data;
        //this.data = data;
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
                Intent newIntent = new Intent(context.getApplicationContext(), ContactInfoActivity.class);
                newIntent.setAction(Intent.ACTION_SEND);
                newIntent.setType("text/plain");

                if (data.get(vwHolder.getAdapterPosition()).getBitmap() != null) {
                    newIntent.putExtra(Intent.EXTRA_TEXT, data.get(vwHolder.getAdapterPosition()).getName()
                            + "'" + data.get(vwHolder.getAdapterPosition()).getNumber() + "'" + data.get(vwHolder.getAdapterPosition()).getOriginalPosition() + "'" + 1 + "'" + data.get(vwHolder.getAdapterPosition()).getFavoritePosition());
                }else {
                    newIntent.putExtra(Intent.EXTRA_TEXT, data.get(vwHolder.getAdapterPosition()).getName()
                            + "'" + data.get(vwHolder.getAdapterPosition()).getNumber() + "'" + data.get(vwHolder.getAdapterPosition()).getOriginalPosition() + "'" + 1 + "'" + data.get(vwHolder.getAdapterPosition()).getFavoritePosition());
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
                //FragmentContact.sm.sendData(data.get(vwHolder.getAdapterPosition()).getName(),data.get(vwHolder.getAdapterPosition()).getNumbers().get(0),1);
                //favoriteButton.setImageResource(R.drawable.ic_favorite_red);
                FragmentFavorite.removeItem(vwHolder.getAdapterPosition(),context);

            }
        });


        return vwHolder;
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(data.get(position).getName());
        holder.phoneNumber.setText(data.get(position).getNumbers().get(0));

        if (data.get(position).getBitmap() !=  null) {
            holder.image.setImageBitmap(bitmapRotation(data.get(position).getBitmap()));
        }

        holder.favorite.setImageResource(R.drawable.ic_favorite_red);
        //holder.name.setText(data.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout item_contact;
        public TextView name;
        public TextView phoneNumber;
        public ImageView image;
        public ImageView favorite;


        public MyViewHolder (View itemView){
            super(itemView);
            item_contact = itemView.findViewById(R.id.element);
            name = itemView.findViewById(R.id.nameContact);
            phoneNumber = itemView.findViewById(R.id.phoneContact);
            image = itemView.findViewById(R.id.contactImage);
            favorite = itemView.findViewById(R.id.favoriteAction);

        }



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