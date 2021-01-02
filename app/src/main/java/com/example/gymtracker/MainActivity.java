package com.example.gymtracker;

import android.app.Application;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDataBase = openOrCreateDatabase("GymTracker", MODE_PRIVATE,null);
        appDataBase.execSQL("CREATE TABLE IF NOT EXISTS users(Username VARCHAR,Password VARCHAR, isAdmin BIT);");
        appDataBase.execSQL("INSERT INTO users VALUES('test','test', 0);");

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
                String[] columnsToReturn = { "Username", "Password" };
                String selection = "Username =?";
                String[] selectionArgs = { user };
                Cursor resultSet = appDataBase.query(table, columnsToReturn, selection, selectionArgs, null, null, null);
                resultSet.moveToFirst();
                String actualUser = resultSet.getString(0);
                String actualPass = resultSet.getString(1);

                if (actualUser.equals(user) && actualPass.equals(pass)) {
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                    openGoogleMapActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "Incorrect credentials", Toast.LENGTH_SHORT).show();
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