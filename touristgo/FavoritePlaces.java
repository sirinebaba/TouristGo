package com.example.user.touristgo;

import android.os.Bundle;

/**
 * Created by sirinebaba on 4/17/17.
 */

public class FavoritePlaces {
    String placeId;
    String userId;
    String pDesc;
    double pLat;
    double pLon;



    public FavoritePlaces(String placeId, String userId,String pDesc, double pLat, double pLon) {
        this.placeId = placeId;
        this.userId = userId;
        this.pDesc = pDesc;
        this.pLat = pLat;
        this.pLon = pLon;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getUserId() {
        return userId;
    }

    public String getpDesc() {
        return pDesc;
    }

    public double getpLat() { return pLat; }

    public double getpLon() {
        return pLon;
    }

}
