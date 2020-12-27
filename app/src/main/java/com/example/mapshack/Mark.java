package com.example.mapshack;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

class Mark {
    String id;
    String name;
    LatLng position;

    Mark(String id, String name, LatLng position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }
}
