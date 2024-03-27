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
import android.widget.TextView;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    MATCH_DTO m_dto = new MATCH_DTO();
    ArrayList<PLAYER_DTO> playerList = new ArrayList<PLAYER_DTO>();
    public static String player_Id1;
    public static String player_Id2;
    public static String player_Last_Name1;
    public static String player_Last_Name2;
    public static String player_First_Name1;
    public static String player_First_Name2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        helper = new DatabaseHelper(this);
        resetDB();
        selectMatch();
        selectPlayername();
        setName();

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

    public void selectMatch(){
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        //String sql1 = "UPDATE MATCH_TBL SET START_TIME = CURRENT_TIMESTAMP WHERE MATCH_ID = 2;";
        //String sql2 = "SELECT * FROM MATCH_TBL WHERE MATCH_ID = 2;";
        String sql1 = "UPDATE MATCH_TBL SET START_TIME = CURRENT_TIMESTAMP WHERE MATCH_ID = 1;";
        String sql2 = "SELECT * FROM MATCH_TBL WHERE MATCH_ID = 1;";
        db.execSQL(sql1);
        Cursor cursor = db.rawQuery(sql2, null);

        while (cursor.moveToNext()) {
            m_dto.setMatch_Id(cursor.getString(0));
            m_dto.setOpponents1(cursor.getString(1));
            m_dto.setOpponents2(cursor.getString(2));
            m_dto.setC_Umpire_Id(cursor.getString(4));
            m_dto.setUmpire_Id(cursor.getString(5));
            m_dto.setCourt_Id(cursor.getString(6));
            m_dto.setDoubles_Flag(cursor.getString(7));
            m_dto.setStart_Time(cursor.getString(8));
        }
        System.out.println("プレイヤー１：" + m_dto.getOpponents1());
        System.out.println("プレイヤー２：" + m_dto.getOpponents2());
    }

    public void selectPlayername(){
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        player_Id1 = m_dto.getOpponents1();
        player_Id2 = m_dto.getOpponents2();
        String sql = "SELECT PLAYER_LAST_NAME,PLAYER_FIRST_NAME FROM PLAYER_TBL WHERE PLAYER_ID IN (" + player_Id1 +","+ player_Id2 +");";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            PLAYER_DTO p_dto = new PLAYER_DTO();
            p_dto.setLastName(cursor.getString(0));
            p_dto.setFirstName(cursor.getString(1));
            playerList.add(p_dto);
        }

        player_Last_Name1 = playerList.get(0).getLastName();
        player_First_Name1 = playerList.get(0).getFirstName();
        player_Last_Name2 = playerList.get(1).getLastName();
        player_First_Name2 = playerList.get(1).getFirstName();
    }


    public void setName(){
        TextView player1 = (TextView)findViewById(R.id.player1);
        TextView player2 = (TextView)findViewById(R.id.player2);

        player1.setText(player_Last_Name1 + player_First_Name1);
        player2.setText(player_Last_Name2 + player_First_Name2);

    }


    public void resetDB(){
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql1 = "DELETE FROM POINT_TBL;";
        String sql2 = "UPDATE GAME_TBL SET V_OPPONENTS_ID = 0, START_TIME = NULL, END_TIME = NULL;";
        String sql3 = "UPDATE SET_TBL SET V_OPPONENTS_ID = 0, START_TIME = NULL, END_TIME = NULL;";
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