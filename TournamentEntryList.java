package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TournamentEntryList extends AppCompatActivity {

    private DatabaseHelper helper;

    private int max;

    private ArrayList<PlayerList_DTO> playerList = new ArrayList<PlayerList_DTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_entry_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        List<Map<String, String>> androidVersionList = selectPlayer();

        Button returnbtn = findViewById(R.id.returnbtn);

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, TournamentEntry.class));
        });

        SimpleAdapter androidVersionListAdapter = new SimpleAdapter(
                getApplicationContext(),
                androidVersionList,
                R.layout.player_list,
                new String[]{"プレイヤーID", "所属高校名", "氏名"},
                new int[]{R.id.platform_version, R.id.api_level, R.id.version_code}
        );

        ListView listView = findViewById(R.id.player_list);

        listView.setAdapter(androidVersionListAdapter);

        Intent intent = new Intent(this, TournamentEntry.class);
        Intent intent2 = getIntent();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> itemMap = (Map<String, String>) listView.getItemAtPosition(position);
                String player_Id = itemMap.get("プレイヤーID");
                String match[] = intent2.getStringArrayExtra("EXTRA_DATA");

                //DB更新処理
                updateMatch(player_Id,match);

                startActivity(intent);
                //showDialog(view);
            }
        });

    }

    public List<Map<String, String>> selectPlayer(){

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT PLAYER_ID, PLAYER_LAST_NAME, PLAYER_FIRST_NAME, GROUP_NAME\n" +
                "FROM PLAYER_TBL\n" +
                "INNER JOIN GROUP_TBL ON PLAYER_TBL.GROUP_ID = GROUP_TBL.GROUP_ID\n" +
                "WHERE PLAYER_ID NOT IN (SELECT OPPONENTS1_ID FROM MATCH_TBL)\n" +
                "AND PLAYER_ID NOT IN (SELECT OPPONENTS2_ID FROM MATCH_TBL) " +
                "AND ABTENTION_FLAG = '0'";

        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            PLAYER_DTO dto = new PLAYER_DTO();
            PlayerList_DTO dto2 = new PlayerList_DTO();
            dto.setPlayer_Id(cursor.getString(0));
            dto.setLastName(cursor.getString(1));
            dto.setFirstName(cursor.getString(2));
            dto2.setDto(dto);
            dto2.setGroup_Name(cursor.getString(3));
            playerList.add(dto2);
        }

        return getPlayer(cursor.getCount());

    }

    public List<Map<String, String>> getPlayer(int length){

        String[] playerIdList = new String[length];

        String[] groupNameList = new String[length];

        String[] playerNameList = new String[length];

        int count = 0;

        for(PlayerList_DTO dto:playerList){
            //カウント増やしながら配列にデータを入れる処理を書く
            PLAYER_DTO dto2 = dto.getDto();
            playerIdList[count] = dto2.getPlayer_Id();
            groupNameList[count] = dto.getGroup_Name();
            playerNameList[count] = dto2.getLastName() + dto2.getFirstName();
            count++;

        }

        List<Map<String, String>> list = new ArrayList<>();

        for (int i = 0; i < playerIdList.length; i++) {
            Map<String, String> data = new HashMap<>();
            data.put("プレイヤーID", playerIdList[i]);
            data.put("所属高校名", groupNameList[i]);
            data.put("氏名", playerNameList[i]);

            list.add(data);

        }

        return list;

    }


    public void updateMatch(String player_Id,String match[]){

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "";

        if(Integer.parseInt(match[0]) > Integer.parseInt(match[2]) / 2){

            onPostExecute("この試合は編集できません");

        }else{

            if(match[1].equals("1")){

                sql ="UPDATE MATCH_TBL SET OPPONENTS1_ID = ? WHERE MATCH_ID = ?;";

            }else if(match[1].equals("2")){

                sql ="UPDATE MATCH_TBL SET OPPONENTS2_ID = ? WHERE MATCH_ID = ?;";

            }

            //試合IDに一致した行の対戦IDを更新
            db.execSQL(sql, new String[]{player_Id,match[0]});

        }


    }

    protected void onPostExecute(Object obj) {
        //画面にメッセージを表示する
        Toast.makeText(TournamentEntryList.this,(String)obj,Toast.LENGTH_LONG).show();
    }

}
