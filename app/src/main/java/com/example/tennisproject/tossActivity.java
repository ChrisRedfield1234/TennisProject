package com.example.tennisproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateHandle;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

public class tossActivity extends AppCompatActivity {

    int toss1 = 0;
    int toss2 = 0;

    int server1 = 0;
    int server2 = 0;

    int receiver1 = 0;
    int receiver2 = 0;

    int left1 = 0;
    int left2 = 0;

    int right1 = 0;
    int right2 = 0;

    int assignment1 = 0;
    int assignment2 = 0;

    private DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toss);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        helper = new DatabaseHelper(this);

        //トスの勝者の CheckBox を取得
        CheckBox checkBox_toss1 = findViewById(R.id.checkBox_toss1);
        CheckBox checkBox_toss2 = findViewById(R.id.checkBox_toss2);
        //Clickイベントを設定
        checkBox_toss1.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_toss2.setEnabled(false);
                toss1 = 1;
                toss2 = 2;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_toss2.setEnabled(true);
                toss1 = 0;
                toss2 = 0;
            }
        });

        //Clickイベントを設定
        checkBox_toss2.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_toss1.setEnabled(false);
                toss2 = 1;
                toss1 = 2;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_toss1.setEnabled(true);
                toss2 = 0;
                toss1 = 0;
            }
        });


        CheckBox checkBox_serve1 = findViewById(R.id.checkbox_serve1);
        CheckBox checkBox_serve2 = findViewById(R.id.checkBox_serve2);
        CheckBox checkBox_receiver1 = findViewById(R.id.checkbox_receiver1);
        CheckBox checkBox_receiver2 = findViewById(R.id.checkBox_receiver2);
        CheckBox checkBox_left1 = findViewById(R.id.checkBox_left1);
        CheckBox checkBox_right1 = findViewById(R.id.checkBox_right1);
        CheckBox checkBox_left2 = findViewById(R.id.checkBox_left2);
        CheckBox checkBox_right2 = findViewById(R.id.checkBox_right2);

        checkBox_serve1.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_serve2.setEnabled(false);
                checkBox_receiver1.setEnabled(false);
                checkBox_receiver2.setEnabled(false);
                checkBox_right1.setEnabled(false);
                checkBox_left1.setEnabled(false);
                server1 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_serve2.setEnabled(true);
                checkBox_receiver1.setEnabled(true);
                checkBox_receiver2.setEnabled(true);
                checkBox_right1.setEnabled(true);
                checkBox_left1.setEnabled(true);
                server1 = 0;
            }
        });

        checkBox_serve2.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_serve1.setEnabled(false);
                checkBox_receiver1.setEnabled(false);
                checkBox_receiver2.setEnabled(false);
                checkBox_right2.setEnabled(false);
                checkBox_left2.setEnabled(false);
                server2 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_serve1.setEnabled(true);
                checkBox_receiver1.setEnabled(true);
                checkBox_receiver2.setEnabled(true);
                checkBox_right2.setEnabled(true);
                checkBox_left2.setEnabled(true);
                server2 = 0;
            }
        });


        checkBox_receiver1.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_serve1.setEnabled(false);
                checkBox_serve2.setEnabled(false);
                checkBox_receiver2.setEnabled(false);
                checkBox_right1.setEnabled(false);
                checkBox_left1.setEnabled(false);
                receiver1 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_serve1.setEnabled(true);
                checkBox_serve2.setEnabled(true);
                checkBox_receiver2.setEnabled(true);
                checkBox_right1.setEnabled(true);
                checkBox_left1.setEnabled(true);
                receiver1 = 0;
            }
        });

        checkBox_receiver2.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_serve1.setEnabled(false);
                checkBox_serve2.setEnabled(false);
                checkBox_receiver1.setEnabled(false);
                checkBox_right2.setEnabled(false);
                checkBox_left2.setEnabled(false);
                receiver2 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_serve1.setEnabled(true);
                checkBox_serve2.setEnabled(true);
                checkBox_receiver1.setEnabled(true);
                checkBox_right2.setEnabled(true);
                checkBox_left2.setEnabled(true);
                receiver2 = 0;
            }
        });




        checkBox_left1.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_right1.setEnabled(false);
                checkBox_left2.setEnabled(false);
                checkBox_right2.setEnabled(false);
                checkBox_serve1.setEnabled(false);
                checkBox_receiver1.setEnabled(false);
                left1 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_right1.setEnabled(true);
                checkBox_left2.setEnabled(true);
                checkBox_right2.setEnabled(true);
                checkBox_serve1.setEnabled(true);
                checkBox_receiver1.setEnabled(true);
                left1 = 0;
            }
        });

        checkBox_right1.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_right2.setEnabled(false);
                checkBox_left1.setEnabled(false);
                checkBox_left2.setEnabled(false);
                checkBox_serve1.setEnabled(false);
                checkBox_receiver1.setEnabled(false);
                right1 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_right2.setEnabled(true);
                checkBox_left1.setEnabled(true);
                checkBox_left2.setEnabled(true);
                checkBox_serve1.setEnabled(true);
                checkBox_receiver1.setEnabled(true);
                right1 = 0;
            }
        });


        checkBox_left2.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_right1.setEnabled(false);
                checkBox_left1.setEnabled(false);
                checkBox_right2.setEnabled(false);
                checkBox_serve2.setEnabled(false);
                checkBox_receiver2.setEnabled(false);
                left2 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_right1.setEnabled(true);
                checkBox_left1.setEnabled(true);
                checkBox_right2.setEnabled(true);
                checkBox_serve2.setEnabled(true);
                checkBox_receiver2.setEnabled(true);
                left2 = 0;
            }
        });

        checkBox_right2.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_right1.setEnabled(false);
                checkBox_left1.setEnabled(false);
                checkBox_left2.setEnabled(false);
                checkBox_serve2.setEnabled(false);
                checkBox_receiver2.setEnabled(false);
                right2 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_right1.setEnabled(true);
                checkBox_left1.setEnabled(true);
                checkBox_left2.setEnabled(true);
                checkBox_serve2.setEnabled(true);
                checkBox_receiver2.setEnabled(true);
                right2 = 0;
            }
        });


        CheckBox checkBox_assignment1 = findViewById(R.id.checkBox_assignment1);
        CheckBox checkBox_assignment2 = findViewById(R.id.checkBox_assignment2);

        checkBox_assignment1.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_assignment2.setEnabled(false);
                assignment1 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_assignment2.setEnabled(true);
                assignment1 = 0;
            }
        });

        checkBox_assignment2.setOnClickListener(view -> {
            CheckBox c = (CheckBox) view;
            if (c.isChecked()) {
                System.out.println("ONに変更されました");
                checkBox_assignment1.setEnabled(false);
                assignment2 = 1;
            } else {
                System.out.println("OFFに変更されました");
                checkBox_assignment1.setEnabled(true);
                assignment2 = 0;
            }
        });


        Button startbtn = findViewById(R.id.button2);
        startbtn.setOnClickListener((View v) -> {
            tossUpdate();
            startActivity(new Intent(this, CountActivity.class));

        });
    }

    public void tossUpdate() {

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "";
        String str = "";

        //サーバーとサイドに属性追加、トス勝者がどっちを選択したかわかるようにする

        try {
            for (int i = 1; i < 3; i++) {

                if (server1 == 1) {
                    if(toss1 == 1){
                        sql = "INSERT INTO SERVER_TBL VALUES('1','1','1');";
                        toss1 = 0;
                    }else if(toss1 == 2){
                        sql = "INSERT INTO SERVER_TBL VALUES('1','1','2');";
                        toss1 = 0;
                    }else{
                        sql = "INSERT INTO SERVER_TBL VALUES('1','1','0');";
                    }
                    server1 = 0;
                    receiver2 = 1;
                } else if (server2 == 1) {
                    if(toss2 == 1){
                        sql = "INSERT INTO SERVER_TBL VALUES('1','2','1');";
                        toss2 = 0;
                    }else if(toss2 == 2){
                        sql = "INSERT INTO SERVER_TBL VALUES('1','2','2');";
                        toss2 = 0;
                    }else{
                        sql = "INSERT INTO SERVER_TBL VALUES('1','2','0');";
                    }
                    server2 = 0;
                    receiver1 = 1;
                }else if(receiver1 == 1){
                    if(toss1 == 1){
                        sql = "INSERT INTO SERVER_TBL VALUES('2','1','1');";
                        toss1 = 0;
                    }else if(toss1 == 2){
                        sql = "INSERT INTO SERVER_TBL VALUES('2','1','2');";
                        toss1 = 0;
                    }else{
                        sql = "INSERT INTO SERVER_TBL VALUES('2','1','0');";
                    }
                    receiver1 = 0;
                    server2 = 1;
                }else if(receiver2 == 1){
                    if(toss2 == 1){
                        sql = "INSERT INTO SERVER_TBL VALUES('2','2','1');";
                        toss2 = 0;
                    }else if(toss2 == 2){
                        sql = "INSERT INTO SERVER_TBL VALUES('2','2','2');";
                        toss2 = 0;
                    }else{
                        sql = "INSERT INTO SERVER_TBL VALUES('2','2','0');";
                    }
                    receiver2 = 0;
                    server1 = 1;
                }


                if (left1 == 1) {
                    if(toss1 == 1){
                        str = "INSERT INTO SIDE_TBL VALUES('1','1','1');";
                        toss1 = 0;
                    }else if(toss1 == 2){
                        str = "INSERT INTO SIDE_TBL VALUES('1','1','2');";
                        toss1 = 0;
                    }else{
                        str = "INSERT INTO SIDE_TBL VALUES('1','1','0');";
                    }
                    left1 = 0;
                    right2 = 1;
                } else if (left2 == 1) {
                    if(toss2 == 1){
                        str = "INSERT INTO SIDE_TBL VALUES('1','2','1');";
                        toss2 = 0;
                    }else if(toss2 == 2){
                        str = "INSERT INTO SIDE_TBL VALUES('1','2','2');";
                        toss2 = 0;
                    }else{
                        str = "INSERT INTO SIDE_TBL VALUES('1','2','0');";
                    }
                    left2 = 0;
                    right1 = 1;
                } else if (right1 == 1) {
                    if(toss1 == 1){
                        str = "INSERT INTO SIDE_TBL VALUES('2','1','1');";
                        toss1 = 0;
                    }else if(toss1 == 2){
                        str = "INSERT INTO SIDE_TBL VALUES('2','1','2');";
                        toss1 = 0;
                    }else{
                        str = "INSERT INTO SIDE_TBL VALUES('2','1','0');";
                    }
                    right1 = 0;
                    left2 = 1;
                } else if (right2 == 1) {
                    if(toss2 == 1){
                        str = "INSERT INTO SIDE_TBL VALUES('2','2','1');";
                        toss2 = 0;
                    }else if(toss2 == 2){
                        str = "INSERT INTO SIDE_TBL VALUES('2','2','2');";
                        toss2 = 0;
                    }else{
                        str = "INSERT INTO SIDE_TBL VALUES('2','2','0');";
                    }
                    right2 = 0;
                    left1 = 1;
                }

                db.execSQL(sql);
                db.execSQL(str);

            }
            }finally{
                db.close();
        }
    }
}