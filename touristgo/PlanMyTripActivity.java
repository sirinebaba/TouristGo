package com.example.user.touristgo;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlanMyTripActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener
        , GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    List<String> list = new ArrayList<String>();

   // Button btn_save;

    DatabaseReference databaseUsers;
    DatabaseReference databaseFavorites;
    public FirebaseDatabase appDatabase;
    public DatabaseReference ref;


    public DatabaseReference usersRef;
    private Marker myMarker;
    private Marker myMarker1;
    private Marker myMarker2;
    private Marker myMarker3;
    private Marker myMarker4;
    private Marker myMarker5;
    List<Marker> markers = new ArrayList<Marker>();
    List<Boolean> isCliked = new ArrayList<Boolean>();
    List<String> description = new ArrayList<String>();
    List<Double> longs = new ArrayList<>();
    List<Double> lats = new ArrayList<>();
    List<String> descs = new ArrayList<>();
    private boolean clickMarker1 = false;
    private boolean clickMarker2 = false;
    private long startTime = 10000;
    private long difference = 10000;

    public final static String TAG = MainActivity.class.getSimpleName();

    private Button homeButton;
    private Button searchButton;
    private Button carButton;
    private Button profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "I am here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_my_trip);

        databaseUsers = FirebaseDatabase.getInstance().getReference("plans");
        databaseFavorites = FirebaseDatabase.getInstance().getReference("favorites");

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Initializing
        markerPoints = new ArrayList<>();
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


                }


            }



            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.print("The read error" + firebaseError.getMessage());

            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Getting reference to Button
        Button btnDraw = (Button)findViewById(R.id.btn_draw);
      //  btn_save = (Button) findViewById(R.id.btn_save);

    //    btn_save.setOnClickListener(new View.OnClickListener() {
     //       @Override
      //      public void onClick(View v) {
       //         addPlan();
        //    }
        // });

        clickableButtons();
        carButton = (Button) findViewById(R.id.carBtn);
        carButton.setBackgroundResource(R.drawable.carclicked);



    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        Button btnDraw = (Button)findViewById(R.id.btn_draw);
        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

        double p1lat = 33.902336404480685;
        double p1Long = 35.4982852935791;
        LatLng place1 = new LatLng(p1lat, p1Long);
        //mMap.addMarker(new MarkerOptions().position(place1).title("zaytounabay"));
        myMarker = googleMap.addMarker(new MarkerOptions()
                .position(place1)
                .title("My Spot")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        for(int i = 0;i<longs.size();i++){
            LatLng place = new LatLng(lats.get(i),longs.get(i));
            Marker m = googleMap.addMarker(new MarkerOptions()
            .position(place)
            .title(descs.get(i))
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            markers.add(m);
            isCliked.add(false);
            markerPoints.add(place);
        }
        markers.add(myMarker);
      //  markerPoints.add(place1);
        isCliked.add(false);
      //  description.add("My Spot");


        double p2lat = 34.25267611710151;
        double p2Long = 36.0186767578125;
        LatLng place2 = new LatLng(p2lat, p2Long);
        /*myMarker1 = googleMap.addMarker(new MarkerOptions()
                .position(place2)
                .title("Bsharri")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        markers.add(myMarker1);
      //  markerPoints.add(place2);
        isCliked.add(false);
        //description.add("Bsharri");

        double p3lat = 33.81680727566873;
        double p3Long = 35.84495544433594;
        LatLng place3 = new LatLng(p3lat, p3Long);
        myMarker2 = googleMap.addMarker(new MarkerOptions()
                .position(place3)
                .title("Shtoura")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        markers.add(myMarker2);
     //   markerPoints.add(place3);
        isCliked.add(false);
       // description.add("Shtoura");

        double p4lat = 33.80882018730745;
        double p4Long = 35.76805114746094;
        final LatLng place4 = new LatLng(p4lat, p4Long);
        myMarker3 = googleMap.addMarker(new MarkerOptions()
                .position(place4)
                .title("dahrelbaydar")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        markers.add(myMarker3);
      //  markerPoints.add(place4);
        isCliked.add(false);
     //   description.add("dahrelbaydar");

        double p5lat = 34.01225777378286;
        double p5Long = 35.824012756347656;
        final LatLng place5 = new LatLng(p5lat, p5Long);
        myMarker4 = googleMap.addMarker(new MarkerOptions()
                .position(place5)
                .title("faraya")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        markers.add(myMarker4);
        //  markerPoints.add(place4);
        isCliked.add(false);
        //   description.add("dahrelbaydar");

    /*    double p6lat = 35.4876;
        double p6Long = 33.8966;
        final LatLng place6 = new LatLng(p6lat, p6Long);
        myMarker5 = googleMap.addMarker(new MarkerOptions()
                .position(place6)
                .title("AUB")
                .snippet("This is my spot!")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));*/
        /*markers.add(myMarker5);

        isCliked.add(false);*/




        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already 10 locations with 8 waypoints and 1 start location and 1 end location.
                // Upto 8 waypoints are allowed in a query for non-business users
                if(markerPoints.size()>=10){
                    return;
                }

                // Adding new item to the ArrayList
                markerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED and
                 * for the rest of markers, the color is AZURE
                 */
                if(markerPoints.size()==1){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else if(markerPoints.size()==2){
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }else{
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                }

                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

            }
        });



        // The map will be cleared on long click
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                // Removes all the points from Google Map

                mMap.clear();

                // Removes all the points in the ArrayList
                markerPoints.clear();
            }
        });

        // Click event handler for Button btn_draw
        btnDraw.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Checks, whether start and end locations are captured

                if(markerPoints.size() >= 2){
                    LatLng origin = markerPoints.get(0);
                    LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
                addPlan();
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

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = (LatLng) markerPoints.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        markerPoints.add(latLng);
        list.add("Current position");
        description.add("Current position");

        double x1 = 100;
        double x2 = 100;

        double y1 = Double.parseDouble(df2.format(Math.round(location.getLatitude())));
        double y2 = Double.parseDouble(df2.format(Math.round(location.getLongitude())));

        for(int i = 0; i < markers.size(); i++) {
            x1 = Double.parseDouble(df2.format(Math.round(markers.get(i).getPosition().latitude)));
            x2 = Double.parseDouble(df2.format(Math.round(markers.get(i).getPosition().longitude)));
            if(x1 == y1 && x2 == y2) {
                markers.get(i).setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
               // Toast.makeText(this,"Succesfully arrived", Toast.LENGTH_LONG).show();
            }
        }

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
      /*  if(!clickMarker1) {
            startTime = System.currentTimeMillis();
            Toast.makeText(this,"Click", Toast.LENGTH_LONG).show();
            clickMarker1 = true;
        }
        else if(clickMarker1) {
            difference = (System.currentTimeMillis() - startTime)/1000;
            //Toast.makeText(this,"d"+difference, Toast.LENGTH_LONG).show();
            if(difference <= 2) {
                //Toast.makeText(this,"doubleClick", Toast.LENGTH_LONG).show();
                marker.showInfoWindow();
                clickMarker1 = false;
            }
            clickMarker1 = false;

        }*/

        for(int i = 0; i < markers.size(); i++) {
            if (marker.equals(markers.get(i))) {
               // Toast.makeText(this,"DONE", Toast.LENGTH_LONG).show();
                if(!isCliked.get(i)) {
                    marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    isCliked.set(i, true);
                    description.add(marker.getTitle());
                    markerPoints.add(marker.getPosition());
                }
                else if(isCliked.get(i)) {
                    marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    isCliked.set(i, false);
                    description.remove(marker.getTitle());
                    markerPoints.remove(marker.getPosition());
                }

            }
        }

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
       // Toast.makeText(this, "Info window clicked",
         //       Toast.LENGTH_SHORT).show();

        String id = databaseFavorites.push().getKey();
        FavoritePlaces favoritePlaces = new FavoritePlaces(id,"KfVZG7SqGkIIpnOVjF9",marker.getTitle(), marker.getPosition().latitude, marker.getPosition().longitude);
        databaseFavorites.child(id).setValue(favoritePlaces);

      //  Plan plan = new Plan(id, "-KfVZG7SqGkIIpnOVjF9", 1, 1, "t", 1, 1, "t", 1, 1, "t", 1, 1, "t", 1, 1, "t", 1, 1, "t");
       // databaseUsers.child(id).setValue(plan);


    }


    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DataParser parser = new DataParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }

    /*  @Override
      public boolean onCreateOptionsMenu(Menu menu) {
          // Inflate the menu; this adds items to the action bar if it is present.
          getMenuInflater().inflate(R.menu.main, menu);
          return true;
      }*/
    public void getCurrentLocation() {
        mMap.clear();
        double longitude = 100;
        double latitude;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(location !=null){
            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }
        Toast.makeText(this,"longitude:"+longitude, Toast.LENGTH_LONG).show();
    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void addPlan() {
        LatLng point  = (LatLng) markerPoints.get(1);
        //Toast.makeText(this,"p"+point.latitude, Toast.LENGTH_LONG).show();

        if(markerPoints.size() < 3) {
            Toast.makeText(this,"You should select at least 2 places", Toast.LENGTH_LONG).show();
        }

        if(markerPoints.size() == 3) {
            Toast.makeText(this,"p", Toast.LENGTH_LONG).show();
            String id = databaseUsers.push().getKey();
            LatLng point0  = (LatLng) markerPoints.get(0);
            LatLng point1  = (LatLng) markerPoints.get(1);
            LatLng point2  = (LatLng) markerPoints.get(2);
            Plan plan = new Plan(id, "-KfVZG7SqGkIIpnOVjF9", point0.latitude, point0.longitude, list.get(0), point1.latitude, point1.longitude, description.get(1), point2.latitude, point2.longitude, description.get(2), 0, 0, "", 0, 0, "", 0, 0, "");
            databaseUsers.child(id).setValue(plan);
        }

        else if(markerPoints.size() == 4) {
            Toast.makeText(this,"p", Toast.LENGTH_LONG).show();
            String id = databaseUsers.push().getKey();
            LatLng point0  = (LatLng) markerPoints.get(0);
            LatLng point1  = (LatLng) markerPoints.get(1);
            LatLng point2  = (LatLng) markerPoints.get(2);
            LatLng point3  = (LatLng) markerPoints.get(3);
            Plan plan = new Plan(id, "-KfVZG7SqGkIIpnOVjF9", point0.latitude, point0.longitude, list.get(0), point1.latitude, point1.longitude, description.get(1), point2.latitude, point2.longitude, description.get(2), point3.latitude, point3.longitude, description.get(3), 0, 0, "", 0, 0, "");
            databaseUsers.child(id).setValue(plan);
        }

        else if(markerPoints.size() == 5) {
            Toast.makeText(this,"p", Toast.LENGTH_LONG).show();
            String id = databaseUsers.push().getKey();
            LatLng point0  = (LatLng) markerPoints.get(0);
            LatLng point1  = (LatLng) markerPoints.get(1);
            LatLng point2  = (LatLng) markerPoints.get(2);
            LatLng point3  = (LatLng) markerPoints.get(3);
            LatLng point4  = (LatLng) markerPoints.get(4);
            Plan plan = new Plan(id, "-KfVZG7SqGkIIpnOVjF9", point0.latitude, point0.longitude, list.get(0), point1.latitude, point1.longitude, description.get(1), point2.latitude, point2.longitude, description.get(2), point3.latitude, point3.longitude, description.get(3), point4.latitude, point4.longitude, description.get(4), 0, 0, "");
            databaseUsers.child(id).setValue(plan);
        }

        else if(markerPoints.size() == 6) {
            Toast.makeText(this,"p", Toast.LENGTH_LONG).show();
            String id = databaseUsers.push().getKey();
            LatLng point0  = (LatLng) markerPoints.get(0);
            LatLng point1  = (LatLng) markerPoints.get(1);
            LatLng point2  = (LatLng) markerPoints.get(2);
            LatLng point3  = (LatLng) markerPoints.get(3);
            LatLng point4  = (LatLng) markerPoints.get(4);
            LatLng point5  = (LatLng) markerPoints.get(5);
            Plan plan = new Plan(id, "-KfVZG7SqGkIIpnOVjF9", point0.latitude, point0.longitude, list.get(0), point1.latitude, point1.longitude, description.get(1), point2.latitude, point2.longitude, description.get(2), point3.latitude, point3.longitude, description.get(3), point4.latitude, point4.longitude, description.get(4), point5.latitude, point5.longitude, description.get(5));
            databaseUsers.child(id).setValue(plan);
        }
        else {
            Toast.makeText(this,"Too many places selected", Toast.LENGTH_LONG).show();
        }

    }
    public void toCamera(View view){
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, CameraActivity.class);
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


}


