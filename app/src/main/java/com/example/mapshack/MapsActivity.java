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
import java.util.UUID;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    public static final String EXTRA_TEXT = "com.example.mapshack.EXTRA_TEXT";
    private final Mark[] marks = new Mark[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        marks[0] = new Mark("Монетка", new LatLng(57.142357545110094, 65.58988839556109));
        marks[1] = new Mark("Пятёрочка", new LatLng(56.142357545110094, 65.58988839556109));
        marks[2] = new Mark("Ашан", new LatLng(55.142357545110094, 65.58988839556109));
        marks[3] = new Mark("Окей", new LatLng(54.142357545110094, 65.58988839556109));
        marks[4] = new Mark("Пятёрочка2", new LatLng(53.142357545110094, 65.58988839556109));
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
        for (Mark mark : marks) {
            mMap.addMarker(new MarkerOptions().position(mark.position).title(mark.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mark.position));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marks[Integer.parseInt(marker.getId().substring(1))].name;
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