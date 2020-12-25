package com.example.mapshack;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String EXTRA_TEXT = "com.example.mapshack.EXTRA_TEXT";
    private final LatLng[] marks = new LatLng[5];
    private final String[] marksIds = new String[5];
    private final String[] marksTitles = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        marks[0] = new LatLng(57.142357545110094, 65.58988839556109);
        marksIds[0] = "f7c76426-46af-11eb-b378-0242ac130002"; marksTitles[0] = "Монетка";
        marks[1] = new LatLng(56.142357545110094, 65.58988839556109);
        marksIds[1] = "fcf6358a-46af-11eb-b378-0242ac130002"; marksTitles[1] = "Пятёрочка";
        marks[2] = new LatLng(55.142357545110094, 65.58988839556109);
        marksIds[2] = "030bca02-46b0-11eb-b378-0242ac130002"; marksTitles[2] = "Ашан";
        marks[3] = new LatLng(54.142357545110094, 65.58988839556109);
        marksIds[3] = "056acd48-46b0-11eb-b378-0242ac130002"; marksTitles[3] = "Окей";
        marks[4] = new LatLng(53.142357545110094, 65.58988839556109);
        marksIds[4] = "071e52fe-46b0-11eb-b378-0242ac130002"; marksTitles[4] = "Пятёрочка2";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < 5 ;i++) {
            mMap.addMarker(new MarkerOptions().position(marks[i]).title(marksTitles[0]));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marks[i]));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marksTitles[Integer.parseInt(marker.getId().substring(1))];
                openPlanActivity(title);
                return true;
            }
        });
    }

    public void openPlanActivity(String title) {
        Intent intent = new Intent(this, PlanLayout.class);
        intent.putExtra(EXTRA_TEXT, title);
        startActivity(intent);
    }
}