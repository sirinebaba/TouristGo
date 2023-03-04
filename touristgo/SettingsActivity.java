package com.example.user.touristgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    private Button homeButton;
    private Button searchButton;
    private Button carButton;
    private Button profileButton;

    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        clickableButtons();

    }

    @Override
    public void onBackPressed() {

    }

    public void clickableButtons(){

        homeButton = (Button) findViewById(R.id.homeBtn);
        searchButton = (Button) findViewById(R.id.searchBtn);
        carButton = (Button) findViewById(R.id.carBtn);
        profileButton = (Button) findViewById(R.id.profileBtn);




        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                homeButton.setBackgroundResource(R.drawable.homeclicked);
                searchButton.setBackgroundResource(R.drawable.search);
                carButton.setBackgroundResource(R.drawable.car);
                profileButton.setBackgroundResource(R.drawable.profile);
                toHome(v);

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                homeButton.setBackgroundResource(R.drawable.home);
                searchButton.setBackgroundResource(R.drawable.searchclicked);
                carButton.setBackgroundResource(R.drawable.car);
                profileButton.setBackgroundResource(R.drawable.profile);
                toSearchHome(v);

            }
        });
        carButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                homeButton.setBackgroundResource(R.drawable.home);
                searchButton.setBackgroundResource(R.drawable.search);
                carButton.setBackgroundResource(R.drawable.carclicked);
                profileButton.setBackgroundResource(R.drawable.profile);
                toSuggestedTrips(v);


            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                homeButton.setBackgroundResource(R.drawable.home);
                searchButton.setBackgroundResource(R.drawable.search);
                carButton.setBackgroundResource(R.drawable.car);
                profileButton.setBackgroundResource(R.drawable.profileclicked);
                toProfile(v);
            }
        });
    }


    public void toHelp(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }

    // move to activties
    public void toSuggestedTrips(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, SuggestedTripsActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }


    public void toSearchHome(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, HomeSearchActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }

    public void toProfile(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }




    public void toHome(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }

    public void toSignIn(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }
}
