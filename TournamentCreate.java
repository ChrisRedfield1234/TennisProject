package com.example.tennisproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TournamentCreate extends AppCompatActivity {

    private DatabaseHelper helper;

    private String participants = "";

    private String block = "";

    private ArrayList<MatchList_DTO> matchList = new ArrayList<MatchList_DTO>();

    private int int_block;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button returnbtn = findViewById(R.id.returnbtn);
        Button next = findViewById(R.id.next);

        selectTournamentInfo();
        //現在のblockの値を受け取る処理 intent

        //ブロックのデータ受け取り用
        Intent tournament = getIntent();

        if(tournament.getStringExtra("TOURNAMENT_DATA") != null){
            block = tournament.getStringExtra("TOURNAMENT_DATA");
        }else{
            block= "A";
        }

        selectPlayer();

        if(Objects.nonNull(participants) && int_block != 0){
            //MyView myView = new MyView(this);
            //setContentView(myView);
        }

        returnbtn.bringToFront();
        next.bringToFront();

        returnbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, ManagementActivity.class));
        });

        next.setOnClickListener((View v) -> {
            Intent intent = new Intent(this, TournamentCreate.class);
            intent.putExtra("TOURNAMENT_DATA", jumpTournament(block));
            System.out.println(block);
            startActivity(intent);
        });

        /*

        if(Objects.nonNull(participants) && Objects.nonNull(block)){
            MyView myView = new MyView(this);
            setContentView(myView);
        }

         */
    }

    public void selectPlayer(){

        helper = new DatabaseHelper(this);

        try {

            helper.createDatabase();

        } catch (

                IOException e) {

            throw new Error("Unable to create database");

        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 = "SELECT MATCH_ID,TOURNAMENT_ID,OPPONENTS1_ID,OPPONENTS2_ID FROM MATCH_TBL WHERE TOURNAMENT_ID = ?";

        Cursor cursor1 = db.rawQuery(sql1, new String[]{block});

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

        System.out.println("件数：" + matchList.size());

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

    }



    class MyView extends View {
        Paint paint;
        Path path;

        float StrokeWidth1 = 20f;
        float StrokeWidth2 = 40f;
        float dp;

        public MyView(Context context) {
            super(context);
            paint = new Paint();
            path = new Path();

            dp = getResources().getDisplayMetrics().density;
            Log.d("debug", "fdp=" + dp);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            int width = getWidth();
            int height = getHeight();
            //height = 1600がデフォルト
            height = 1300;

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setColor(Color.BLACK);

            // マージンを設定
            int margin = 50;

            //縦線のy終わり値
            int stop_y = 500;

            //参加人数
            double par = Double.parseDouble(participants);
            par = 8;

            int divisor = 4;

            //座標保存リスト
            int[][] intList = new int[(int) par][2];

            // トーナメントの幅を計算（ここでは画面の幅を基準にします）
            //int stepWidth = (width - 2 * margin) / 7;
            int stepWidth = (int) ((width - 2 * margin) / (par - 1));

            int j = 0;

            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(5);
            paint.setTextSize(50);
            paint.setColor(Color.BLACK);

            // 1ラウンド目の線を描画
            for (int i = 0; i < par / 2; i++) {
                int x1 = margin + stepWidth * j;
                int x2 = margin + stepWidth * (j + 1);

                //縦線1
                canvas.drawLine(x1, height - margin, x1, height - margin - 150, paint);

                //縦線2
                canvas.drawLine(x2, height - margin, x2, height - margin - 150, paint);

                //横線1
                canvas.drawLine(x1, height - margin - 150, (x1 + x2) / 2, height - margin - 150, paint);

                //横線2
                canvas.drawLine(x2, height - margin - 150, (x1 + x2) / 2, height - margin - 150, paint);

                //次の試合の縦線
                canvas.drawLine((x1 + x2) / 2, height - margin - 150, (x1 + x2) / 2, height - margin - 300, paint);

                // Textの表示
                canvas.drawText(matchList.get(0).getP_dto1().getLastName(), x1 -30, height, paint);
                canvas.drawText(matchList.get(0).getP_dto2().getLastName(), x2 -30, height, paint);

                intList[i][0] = (x1 + x2) / 2;
                intList[i][1] = height - margin - 300;

                j += 2;

            }

            int vertical = 500;

            while (par / divisor >= 1) {
                j = 0;

                for (int i = 0; i < par / divisor; i++) {

                    int x1 = intList[j][0];
                    int x2 = intList[j + 1][0];
                    int y = intList[j][1];

                    //横線1
                    canvas.drawLine(x1, y, (x1 + x2) / 2, y, paint);

                    //横線2
                    canvas.drawLine(x2, y, (x1 + x2) / 2, y, paint);

                    //次の試合の縦線
                    canvas.drawLine((x1 + x2) / 2, y, (x1 + x2) / 2, height - margin - vertical, paint);

                    intList[i][0] = (x1 + x2) / 2;
                    intList[i][1] = height - margin - vertical;

                    j += 2;

                }

                divisor *= 2;
                vertical += 200;

            }
        }

    }

    public void selectTournamentInfo(){

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "SELECT PARTICIPANTS,BLOCK FROM TOURNAMENT_INFO_TBL;";

        Cursor cursor = db.rawQuery(sql, null);

        cursor.moveToNext();
        participants = cursor.getString(0);
        int_block = Integer.parseInt(cursor.getString(1));

    }


    public String jumpTournament(String block){

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
