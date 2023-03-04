package com.example.user.touristgo;

import java.util.List;

/**
 * Created by sirinebaba on 4/16/17.
 */

public class Plan {
    String planId;
    String userId;
    double place1Latitude;
    double place1longitude;
    String place1Description;
    double place2Latitude;
    double place2longitude;
    String place2Description;
    double place3Latitude;
    double place3longitude;
    String place3Description;
    double place4Latitude;
    double place4longitude;
    String place4Description;
    double place5Latitude;
    double place5longitude;
    String place5Description;
    double place6Latitude;
    double place6longitude;
    String place6Description;

    public Plan() {

    }

    public Plan(String planId, String userId, double place1Latitude, double place1longitude, String place1Description, double place2Latitude, double place2longitude, String place2Description, double place3Latitude, double place3longitude, String place3Description, double place4Latitude, double place4longitude, String place4Description, double place5Latitude, double place5longitude, String place5Description, double place6Latitude, double place6longitude, String place6Description) {
        this.planId = planId;
        this.userId = userId;
        this.place1Latitude = place1Latitude;
        this.place1longitude = place1longitude;
        this.place1Description = place1Description;
        this.place2Latitude = place2Latitude;
        this.place2longitude = place2longitude;
        this.place2Description = place2Description;
        this.place3Latitude = place3Latitude;
        this.place3longitude = place3longitude;
        this.place3Description = place3Description;
        this.place4Latitude = place4Latitude;
        this.place4longitude = place4longitude;
        this.place4Description = place4Description;
        this.place5Latitude = place5Latitude;
        this.place5longitude = place5longitude;
        this.place5Description = place5Description;
        this.place6Latitude = place6Latitude;
        this.place6longitude = place6longitude;
        this.place6Description = place6Description;
    }

    public String getPlanId() {
        return planId;
    }

    public String getUserId() {
        return userId;
    }

    public double getPlace1Latitude() {
        return place1Latitude;
    }

    public double getPlace1longitude() {
        return place1longitude;
    }

    public String getPlace1Description() {
        return place1Description;
    }

    public double getPlace2Latitude() {
        return place2Latitude;
    }

    public double getPlace2longitude() {
        return place2longitude;
    }

    public String getPlace2Description() {
        return place2Description;
    }

    public double getPlace3Latitude() {
        return place3Latitude;
    }

    public double getPlace3longitude() {
        return place3longitude;
    }

    public String getPlace3Description() {
        return place3Description;
    }

    public double getPlace4Latitude() {
        return place4Latitude;
    }

    public double getPlace4longitude() {
        return place4longitude;
    }

    public String getPlace4Description() {
        return place4Description;
    }

    public double getPlace5Latitude() {
        return place5Latitude;
    }

    public double getPlace5longitude() {
        return place5longitude;
    }

    public String getPlace5Description() {
        return place5Description;
    }

    public double getPlace6Latitude() {
        return place6Latitude;
    }

    public double getPlace6longitude() {
        return place6longitude;
    }

    public String getPlace6Description() {
        return place6Description;
    }
}
