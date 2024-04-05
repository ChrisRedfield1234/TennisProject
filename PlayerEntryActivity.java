package com.example.tennisproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class PlayerEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_entry);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        EditText lastname = findViewById(R.id.edit_lastname);
        EditText firstname = findViewById(R.id.edit_firstname);
        EditText group = findViewById(R.id.edit_group);
        Button entry = findViewById(R.id.entry);

        entry.setOnClickListener((View v) -> {

        });


    }

}
