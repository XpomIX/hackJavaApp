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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // Add a marker in Sydney and move the camera
        LatLng tyumen = new LatLng(57.142357545110094, 65.58988839556109);
        mMap.addMarker(new MarkerOptions().position(tyumen).title("Магнит"));
        LatLng tyumen2 = new LatLng(54.142357545110094, 63.58988839556109);
        mMap.addMarker(new MarkerOptions().position(tyumen2).title("Монетка"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tyumen));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println(marker.getId());
                openPlanActivity();
                return true;
            }
        });
    }

    public void openPlanActivity() {
        Intent intent = new Intent(this, PlanLayout.class);
        startActivity(intent);
    }
}