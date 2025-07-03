package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.Executors;

public class ManageCouchProfile extends AppCompatActivity {

    private EditText edtFirstName, edtSurname, edtUsername, edtPassword, edtConfirmPassword;
    private Spinner spinnerRole;
    private long currentCoachId;
    private SupaBaseClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_couch_profile);

        // Retrieve coach ID from SharedPreferences
        currentCoachId = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                .getLong("coach_ID", -1);

        if (currentCoachId == -1) {
            Toast.makeText(this, "Coach ID not found. Please log in again.", Toast.LENGTH_SHORT).show();
            finish(); // Optionally redirect to login screen
            return;
        }

        //Set up UI
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

        client = new SupaBaseClient();

        //Fetch and prefill coach data
        Executors.newSingleThreadExecutor().execute(() -> {
            Coach coach = client.getCoachById(currentCoachId);

            runOnUiThread(() -> {
                if (coach != null) {
                        edtFirstName.setText(coach.coach_firstname);
                        edtSurname.setText(coach.coach_surname);
                        edtUsername.setText(coach.coach_username);
                        edtPassword.setText(coach.coach_password);
                        edtConfirmPassword.setText(coach.coach_password); // assume match

                        int spinnerPosition = adapter.getPosition(coach.coach_role);
                        spinnerRole.setSelection(spinnerPosition);
                } else {
                    Toast.makeText(this, "Coach profile not found", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    public void onUpdateProfileClicked(View view) {
            String firstname = edtFirstName.getText().toString();
            String surname = edtSurname.getText().toString();
            String role = spinnerRole.getSelectedItem().toString();
            String username = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                return;
            }

            Coach updatedCoach = new Coach();
            updatedCoach.coach_firstname = firstname;
            updatedCoach.coach_surname = surname;
            updatedCoach.coach_role = role;
            updatedCoach.coach_username = username;
            updatedCoach.coach_password = password;

            Executors.newSingleThreadExecutor().execute(() -> {
                Coach result = client.updateCoachProfile(currentCoachId, updatedCoach);
                runOnUiThread(() -> {
                    if (result != null) {
                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
                        // maybe go back or refresh UI
                    } else {
                        Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
    }
}