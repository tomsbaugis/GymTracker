package com.example.gymtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMapActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, OnMapReadyCallback {
    private Button button;
    private MapView mapView;
    private Button searchGym;
    private GoogleMap mapTest;
    SQLiteDatabase appDataBase = MainActivity.appDataBase;

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
                String city = "Valmiera";
                String table = "gyms";
                String[] columnsToReturn = {"Title", "City", "Street", "Subscription", "Trainer", "Stars"};
                String selection = "City =?";
                String[] selectionArgs = {city};
                Cursor resultSet = appDataBase.query(table, columnsToReturn, selection, selectionArgs, null, null, null);
                if(resultSet != null){
                    if(resultSet.getCount() > 0){
                        for(resultSet.move(0);resultSet.moveToNext();resultSet.isAfterLast()){
                            String gymTitle = resultSet.getString(0);
                            String gymCity = resultSet.getString(1);
                            String gymStreet = resultSet.getString(2);
                            String gymSub = resultSet.getString(3);
                            String gymTrainer = resultSet.getString(4);
                            String gymStars = resultSet.getString(5);
                            String subAvailability = "";
                            String trainerAvailability = "";
                            if (gymSub.equals("0")) {
                                subAvailability = "Not available";
                            } else {
                                subAvailability = "Available";
                            }
                            if (gymTrainer.equals("0")) {
                                trainerAvailability = "Not available";
                            } else {
                                trainerAvailability = "Available";
                            }
                            String gymAddress = gymCity + ", " + gymStreet;
                            Geocoder geocoder = new Geocoder(GoogleMapActivity.this);
                            List<Address> list = null;
                            while (list == null) {
                                try {
                                    list = geocoder.getFromLocationName(gymAddress, 1);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Address address = list.get(0);
                            double lat = address.getLatitude();
                            double lng = address.getLongitude();
                            LatLng yes = new LatLng(lat, lng);
                            MarkerOptions marker1 = new MarkerOptions().position(yes).title(gymTitle).snippet("Subscription: " + subAvailability + "\n" + "Trainer: " + trainerAvailability + "\n" + "Stars: " + gymStars);
                            mapTest.addMarker(marker1);
                            mapTest.moveCamera(CameraUpdateFactory.newLatLngZoom(yes, 10));
                        }
                    }
                    resultSet.close();
                }
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
        mapTest.moveCamera(CameraUpdateFactory.newLatLngZoom(city, 10));
        mapTest.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext();

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
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