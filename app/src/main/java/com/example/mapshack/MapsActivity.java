package com.example.mapshack;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static com.example.mapshack.NetworkUtils.getResponseFromURL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Mark> marks = new ArrayList<Mark>();

    ActionBarDrawerToggle toggle;

    public static final String EXTRA_TEXT = "com.example.mapshack.EXTRA_TEXT";

    private class AsyncGetAllCities extends AsyncTask<String, Void, String> {

        private GoogleMap googleMap;

        public AsyncGetAllCities(GoogleMap googleMap) {
            this.googleMap = googleMap;
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = null;
            try {
                result =  getResponseFromURL(strings[0], strings[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray shopsArray = jsonObject.getJSONArray("shops");
                String testString = shopsArray.toString();
                System.out.println("1");
                for (int i = 0; i < shopsArray.length(); i++) {
                    JSONObject object = shopsArray.getJSONObject(i);
                    String name = object.getString("name");

                    JSONObject position = object.getJSONObject("position");
                    double lat = Double.parseDouble(position.getString("lat"));
                    double lng = Double.parseDouble(position.getString("lng"));
                    LatLng latLng = new LatLng(lat, lng);
                    marks.add(new Mark(name, latLng));
                    mMap.addMarker(new MarkerOptions().position(latLng).title(name));
                }

                for (Mark mark : marks) {
                    mMap.addMarker(new MarkerOptions().position(mark.position).title(mark.name));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(mark.position));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item1:
                        Toast.makeText(MapsActivity.this, "Item2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.item2:
                        Toast.makeText(MapsActivity.this, "Item2", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        AsyncGetAllCities getAllCities = new AsyncGetAllCities(mMap);
        getAllCities.execute("api/shop/all", "{\"city\": \"Тюмень\"}");
        for (Mark mark : marks) {
            mMap.addMarker(new MarkerOptions().position(mark.position).title(mark.name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mark.position));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String title = marks.get(Integer.parseInt(marker.getId().substring(1))).name;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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