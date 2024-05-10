package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
        Button list = findViewById(R.id.list);
        Button t_edit = findViewById(R.id.tournament_edit);
        Button t_view = findViewById(R.id.tournament_view);
        Button t_entry = findViewById(R.id.tournament_entry);

        entry.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlayerEntryActivity.class));
        });

        list.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlayerManagement.class));
        });

        t_edit.setOnClickListener((View v) -> {
            startActivity(new Intent(this, TournamentEdit.class));
        });

        t_view.setOnClickListener((View v) -> {
            startActivity(new Intent(this, TournamentCreate.class));
        });

        t_entry.setOnClickListener((View v) -> {
            startActivity(new Intent(this, TournamentEntry.class));
        });

    }
}
