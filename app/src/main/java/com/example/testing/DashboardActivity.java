package com.example.testing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void onCoachProfileClicked(View view) {

        Intent intent = new Intent(DashboardActivity.this, ManageCouchProfile.class);
        startActivity(intent);
        finish();

    }
}