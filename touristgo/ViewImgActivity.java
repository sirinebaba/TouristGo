package com.example.user.touristgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ViewImgActivity extends AppCompatActivity {

    public final static String TAG = MainActivity.class.getSimpleName();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "I am here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_img);
    }

    @Override
    public void onBackPressed() {

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
    public void toSignIn(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, SignInActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }
}
