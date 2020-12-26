package com.example.mapshack;

import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

class Mark {
    String id;
    String name;
    LatLng position;

    Mark(String name, LatLng position) {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.name = name;
        this.position = position;
    }
}
