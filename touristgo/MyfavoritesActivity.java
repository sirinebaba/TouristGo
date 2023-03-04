package com.example.user.touristgo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by sirinebaba on 3/17/17.
 */

public class MyfavoritesActivity extends ArrayAdapter<FavoritePlaces> {
    private Activity context;
    private List<FavoritePlaces> favoriteList;

    public  MyfavoritesActivity(Activity context, List<FavoritePlaces> favoriteList) {
        super(context, R.layout.activity_my_plans,favoriteList);
        this.context = context;
        this.favoriteList = favoriteList;
    }

    public final static String TAG = MainActivity.class.getSimpleName();

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem1 = inflater.inflate(R.layout.activity_myfavorites, null, true);

        TextView favorite = (TextView) listViewItem1.findViewById(R.id.Favorites);

        FavoritePlaces favoritePlaces = favoriteList.get(position);

        favorite.setText(favoritePlaces.getpDesc());


        return listViewItem1;
    }
}

