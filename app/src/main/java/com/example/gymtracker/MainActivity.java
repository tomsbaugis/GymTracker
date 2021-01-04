package com.example.gymtracker;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button button1;
    private TextInputEditText username;
    private TextInputEditText password;
    public static SQLiteDatabase appDataBase;
    public static final String ADMIN = "adminKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDataBase = openOrCreateDatabase("GymTracker", MODE_PRIVATE,null);
        appDataBase.execSQL("CREATE TABLE IF NOT EXISTS users(ID INTEGER PRIMARY KEY AUTOINCREMENT, Username VARCHAR,Password VARCHAR, isAdmin SMALLINT);");
        appDataBase.execSQL("CREATE TABLE IF NOT EXISTS gyms(ID INTEGER PRIMARY KEY AUTOINCREMENT, Title VARCHAR, City VARCHAR,Street VARCHAR, Subscription SMALLINT, Trainer SMALLINT,Density INT, Stars REAL);");
        appDataBase.execSQL("INSERT INTO users (Username,Password,isAdmin) VALUES('admin','admin', 1);");
        appDataBase.execSQL("INSERT INTO gyms (Title,City,Street,Subscription,Trainer,Density,Stars) VALUES('Lauvas klubs','Valmiera','Tērbatas iela 1', 0, 1, 20, 4.5);");
        appDataBase.execSQL("INSERT INTO gyms (Title,City,Street,Subscription,Trainer,Density,Stars) VALUES('Vidzemes olimpiskais centrs','Valmiera','Rīgas iela 91', 1, 1, 67, 4.6);");
        appDataBase.execSQL("INSERT INTO gyms (Title,City,Street,Subscription,Trainer,Density,Stars) VALUES('F1, Fitnesa klubs','Rīga','Krišjāņa Barona iela 69', 1, 1, 76, 4.4);");
        appDataBase.execSQL("INSERT INTO gyms (Title,City,Street,Subscription,Trainer,Density,Stars) VALUES('Jols , SIA, Fitnesa klubs','Daugavpils','Viestura iela 68', 1, 0, 43, 4.2);");

        button = (Button) findViewById(R.id.buttonRegistration);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationActivity();
            }
        });

        button1 = (Button) findViewById(R.id.buttonLogin);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = findViewById(R.id.username1);
                String user = username.getText().toString();
                password = findViewById(R.id.password1);
                String pass = password.getText().toString();
                String table = "users";
                String[] columnsToReturn = { "Username", "Password", "isAdmin" };
                String selection = "Username =?";
                String[] selectionArgs = { user };
                Cursor resultSet = appDataBase.query(table, columnsToReturn, selection, selectionArgs, null, null, null);
                resultSet.moveToFirst();
                String actualUser = resultSet.getString(0);
                String actualPass = resultSet.getString(1);
                String adminStatus = resultSet.getString(2);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(ADMIN, adminStatus);
                editor.commit();


                if (actualUser.equals(user) && actualPass.equals(pass)) {
                    Toast.makeText(getApplicationContext(), "Autorizācija veiksmīga", Toast.LENGTH_SHORT).show();
                    openGoogleMapActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Nepareizi dati", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openGoogleMapActivity(){
        Intent intent = new Intent(this, GoogleMapActivity.class);
        startActivity(intent);
    }

    public void openRegistrationActivity(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
}