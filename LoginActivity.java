package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

        Button loginbtn = findViewById(R.id.loginbtn);

        loginbtn.setOnClickListener((View v) -> {
            checkUser();
        });

    }


    public void checkUser(){
        helper = new DatabaseHelper(this);

        EditText user = findViewById(R.id.user);
        EditText password = findViewById(R.id.password);

        String user_Name = user.getText().toString();
        String user_Password = password.getText().toString();

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        Intent umpire = new Intent(this, Umpire_Main.class);

        try {

            String sql = "select USER_ID,PERMISSION from USER_TBL where USER_NAME = ? AND PASSWORD = ?;";

            Cursor cursor = db.rawQuery(sql, new String[]{user_Name,user_Password});

            cursor.moveToNext();

            if(cursor.getCount() == 0){

                onPostExecute("資格情報が無効です");

            }else if(cursor.getString(1).equals("管理者")){

                startActivity(new Intent(this, ManagementActivity.class));

            }else if(cursor.getString(1).equals("ユーザー")){

                umpire.putExtra("EXTRA_DATA",cursor.getString(0));
                startActivity(umpire);

            }

        }finally {
            db.close();
        }

    }


    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(LoginActivity.this,(String)obj,Toast.LENGTH_LONG).show();
    }




}
