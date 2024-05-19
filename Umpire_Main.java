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

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Umpire_Main extends AppCompatActivity {

    private DatabaseHelper helper;

    public static String umpire_Id;

    private ArrayList<MatchList_DTO> matchList = new ArrayList<MatchList_DTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.umpire_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button returnbtn = findViewById(R.id.returnbtn);

        Intent tournament = getIntent();

        umpire_Id = tournament.getStringExtra("EXTRA_DATA");

        List<Map<String, String>> tournament_List = selectAssignMatch(umpire_Id);

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        SimpleAdapter androidVersionListAdapter = new SimpleAdapter(
                getApplicationContext(),
                tournament_List,
                R.layout.umpire_list,
                new String[]{"試合番号","トーナメントID", "選手ID１", "選手名１", "選手ID２", "選手名２"},
                new int[]{R.id.matchid, R.id.tournamentid,R.id.opponents1_Id, R.id.opponents1_Name, R.id.opponents2_Id, R.id.opponents2_Name}
        );

        ListView listView = findViewById(R.id.tournament_list);

        listView.setAdapter(androidVersionListAdapter);

        //遷移先のアクティビティーを指定
        Intent list = new Intent(this, MainActivity.class);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> itemMap = (Map<String, String>) listView.getItemAtPosition(position);
                list.putExtra("EXTRA_DATA",itemMap.get("試合番号"));
                startActivity(list);
            }

        });

    }

    public List<Map<String, String>> selectAssignMatch(String umpire_Id){

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 = "SELECT MATCH_ID,TOURNAMENT_ID,OPPONENTS1_ID,OPPONENTS2_ID FROM MATCH_TBL WHERE UMPIRE_ID = ? AND V_OPPONENTS_ID = 0";

        Cursor cursor1 = db.rawQuery(sql1, new String[]{umpire_Id});

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

            p_dto1.setLastName(cursor2.getString(0));
            p_dto1.setFirstName(cursor2.getString(1));

            dto.setP_dto1(p_dto1);

            Cursor cursor3 = db.rawQuery(sql2, new String[]{dto.getM_dto().getOpponents2()});

            cursor3.moveToNext();
            PLAYER_DTO p_dto2 = new PLAYER_DTO();

            p_dto2.setLastName(cursor3.getString(0));
            p_dto2.setFirstName(cursor3.getString(1));

            dto.setP_dto2(p_dto2);

        }

        return getMatch(cursor1.getCount());

    }

    public List<Map<String, String>> getMatch(int length){

        String[] match_Id_List = new String[length];

        String[] tounament_Id_List = new String[length];

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
            tounament_Id_List[count] = m_dto.getTournament_Id();
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
            data.put("トーナメントID", tounament_Id_List[i]);
            data.put("選手ID１", opponents1_Id_List[i]);
            data.put("選手名１", opponents1_Name_List[i]);
            data.put("選手ID２", opponents2_Id_List[i]);
            data.put("選手名２", opponents2_Name_List[i]);

            list.add(data);

        }

        return list;

    }

}
