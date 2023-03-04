package com.example.user.touristgo;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SuggestedTripsListActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener {

    protected Button trip1;
    protected Button trip2;
    protected Button trip3;
    protected ImageButton c1;
    protected ImageButton c2;
    protected ImageButton c3;
    protected TextView t1Place1;
    protected TextView t1Place2;
    protected TextView t1Place3;
    protected TextView t2Place1;
    protected TextView t2Place2;
    protected TextView t2Place3;
    protected TextView t3Place1;
    protected TextView t3Place2;
    protected TextView t3Place3;

   // protected View map1;
   // protected View map2;
  //  protected View map3;

    private Button homeButton;
    private Button searchButton;
    private Button carButton;
    private Button profileButton;

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    protected boolean showHide = false;

    //Button button2;

      List<com.example.user.touristgo.Place> listInfo;
    DatabaseReference databaseUsers;
   // TextView testing;

    double p1Lon = 100;
    double p1lat = 100;
    double p2Lon = 100;
    double p2lat = 100;
    double p3Lon = 100;
    double p3lat = 100;
    String name1 = "";
    String name2 = "";
    String name3 = "";

    double p1dlat = 100;
    double p1dLon = 100;
    double p1hlat = 100;
    double p1hLon = 100;
    double p1llat = 100;
    double p1lLon = 100;
    String name1d = "";
    String name1h = "";
    String name1l = "";

    double minm = 300;
    double minn = 300;
    double mino = 300;
    double p1Lonm = 100;
    double p1latm = 100;
    double p2Lonn = 100;
    double p2latn = 100;
    double p3Lono = 100;
    double p3lato = 100;
    String name1m = "";
    String name2n = "";
    String name3o = "";

    protected boolean map1isOpen = false;
    protected boolean map2isOpen = false;
    protected boolean map3isOpen = false;

    protected double longestD1 = 100;

    public double myLocationLat = 1;

    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "I am here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_trips_list2);
        initializeElements();
        showElemetsOnClcik();

        databaseUsers = FirebaseDatabase.getInstance().getReference("places");
   //     testing = (TextView) findViewById(R.id.testing);
       // button2 = (Button) findViewById(R.id.button2);
        listInfo = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //checkLocationPermission();
        }
        // Initializing
        MarkerPoints = new ArrayList<>();

        clickableButtons();
        carButton = (Button) findViewById(R.id.carBtn);
        carButton.setBackgroundResource(R.drawable.carclicked);



    }

    @Override
    public void onBackPressed() {

    }

    protected void initializeElements(){

        // Arraylist of suggested trips
        trip1 = (Button)findViewById(R.id.trip1Btn);
        trip2 = (Button)findViewById(R.id.trip2Btn);
        trip3 = (Button)findViewById(R.id.trip3Btn);
        trip1.setPaintFlags(trip1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        trip2.setPaintFlags(trip2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        trip3.setPaintFlags(trip3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        c1 = (ImageButton)findViewById(R.id.check1);
        c2 = (ImageButton)findViewById(R.id.check2);
        c3 = (ImageButton)findViewById(R.id.check3);

        t1Place1 = (TextView)findViewById(R.id.t1Place1);
        t1Place2 = (TextView)findViewById(R.id.t1Place2);
        t1Place3 = (TextView)findViewById(R.id.t1Place3);
        t2Place1 = (TextView)findViewById(R.id.t2Place1);
        t2Place2 = (TextView)findViewById(R.id.t2Place2);
        t2Place3 = (TextView)findViewById(R.id.t2Place3);
        t3Place1 = (TextView)findViewById(R.id.t3Place1);
        t3Place2 = (TextView)findViewById(R.id.t3Place2);
        t3Place3 = (TextView)findViewById(R.id.t3Place3);

     //   map1 = findViewById(R.id.map1);
     //   map2 = findViewById(R.id.map2);
      //  map3 = findViewById(R.id.map3);


    }

    protected void showElemetsOnClcik(){
        c1.setVisibility(View.GONE);
        c2.setVisibility(View.GONE);
        c3.setVisibility(View.GONE);

        t1Place1.setVisibility(View.GONE);
        t1Place2.setVisibility(View.GONE);
        t1Place3.setVisibility(View.GONE);
        t2Place1.setVisibility(View.GONE);
        t2Place2.setVisibility(View.GONE);
        t2Place3.setVisibility(View.GONE);
        t3Place1.setVisibility(View.GONE);
        t3Place2.setVisibility(View.GONE);
        t3Place3.setVisibility(View.GONE);

      //  map1.setVisibility(View.GONE);
     //   map2.setVisibility(View.GONE);
      //  map3.setVisibility(View.GONE);


        //Show/hide trip map

        trip1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(showHide == false){ //hide

                    c1.setVisibility(View.GONE);
                   // map1.setVisibility(View.GONE);
                    t1Place1.setVisibility(View.GONE);
                    t1Place2.setVisibility(View.GONE);
                    t1Place3.setVisibility(View.GONE);

                    showHide = true;
                }else{ //show
                    c1.setVisibility(View.VISIBLE);
                  //  map1.setVisibility(View.VISIBLE);
                    t1Place1.setVisibility(View.VISIBLE);
                    t1Place2.setVisibility(View.VISIBLE);
                    t1Place3.setVisibility(View.VISIBLE);

                    c2.setVisibility(View.GONE);
                  //  map2.setVisibility(View.GONE);
                    t2Place1.setVisibility(View.GONE);
                    t2Place2.setVisibility(View.GONE);
                    t2Place3.setVisibility(View.GONE);

                    c3.setVisibility(View.GONE);
               //     map3.setVisibility(View.GONE);
                    t3Place1.setVisibility(View.GONE);
                    t3Place2.setVisibility(View.GONE);
                    t3Place3.setVisibility(View.GONE);

                    showHide = false;
                    showMap1();
                }

            }
        });

        trip2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(showHide == false){ //hide

                    c2.setVisibility(View.GONE);
                   // map2.setVisibility(View.GONE);
                    t2Place1.setVisibility(View.GONE);
                    t2Place2.setVisibility(View.GONE);
                    t2Place3.setVisibility(View.GONE);


                    showHide = true;
                }else{ //show
                    c1.setVisibility(View.GONE);
                  //  map1.setVisibility(View.GONE);
                    t1Place1.setVisibility(View.GONE);
                    t1Place2.setVisibility(View.GONE);
                    t1Place3.setVisibility(View.GONE);

                    c2.setVisibility(View.VISIBLE);
                  //  map2.setVisibility(View.VISIBLE);
                    t2Place1.setVisibility(View.VISIBLE);
                    t2Place2.setVisibility(View.VISIBLE);
                    t2Place3.setVisibility(View.VISIBLE);

                    c3.setVisibility(View.GONE);
                  //  map3.setVisibility(View.GONE);
                    t3Place1.setVisibility(View.GONE);
                    t3Place2.setVisibility(View.GONE);
                    t3Place3.setVisibility(View.GONE);

                    showHide = false;
                    showMap2();
                }

            }
        });

        trip3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(showHide == false){ //hide
                    c3.setVisibility(View.GONE);
                   // map3.setVisibility(View.GONE);
                    t3Place1.setVisibility(View.GONE);
                    t3Place2.setVisibility(View.GONE);
                    t3Place3.setVisibility(View.GONE);

                    showHide = true;
                }else{ //show
                    c1.setVisibility(View.GONE);
                  //  map1.setVisibility(View.GONE);
                    t1Place1.setVisibility(View.GONE);
                    t1Place2.setVisibility(View.GONE);
                    t1Place3.setVisibility(View.GONE);

                    c2.setVisibility(View.GONE);
                  //  map2.setVisibility(View.GONE);
                    t2Place1.setVisibility(View.GONE);
                    t2Place2.setVisibility(View.GONE);
                    t2Place3.setVisibility(View.GONE);

                    c3.setVisibility(View.VISIBLE);
                  //  map3.setVisibility(View.VISIBLE);
                    t3Place1.setVisibility(View.VISIBLE);
                    t3Place2.setVisibility(View.VISIBLE);
                    t3Place3.setVisibility(View.VISIBLE);

                    showHide = false;
                    showMap3();
                }

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


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
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

        if(map1isOpen) {
            mMap.clear();
            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.marker1);
            LatLng latLng1 = new LatLng(p1lat, p1Lon);
            mMap.addMarker(new MarkerOptions().position(latLng1).title(name1).icon(icon1));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));


            LatLng latLng2 = new LatLng(p2lat, p2Lon);
            mMap.addMarker(new MarkerOptions().position(latLng2).title(name2));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));

            LatLng latLng3 = new LatLng(p3lat, p3Lon);
            mMap.addMarker(new MarkerOptions().position(latLng3).title(name3));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng3));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(5));
        }
        if(map2isOpen) {
            mMap.clear();
            LatLng latLng1 = new LatLng(p1dlat, p1dLon);
            mMap.addMarker(new MarkerOptions().position(latLng1).title(name1d));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));

            LatLng latLng2 = new LatLng(p1hlat, p1hLon);
            mMap.addMarker(new MarkerOptions().position(latLng2).title(name1h));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));

            LatLng latLng3 = new LatLng(p1llat, p1lLon);
            mMap.addMarker(new MarkerOptions().position(latLng3).title(name1l));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng3));
        }
        if(map3isOpen) {
            mMap.clear();
            LatLng latLng1 = new LatLng(p1latm, p1Lonm);
            mMap.addMarker(new MarkerOptions().position(latLng1).title(name1m));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));

            LatLng latLng2 = new LatLng(p2latn, p2Lonn);
            mMap.addMarker(new MarkerOptions().position(latLng2).title(name2n));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng2));

            LatLng latLng3 = new LatLng(p3lato, p3Lono);
            mMap.addMarker(new MarkerOptions().position(latLng3).title(name3o));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng3));
        }

        // Setting onclick event listener for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (MarkerPoints.size() > 0) {
                    MarkerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                MarkerPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (MarkerPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (MarkerPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }


                // Add new marker to the Google Map Android API V2
                mMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (MarkerPoints.size() >= 1) {
                    LatLng origin = MarkerPoints.get(0);
                    //  LatLng dest = MarkerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getUrl(origin);
                    Log.d("onMapClick", url.toString());
                    SuggestedTripsListActivity.FetchUrl FetchUrl = new SuggestedTripsListActivity.FetchUrl();

                    // Start downloading json data from Google Directions API
                    FetchUrl.execute(url);
                    //move map camera
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                }

            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        openMap(marker.getPosition().latitude, marker.getPosition().longitude, marker.getTitle());
        return false;
    }

    private String getUrl(LatLng dest) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        // Origin of route
        String str_origin = "origin=" + location.getLatitude() + "," + location.getLongitude();

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            SuggestedTripsListActivity.ParserTask parserTask = new SuggestedTripsListActivity.ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
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

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

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
        myLocationLat = location.getLatitude();
       // Toast.makeText(this,"m"+myLocationLat, Toast.LENGTH_LONG).show();

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    private void getCurrentLocation() {
        //  mMap.clear();
        double longitude = 0;
        double latitude = 0;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }
        Toast.makeText(this,"longitude:"+longitude, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
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
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
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

    protected void onStart() {
        super.onStart();


        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listInfo.clear();
                Intent myIntent = getIntent();
                String idString = myIntent.getStringExtra("id");

                double min1 = 300;
                double min2 = 300;
                double min3 = 300;


                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    com.example.user.touristgo.Place place = userSnapshot.getValue(com.example.user.touristgo.Place.class);
      
                    if(calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude()) < min1 && !place.getName().equals("zaytounabay")) {
                        min1 = calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude() );
                        p1lat = place.getLatitude();
                        p1Lon = place.getLongitude();
                        name1 = place.getName();
                    }


                }
                double d1 = calculteDistance(33.902336404480685, 35.4982852935791, p1lat, p1Lon);
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    com.example.user.touristgo.Place place = userSnapshot.getValue(com.example.user.touristgo.Place.class);
                    if(calculteDistance(p1lat, p1Lon, place.getLatitude(), place.getLongitude()) < min2 && !place.getName().equals(name1) && !place.getName().equals("zaytounabay")) {
                        min2 = calculteDistance(p1lat, p1Lon, place.getLatitude(), place.getLongitude());
                        p2lat = place.getLatitude();
                        p2Lon = place.getLongitude();
                        name2 = place.getName();
                    }

                }
                double d2 = calculteDistance(p1lat, p1Lon, p2lat, p2Lon);
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    com.example.user.touristgo.Place place = userSnapshot.getValue(com.example.user.touristgo.Place.class);
                    if(calculteDistance(p2lat, p2Lon, place.getLatitude(), place.getLongitude()) < min3 && !place.getName().equals(name2) && !place.getName().equals(name1)) {
                        min3 = calculteDistance(p2lat, p2Lon, place.getLatitude(), place.getLongitude());
                        p3lat = place.getLatitude();
                        p3Lon = place.getLongitude();
                        name3 = place.getName();
                    }

                }
                double d3 = calculteDistance(p2lat, p2Lon, p3lat, p3Lon);

                double min1a = 300;
                double min1b = 300;
                double min1c = 300;
                double p1aLon = 100;
                double p1alat = 100;
                double p1bLon = 100;
                double p1blat = 100;
                double p1cLon = 100;
                double p1clat = 100;
                String name1a = "";
                String name1b = "";
                String name1c = "";


                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    com.example.user.touristgo.Place place = userSnapshot.getValue(com.example.user.touristgo.Place.class);

                    if(calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude()) < min1a && !place.getName().equals("zaytounabay") && !place.getName().equals(name1a) && !place.getName().equals(name1b) && !place.getDescription().equals(name1c)) {
                        min1a = calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude() );
                        p1alat = place.getLatitude();
                        p1aLon = place.getLongitude();
                        name1a = place.getName();
                    }
                    else if(calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude()) < min1b && !place.getName().equals("zaytounabay") && !place.getName().equals(name1a) && !place.getName().equals(name1b) && !place.getName().equals(name1c)) {
                        min1b = calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude() );
                        p1blat = place.getLatitude();
                        p1bLon = place.getLongitude();
                        name1b = place.getName();
                    }
                    else if(calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude()) < min1c && !place.getName().equals("zaytounabay") && !place.getName().equals(name1a) && !place.getName().equals(name1b) && !place.getName().equals(name1c)) {
                        min1c = calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude() );
                        p1clat = place.getLatitude();
                        p1cLon = place.getLongitude();
                        name1c = place.getName();
                    }
                }

                Random r = new Random();
                int i1 = r.nextInt(4 - 1) + 1;

                if(i1 == 1) {
                    p1dlat = p1alat;
                    p1dLon = p1aLon;
                    name1d = name1a;
                }

                else if(i1 == 2) {
                    p1dlat = p1blat;
                    p1dLon = p1bLon;
                    name1d = name1b;
                }
                else {
                    p1dlat = p1clat;
                    p1dLon = p1cLon;
                    name1d = name1c;
                }

                double d1a = calculteDistance(33.902336404480685, 35.4982852935791, p1dlat, p1dLon);

                double min1e = 300;
                double min1f = 300;
                double min1g = 300;
                double p1eLon = 100;
                double p1elat = 100;
                double p1fLon = 100;
                double p1flat = 100;
                double p1gLon = 100;
                double p1glat = 100;
                String name1e = "";
                String name1f = "";
                String name1g = "";

                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    com.example.user.touristgo.Place place = userSnapshot.getValue(com.example.user.touristgo.Place.class);

                    if(calculteDistance(p1dlat, p1dLon, place.getLatitude(), place.getLongitude()) < min1e && !place.getName().equals(name1d) && !place.getName().equals(name1e) && !place.getName().equals(name1f) && !place.getName().equals(name1g)) {
                        min1e = calculteDistance(p1dlat, p1dLon, place.getLatitude(), place.getLongitude() );
                        p1elat = place.getLatitude();
                        p1eLon = place.getLongitude();
                        name1e = place.getName();
                    }
                    else if(calculteDistance(p1dlat, p1dLon, place.getLatitude(), place.getLongitude()) < min1f && !place.getName().equals(name1d) && !place.getName().equals(name1e) && !place.getName().equals(name1f) && !place.getName().equals(name1g)) {
                        min1f = calculteDistance(p1dlat, p1dLon, place.getLatitude(), place.getLongitude() );
                        p1flat = place.getLatitude();
                        p1fLon = place.getLongitude();
                        name1f = place.getName();
                    }
                    else if(calculteDistance(p1dlat, p1dLon, place.getLatitude(), place.getLongitude()) < min1g && !place.getName().equals(name1d) && !place.getName().equals(name1e) && !place.getName().equals(name1f) && !place.getName().equals(name1g)  && !place.getName().equals(name1f) && !place.getName().equals(name1f)) {
                        min1g = calculteDistance(p1dlat, p1dLon, place.getLatitude(), place.getLongitude() );
                        p1glat = place.getLatitude();
                        p1gLon = place.getLongitude();
                        name1g = place.getName();
                    }
                }

                i1 = r.nextInt(4 - 1) + 1;
                if(i1 == 1) {
                    p1hlat = p1elat;
                    p1hLon = p1eLon;
                    name1h = name1e;
                }

                else if(i1 == 2) {
                    p1hlat = p1flat;
                    p1hLon = p1fLon;
                    name1h = name1f;
                }
                else {
                    p1hlat = p1glat;
                    p1hLon = p1gLon;
                    name1h = name1g;
                }

                double d2a = calculteDistance(p1dlat, p1dLon, p1hlat, p1hLon);

                double min1i = 300;
                double min1j = 300;
                double min1k = 300;
                double p1iLon = 100;
                double p1ilat = 100;
                double p1jLon = 100;
                double p1jlat = 100;
                double p1kLon = 100;
                double p1klat = 100;
                String name1i = "";
                String name1j = "";
                String name1k = "";

                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    com.example.user.touristgo.Place place = userSnapshot.getValue(com.example.user.touristgo.Place.class);

                    if(calculteDistance(p1hlat, p1hLon, place.getLatitude(), place.getLongitude()) < min1i && !place.getName().equals(name1h) && !place.getName().equals(name1i) && !place.getName().equals(name1j) && !place.getName().equals(name1k)) {
                        min1i = calculteDistance(p1hlat, p1hLon, place.getLatitude(), place.getLongitude() );
                        p1ilat = place.getLatitude();
                        p1iLon = place.getLongitude();
                        name1i = place.getName();
                    }
                    else if(calculteDistance(p1hlat, p1hLon, place.getLatitude(), place.getLongitude()) < min1j && !place.getName().equals(name1h) && !place.getName().equals(name1i) && !place.getName().equals(name1j) && !place.getName().equals(name1k)) {
                        min1j = calculteDistance(p1hlat, p1hLon, place.getLatitude(), place.getLongitude() );
                        p1jlat = place.getLatitude();
                        p1jLon = place.getLongitude();
                        name1j = place.getName();
                    }
                    else if(calculteDistance(p1hlat, p1hLon, place.getLatitude(), place.getLongitude()) < min1k && !place.getName().equals(name1h) && !place.getName().equals(name1i) && !place.getName().equals(name1j) && !place.getName().equals(name1k) && !place.getName().equals(name1j)) {
                        min1k = calculteDistance(p1hlat, p1hLon, place.getLatitude(), place.getLongitude() );
                        p1klat = place.getLatitude();
                        p1kLon = place.getLongitude();
                        name1k = place.getName();
                    }
                }

                i1 = r.nextInt(4 - 1) + 1;

                if(i1 == 1) {
                    p1llat = p1ilat;
                    p1lLon = p1iLon;
                    name1l = name1i;
                }

                else if(i1 == 2) {
                    p1llat = p1jlat;
                    p1lLon = p1jLon;
                    name1l = name1j;
                }
                else {
                    p1llat = p1klat;
                    p1lLon = p1kLon;
                    name1l = name1k;
                }

                double d3a = calculteDistance(p1hlat, p1hLon, p1llat, p1lLon);

                //The third Plan places is here


                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    com.example.user.touristgo.Place place = userSnapshot.getValue(com.example.user.touristgo.Place.class);
                    //first nearest place from my place
                    if(calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude()) < minm && !place.getName().equals("zaytounabay")) {
                        minm = calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude() );
                        p1latm = place.getLatitude();
                        p1Lonm = place.getLongitude();
                        name1m = place.getName();
                    }
                    //Second nearest from my place
                    else if(calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude()) < minn && !place.getName().equals("zaytounabay") && !place.getName().equals(name1m)) {
                        minn = calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude() );
                        p2latn = place.getLatitude();
                        p2Lonn = place.getLongitude();
                        name2n = place.getName();
                    }
                    //Third nearest place
                    else if(calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude()) < mino && !place.getName().equals("zaytounabay") && !place.getName().equals(name1m) && !place.getName().equals(name2n)) {
                        mino = calculteDistance(33.902336404480685, 35.4982852935791, place.getLatitude(), place.getLongitude() );
                        p3lato = place.getLatitude();
                        p3Lono = place.getLongitude();
                        name3o = place.getName();
                    }

                }




                String Text = "Min1:" + min1 + "Min2:" + min2 + "Min3:" + min3;
             //   testing.setText(Text);


                t1Place1.setText("1. "+ name1 + " (" + d1 + ")");
                final double finalP1lat1 = p1lat;
                final double finalP1Lon1 = p1Lon;
                final String finalDesc3 = name1;
                t1Place1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP1lat1, finalP1Lon1, finalDesc3);
                    }
                });

                t1Place2.setText("2. "+ name2 + " (" + d2 + ")");
                final double finalP2lat1 = p2lat;
                final double finalP2Lon1 = p2Lon;
                final String finalDesc4 = name2;
                t1Place2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP2lat1, finalP2Lon1, finalDesc4);
                    }
                });

                t1Place3.setText("3. "+ name3 + " (" + d3 + ")");
                final double finalP3lat1 = p3lat;
                final double finalP3Lon1 = p3Lon;
                final String finalDesc5 = name3;
                t1Place3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP3lat1, finalP3Lon1, finalDesc5);
                    }
                });


                t2Place1.setText("1. "+ name1d + " (" + d1a + ")");
                final double finalP1dlat1 = p1dlat;
                final double finalP1dLon1 = p1dLon;
                final String finalDesc1d1 = name1d;
                t2Place1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP1dlat1, finalP1dLon1, finalDesc1d1);
                    }
                });

                t2Place2.setText("2. "+ name1h + " (" + d2a + ")");
                final double finalP1hlat1 = p1hlat;
                final double finalP1hLon1 = p1hLon;
                final String finalDesc1h1 = name1h;
                t2Place2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP1hlat1, finalP1hLon1, finalDesc1h1);
                    }
                });

                t2Place3.setText("3. "+ name1l + " (" + d3a + ")");
                final double finalP1llat1 = p1llat;
                final double finalP1lLon1 = p1lLon;
                final String finalDesc1l1 = name1l;
                t2Place3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP1llat1, finalP1lLon1, finalDesc1l1);
                    }
                });


                t3Place1.setText("1. "+ name1m);
                final double finalP1latm1 = p1latm;
                final double finalP1Lonm1 = p1Lonm;
                final String finalDesc1m1 = name1m;
                t3Place1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP1latm1, finalP1Lonm1, finalDesc1m1);
                    }
                });

                t3Place2.setText("2. "+ name2n);
                final double finalP2latn1 = p2latn;
                final double finalP2Lonn1 = p2Lonn;
                final String finalDesc2n1 = name2n;
                t3Place2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP2latn1, finalP2Lonn1, finalDesc2n1);
                    }
                });

                t3Place3.setText("3. "+ name3o);
                final double finalP3lato1 = p3lato;
                final double finalP3Lono1 = p3Lono;
                final String finalDesc3o1 = name3o;
                t3Place3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMap(finalP3lato1, finalP3Lono1, finalDesc3o1);
                    }
                });

                final double finalP1lat = p1lat;
                final double finalP1Lon = p1Lon;
                final double finalP2lat = p2lat;
                final double finalP2Lon = p2Lon;
                final double finalP3lat = p3lat;
                final double finalP3Lon = p3Lon;
                final String finalDesc = name3;
                final String finalDesc1 = name2;
                final String finalDesc2 = name1;
                c1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMaps(finalP1lat, finalP1Lon, finalDesc2, finalP2lat, finalP2Lon, finalDesc1, finalP3lat, finalP3Lon, finalDesc);
                    }
                });

                final double finalP1dlat = p1dlat;
                final double finalP1dLon = p1dLon;
                final double finalP1hlat = p1hlat;
                final double finalP1hLon = p1hLon;
                final double finalP1llat = p1llat;
                final double finalP1lLon = p1lLon;
                final String finalDesc1d = name1d;
                final String finalDesc1h = name1h;
                final String finalDesc1l = name1l;
                c2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMaps(finalP1dlat, finalP1dLon, finalDesc1d, finalP1hlat, finalP1hLon, finalDesc1h, finalP1llat, finalP1lLon, finalDesc1l);
                    }
                });


                final double finalP1latm = p1latm;
                final double finalP1Lonm = p1Lonm;
                final String finalDesc1m = name1m;
                final double finalP2latn = p2latn;
                final double finalP2Lonn = p2Lonn;
                final String finalDesc2n = name2n;
                final double finalP3lato = p3lato;
                final double finalP3Lono = p3Lono;
                final String finalDesc3o = name3o;
                c3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openMaps(finalP1latm, finalP1Lonm, finalDesc1m, finalP2latn, finalP2Lonn, finalDesc2n, finalP3lato, finalP3Lono, finalDesc3o);
                    }
                });

                //   UserList adapter = new UserList(EditProfile.this, userInfo);
                //  listViewUsers.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public double calculteDistance(double x1, double y1, double x2, double y2) {
        Location loc1 = new Location("");
        loc1.setLatitude(Double.parseDouble("33.81"));
        loc1.setLongitude(Double.parseDouble("35.59"));

        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble("33.97"));
        loc2.setLongitude(Double.parseDouble("35.65"));

        //  String Text = "distance is:"+distance(33.81, 35.59,33.97,35.65);
        return distance(x1,y1, x2, y2);

    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public void openMaps(double p1lat, double p1Long, String name1, double p2lat, double p2Long, String name2, double p3lat, double p3Long, String name3) {
        Intent intent = new Intent(this, PlanMapActivity.class);
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        b.putDouble("p1lat",p1lat);
        b.putDouble("p1Long",p1Long);
        b.putString("p1Name",name1);
        b.putDouble("p2lat",p2lat);
        b.putDouble("p2Long",p2Long);
        b.putString("p2Name",name2);
        b.putDouble("p3lat",p3lat);
        b.putDouble("p3Long",p3Long);
        b.putString("p3Name",name3);
        b.putString("id", idString);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void openMap(double lat, double lon, String desc) {

        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
       // Intent intent = new Intent(this, InfoActivity.class);
       // intent.putExtra("id",idString);
        //startActivity(intent);

        Intent intent = new Intent(this, Map1PlaceActivity.class);
        //Bundle b = new Bundle();
        b.putDouble("p1lat",lat);
        b.putDouble("p1Long",lon);
        b.putString("p1Desc",desc);
        b.putString("id", idString);
        intent.putExtras(b);
        startActivity(intent);
    }

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
        // Intent intent = new Intent(this, HomeSearchActivity.class);
        // startActivity(intent);
    }

    public void toProfile(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }

    public void toMapView(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, SuggestedTripsActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }
    public void toPlanMyTrip(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, PlanMyTripActivity.class);
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

    public void showMap1() {
        //    mMap.clear();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        map1isOpen = true;
        map2isOpen = false;
        map3isOpen = false;
    }
    public void showMap2() {
        //    mMap.clear();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        map2isOpen = true;
        map1isOpen = false;
        map3isOpen = false;
    }
    public void showMap3() {
        //    mMap.clear();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map3);
        mapFragment.getMapAsync(this);
        map3isOpen = true;
        map2isOpen = false;
        map1isOpen = false;
    }

}
