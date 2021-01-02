package com.example.gymtracker;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrationActivity extends AppCompatActivity {
    private Button button;
    private Button registerUser;
    SQLiteDatabase appDataBase = MainActivity.appDataBase;
    private TextInputEditText username;
    private TextInputEditText password;
    private TextInputEditText repeatedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registerUser = findViewById(R.id.buttonRegisterUser);
        button = findViewById(R.id.buttonAlreadyRegistered);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = findViewById(R.id.editedUsername);
                String user = username.getText().toString();
                password = findViewById(R.id.registerPassword);
                String pass = password.getText().toString();
                repeatedPassword = findViewById(R.id.repeatedPassword);
                String passRepeat = password.getText().toString();
                if (pass.equals(passRepeat)) {
                    String sql = "INSERT INTO users (Username, Password) VALUES (?, ?)";
                    SQLiteStatement statement = appDataBase.compileStatement(sql);

                    String userValue = user;
                    String passwordValue = passRepeat;

                    statement.bindString(1, userValue);
                    statement.bindString(2, passwordValue);

                    long rowId = statement.executeInsert();
                    Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}