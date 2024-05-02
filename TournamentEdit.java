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

    }

    public ArrayAdapter<String> setSpinner(ArrayAdapter<String> adapter){
        for(int i = 1;i < 5;i++){
            adapter.add(String.valueOf(i));
        }

        return adapter;
    }

    public void saveinfo(){
        TextView participants = findViewById(R.id.participants);
        Spinner spinner = findViewById(R.id.block);

        String par = String.valueOf(participants.getText());
        String block = String.valueOf(spinner.getSelectedItem());

        if(par.isEmpty() || block.isEmpty()){
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

            String sql = "UPDATE TOURNAMENT_INFO_TBL SET PARTICIPANTS = ?,BLOCK = ? WHERE TOURNAMENT_INFO_ID = '1'";

            db.execSQL(sql, new String[]{par,block});

            onPostExecute("トーナメント情報の保存が完了しました");
        }

    }





    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(TournamentEdit.this,(String)obj,Toast.LENGTH_LONG).show();
    }

}
