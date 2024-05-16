package com.example.tennisproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class TournamentEntry extends AppCompatActivity {

    private DatabaseHelper helper;
    private ArrayList<MatchList_DTO> matchList = new ArrayList<MatchList_DTO>();

    private int int_block;

    private String participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_entry);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button returnbtn = findViewById(R.id.returnbtn);
        Button editbtn1 = findViewById(R.id.edit1);
        Button editbtn2 = findViewById(R.id.edit2);
        Button next = findViewById(R.id.next);
        Button assign = findViewById(R.id.assign);

        AtomicBoolean edit_Flag1 = new AtomicBoolean(false);
        AtomicBoolean edit_Flag2 = new AtomicBoolean(false);
        AtomicBoolean assign_Flag = new AtomicBoolean(false);

        selectTournamentInfo();

        String t_block= "";

        //ブロックのデータ受け取り用
        Intent tournament = getIntent();

        if(tournament.getStringExtra("TOURNAMENT_DATA") != null){
            t_block = tournament.getStringExtra("TOURNAMENT_DATA");
        }else{
            t_block= "A";
        }

        List<Map<String, String>> tournament_List = selectPlayer(t_block);

        SimpleAdapter androidVersionListAdapter = new SimpleAdapter(
                getApplicationContext(),
                tournament_List,
                R.layout.tournament_list,
                new String[]{"試合番号", "選手ID１", "選手名１", "選手ID２", "選手名２"},
                new int[]{R.id.matchid, R.id.opponents1_Id, R.id.opponents1_Name, R.id.opponents2_Id, R.id.opponents2_Name}
        );

        editbtn1.setOnClickListener((View v) -> {
            if(!edit_Flag1.get()){

                editbtn1.setBackgroundColor(Color.RED);
                edit_Flag1.set(true);
                editbtn2.setBackgroundColor(Color.BLUE);
                edit_Flag2.set(false);
                assign.setBackgroundColor(Color.BLUE);
                assign_Flag.set(false);

            }else if(edit_Flag1.get()){
                editbtn1.setBackgroundColor(Color.BLUE);
                edit_Flag1.set(false);
            }

        });

        editbtn2.setOnClickListener((View v) -> {
            if (!edit_Flag2.get()) {

                editbtn2.setBackgroundColor(Color.RED);
                edit_Flag2.set(true);
                editbtn1.setBackgroundColor(Color.BLUE);
                edit_Flag1.set(false);
                assign.setBackgroundColor(Color.BLUE);
                assign_Flag.set(false);

            } else if (edit_Flag2.get()) {

                editbtn2.setBackgroundColor(Color.BLUE);
                edit_Flag2.set(false);

            }
        });

        assign.setOnClickListener((View v) -> {
            if(!assign_Flag.get()){
                assign.setBackgroundColor(Color.RED);
                assign_Flag.set(true);
                editbtn1.setBackgroundColor(Color.BLUE);
                edit_Flag1.set(false);
                editbtn2.setBackgroundColor(Color.BLUE);
                edit_Flag2.set(false);

            }else if(assign_Flag.get()){

                assign.setBackgroundColor(Color.BLUE);
                assign_Flag.set(false);

            }

        });

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, ManagementActivity.class));
        });


        String finalT_block = t_block;

        next.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, TournamentEntry.class);
            intent.putExtra("TOURNAMENT_DATA", jumpTournament(finalT_block,false));
            startActivity(intent);
        });

            ListView listView = findViewById(R.id.tournament_list);

            listView.setAdapter(androidVersionListAdapter);

        //遷移先のアクティビティーを指定
        Intent list = new Intent(this, TournamentEntryList.class);
        Intent assing = new Intent(this, MatchAssign.class);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Map<String, String> itemMap = (Map<String, String>) listView.getItemAtPosition(position);
                    String match_Id = "";
                    String player[] = {};
                    if(edit_Flag1.get()){
                        player = new String[]{itemMap.get("試合番号"), "1",participants};
                        list.putExtra("EXTRA_DATA",player);
                        startActivity(list);
                    }else if(edit_Flag2.get()){
                        player = new String[]{itemMap.get("試合番号"), "2",participants};
                        list.putExtra("EXTRA_DATA",player);
                        startActivity(list);
                    }else if(assign_Flag.get()){

                        if(!itemMap.get("選手ID１").equals("0") || !itemMap.get("選手ID２").equals("0")){

                            match_Id = itemMap.get("試合番号");
                            assing.putExtra("EXTRA_DATA",match_Id);
                            startActivity(assing);

                        }

                    }
                }
            });

        }

        public List<Map<String, String>> selectPlayer(String t_block){

            helper = new DatabaseHelper(this);

            try {
                helper.createDatabase();
            } catch (
                    IOException e) {
                throw new Error("Unable to create database");
            }

            SQLiteDatabase db = helper.getReadableDatabase();

            //String sql = "SELECT PLAYER_ID,PLAYER_LAST_NAME,PLAYER_FIRST_NAME,GROUP_NAME FROM PLAYER_TBL INNER JOIN GROUP_TBL ON PLAYER_TBL.GROUP_ID = GROUP_TBL.GROUP_ID;";
            String sql1 = "SELECT MATCH_ID,TOURNAMENT_ID,OPPONENTS1_ID,OPPONENTS2_ID FROM MATCH_TBL WHERE TOURNAMENT_ID = ?";

            Cursor cursor1 = db.rawQuery(sql1, new String[]{t_block});

            while(cursor1.moveToNext()){
                MatchList_DTO dto = new MatchList_DTO();
                MATCH_DTO dto2 = new MATCH_DTO();
                dto2.setMatch_Id(cursor1.getString(0));
                dto2.setTournament_Id(cursor1.getString(1));
                dto2.setOpponents1(cursor1.getString(2));
                dto2.setOpponents2(cursor1.getString(3));
                dto.setM_dto(dto2);
                matchList.add(dto);
            }

            for(MatchList_DTO dto:matchList){

                String sql2 = "SELECT PLAYER_LAST_NAME,PLAYER_FIRST_NAME FROM PLAYER_TBL WHERE PLAYER_ID =?;";

                Cursor cursor2 = db.rawQuery(sql2, new String[]{dto.getM_dto().getOpponents1()});

                cursor2.moveToNext();
                PLAYER_DTO p_dto1 = new PLAYER_DTO();

                if(cursor2.getCount() != 0){
                    p_dto1.setLastName(cursor2.getString(0));
                    p_dto1.setFirstName(cursor2.getString(1));
                }else{
                    p_dto1.setLastName("");
                    p_dto1.setFirstName("");
                }

                dto.setP_dto1(p_dto1);

                Cursor cursor3 = db.rawQuery(sql2, new String[]{dto.getM_dto().getOpponents2()});

                cursor3.moveToNext();
                PLAYER_DTO p_dto2 = new PLAYER_DTO();

                if(cursor3.getCount() != 0){
                    p_dto2.setLastName(cursor3.getString(0));
                    p_dto2.setFirstName(cursor3.getString(1));
                }else{
                    p_dto2.setLastName("");
                    p_dto2.setFirstName("");
                }

                dto.setP_dto2(p_dto2);

            }

            return getMatch(cursor1.getCount());

        }

        public List<Map<String, String>> getMatch(int length){

            String[] match_Id_List = new String[length];

            String[] opponents1_Id_List = new String[length];

            String[] opponents1_Name_List = new String[length];

            String[] opponents2_Id_List = new String[length];

            String[] opponents2_Name_List = new String[length];

            int count = 0;

            for(MatchList_DTO dto:matchList){
                //カウント増やしながら配列にデータを入れる処理を書く
                PLAYER_DTO p_dto1 = dto.getP_dto1();
                PLAYER_DTO p_dto2 = dto.getP_dto2();
                MATCH_DTO m_dto = dto.getM_dto();

                match_Id_List[count] = m_dto.getMatch_Id();
                opponents1_Id_List[count] = m_dto.getOpponents1();
                opponents1_Name_List[count] = p_dto1.getLastName() + p_dto1.getFirstName();
                opponents2_Id_List[count] = m_dto.getOpponents2();
                opponents2_Name_List[count] = p_dto2.getLastName() + p_dto2.getFirstName();
                count++;

            }

            List<Map<String, String>> list = new ArrayList<>();

            for (int i = 0; i < match_Id_List.length; i++) {
                Map<String, String> data = new HashMap<>();
                data.put("試合番号", match_Id_List[i]);
                data.put("選手ID１", opponents1_Id_List[i]);
                data.put("選手名１", opponents1_Name_List[i]);
                data.put("選手ID２", opponents2_Id_List[i]);
                data.put("選手名２", opponents2_Name_List[i]);

                list.add(data);

            }

            return list;

        }

        public void selectTournamentInfo(){

            helper = new DatabaseHelper(this);

            try {
                helper.createDatabase();
            } catch (
                    IOException e) {
                throw new Error("Unable to create database");
            }

            SQLiteDatabase db = helper.getReadableDatabase();

            String sql = "SELECT * FROM TOURNAMENT_INFO_TBL;";

            Cursor cursor = db.rawQuery(sql, null);

            cursor.moveToNext();
            participants = cursor.getString(1);
            int_block = Integer.parseInt(cursor.getString(2));

        }

        public String jumpTournament(String block,boolean flag){

            if(block.equals("A") && int_block >= 2){

                block = "B";

            }else if(block.equals("B") && int_block == 2){

                block = "A";

            }else if(block.equals("B") && int_block >= 3) {

                block = "C";

            }else if(block.equals("C") && int_block == 3){

                block = "A";

            }else if(block.equals("C") && int_block >= 4){

                block = "D";

            }else if(block.equals("D")){

                block = "A";

            }

            return block;
        }


    }