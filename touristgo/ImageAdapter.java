package com.example.user.touristgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.user.touristgo.R.drawable.list;

public class ImageAdapter extends BaseAdapter  {
    private Context mContext;
    private ArrayList<String> mThumbIds ;
    public FirebaseDatabase appDatabase;
    public DatabaseReference ref;
    public DatabaseReference usersRef;


    public ImageAdapter(Context c,ArrayList<String> mThumbIds) {
        mContext = c;
        this.mThumbIds = mThumbIds;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public String getItem(int position) {
        return mThumbIds.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(7, 7, 7, 7);
        } else {
            imageView = (ImageView) convertView;
        }
        String url = getItem(position);
        Glide.with(mContext).load(url).into(imageView);


        //imageView.setImageURI(Uri.parse(mThumbIds.get(position)));


        return imageView;
    }

    // references to our images

}