package com.example.user.touristgo;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

//import com.firebase.client.FirebaseError;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeSearchActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnInfoWindowClickListener,
        View.OnClickListener {

    private GoogleMap mMap;
    private double longitude;
    private double latitude;






    private GoogleApiClient googleApiClient;
    private List<Place> list = new ArrayList<>();
    public List<Double> longs = new ArrayList<>();
    public List<Double> lats = new ArrayList<>();
    public List<String> descs = new ArrayList<>();
    public List<String> urls = new ArrayList<>();
    public List<String> names = new ArrayList<>();
    private ArrayAdapter<String> adapter ;
    public FirebaseDatabase appDatabase;
    public DatabaseReference ref;
    int j = 0;
    int k = 0;

    public DatabaseReference usersRef;
    SupportMapFragment mapFragment;
    ImageButton searchBtn;
    EditText searchPlace;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_search);
        searchBtn = (ImageButton)findViewById(R.id.searchBtn);

        adapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,descs);


        searchPlace = (EditText)findViewById(R.id.searchPlace);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //mapFragment.getMapAsync(this);


        appDatabase = FirebaseDatabase.getInstance();
        ref = appDatabase.getReference();
        usersRef = ref.child("places");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                ;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Place p = postSnapshot.getValue(Place.class);
                    longs.add((Double)postSnapshot.child("longitude").getValue());
                    lats.add((Double)postSnapshot.child("latitude").getValue());
                    descs.add((String)postSnapshot.child("name").getValue());
                    names.add((String)postSnapshot.child("description").getValue());
                    urls.add((String) postSnapshot.child("img_URL").getValue());

                }
                System.out.println(longs.size());
                for(int i = 0;i<descs.size();i++){
                    System.out.println(descs.get(i));
                }
                System.out.print(urls.get(0));

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.print("The read error" + firebaseError.getMessage());

            }
        });


        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();





    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
        ;
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(googleApiClient, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(googleApiClient, getIndexApiAction());
    }

    private void getCurrentLocation() {
        mMap.clear();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location !=null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            moveMap();
        }
    }

    private void moveMap() {


        LatLng latLng = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .title("Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        for (int i = 0; i < longs.size(); i++) {
            LatLng latLng1 = new LatLng(lats.get(i), longs.get(i));
            /*mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {


                    View v = getLayoutInflater().inflate(R.layout.info_window, null);
                    TextView title = (TextView)v.findViewById(R.id.title);
                    title.setText(descs.get(0));
                    String s = "C:\\Users\\student\\AndroidStudioProjects\\touristGo\\app\\src\\m\\ain\\res\\drawable\\manara";
                    ImageView img = (ImageView) v.findViewById(R.id.imageView1);
                    Picasso.with(getApplicationContext()).load("http://souar.com//data//media//3//lebanon_oct_2010%20(45).jpg").into(img);



                    return v;


                }



            });*/



            mMap.addMarker(new MarkerOptions()
                    .position(latLng1)
                    .draggable(true)
                    .title(descs.get(i)))
                    .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            j = j+1;









        }
        searchPlace.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    findPlace();
                    return false;


                }
                return true;
            }
        });




        // add markers for locations
        /*LatLng latLng1 = new LatLng(33.88716123809009, 35.47262191772461);
        mMap.addMarker(new MarkerOptions()
                .position(latLng1)
                .draggable(true)
                .title("Rawche")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        //
        LatLng latLng2 = new LatLng(33.903048787241495, 35.49837112426758);
        mMap.addMarker(new MarkerOptions()
                .position(latLng2)
                .draggable(true)
                .title("Zaitunay Bay")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        //
        LatLng latLng3 = new LatLng(33.89685085784885, 35.50450801849365);
        mMap.addMarker(new MarkerOptions()
                .position(latLng3)
                .draggable(true)
                .title("DownTown")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));*/

    }



    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;

        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        for(int i = 0; i<longs.size();i++){
            final String s = descs.get(i);
            LatLng latLng1 = new LatLng(lats.get(i), longs.get(i));


           /* mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);
                    TextView title = (TextView) v.findViewById(R.id.title);
                    title.setText(descs.get(0));
                    ImageView img = (ImageView) v.findViewById(R.id.imageView1);
                    Picasso.with(getApplicationContext()).load(urls.get(0)).placeholder(R.layout.info_window).into(img);

                    return v;


                }


            });*/
            mMap.addMarker(new MarkerOptions().title(descs.get(i))
                    .position(latLng1)
                    .draggable(true)).showInfoWindow();
            mMap.setOnMarkerDragListener(this);
            mMap.setOnInfoWindowClickListener(this);
            mMap.setOnMapLongClickListener(this);
            k++;



        }
        searchPlace.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    findPlace();
                    return false;


                }
                return true;
            }
        });
    }
    public void findPlace(){
        AutoCompleteTextView acTextView = (AutoCompleteTextView)findViewById(R.id.searchPlace);
        acTextView.setThreshold(3);
        acTextView.setAdapter(adapter);
        for(int i = 0;i<descs.size();i++){
            //mMap.clear();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            /*Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if(location !=null){*/
            if(!acTextView.getText().toString().equals("")&&acTextView.getText().toString().equalsIgnoreCase(descs.get(i))) {
                longitude = longs.get(i);
                latitude = lats.get(i);

                moveMap();
                //}
            }
        }
    }
    //////////////////////////////
        /*LatLng latLng1 = new LatLng(33.88716123809009,35.47262191772461);
        mMap.addMarker(new MarkerOptions()
                .position(latLng1)
                .draggable(true)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

        //
        LatLng latLng2 = new LatLng(33.903048787241495,35.49837112426758);
        mMap.addMarker(new MarkerOptions()
                .position(latLng2)
                .draggable(true)
                .title("Zaitunay Bay")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        //
        LatLng latLng3 = new LatLng(33.89685085784885,35.50450801849365);
        mMap.addMarker(new MarkerOptions()
                .position(latLng3)
                .draggable(true)
                .title("DownTown")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);*/


    @Override
    public void onConnected(Bundle bundle){
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i){

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult){

    }

    @Override
    public void onMapLongClick(LatLng latLng){

    }

    @Override
    public void onMarkerDragStart(Marker marker){

    }

    @Override
    public void onMarkerDrag(Marker marker){

    }

    @Override
    public void onMarkerDragEnd(Marker marker){
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        moveMap();
    }



    // move to activties
    public void toSuggestedTrips(View view) {
        Intent intent = new Intent(this, SuggestedTripsActivity.class);
        startActivity(intent);
    }

    public void toHome(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void toProfile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Home Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Click Info Window", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onClick(View v) {

    }
}