package com.example.user.touristgo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.media.audiofx.BassBoost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    ArrayList<String> mThumbIds = new ArrayList<>();

    Button plane;
    Button star;
    Button images;
    public FirebaseDatabase appDatabase;
    public DatabaseReference ref;
    public DatabaseReference usersRef;
    ImageView img;

    /*ImageButton image1;
    ImageButton image2;
    ImageButton image3;
    ImageButton image4;
    ImageButton image5;*/


    DatabaseReference databaseUsers;
    DatabaseReference databaseFavorites;
    ListView listViewItem;
    ListView listViewItem1;
    List<Plan> planList;
    List<FavoritePlaces> favoritePlacesList;

    public final static String TAG = MainActivity.class.getSimpleName();

    private Button homeButton;
    private Button searchButton;
    private Button carButton;
    private Button profileButton;

    GridView gridView;
    final ArrayList<String> numbers = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appDatabase = FirebaseDatabase.getInstance();
        ref = appDatabase.getReference();
        usersRef = ref.child("image");
        //View v = getLayoutInflater().inflate(R.layout.info_window, null);
        //img = (ImageView) v.findViewById(R.id.imgs);
        Intent myIntent = getIntent();
        final String idString = myIntent.getStringExtra("id");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numbers.clear();
                ;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Place p = postSnapshot.getValue(Place.class);

                    String s = (String) postSnapshot.child("userId").getValue();
                    if (s.equals(idString)) {

                        String url = (String) postSnapshot.child("url").getValue();
                        //Glide.with(getApplicationContext()).load(url).override(100, 100).into(img);

                        if (url != null) {



                            numbers.add(url);

                            System.out.print(url);
                        }
                    }
                    //}


                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                System.out.print("The read error" + firebaseError.getMessage());

            }
        });
                    Log.i(TAG, "I am here!");
                    super.onCreate(savedInstanceState);
                    setContentView(R.layout.activity_profile);

                    /*gridView = (GridView) findViewById(R.id.gridview);

                    ArrayAdapter<Image> adapter = new ArrayAdapter<Image>(this,
                            android.R.layout.simple_list_item_1, numbers);

                    gridView.setAdapter(adapter);

                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Toast.makeText(getApplicationContext(),
                                    ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                        }
                    });*/

                    planList = new ArrayList<>();
                    favoritePlacesList = new ArrayList<>();
        /*listViewItem = (ListView) findViewById(R.id.listViewItem);
        listViewItem1 = (ListView) findViewById(R.id.listViewItem1);*/
        /*listViewItem.setVisibility(View.GONE);
        listViewItem1.setVisibility(View.GONE);*/
                    databaseUsers = FirebaseDatabase.getInstance().getReference("plans");
                    databaseFavorites = FirebaseDatabase.getInstance().getReference("favorites");

                    plane = (Button) findViewById(R.id.tripsBtn);
                    star = (Button) findViewById(R.id.favBtn);
                    images = (Button) findViewById(R.id.imgBtn);

      /* image1 = (ImageButton) findViewById(R.id.image1);
        image2 = (ImageButton) findViewById(R.id.image2);
        image3 = (ImageButton) findViewById(R.id.image3);
        image4 = (ImageButton) findViewById(R.id.image4);
        image5 = (ImageButton) findViewById(R.id.image5);

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.setVisibility(View.GONE);
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);
                image4.setVisibility(View.GONE);
                image5.setVisibility(View.GONE);
                listViewItem.setVisibility(View.VISIBLE);
            }
        });

        plane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image4.setVisibility(View.VISIBLE);
                image5.setVisibility(View.VISIBLE);
                listViewItem.setVisibility(View.GONE);
            }
        });

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image1.setVisibility(View.GONE);
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.GONE);
                image4.setVisibility(View.GONE);
                image5.setVisibility(View.GONE);
                listViewItem.setVisibility(View.GONE);
                listViewItem1.setVisibility(View.VISIBLE);
            }
        });*/
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this,numbers));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                toViewImg(v);
            }
        });

                    clickableButtons();
                    profileButton = (Button) findViewById(R.id.profileBtn);
                    profileButton.setBackgroundResource(R.drawable.profileclicked);


                }

                @Override
                public void onBackPressed () {

                }

                @Override
                protected void onStart () {
                    super.onStart();
                    Bundle b = getIntent().getExtras();
                    final String idString = b.getString("id");

                    databaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            planList.clear();


                            for (DataSnapshot planSnapshot : dataSnapshot.getChildren()) {
                                Plan plan = planSnapshot.getValue(Plan.class);
                                //   makeToast(plan.getPlace1Description());
                                if (plan.getUserId().equals(idString)) {
                                    planList.add(plan);
                                }

                            }
                            MyPlansActivity adapter = new MyPlansActivity(ProfileActivity.this, planList);
                            //listViewItem.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

      /*  databaseFavorites.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                favoritePlacesList.clear();


                for(DataSnapshot favoritesSnapshot:dataSnapshot.getChildren()) {
                    FavoritePlaces favoritePlaces = favoritesSnapshot.getValue(FavoritePlaces.class);
                    //   makeToast(plan.getPlace1Description());
                    if(favoritePlaces.getUserId().equals(idString)) {
                        favoritePlacesList.add(favoritePlaces);
                    }

                }
                MyfavoritesActivity adapter1 = new MyfavoritesActivity(ProfileActivity.this, favoritePlacesList);
                listViewItem1.setAdapter(adapter1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }); */
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





    public void toEditProfile(View view) {
        Intent myIntent = getIntent();
        String idString = myIntent.getStringExtra("id");
        Intent intent = new Intent(this, EditProfile.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }
    public void toInfo(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }


    public void toViewImg(View view) {
        Bundle b = getIntent().getExtras();
        String idString = b.getString("id");
        Intent intent = new Intent(this, ViewImgActivity.class);
        intent.putExtra("id",idString);
        startActivity(intent);
    }

    public void makeToast(String x) {
        Toast.makeText(this,x, Toast.LENGTH_LONG).show();
    }

}
