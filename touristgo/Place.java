package com.example.user.touristgo;

import android.app.ProgressDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by student on 3/2/2017.
 */

public  class Place {
    public double longitude;
    public double latitude;
    public String description;
    public String img_URL;
    public String name;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener auth;
    private ProgressDialog progress;
    public FirebaseDatabase appDatabase;
    public DatabaseReference ref;

    public DatabaseReference usersRef;


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }
    public String getName(){  return name; }
    public String getImg_URL(){
        return img_URL;
    }

    public Place(double longitude, double latitude, String name,String description, String img_URL){
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.img_URL = img_URL;
        this.name = name;

    }
    public Place(){

    }
    public void addPlace(){
        firebaseAuth = FirebaseAuth.getInstance();
        appDatabase = FirebaseDatabase.getInstance();
        ref = appDatabase.getReference();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("places")){
                    usersRef = ref.child("places");
                    usersRef.push().setValue(new Place(longitude, latitude,name,description,img_URL));



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public void setName(String name){
        this.name = name;
    }
}
