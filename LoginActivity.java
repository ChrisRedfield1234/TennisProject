package com.example.tennisproject;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        EditText user = findViewById(R.id.user);
        EditText password = findViewById(R.id.password);
        Button loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener((View v) -> {
            checkUser();
        });



    }


    public void checkUser(){
        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "select COUNT(*) from SERVER_TBL;";



        onPostExecute("資格情報が無効です");



    }


    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(LoginActivity.this,(String)obj,Toast.LENGTH_LONG).show();
    }




}
