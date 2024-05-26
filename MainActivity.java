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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    MATCH_DTO m_dto = new MATCH_DTO();
    ArrayList<PLAYER_DTO> playerList = new ArrayList<PLAYER_DTO>();
    public static String match_Id;
    public static String tournament_Id;
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

        Button startbtn = findViewById(R.id.start);
        Button reset = findViewById(R.id.reset);

        Intent tournament = getIntent();

        match_Id = tournament.getStringExtra("EXTRA_DATA");

        selectMatch(match_Id);
        selectPlayername();
        resetDB();
        insertGame(match_Id);
        setName();

        Intent intent = new Intent(this, TossActivity.class);

        startbtn.setOnClickListener((View v) -> {

            intent.putExtra("EXTRA_DATA",new String[]{player_Id1,player_Id2});
            startActivity(intent);

        });

        reset.setOnClickListener((View v) -> {

            resetDB();

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

    public void selectMatch(String match_Id){
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        String sql1 = "UPDATE MATCH_TBL SET START_TIME = CURRENT_TIMESTAMP WHERE MATCH_ID = ?;";
        String sql2 = "SELECT MATCH_ID,TOURNAMENT_ID,OPPONENTS1_ID,OPPONENTS2_ID,UMPIRE_ID FROM MATCH_TBL WHERE MATCH_ID = ?;";
        db.execSQL(sql1,new String[]{match_Id});
        Cursor cursor = db.rawQuery(sql2, new String[]{match_Id});

        while (cursor.moveToNext()) {
            m_dto.setMatch_Id(cursor.getString(0));
            m_dto.setTournament_Id(cursor.getString(1));
            m_dto.setOpponents1(cursor.getString(2));
            m_dto.setOpponents2(cursor.getString(3));
            m_dto.setUmpire_Id(cursor.getString(4));
        }

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
        tournament_Id = m_dto.getTournament_Id();
        String sql = "SELECT PLAYER_LAST_NAME,PLAYER_FIRST_NAME FROM PLAYER_TBL WHERE PLAYER_ID IN (?,?);";

        Cursor cursor = db.rawQuery(sql, new String[]{player_Id1,player_Id2});

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
        } finally {
            db.close();
        }
    }

    public void insertGame(String match_Id){

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        String sql1 = "INSERT INTO SET_TBL VALUES(1,1,0,null,null);";

        db.execSQL(sql1);

        String sql2 = "INSERT INTO GAME_TBL VALUES(?,?,?,'0',null,null);";

        for(int i = 0;i < 14;i++){

            String game_Id = "";

            if(i < 10){
                game_Id = "0" + String.valueOf(i);
            }else{
                game_Id = String.valueOf(i);
            }

            db.execSQL(sql2,new String[]{match_Id,"1",game_Id});

        }

    }

}