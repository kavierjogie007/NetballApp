package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity
{
    private EditText edtUsername, edtPassword;
    private SupaBaseClient client = new SupaBaseClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        edtUsername = findViewById(R.id.username);
        edtPassword = findViewById(R.id.password);
    };

    public void btnLoginClicked(View view) {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        Executors.newSingleThreadExecutor().execute(() -> {
            Coach coach = client.loginCoach(username, password);
            runOnUiThread(() -> {
                if (coach != null) {
                    getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                            .edit()
                            .putLong("coach_ID", coach.coach_ID)
                            .apply();

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }


    public void onRegisterCoachClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterCoachActivity.class);
        startActivity(intent);
        finish();
    }
}
