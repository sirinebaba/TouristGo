package com.example.user.touristgo;

/**
 * Created by sirinebaba on 3/16/17.
 */

public class User {

    String userId;
    String username;
    String firstName;
    String lastName;
    //String username;
    String email;
    String password;

    public User() {

    }

    public User(String userId, String username,String firstName, String lastName,String email, String password) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
     //   this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

   // public String getUsername() {
      //  return username;
   // }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
