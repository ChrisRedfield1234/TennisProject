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


    String userbox ="";
    String passbox ="";

    String Auserbox ="";
    String Apassbox ="";



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

        String sqlUser = "select * from USER_TBL";
        //String sqlUser = "select USER_NAME from USER_TBL where USER_ID = 02";

        String sqlPass = "select PASSWORD from USER_TBL where USER_ID = '02'";
        //String sqlUser = "select COURT_NAME from COURT_TBL where COURT_ID = 1";
        //String sqlPass = "select COURT_ID from COURT_TBL where COURT_NAME = 'Aコート'";

        String AsqlUser = "select USER_NAME from USER_TBL where USER_ID = 01";
        String AsqlPass = "select PASSWORD from USER_TBL where USER_ID = 01";
        //String AsqlUser = "select COURT_NAME from COURT_TBL where COURT_ID = 2";
        //String AsqlPass = "select COURT_ID from COURT_TBL where COURT_NAME = 'Bコート'";


        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();




        //String sql = "select COUNT(*) from SERVER_TBL;";


        try {

            String usertest = user.getText().toString();
            String passtest = password.getText().toString();

            String Ausertest = user.getText().toString();
            String Apasstest = password.getText().toString();

            Cursor cursor1 = db.rawQuery(sqlUser, null);
            Cursor cursor2 = db.rawQuery(sqlPass, null);
            Cursor cursor3 = db.rawQuery(AsqlUser, null);
            Cursor cursor4 = db.rawQuery(AsqlPass, null);



            while (cursor1.moveToNext()) {
                userbox = cursor1.getString(0);
            }

            while (cursor2.moveToNext()) {
                passbox = cursor2.getString(0);
            }
            while (cursor3.moveToNext()) {
                Auserbox = cursor3.getString(0);
            }

            while (cursor4.moveToNext()) {
                Apassbox = cursor4.getString(0);
            }

            System.out.println("0" + userbox);
            System.out.println("1" + passbox);
            System.out.println("2" + Auserbox);
            System.out.println("3" + Apassbox);


                if ((usertest.equals(userbox)&&passtest.equals(passbox))||(Ausertest.equals(Auserbox)&&Apasstest.equals(Apassbox))) {
//

                    //onPostExecute("OK");
                    startActivity(new Intent(this, MainActivity.class));


                } else {
                    onPostExecute("資格情報が無効です");

                }

        }finally {
            db.close();
        }


        //onPostExecute("資格情報が無効です");



    }


    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(LoginActivity.this,(String)obj,Toast.LENGTH_LONG).show();
    }




}
