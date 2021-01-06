package com.example.gymtracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
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
    String densityString = "";
    String densityPreferences = "";
    private int foundGyms = 0;
    private Spinner spinner;

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final String CITY = "cityKey";
    public static final String SUB = "subKey";
    public static final String TRAINER = "trainerKey";
    public static final String DENSITY = "densityKey";
    public static final String RATING = "ratingKey";
    public static final String ADMIN = "adminKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;
        String admin = sharedPreferences.getString(ADMIN, "adminKey");

        spinner = findViewById(R.id.spinner);
        if (admin.equals("1")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.options, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        } else if (admin.equals("0")) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.options2, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item.toString().equals("Atslēgties")) {
                    openMainActivity();
                }
                if (item.toString().equals("Rediģēt preferences")) {
                    openPreferencesActivity();
                }
                if (item.toString().equals("Pievienot sporta zāli (ADMIN)")) {
                    openAddFacilityActivity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());;
                String cityFromPreferences = sharedPreferences.getString(CITY, "cityKey");
                int subFromPreferences = sharedPreferences.getInt(SUB, 0);
                int trainerFromPreferences = sharedPreferences.getInt(TRAINER, 0);
                String subPref = "";
                String trainerPref = "";
                if (subFromPreferences == 0) {
                    subPref = "Nav pieejams";
                } else {
                    subPref = "Ir pieejams";
                }
                if (trainerFromPreferences == 0) {
                    trainerPref = "Nav pieejami";
                } else {
                    trainerPref = "Ir pieejami";
                }
                int densityFromPreferences = sharedPreferences.getInt(DENSITY, 0);
                if (densityFromPreferences > 0 && densityFromPreferences < 33) {
                    densityPreferences = "Zems";
                }
                else if (densityFromPreferences > 33 && densityFromPreferences < 66) {
                    densityPreferences = "Vidējs";
                }
                else if (densityFromPreferences > 66) {
                    densityPreferences = "Augsts";
                }
                float ratingFromPreferences = sharedPreferences.getFloat(RATING, 0);
                String table = "gyms";
                String[] columnsToReturn = {"Title", "City", "Street", "Subscription", "Trainer", "Density", "Stars"};
                String selection = "City =?";
                String[] selectionArgs = {cityFromPreferences};
                Cursor resultSet = appDataBase.query(table, columnsToReturn, selection, selectionArgs, null, null, null);
                if(resultSet != null){
                    if(resultSet.getCount() > 0){
                        for(resultSet.move(0);resultSet.moveToNext();resultSet.isAfterLast()){
                            String gymTitle = resultSet.getString(0);
                            String gymCity = resultSet.getString(1);
                            String gymStreet = resultSet.getString(2);
                            String gymSub = resultSet.getString(3);
                            String gymTrainer = resultSet.getString(4);
                            String gymDensity = resultSet.getString(5);
                            int gymDensityNumber = Integer.parseInt(gymDensity);
                            if (gymDensityNumber > 0 && gymDensityNumber < 33) {
                                densityString = "Zems";
                            }
                            else if (gymDensityNumber > 33 && gymDensityNumber < 66) {
                                densityString = "Vidējs";
                            }
                            else if (gymDensityNumber > 66) {
                                densityString = "Augsts";
                            }
                            double gymStars = Double.parseDouble(resultSet.getString(6));
                            String subAvailability = "";
                            String trainerAvailability = "";
                            if (gymSub.equals("0")) {
                                subAvailability = "Nav pieejams";
                            } else {
                                subAvailability = "Ir pieejams";
                            }
                            if (gymTrainer.equals("0")) {
                                trainerAvailability = "Nav pieejami";
                            } else {
                                trainerAvailability = "Ir pieejami";
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
                            if (subPref.equals(subAvailability) && trainerPref.equals(trainerAvailability) && densityPreferences.equals(densityString) && gymStars >= ratingFromPreferences) {
                                foundGyms++;
                                MarkerOptions marker1 = new MarkerOptions().position(yes).title(gymTitle).snippet("Abonements: " + subAvailability + "\n" + "Personīgie treneri: " + trainerAvailability + "\n" + "Apmeklējums: " + densityString + "\n" + "Reitings: " + gymStars);
                                mapTest.addMarker(marker1);
                            }
                            mapTest.moveCamera(CameraUpdateFactory.newLatLngZoom(yes, 10));
                        } if (foundGyms == 0){
                            Toast.makeText(getApplicationContext(), "Neviena sporta zāle neatbilda jūsu vēlmēm", Toast.LENGTH_SHORT).show();
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

    public void openAddFacilityActivity(){
        Intent intent = new Intent(this, AddFacilityActivity.class);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}