package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TournamentEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Button returnbtn = findViewById(R.id.returnbtn);
        Button savebtn = findViewById(R.id.save);
        Spinner block = findViewById(R.id.block);
        TextView participants = findViewById(R.id.participants);

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, ManagementActivity.class));
        });

        savebtn.setOnClickListener((View v) -> {

        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = setSpinner(adapter);
        Spinner spinner = (Spinner) findViewById(R.id.block);
        spinner.setAdapter(adapter);

    }

    public ArrayAdapter<String> setSpinner(ArrayAdapter<String> adapter){
        for(int i = 1;i < 5;i++){
            adapter.add(String.valueOf(i));
        }

        return adapter;
    }

    public void saveinfo(){
        //テーブル更新処理
    }

}
