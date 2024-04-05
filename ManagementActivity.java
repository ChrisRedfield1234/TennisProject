package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManagementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.management);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button entry = findViewById(R.id.entry);
        Button edit = findViewById(R.id.edit);

        entry.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlayerEntryActivity.class));
        });

        edit.setOnClickListener((View v) -> {

        });

    }
}