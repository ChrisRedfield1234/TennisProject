package com.example.tennisproject;

import static com.example.tennisproject.MainActivity.player_Id2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class PlayerEntryActivity extends AppCompatActivity {

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_entry);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button entry = findViewById(R.id.entry);
        Button returnbtn = findViewById(R.id.returnbtn);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = setSpinner(adapter);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);

        entry.setOnClickListener((View v) -> {
            entryPlayer();
        });

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, ManagementActivity.class));
        });

    }

    public void entryPlayer(){
        EditText lastname = findViewById(R.id.edit_lastname);
        EditText firstname = findViewById(R.id.edit_firstname);
        Spinner spinner = findViewById(R.id.spinner);

        String last = String.valueOf(lastname.getText());
        String first = String.valueOf(firstname.getText());
        String gp = String.valueOf(spinner.getSelectedItem());

        helper = new DatabaseHelper(this);

        if(last.isEmpty() || first.isEmpty() || gp.isEmpty()){

            onPostExecute("入力情報が不足しています");

        }else {

            try {
                helper.createDatabase();
            } catch (
                    IOException e) {
                throw new Error("Unable to create database");
            }

            SQLiteDatabase db = helper.getWritableDatabase();

            String sql1 = "SELECT GROUP_ID FROM GROUP_TBL WHERE GROUP_NAME = ?;";
            String sql2 = "INSERT INTO PLAYER_TBL(GROUP_ID,PLAYER_LAST_NAME,PLAYER_FIRST_NAME) VALUES(?,?,?);";

            Cursor cursor = db.rawQuery(sql1, new String[]{gp});
            cursor.moveToNext();
            String gn = cursor.getString(0);

            db.execSQL(sql2, new String[]{gn,last,first});

            onPostExecute("選手登録が完了しました");

        }

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

    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(PlayerEntryActivity.this,(String)obj,Toast.LENGTH_LONG).show();
    }


}
