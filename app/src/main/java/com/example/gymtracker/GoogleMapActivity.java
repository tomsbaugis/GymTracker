package com.example.gymtracker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private Button button;
    private MapView mapView;
    private Button searchGym;
    private GoogleMap mapTest;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        button = (Button) findViewById(R.id.buttonPreferences);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPreferencesActivity();
            }
        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);

        mapView.getMapAsync(this);

        searchGym = findViewById(R.id.buttonSearchGym);
        searchGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addressString = "Valmiera";
                Geocoder geocoder = new Geocoder(GoogleMapActivity.this);
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(addressString, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert list != null;
                Address address = list.get(0);
                double lat = address.getLatitude();
                double lng = address.getLongitude();

                LatLng yes = new LatLng(lat, lng);
                MarkerOptions marker1 = new MarkerOptions().position(yes).title(addressString);
                mapTest.addMarker(marker1);
                mapTest.moveCamera(CameraUpdateFactory.newLatLngZoom(yes, 15));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mapTest = map;
        LatLng city = new LatLng(57.541651, 25.428387);
        LatLng latLng1 = new LatLng(57.541018, 25.426197);
        MarkerOptions marker1 = new MarkerOptions().position(latLng1).title("Gym1");
        LatLng latLng2 = new LatLng(57.541651, 25.428387);
        MarkerOptions marker2 = new MarkerOptions().position(latLng2).title("VIA");
        mapTest.addMarker(marker1);
        mapTest.addMarker(marker2);
        mapTest.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 15));
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void openPreferencesActivity(){
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);
    }
}