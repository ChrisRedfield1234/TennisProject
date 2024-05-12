package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchAssign extends AppCompatActivity {

    private DatabaseHelper helper;

    private ArrayList<User_DTO> userList = new ArrayList<User_DTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_assign);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        List<Map<String, String>> androidVersionList = selectUser();

        SimpleAdapter androidVersionListAdapter = new SimpleAdapter(
                getApplicationContext(),
                androidVersionList,
                R.layout.user_list,
                new String[]{"ユーザーID", "ユーザー名"},
                new int[]{R.id.userid, R.id.username}
        );

        ListView listView = findViewById(R.id.user_list);

        listView.setAdapter(androidVersionListAdapter);

        Intent match = getIntent();
        String match_Id = match.getStringExtra("EXTRA_DATA");
        Intent intent = new Intent(this, TournamentEntry.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> itemMap = (Map<String, String>) listView.getItemAtPosition(position);
                updateMatch(itemMap.get("ユーザーID"),match_Id);
                startActivity(intent);
            }
        });



    }

    public List<Map<String, String>> selectUser(){
        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT USER_ID,USER_NAME FROM USER_TBL WHERE USER_ID >= '02';";

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()) {
            User_DTO dto = new User_DTO();
            dto.setUser_Id(cursor.getString(0));
            dto.setUser_Name(cursor.getString(1));
            userList.add(dto);
        }

        return getUser(cursor.getCount());

    }

    public List<Map<String, String>> getUser(int length){

        String[] userIdList = new String[length];

        String[] userNameList = new String[length];

        int count = 0;

        for(User_DTO dto:userList){
            //カウント増やしながら配列にデータを入れる処理を書く
            userIdList[count] = dto.getUser_Id();
            userNameList[count] = dto.getUser_Name();
            count++;

        }

        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < userNameList.length; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("ユーザーID", userIdList[i]);
            data.put("ユーザー名", userNameList[i]);
            list.add(data);

        }

        return list;

    }

    public void updateMatch(String user_Id,String match){

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql ="UPDATE MATCH_TBL SET UMPIRE_ID = ? WHERE MATCH_ID = ?;";

        //試合IDに一致した行の対戦IDを更新
        db.execSQL(sql, new String[]{user_Id,match});


    }

}
