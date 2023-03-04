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

public class MyPlansActivity extends ArrayAdapter<Plan> {
    private Activity context;
    private List<Plan> planList;

    public final static String TAG = MainActivity.class.getSimpleName();

    public  MyPlansActivity(Activity context, List<Plan> planList) {
        super(context, R.layout.activity_my_plans,planList);
        this.context = context;
        this.planList = planList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.activity_my_plans, null, true);

        TextView title = (TextView) listViewItem.findViewById(R.id.Title);
        TextView Place1 = (TextView) listViewItem.findViewById(R.id.Place1);
        TextView Place2 = (TextView) listViewItem.findViewById(R.id.Place2);
        TextView Place3 = (TextView) listViewItem.findViewById(R.id.Place3);
        TextView Place4 = (TextView) listViewItem.findViewById(R.id.Place4);
        TextView Place5 = (TextView) listViewItem.findViewById(R.id.Place5);
        TextView Place6 = (TextView) listViewItem.findViewById(R.id.Place6);

        Plan plan = planList.get(position);



        title.setText("Title");
        Place1.setText(plan.getPlace1Description());
        Place2.setText(plan.getPlace2Description());
        Place3.setText(plan.getPlace3Description());
        if(!plan.getPlace4Description().equals("")) {
            Place4.setText(plan.getPlace4Description());
        }
        else {
            Place4.setVisibility(View.GONE);
        }
        if(!plan.getPlace5Description().equals("")) {
            Place5.setText(plan.getPlace5Description());
        }
        else {
            Place5.setVisibility(View.GONE);
        }
        if(!plan.getPlace6Description().equals("")) {
            Place6.setText(plan.getPlace6Description());
        }
        else {
            Place6.setVisibility(View.GONE);
        }

        return listViewItem;
    }
}

