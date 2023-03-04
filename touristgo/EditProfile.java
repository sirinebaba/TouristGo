package com.example.user.touristgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    EditText fNameInput;
    EditText lNameInput;
    EditText uNameInput;
    EditText emailInput;

    Button SaveBtn;

    DatabaseReference databaseUsers;
  //  ListView listViewUsers;
    List<User> userInfo;

    private Button homeButton;
    private Button searchButton;
    private Button carButton;
    private Button profileButton;

    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "I am here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fNameInput = (EditText) findViewById(R.id.fNameInput);
        lNameInput = (EditText) findViewById(R.id.lNameInput);
        uNameInput = (EditText) findViewById(R.id.uNameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);

        SaveBtn = (Button) findViewById(R.id.SaveBtn);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
     //   listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        userInfo = new ArrayList<>();

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser();
            }
        });

        clickableButtons();
        profileButton = (Button) findViewById(R.id.profileBtn);
        profileButton.setBackgroundResource(R.drawable.profileclicked);



    }

    @Override
    public void onBackPressed() {

    }

    protected void onStart() {
        super.onStart();


        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userInfo.clear();
                Intent myIntent = getIntent();
                String idString = myIntent.getStringExtra("id");

                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                        if(user.getUserId().equals(idString)) {
                            userInfo.add(user);
                            fNameInput.setText(user.getFirstName());
                            lNameInput.setText(user.getLastName());
                            uNameInput.setText(user.getUsername());
                            emailInput.setText(user.getEmail());

                        }


                    }
             //   UserList adapter = new UserList(EditProfile.this, userInfo);
              //  listViewUsers.setAdapter(adapter);

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
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

    public void toSettings(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
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

     public void toHome(View view) {
         Bundle b = getIntent().getExtras();
         String idString = b.getString("id");
         Intent intent = new Intent(this, MapsActivity.class);
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

    public void toChangePass(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, ChangePass.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }
    private void updateUser() {
        Intent myIntent = getIntent();
        String idString = myIntent.getStringExtra("id");
        String firstName = fNameInput.getText().toString().trim();
        String lastName = lNameInput.getText().toString().trim();
        String username = uNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        databaseUsers.child(idString).child("firstName").setValue(firstName);
        databaseUsers.child(idString).child("lastName").setValue(lastName);
        databaseUsers.child(idString).child("username").setValue(username);
        databaseUsers.child(idString).child("email").setValue(email);
        myIntent = new Intent(this, ProfileActivity.class);
        startActivity(myIntent);
    }
}
