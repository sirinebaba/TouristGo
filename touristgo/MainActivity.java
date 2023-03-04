package com.example.user.touristgo;

import android.content.Intent;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.user.touristgo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    GestureDetectorCompat gestureObject;
    InputStream is;

    BufferedReader reader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
        try {
            is = getAssets().open("placesInfo.txt");
            reader = new BufferedReader(new InputStreamReader(is));
            String line;

            do {
                line = reader.readLine();
                if (line != null && !line.trim().equals("")) {

                    String info = "";
                    String d = "";
                    String im_url = "";
                    String[] words = line.split(",");
                    if (!words[0].equals("") && !words[1].equals("") && !words[2].equals("")) {
                        /*System.out.print(words[0] + " ");
                        System.out.print(words[1] + " ");

                        System.out.print(words[2] + "\n");*/
                        try {


                            double longitude = Double.valueOf(words[0].trim()).doubleValue();
                            ;
                            double latitude = Double.valueOf(words[1].trim()).doubleValue();
                            ;
                            info = words[2];
                            d = words[3];
                            im_url = words[4];
                            Place a = new Place(longitude, latitude, info,d,im_url);
                            a.addPlace();
                        }
                        catch(NumberFormatException e){
                            e.printStackTrace();

                        }



                    }
                }


            } while (line != null);

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
