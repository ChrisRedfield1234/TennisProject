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
    private String player_Id;
    private String tournament_Id;

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

        Intent intent2 = new Intent(this, PlayerEditActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> itemMap = (Map<String, String>) listView.getItemAtPosition(position);
                if(edit_Flag.get()){
                    player_Id = itemMap.get("プレイヤーID");
                    intent2.putExtra("EXTRA_DATA",player_Id);
                    startActivity(intent2);
                }else if(delete_Flag.get()){
                    player_Id = itemMap.get("プレイヤーID");
                    showDialog(view);

                }

            }
        });

    }

    public void showDialog(View view) {
        DialogFragment dialogFragment = new EntryDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog");

    }

    public void onReturnValue(String value) {

        if(value.equals("true")){
            String list[] = updateAbtention(player_Id);
            addMatch(list[0],list[1]);

        }

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

        String sql = "SELECT PLAYER_ID,PLAYER_LAST_NAME,PLAYER_FIRST_NAME,GROUP_NAME FROM PLAYER_TBL INNER JOIN GROUP_TBL ON PLAYER_TBL.GROUP_ID = GROUP_TBL.GROUP_ID WHERE ABTENTION_FLAG = '0';";

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

        db.close();
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

    public String[] updateAbtention(String player_Id){

        String re_str[] = new String[2];

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 = "UPDATE PLAYER_TBL SET LOSER_FLAG = '1' , ABTENTION_FLAG = '1' WHERE PLAYER_ID = ?;";

        db.execSQL(sql1, new String[]{player_Id});

        //String sql2 = "UPDATE MATCH_TBL SET OPPONENTS1_ID = '0' WHERE OPPONENTS1_ID = ?;";

        //db.execSQL(sql2, new String[]{player_Id});

        //String sql3 = "UPDATE MATCH_TBL SET OPPONENTS2_ID = '0' WHERE OPPONENTS2_ID = ?;";

        //db.execSQL(sql3, new String[]{player_Id});

        String sql4 = "SELECT MAX(MATCH_ID),TOURNAMENT_ID,OPPONENTS1_ID,OPPONENTS2_ID FROM MATCH_TBL WHERE OPPONENTS1_ID = ? OR OPPONENTS2_ID = ?;";

        Cursor cursor = db.rawQuery(sql4, new String[]{player_Id,player_Id});

        cursor.moveToNext();
        String match_Id = cursor.getString(0);
        tournament_Id = cursor.getString(1);
        String op_Id1 = cursor.getString(2);
        String op_Id2 = cursor.getString(3);
        System.out.println("player_Id：" + player_Id);
        System.out.println("match_Id：" + match_Id);
        System.out.println("op_Id1" + op_Id1);
        System.out.println("op_Id2" + op_Id2);

        String sql5 = "UPDATE MATCH_TBL SET V_OPPONENTS_ID = ? WHERE MATCH_ID = ?";

        re_str[0] = match_Id;

        if(op_Id1.equals(player_Id)){

            db.execSQL(sql5, new String[]{op_Id2,match_Id});
            re_str[1] = op_Id2;

        }else if(op_Id2.equals(player_Id)){

            db.execSQL(sql5, new String[]{op_Id1,match_Id});
            re_str[1] = op_Id1;

        }

        db.close();
        return re_str;

    }



    public void addMatch(String match_Id,String player_Id){

        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 = "SELECT PARTICIPANTS,BLOCK FROM TOURNAMENT_INFO_TBL;";

        Cursor cursor1 = db.rawQuery(sql1, null);

        cursor1.moveToNext();
        int participants = Integer.parseInt(cursor1.getString(0));
        String block = cursor1.getString(1);

        Map<Integer, Integer> nextMatchMapping = generateNextMatchMappings(participants);
        int nextMatch = getNextMatchNumber(Integer.parseInt(match_Id),nextMatchMapping);

        String next = "";

        if(nextMatch < 10){
            next = "00" + nextMatch;
        }else if(nextMatch >= 10){
            next = "0" + nextMatch;
        }else{
            next = String.valueOf(nextMatch);
        }

        String sql2 = "SELECT COUNT(*) FROM MATCH_TBL WHERE MATCH_ID = ?";

        Cursor cursor2 = db.rawQuery(sql2, new String[]{next});

        if(block.equals("1") && nextMatch == participants - 1 || block.equals("2") && nextMatch >= participants -2 || block.equals("4") && nextMatch >= participants -3){
            tournament_Id = "E";
        }

        System.out.println("tournament_Id：" + tournament_Id);

        cursor2.moveToNext();
        if(cursor2.getString(0).equals("0") && Integer.parseInt(match_Id) % 2 == 1){
            System.out.println("A");

            String sql3 = "INSERT INTO MATCH_TBL VALUES(?,?,?,0,0,0,1,0,null,null);";
            db.execSQL(sql3,new String[]{next,tournament_Id,player_Id});

        }else if(cursor2.getString(0).equals("0") && Integer.parseInt(match_Id) % 2 == 0){
            System.out.println("B");

            String sql3 = "INSERT INTO MATCH_TBL VALUES(?,?,0,?,0,0,1,0,null,null);";
            db.execSQL(sql3,new String[]{next,tournament_Id,player_Id});

        }else if(cursor2.getString(0).equals("1") && Integer.parseInt(match_Id) % 2 == 1){
            System.out.println("C");

            String sql3 = "UPDATE MATCH_TBL SET OPPONENTS1_ID = ? WHERE MATCH_ID = ?;";
            db.execSQL(sql3,new String[]{player_Id,next});

        }else if(cursor2.getString(0).equals("1") && Integer.parseInt(match_Id) % 2 == 0){
            System.out.println("D");

            String sql3 = "UPDATE MATCH_TBL SET OPPONENTS2_ID = ? WHERE MATCH_ID = ?;";
            db.execSQL(sql3,new String[]{player_Id,next});

        }

    }

    private static Map<Integer, Integer> generateNextMatchMappings(int numPlayers) {
        Map<Integer, Integer> nextMatchMapping = new HashMap<>();
        int totalMatches = numPlayers - 1;
        int firstRoundMatches = numPlayers / 2;
        int matchCounter = firstRoundMatches + 1;

        // 1回戦のマッピング
        for (int i = 1; i <= firstRoundMatches; i += 2) {
            nextMatchMapping.put(i, matchCounter);
            nextMatchMapping.put(i + 1, matchCounter);
            matchCounter++;
        }

        int matchesInRound = firstRoundMatches / 2;

        // 残りのラウンドのマッピング
        int baseIndex = firstRoundMatches + 1;
        while (matchesInRound > 0) {
            int startMatch = baseIndex;
            for (int i = 0; i < matchesInRound; i += 2) {
                nextMatchMapping.put(startMatch + i, matchCounter);
                nextMatchMapping.put(startMatch + i + 1, matchCounter);
                matchCounter++;
            }
            baseIndex += matchesInRound;
            matchesInRound /= 2;
        }

        return nextMatchMapping;
    }


    private static int getNextMatchNumber(int finishedMatch,Map<Integer, Integer> nextMatchMapping) {
        return nextMatchMapping.getOrDefault(finishedMatch, -1);
    }


}
