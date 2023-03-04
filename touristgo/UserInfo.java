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
 * Created by sirinebaba on 3/18/17.
 */

public class UserInfo extends ArrayAdapter<User> {

    private Activity context;
    private List<User> userInfo;

    public  UserInfo(Activity context, List<User> userInfo) {
        super(context, R.layout.list_layout1,userInfo);
        this.context = context;
        this.userInfo = userInfo;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout1, null, true);

        TextView fNameInput = (TextView) listViewItem.findViewById(R.id.textViewEmail);
        TextView lNameInput = (TextView) listViewItem.findViewById(R.id.textViewPassword);

        User user = userInfo.get(position);

        fNameInput.setText(user.getFirstName());
        lNameInput.setText(user.getLastName());

        return listViewItem;
    }
}
