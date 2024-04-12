package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;

import java.io.IOException;

public class PlayerEditActivity extends AppCompatActivity {

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = getIntent();
        String player_Id = intent.getStringExtra("EXTRA_DATA");
        setPlayer(player_Id);

        Button returnbtn = findViewById(R.id.returnbtn);

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlayerManagement.class));
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = setSpinner(adapter);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

    }


    public void setPlayer(String player_Id){
        TextView lastName = findViewById(R.id.edit_lastname);
        TextView firstName = findViewById(R.id.edit_firstname);
        Spinner groupName = findViewById(R.id.spinner);

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT PLAYER_LAST_NAME,PLAYER_FIRST_NAME,GROUP_NAME FROM PLAYER_TBL INNER JOIN GROUP_TBL ON PLAYER_TBL.GROUP_ID = GROUP_TBL.GROUP_ID WHERE PLAYER_ID = ?;";

        Cursor cursor = db.rawQuery(sql, new String[]{player_Id});

        cursor.moveToNext();

        lastName.setText(cursor.getString(0));
        firstName.setText(cursor.getString(1));

        PlayerList_DTO dto = new PlayerList_DTO();
        PLAYER_DTO dto2 = new PLAYER_DTO();
        dto2.setLastName(cursor.getString(0));
        dto2.setFirstName(cursor.getString(1));
        dto.setDto(dto2);
        dto.setGroup_Name(cursor.getString(2));

    }


    public ArrayAdapter<String> setSpinner(ArrayAdapter<String> adapter){
        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "SELECT GROUP_NAME FROM GROUP_TBL;";

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            adapter.add(cursor.getString(0));
        }

        return adapter;
    }

}
