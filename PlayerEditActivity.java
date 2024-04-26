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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        Button updatebtn = findViewById(R.id.save);
        Button returnbtn = findViewById(R.id.returnbtn);

        updatebtn.setOnClickListener((View v) -> {
            updatePlayer(player_Id);
        });

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, PlayerManagement.class));
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = setSpinner(adapter);
        Spinner spinner = (Spinner) findViewById(R.id.block);
        spinner.setAdapter(adapter);

    }


    public void setPlayer(String player_Id){
        TextView lastName = findViewById(R.id.edit_);
        TextView firstName = findViewById(R.id.edit_firstname);
        Spinner groupName = findViewById(R.id.block);

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

    public void updatePlayer(String player_Id){
        TextView lastName = findViewById(R.id.edit_);
        TextView firstName = findViewById(R.id.edit_firstname);
        Spinner groupName = findViewById(R.id.block);

        String last = String.valueOf(lastName.getText());
        String first = String.valueOf(firstName.getText());
        String gp = String.valueOf(groupName.getSelectedItem());

        helper = new DatabaseHelper(this);

        if(last.isEmpty() || first.isEmpty() || gp.isEmpty()){

            onPostExecute("入力情報が不足しています");

        }else{

            try {

                helper.createDatabase();

            } catch (IOException e) {
                throw new Error("Unable to create database");
            }

            SQLiteDatabase db = helper.getWritableDatabase();

            String sql ="UPDATE PLAYER_TBL SET GROUP_ID = (SELECT GROUP_TBL.GROUP_ID FROM GROUP_TBL WHERE GROUP_NAME = ?),PLAYER_LAST_NAME = ?,PLAYER_FIRST_NAME = ? WHERE PLAYER_ID = ?;";

            db.execSQL(sql, new String[]{gp,last,first,player_Id});

            onPostExecute("選手情報の更新が完了しました");

        }

    }

    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(PlayerEditActivity.this,(String)obj,Toast.LENGTH_LONG).show();
    }


}
