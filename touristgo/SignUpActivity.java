package com.example.user.touristgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText usernameInput;
    EditText fNameInput;
    EditText lNameInput;
    EditText emailInput;
    EditText passInput;
    EditText confPassInput;

    Button SaveBtn;

    DatabaseReference databaseUsers;

  //  ListView listViewUsers;
   // List<User> userList;

    public final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "I am here!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        fNameInput = (EditText) findViewById(R.id.fNameInput);
        lNameInput = (EditText) findViewById(R.id.lNameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        passInput = (EditText) findViewById(R.id.passInput);
        SaveBtn = (Button) findViewById(R.id.SaveBtn);
        confPassInput = (EditText) findViewById(R.id.confPassInput);

    //    listViewUsers = (ListView) findViewById(R.id.listViewUsers);
     //   userList = new ArrayList<>();

        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });


    }

  /*  @Override
    protected void onStart() {
        super.onStart();

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();

                for(DataSnapshot userSnapshot:dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);

                        userList.add(user);


                }

                UserList adapter = new UserList(SignUpActivity.this, userList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    private void addUser() {
        final String username = usernameInput.getText().toString().trim();
        String firstName = fNameInput.getText().toString().trim();
        String lastName = lNameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passInput.getText().toString().trim();
        String confirm = confPassInput.getText().toString().trim();


        if(TextUtils.isEmpty(username)) {
            Toast.makeText(this,"You  should enter a username", Toast.LENGTH_LONG).show();
        }

        else if(!uniqueUserName(username)) {

        }
        else if(TextUtils.isEmpty(firstName)) {
            Toast.makeText(this,"You  should enter your first name", Toast.LENGTH_LONG).show();
        }
        else if(!isEmailValid(email)) {
            Toast.makeText(this,"Email should have a valid form", Toast.LENGTH_LONG).show();
        }
        else if(!uniqueEmail(email)) {

        }
        else if(!(password.length() >= 5) ) {
            Toast.makeText(this,"Password should be at least 5 characters", Toast.LENGTH_LONG).show();
        }
        else if(!confirm.equals(password)) {
            Toast.makeText(this,"Password and confirm password should be equal", Toast.LENGTH_LONG).show();
        }

        else {

            String id = databaseUsers.push().getKey();
            User user = new User(id, username, firstName, lastName,email, password);
            databaseUsers.child(id).setValue(user);
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public boolean isEmailValid(String email)
    {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if(matcher.matches())
            return true;
        else
            return false;
    }
    public void toastMake() {
        Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show();
    }
    public boolean uniqueUserName(final String username) {

        databaseUsers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    com.example.user.touristgo.User user = userSnapshot.getValue(com.example.user.touristgo.User.class);
                    if (user.getUsername().equals(username)) {
                        toastMake();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return true;

    }

    public boolean uniqueEmail(final String email) {

        databaseUsers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    com.example.user.touristgo.User user = userSnapshot.getValue(com.example.user.touristgo.User.class);
                    if (user.getEmail().equals(email.toString().trim())) {
                        toastMake1();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        return true;

    }

    public void toastMake1() {
        Toast.makeText(this, "Email already exists", Toast.LENGTH_LONG).show();
    }
}
