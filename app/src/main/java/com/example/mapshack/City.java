package com.example.mapshack;

import com.google.android.gms.maps.model.LatLng;

public class City {
    private String name;
    private LatLng latLng;

    public City(String name, LatLng latLng) {
        this.name = name;
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
