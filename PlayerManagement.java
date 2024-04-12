package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerManagement extends AppCompatActivity {
    private DatabaseHelper helper;
    private ArrayList<PlayerList_DTO> playerList = new ArrayList<PlayerList_DTO>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_management);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        List<Map<String, String>> androidVersionList = selectPlayer();

        Button returnbtn = findViewById(R.id.returnbtn);
        Button editbtn = findViewById(R.id.edit);
        Button deletebtn = findViewById(R.id.delete);
        AtomicBoolean edit_Flag = new AtomicBoolean(false);
        AtomicBoolean delete_Flag = new AtomicBoolean(false);

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, ManagementActivity.class));
        });

        editbtn.setOnClickListener((View v) -> {
            if(!edit_Flag.get()){

                editbtn.setBackgroundColor(Color.RED);
                edit_Flag.set(true);
                deletebtn.setBackgroundColor(Color.BLUE);
                delete_Flag.set(false);

            }else if(edit_Flag.get()){
                editbtn.setBackgroundColor(Color.BLUE);
                edit_Flag.set(false);
            }

        });

        deletebtn.setOnClickListener((View v) -> {
            if(!delete_Flag.get()){

                deletebtn.setBackgroundColor(Color.RED);
                delete_Flag.set(true);
                editbtn.setBackgroundColor(Color.BLUE);
                edit_Flag.set(false);

            }else if(delete_Flag.get()){

                deletebtn.setBackgroundColor(Color.BLUE);
                delete_Flag.set(false);

            }


        });

        SimpleAdapter androidVersionListAdapter = new SimpleAdapter(
                getApplicationContext(),
                androidVersionList,
                R.layout.player_list,
                new String[]{"プレイヤーID", "所属高校名", "氏名"},
                new int[]{R.id.platform_version, R.id.api_level, R.id.version_code}
        );

        ListView listView = findViewById(R.id.android_version_list);

        listView.setAdapter(androidVersionListAdapter);

        Intent intent = new Intent(this, PlayerEditActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> itemMap = (Map<String, String>) listView.getItemAtPosition(position);
                if(edit_Flag.get()){
                    String player_Id = itemMap.get("プレイヤーID");
                    intent.putExtra("EXTRA_DATA",player_Id);
                    startActivity(intent);
                }
                //showDialog(view);
            }
        });



    }

    public void showDialog(View view) {
        DialogFragment dialogFragment = new PlayerDelete_Dialog();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog");

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

        String sql = "SELECT PLAYER_ID,PLAYER_LAST_NAME,PLAYER_FIRST_NAME,GROUP_NAME FROM PLAYER_TBL INNER JOIN GROUP_TBL ON PLAYER_TBL.GROUP_ID = GROUP_TBL.GROUP_ID;";

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
}
