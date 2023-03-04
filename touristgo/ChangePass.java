package com.example.user.touristgo;

import android.content.Intent;
import android.icu.text.IDNA;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ChangePass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
    }

    public void toInfo(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }
    public void toSettings(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, SettingsActivity.class);
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
    public void toProfile(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }
}
