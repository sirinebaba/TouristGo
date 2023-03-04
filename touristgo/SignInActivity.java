package com.example.user.touristgo;


import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class SignInActivity extends AppCompatActivity {

    EditText emailOrUsernameInput;
    EditText passInput;
    Button signUp;
    Button forgotPass;

    DatabaseReference databaseUsers;

    //  ListView listViewUsers;
    List<User> userInfo;


    //boolean check=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        forgotPass = (Button) findViewById(R.id.forgotPassBtn);
        forgotPass.setPaintFlags(forgotPass.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        signUp = (Button) findViewById(R.id.signUpBtn);
        signUp.setPaintFlags(signUp.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        emailOrUsernameInput = (EditText) findViewById(R.id.emailOrUsernameInput);
        passInput = (EditText) findViewById(R.id.passInput);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //   listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        userInfo = new ArrayList<>();


    }

  /*  protected void onStart() {
        super.onStart();

      //  final boolean check=false;

        final String username = emailOrUsernameInput.getText().toString().trim();
        String password = passInput.getText().toString().trim();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();

                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if(user.getEmail().equals(username)) {
                        userList.add(user);
                      enter();

                    }


                }

                UserList adapter = new UserList(SignInActivity.this, userList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    public void login() {


        //  final boolean check=false;

        final String username = emailOrUsernameInput.getText().toString().trim();
        final String password = passInput.getText().toString().trim();

        databaseUsers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userInfo.clear();
                boolean check = false;
                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    if((user.getEmail().equals(username) && user.getPassword().equals(password))
                            || (user.getUsername().equals(username) && user.getPassword().equals(password))) {
                        userInfo.add(user);
                        check = true;
                        enter(user.getUserId());

                    }

                }
                if(!check) {
                    makeToast();
                }

                UserList adapter = new UserList(SignInActivity.this, userInfo);
                //    listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void toSignUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void toForgotPass(View view) {
        Intent intent = new Intent(this, ForgotPassActivity.class);
        startActivity(intent);
    }

    public void toHome(View view) {
        login();


    }

    public void enter(String id) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    public void makeToast() {
        Toast.makeText(this,"Invalid username or password", Toast.LENGTH_LONG).show();
    }

}
