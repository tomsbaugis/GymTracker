package com.example.gymtracker;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AddFacilityActivity extends AppCompatActivity {

    SQLiteDatabase appDataBase = MainActivity.appDataBase;
    private Button button;
    private TextView newTitle;
    private TextView newCity;
    private TextView newAddress;
    private CheckBox newSub;
    private CheckBox newTrainer;
    private SeekBar newDensity;
    public int sub;
    public int trainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_facility);

        button = findViewById(R.id.buttonAddFacility);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTitle = findViewById(R.id.newTitle);
                String title = newTitle.getText().toString();
                newCity = findViewById(R.id.newCity);
                String city = newCity.getText().toString();
                newAddress = findViewById(R.id.newAddress);
                String address = newAddress.getText().toString();
                newSub = findViewById(R.id.newSub);
                if (newSub.isChecked()) {
                    sub = 1;
                } else {
                    sub = 0;
                }
                newTrainer = findViewById(R.id.newTrainer);
                if (newTrainer.isChecked()) {
                    trainer = 1;
                } else {
                    trainer = 0;
                }
                newDensity = findViewById(R.id.newDensity);
                int density = newDensity.getProgress();

                String sql = "INSERT INTO gyms (Title,City,Street,Subscription,Trainer,Density,Stars) VALUES (?, ?, ?, ?, ?, ?, ?)";
                SQLiteStatement statement = appDataBase.compileStatement(sql);

                String titleValue = title;
                String cityValue = city;
                String addressValue = address;
                int subValue = sub;
                int trainerValue = trainer;
                int densityValue = density;

                statement.bindString(1, titleValue);
                statement.bindString(2, cityValue);
                statement.bindString(3, addressValue);
                statement.bindDouble(4, subValue);
                statement.bindDouble(5, trainerValue);
                statement.bindDouble(6, densityValue);
                statement.bindDouble(7, 0);

                long rowId = statement.executeInsert();
                Toast.makeText(getApplicationContext(), "Jauna sporta zāle veiksmīgi pievienota!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}