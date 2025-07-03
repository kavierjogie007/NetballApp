package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.Executors;

public class RegisterCoachActivity extends AppCompatActivity {

    private EditText edtFirstName, edtSurname,edtUsername,edtPassword,edtConfirmPassword;
    private Spinner spinnerRole;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_coach);

        edtFirstName = findViewById(R.id.edtFirstName);
        edtSurname = findViewById(R.id.edtSurname);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        spinnerRole = findViewById(R.id.spinnerRole);
        String[] roles = {"Head Coach", "Assistant Coach"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
    }

    public void onRegisterClicked(View view) {
        String firstName = edtFirstName.getText().toString().trim();
        String surname = edtSurname.getText().toString().trim();
        String role = spinnerRole.getSelectedItem().toString();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        Coach coach = new Coach();
        coach.coach_firstname = firstName;
        coach.coach_surname = surname;
        coach.coach_role = role;
        coach.coach_username = username;
        coach.coach_password = password;

        Executors.newSingleThreadExecutor().execute(() -> {
            SupaBaseClient client = new SupaBaseClient();
            Coach registeredCoach = client.registerCoach(coach);  // Note: returns Coach object now

            runOnUiThread(() -> {
                if (registeredCoach != null) {
                    Toast.makeText(this, "Coach registered successfully", Toast.LENGTH_SHORT).show();


                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putLong("coach_ID", registeredCoach.coach_ID)
                            .apply();

                    Intent intent = new Intent(RegisterCoachActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }


    public void onBackClicked(View view) {
        Intent intent = new Intent(RegisterCoachActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}