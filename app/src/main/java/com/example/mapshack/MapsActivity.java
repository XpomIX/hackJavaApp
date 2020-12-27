package com.example.mapshack;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
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
    private String City = "tyumen";

    ActionBarDrawerToggle toggle;

    public static final String EXTRA_TEXT = "com.example.mapshack.EXTRA_TEXT";
    public static final String EXTRA_NAME = "com.example.mapshack.EXTRA_NAME";

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
                    String id = object.getString("id");

                    JSONObject position = object.getJSONObject("position");
                    double lat = Double.parseDouble(position.getString("lat"));
                    double lng = Double.parseDouble(position.getString("lng"));
                    LatLng latLng = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(id));
                }

//                for (int i = 0; i < marks.size(); i++) {
//                    mMap.addMarker(new MarkerOptions().position(marks.get(i).position).title(marks.get(i).id));
//                }
                setCameraGmap(new LatLng(57.155339, 65.561864));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void setCameraGmap(LatLng position) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)
                .zoom(11)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.animateCamera(cameraUpdate);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        final DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.item1:
                        break;
                    case R.id.item2:
                        break;
                    case R.id.tyumen:
                        City = "tyumen";
                        setCameraGmap(new LatLng(57.155339, 65.561864));
                        break;
                    case R.id.ekb:
                        City = "ekb";
                        setCameraGmap(new LatLng(56.8519, 60.6122));
                        break;
                    case R.id.perm:
                        City = "perm";
                        setCameraGmap(new LatLng(58.0105, 56.2502));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String id = marker.getTitle();
                openPlanActivity(id);
                return true;
            }
        });
    }

    public void openPlanActivity(String id) {
        Intent intent = new Intent(this, PlanLayout.class);
        intent.putExtra(EXTRA_TEXT, id);
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

