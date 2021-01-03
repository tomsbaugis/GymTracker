package com.example.gymtracker;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PreferencesActivity extends AppCompatActivity {
    private TextView preferedCity;
    private CheckBox preferedSub;
    private CheckBox preferedTrainer;
    private SeekBar preferedDensity;
    private RatingBar preferedRating;
    private Button button1;
    public int sub;
    public int trainer;
    public static final String CITY = "cityKey";
    public static final String SUB = "subKey";
    public static final String TRAINER = "trainerKey";
    public static final String DENSITY = "densityKey";
    public static final String RATING = "ratingKey";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_preferences);


        button1 = findViewById(R.id.buttonSavePreferences);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferedCity = findViewById(R.id.inputCity);
                String city = preferedCity.getText().toString();
                preferedSub = findViewById(R.id.inputSub);
                if (preferedSub.isChecked()) {
                    sub = 1;
                } else {
                    sub = 0;
                }
                preferedTrainer = findViewById(R.id.inputTrainer);
                if (preferedTrainer.isChecked()) {
                    trainer = 1;
                } else {
                    trainer = 0;
                }
                preferedDensity = findViewById(R.id.inputDensity);
                int density = preferedDensity.getProgress();
                preferedRating = findViewById(R.id.ratingBar);
                float rating = preferedRating.getRating();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(CITY, city);
                editor.putInt(SUB, sub);
                editor.putInt(TRAINER, trainer);
                editor.putInt(DENSITY, density);
                editor.putFloat(RATING, rating);
                editor.commit();
                finish();
            }
        });
    }
}