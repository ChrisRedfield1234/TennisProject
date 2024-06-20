package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

public class TournamentEdit extends AppCompatActivity {
    private DatabaseHelper helper;

    public static String participants = "32";

    public static String block = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_edit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button returnbtn = findViewById(R.id.returnbtn);
        Button savebtn = findViewById(R.id.save);

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, ManagementActivity.class));
        });

        savebtn.setOnClickListener((View v) -> {
            saveinfo();
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter = setSpinner(adapter);
        Spinner block = (Spinner) findViewById(R.id.block);
        block.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2 = setSpinner2(adapter2);
        Spinner participants =  (Spinner) findViewById(R.id.participants);
        participants.setAdapter(adapter2);

    }

    public ArrayAdapter<String> setSpinner(ArrayAdapter<String> adapter){
        for(int i = 1;i < 5;i++){
            adapter.add(String.valueOf(i));
        }

        return adapter;
    }

    public ArrayAdapter<String> setSpinner2(ArrayAdapter<String> adapter){
        adapter.add("32");
        adapter.add("64");
        return adapter;
    }

    public void saveinfo(){
        Spinner spinner2 = findViewById(R.id.participants);
        Spinner spinner = findViewById(R.id.block);

        participants = String.valueOf(spinner2.getSelectedItem());
        block = String.valueOf(spinner.getSelectedItem());

        if(participants.isEmpty() || block.isEmpty()){
            onPostExecute("入力情報が不足しています");
        }else{
            helper = new DatabaseHelper(this);

            try {
                helper.createDatabase();
            } catch (
                    IOException e) {
                throw new Error("Unable to create database");
            }

            SQLiteDatabase db = helper.getWritableDatabase();

            String sql1 = "UPDATE TOURNAMENT_INFO_TBL SET PARTICIPANTS = ?,BLOCK = ? WHERE TOURNAMENT_INFO_ID = '1'";

            db.execSQL(sql1, new String[]{participants,block});

            String sql2 = "DELETE FROM MATCH_TBL;";

            db.execSQL(sql2);

            int j = 1;
            String p = "";
            String b = "";

            for(int i = 1;i <= Integer.parseInt(participants)/2;i++){

                if(i > ((Integer.parseInt(participants) / 2) / Integer.parseInt(block)) * j){
                    j++;
                }

                if(i < 10){
                    p = "00" + String.valueOf(i);
                }else if(i >= 10){
                    p = "0" + String.valueOf(i);
                }else{
                    p = String.valueOf(i);
                }

                if(j == 1){
                    b = "A";
                }else if(j == 2){
                    b = "B";
                }else if(j == 3){
                    b = "C";
                }else if(j == 4){
                    b = "D";
                }

                String sql3 = "INSERT INTO MATCH_TBL VALUES(?,?,0,0,0,1,1,0,null,null);";

                db.execSQL(sql3, new String[]{p,b});

            }

            String sql4 = "DELETE FROM PLAYER_TBL;";

            db.execSQL(sql4);

            String player_Id;

            for(int i = 1;i <= Integer.parseInt(participants);i++){
                String sql5 = "INSERT INTO PLAYER_TBL VALUES(?,0,0,0,0,0);";

                if(i < 10){
                    player_Id = "00" + String.valueOf(i);
                }else if(i < 100){
                    player_Id = "0" + String.valueOf(i);
                }else{
                    player_Id = String.valueOf(i);
                }

                db.execSQL(sql5, new String[]{player_Id});

            }

            onPostExecute("トーナメント情報の保存が完了しました");

            db.close();

        }

    }

    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(TournamentEdit.this,(String)obj,Toast.LENGTH_LONG).show();
    }

}
