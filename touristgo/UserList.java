package com.example.user.touristgo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sirinebaba on 3/17/17.
 */

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public  UserList(Activity context, List<User> userList) {
        super(context, R.layout.list_layout1,userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout1, null, true);

        TextView textViewEmail = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView textViewPassword = (TextView) listViewItem.findViewById(R.id.textViewPassword);

        User user = userList.get(position);

        textViewEmail.setText(user.getEmail());
        textViewPassword.setText(user.getPassword());

        return listViewItem;
    }
}
