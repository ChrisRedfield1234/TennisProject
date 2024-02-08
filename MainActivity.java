package com.example.tennisproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        helper = new DatabaseHelper(this);
        DB();

        Button startbtn = findViewById(R.id.start);
        startbtn.setOnClickListener((View v) -> {
            //DB();
            startActivity(new Intent(this, tossActivity.class));
        });

        Button dialogbtn = findViewById(R.id.dialog);
        dialogbtn.setOnClickListener((View v) -> {

            showDialog(v);
        });



    }

    public void showDialog(View view) {
        DialogFragment dialogFragment = new EntryDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog");

    }

    public void DB(){
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql1 = "DELETE FROM POINT_TBL;";
        String sql2 = "DELETE FROM GAME_TBL;";
        String sql3 = "DELETE FROM SET_TBL;";
        String sql4 = "DELETE FROM SERVER_TBL;";
        String sql5 = "DELETE FROM SIDE_TBL;";

        try {
            db.execSQL(sql1);
            db.execSQL(sql2);
            db.execSQL(sql3);
            db.execSQL(sql4);
            db.execSQL(sql5);
            System.out.println("DBをリセット");
        } finally {
            db.close();
        }
    }

}