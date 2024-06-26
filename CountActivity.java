package com.example.tennisproject;

import static com.example.tennisproject.MainActivity.player_First_Name1;
import static com.example.tennisproject.MainActivity.player_First_Name2;
import static com.example.tennisproject.MainActivity.player_Id1;
import static com.example.tennisproject.MainActivity.player_Id2;
import static com.example.tennisproject.MainActivity.player_Last_Name1;
import static com.example.tennisproject.MainActivity.player_Last_Name2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class CountActivity extends AppCompatActivity {
    int count1 = 0;
    int count2 = 0;
    int gCount1 = 0;
    int gCount2 = 0;
    int sCount1 = 0;
    int sCount2 = 0;
    boolean F_Flag1 = false;
    boolean F_Flag2 = false;
    boolean c_Flag1 = false;
    boolean c_Flag2 = false;
    boolean f_Flag1 = false;
    boolean f_Flag2 = false;
    boolean a_Flag1 = false;
    boolean a_Flag2 = false;
    boolean sideFlag = true;
    boolean tie_Flag =false;
    private DatabaseHelper helper;
    String f_flag1 = "0";
    String w_flag1 = "0";
    String f_flag2 = "0";
    String w_flag2 = "0";
    String a_flag1 = "0";
    String a_flag2 = "0";
    String s_flag;
    ArrayList<Server_DTO> serverList = new ArrayList<Server_DTO>();
    ArrayList<Side_DTO> sideList = new ArrayList<Side_DTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TextView name1 = findViewById(R.id.name1);
        TextView name2 = findViewById(R.id.name2);
        TextView count01 = findViewById(R.id.point1);
        TextView count02 = findViewById(R.id.point2);
        Button fault1 = findViewById(R.id.fault1);
        Button fault2 = findViewById(R.id.fault2);
        Button ace1 = findViewById(R.id.ace1);
        Button ace2 = findViewById(R.id.ace2);
        Button decrement = findViewById(R.id.returnbtn);

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 = "select * from SERVER_TBL ORDER BY SERVER_ID ASC";
        String sql2 = "select * from SIDE_TBL ORDER BY SIDE_ID ASC";

        try {
            Cursor cursor1 = db.rawQuery(sql1, null);
            Cursor cursor2 = db.rawQuery(sql2, null);

            while (cursor1.moveToNext()) {
                Server_DTO dto = new Server_DTO();
                dto.setServer_Id(cursor1.getString(0));
                dto.setPlayer_Id(cursor1.getString(1));
                dto.setToss_Winner(cursor1.getString(2));
                serverList.add(dto);
            }

            while (cursor2.moveToNext()) {
                Side_DTO dto = new Side_DTO();
                dto.setSide_Id(cursor2.getString(0));
                dto.setPlayer_Id(cursor2.getString(1));
                dto.setToss_Winner(cursor2.getString(2));
                sideList.add(dto);
            }


        } finally {
            db.close();
        }


        if(sideList.get(0).getPlayer_Id().equals(player_Id1) || sideList.get(1).getPlayer_Id().equals(player_Id2)){
            name1.setText(player_Last_Name1 + player_First_Name1);
            name2.setText(player_Last_Name2 + player_First_Name2);
        }else if(sideList.get(0).getPlayer_Id().equals(player_Id2) || sideList.get(1).getPlayer_Id().equals(player_Id1)){
            name1.setText(player_Last_Name2 + player_First_Name2);
            name2.setText(player_Last_Name1 + player_First_Name1);
            sideFlag = false;
        }

        serverActivity();

        count01.setOnClickListener((View v) -> {
            countActivity1();
            historyActivity(c_Flag1);
            count01.setTextColor(Color.BLUE);
            c_Flag1 = true;

        });

        count02.setOnClickListener((View v) -> {
            countActivity2();
            historyActivity(c_Flag2);
            count02.setTextColor(Color.BLUE);
            c_Flag2 = true;
        });

        fault1.setOnClickListener((View v) -> {
            faultActivity1();
            historyActivity(f_Flag1);
            if(f_Flag1){
                count02.setTextColor(Color.BLUE);
            }
            fault1.setTextColor(Color.BLUE);
            f_Flag1 = true;
        });

        fault2.setOnClickListener((View v) -> {
            faultActivity2();
            historyActivity(f_Flag2);
            if(f_Flag2){
                count01.setTextColor(Color.BLUE);
            }
            fault2.setTextColor(Color.BLUE);
            f_Flag2 = true;
        });

        ace1.setOnClickListener((View v) -> {
            historyActivity(a_Flag1);
            count01.setTextColor(Color.BLUE);
            ace1.setTextColor(Color.BLUE);
            a_Flag1 = true;
            a_flag1 = "1";
            countActivity1();
            a_flag1 = "0";
        });

        ace2.setOnClickListener((View v) -> {
            historyActivity(a_Flag2);
            count02.setTextColor(Color.BLUE);
            ace2.setTextColor(Color.BLUE);
            a_Flag2 = true;
            a_flag2 = "1";
            countActivity2();
            a_flag2 = "0";
        });

        decrement.setOnClickListener((View v) -> {
            if(count1 + count2 >= 1){
                pointDelete();
            }
        });

    }
    //上のインサート別クラスに移動させる？
    public void countActivity1(){
        TextView point1 = (TextView)findViewById(R.id.point1);
        TextView point2 = (TextView)findViewById(R.id.point2);
        //0から15
        if(!tie_Flag) {
            if (count1 == 0) {
                point1.setText("15");
                count1++;
                pointInsert(true);
            } else if (count1 == 1) {
                point1.setText("30");
                count1++;
                pointInsert(true);
            } else if (count1 == 2) {
                point1.setText("40");
                count1++;
                pointInsert(true);
            } else if (count1 == 3 && count2 < 3) {
                point1.setText("0");
                point2.setText("0");
                count1++;
                pointInsert(true);
                checkGamecount1();
                count1 = 0;
                count2 = 0;
            } else if (count1 + count2 >= 6 && count1 == count2) {
                point1.setText("Adv");
                count1++;
                pointInsert(true);
            } else if (count1 + count2 >= 6 && count1 > count2) {
                point1.setText("0");
                point2.setText("0");
                count1++;
                pointInsert(true);
                checkGamecount1();
                count1 = 0;
                count2 = 0;
            } else if (count1 + count2 >= 6 && count1 < count2) {
                point2.setText("40");
                count1++;
                pointInsert(true);
            }
        }else if(tie_Flag){
            //タイブレーク終了処理
            if(count1 >= 7 && count1 - count2 == 1 || count1 >= 6 && count1 - count2 >= 2){
                count1++;
                point1.setText(String.valueOf(count1));
                pointInsert(true);
                checkGamecount1();
                startActivity(new Intent(this, ExportActivity.class));
            }else{
                count1++;
                point1.setText(String.valueOf(count1));
                pointInsert(true);
            }

            if((count1 + count2) % 6 == 0) {
                sideChangeActivity();
            }

            if(count1 + count2 == 1 || (count1 + count2) % 2 == 1){
                serverActivity();
            }

        }
        f_flag1 = "0";
        w_flag1 = "0";
        falseLiset();

    }

    public void countActivity2(){
        TextView point1 = (TextView)findViewById(R.id.point1);
        TextView point2 = (TextView)findViewById(R.id.point2);
        if(!tie_Flag) {
            if (count2 == 0) {
                point2.setText("15");
                count2++;
                pointInsert(false);
            } else if (count2 == 1) {
                point2.setText("30");
                count2++;
                pointInsert(false);
            } else if (count2 == 2) {
                point2.setText("40");
                count2++;
                pointInsert(false);
            } else if (count2 == 3 && count1 < 3) {
                point1.setText("0");
                point2.setText("0");
                count2++;
                pointInsert(false);
                checkGamecount2();
                count1 = 0;
                count2 = 0;
            } else if (count1 + count2 >= 6 && count1 == count2) {
                point2.setText("Adv");
                count2++;
                pointInsert(false);
            } else if (count1 + count2 >= 6 && count2 > count1) {
                point1.setText("0");
                point2.setText("0");
                count2++;
                pointInsert(false);
                checkGamecount2();
                count1 = 0;
                count2 = 0;
            } else if (count1 + count2 >= 6 && count2 < count1) {
                point1.setText("40");
                count2++;
                pointInsert(false);
            }
        }else if(tie_Flag) {
            //タイブレーク終了処理
            if (count2 >= 7 && count2- count1 == 1 || count2 >= 6 && count2- count1 >= 2) {
                count2++;
                point2.setText(String.valueOf(count2));
                pointInsert(false);
                checkGamecount2();
                startActivity(new Intent(this, ExportActivity.class));
            }else{
                count2++;
                point2.setText(String.valueOf(count2));
                pointInsert(false);
            }

            if((count2 + count1) % 6 == 0) {
                sideChangeActivity();
            }

            if(count1 + count2 == 1 || (count1 + count2) % 2 == 1){
                serverActivity();
            }

        }
        f_flag2 = "0";
        w_flag2 = "0";
        falseLiset();

    }

    public void falseLiset(){
        TextView faultMark1 = (TextView)findViewById(R.id.faultmark1);
        TextView faultMark2 = (TextView)findViewById(R.id.faultmark2);
        F_Flag1 = false;
        F_Flag2 = false;
        faultMark1.setText("");
        faultMark2.setText("");
    }

    public void checkGamecount1(){
        TextView gPoint1 = (TextView)findViewById(R.id.gamecount1);
        if(gCount1 == 5 && gCount2 < 5){
            gCount1++;
            sCount1++;
            gameInsert(true);
            setInsert(true);
            serverActivity();
            checkSetcount1();
        }else if(gCount1 == 6 && gCount2 == 5){
            gCount1++;
            sCount1++;
            gameInsert(true);
            setInsert(true);
            serverActivity();
            checkSetcount1();
        }else if(gCount1 == 5 && gCount2 == 6) {
            gCount1++;
            gPoint1.setText(Integer.toString(gCount1));
            callActivity(true);
            gameInsert(true);
            serverActivity();
            tiebreakActivity();
        }else if(gCount1 == 6 && gCount2 == 6){
            //タイブレーク勝利
            gCount1++;
            sCount1++;
            gPoint1.setText(Integer.toString(gCount1));
            gameInsert(true);
            checkSetcount1();
        }else if(gCount2 <= 5){
            gCount1++;
            gPoint1.setText(Integer.toString(gCount1));
            callActivity(true);
            gameInsert(true);
            sideChangeActivity();
            serverActivity();
        }
    }

    public void checkGamecount2(){
        TextView gPoint2 = (TextView)findViewById(R.id.gamecount2);

        if(gCount2 == 5 && gCount1 < 5){
            gCount2++;
            sCount2++;
            gameInsert(false);
            setInsert(false);
            serverActivity();
            checkSetcount2();
        }else if(gCount2 == 6 && gCount1 == 5){
            gCount2++;
            sCount2++;
            gameInsert(false);
            setInsert(false);
            serverActivity();
            checkSetcount2();
        }else if(gCount2 == 5 && gCount1 == 6) {
            gCount2++;
            gPoint2.setText(Integer.toString(gCount2));
            callActivity(false);
            gameInsert(false);
            serverActivity();
            tiebreakActivity();
        }else if(gCount1 == 6 && gCount2 == 6){
            //タイブレーク勝利
            gCount2++;
            sCount2++;
            gPoint2.setText(Integer.toString(gCount2));
            gameInsert(false);
            checkSetcount2();
        }else if(gCount1 <= 5){
            gCount2++;
            gPoint2.setText(Integer.toString(gCount2));
            callActivity(false);
            gameInsert(false);
            sideChangeActivity();
            serverActivity();
        }
    }

    public void checkSetcount1(){
        if(sCount1 == 1){
            startActivity(new Intent(this, ExportActivity.class));
        }else{
            startActivity(new Intent(this, ExportActivity.class));

        }
    }

    public void checkSetcount2(){
        if(sCount2 == 1){
            startActivity(new Intent(this, ExportActivity.class));
        }else{
            startActivity(new Intent(this, ExportActivity.class));
        }
    }

    public void tiebreakActivity() {
        tie_Flag = true;
    }

    public void sideChangeActivity(){
        //ゲームカウント数の合計が奇数の場合、サイドチェンジ
        if((gCount1 + gCount2) % 2 == 1 || tie_Flag){
            TextView sPoint1 = (TextView)findViewById(R.id.setcount1);
            TextView sPoint2 = (TextView)findViewById(R.id.setcount2);
            TextView gPoint1 = (TextView)findViewById(R.id.gamecount1);
            TextView gPoint2 = (TextView)findViewById(R.id.gamecount2);
            TextView point1 = (TextView)findViewById(R.id.point1);
            TextView point2 = (TextView)findViewById(R.id.point2);
            TextView player1 = (TextView)findViewById(R.id.name1);
            TextView player2 = (TextView)findViewById(R.id.name2);

            //画面の値をStringに代入
            String setPoint1 = sPoint1.getText().toString();
            String setPoint2 = sPoint2.getText().toString();
            String gamePoint1 = gPoint1.getText().toString();
            String gamePoint2 = gPoint2.getText().toString();
            String countPoint1 = point1.getText().toString();
            String countPoint2 = point2.getText().toString();
            String n1 = player1.getText().toString();
            String n2 = player2.getText().toString();

            //入れ替え処理
            sPoint1.setText(setPoint2);
            sPoint2.setText(setPoint1);
            gPoint1.setText(gamePoint2);
            gPoint2.setText(gamePoint1);
            player1.setText(n2);
            player2.setText(n1);

            int dairi1 = sCount1;
            int dairi2 = gCount1;
            sCount1 = sCount2;
            sCount2 = dairi1;
            gCount1 = gCount2;
            gCount2 = dairi2;

            if(tie_Flag){
                point1.setText(countPoint2);
                point2.setText(countPoint1);
                int n = count1;
                count1 = count2;
                count2 = n;
            }

            if(sideFlag){
                sideFlag = false;
            }else if(!sideFlag){
                sideFlag = true;
            }

        }

    }

    public void faultActivity1(){
        TextView faultMark1 = (TextView)findViewById(R.id.faultmark1);
        if(!F_Flag1){
            faultMark1.setText("・");
            F_Flag1 = true;
            f_flag1 = "1";
        }else if(F_Flag1){
            faultMark1.setText("");
            F_Flag1 = false;
            w_flag1 = "1";
            countActivity2();
            f_flag1 = "0";
            w_flag1 = "0";
        }
    }

    public void faultActivity2(){
        TextView faultMark2 = (TextView)findViewById(R.id.faultmark2);
        if(!F_Flag2){
            faultMark2.setText("・");
            F_Flag2 = true;
            f_flag2 = "1";
        }else if(F_Flag2){
            faultMark2.setText("");
            F_Flag2 = false;
            w_flag2 = "1";
            countActivity1();
            f_flag2 = "0";
            w_flag2 = "0";
        }
    }

    public void callActivity(boolean flag) {
        String message = "";
        //GUIの数字を引っ張ってこないとカウントおかしくなる
        TextView player1 = findViewById(R.id.name1);
        String p1 = player1.getText().toString();
        TextView player2 = findViewById(R.id.name2);
        String p2 = player2.getText().toString();
        TextView gameCount1 = findViewById(R.id.gamecount1);
        int g1 = Integer.parseInt(gameCount1.getText().toString());
        TextView gameCount2 = findViewById(R.id.gamecount2);
        int g2 = Integer.parseInt(gameCount2.getText().toString());
        if(g1+g2 == 1 && g1 > g2){
            message = "ゲーム" + p1 + "ファーストゲーム";
        }else if(g1+g2 == 1 && g1 < g2){
            message = "ゲーム" + p2 + "ファーストゲーム";
        }else if(g1+g2 >= 2 && g1 == g2 && flag){
            message = "ゲーム" + p1 + g1 + "オール";
        }else if(g1+g2 >= 2 && g1 == g2 && !flag){
            message = "ゲーム" + p2 + g1 + "オール";
        }else if(g1+g2 >= 2 && g1 > g2 && flag){
            message = "ゲーム" + p1 + "　" + p1 + "リーズ" + g1 + "-" + g2;
        }else if(g1+g2 >= 2 && g1 < g2 && !flag){
            message = "ゲーム" + p2 + "　" + p2 + "リーズ" + g2 + "-" + g1;
        }else if(g1+g2 >= 2 && g1 < g2 && flag){
            message = "ゲーム" + p1 + "　" + p2 + "リーズ" + g2 + "-" + g1;
        }else if(g1+g2 >= 2 && g1 > g2 && !flag){
            message = "ゲーム" + p2 + "　" + p1 + "リーズ" + g1 + "-" + g2;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("コールしてください")
                .setPositiveButton("コールしました", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
// ボタンをクリックしたときの動作
                    }
                });
        builder.show();
    }


    public void pointCallActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("仮")
                .setTitle("コールしてください")
                .setPositiveButton("コールしました", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
// ボタンをクリックしたときの動作
                    }
                });

    }


    public void historyActivity(boolean flag){
        TextView count1 = findViewById(R.id.point1);
        TextView count2 = findViewById(R.id.point2);
        Button fault1 = findViewById(R.id.fault1);
        Button fault2 = findViewById(R.id.fault2);
        Button ace1 = findViewById(R.id.ace1);
        Button ace2 = findViewById(R.id.ace2);
        if(flag){

        } else if(c_Flag1){
            count1.setTextColor(Color.BLACK);
            c_Flag1 = false;
        }else if(c_Flag2){
            count2.setTextColor(Color.BLACK);
            c_Flag2 = false;
        }else if(f_Flag1){
            fault1.setTextColor(Color.WHITE);
            count2.setTextColor(Color.BLACK);
            f_Flag1 = false;
        }else if(a_Flag1){
            ace1.setTextColor(Color.WHITE);
            count1.setTextColor(Color.BLACK);
            a_Flag1 = false;
        }else if(a_Flag2){
            ace2.setTextColor(Color.WHITE);
            count2.setTextColor(Color.BLACK);
            a_Flag2 = false;
        }

    }

    public void startendActivity(boolean flag){
        String message = "";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("コールしてください")
                .setPositiveButton("コールしました", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
// ボタンをクリックしたときの動作
                    }
                });
        builder.show();
    }


    public void serverActivity(){
        TextView s_Mark1 = (TextView)findViewById(R.id.servermark1);
        TextView s_Mark2 = (TextView)findViewById(R.id.servermark2);
        TextView name1 = (TextView)findViewById(R.id.name1);
        TextView name2 = (TextView)findViewById(R.id.name2);
        Button fault1 = findViewById(R.id.fault1);
        Button fault2 = findViewById(R.id.fault2);
        Button ace1 = findViewById(R.id.ace1);
        Button ace2 = findViewById(R.id.ace2);

        if(s_Mark1.getText().equals("") && s_Mark2.getText().equals("")){

            if(serverList.get(0).getPlayer_Id().equals(player_Id1) || serverList.get(1).getPlayer_Id().equals(player_Id2)){
                s_flag = player_Id1;

            }else if(serverList.get(1).getPlayer_Id().equals(player_Id1) || serverList.get(0).getPlayer_Id().equals(player_Id2)){
                s_flag = player_Id2;
            }
        }else{
            //サーバーフラグの切り替え
            if(s_flag.equals(player_Id1)) {
                s_flag = player_Id2;
            }else if(s_flag.equals(player_Id2)) {
                s_flag = player_Id1;
            }
        }

        if(s_flag.equals(player_Id1)){
            if(name1.getText().equals(player_Last_Name1 + player_First_Name1)){
                s_Mark1.setText("▶");
                s_Mark2.setText("");
                fault1.setEnabled(true);
                fault2.setEnabled(false);
                ace1.setEnabled(true);
                ace2.setEnabled(false);
            }else if(name2.getText().equals(player_Last_Name1 + player_First_Name1)){
                s_Mark2.setText("▶");
                s_Mark1.setText("");
                fault1.setEnabled(false);
                fault2.setEnabled(true);
                ace1.setEnabled(false);
                ace2.setEnabled(true);

            }
        }else if(s_flag.equals(player_Id2)){
            if(name1.getText().equals(player_Last_Name2 + player_First_Name2)){
                s_Mark1.setText("▶");
                s_Mark2.setText("");
                fault1.setEnabled(true);
                fault2.setEnabled(false);
                ace1.setEnabled(true);
                ace2.setEnabled(false);

            }else if(name2.getText().equals(player_Last_Name2 + player_First_Name2)){
                s_Mark2.setText("▶");
                s_Mark1.setText("");
                fault1.setEnabled(false);
                fault2.setEnabled(true);
                ace1.setEnabled(false);
                ace2.setEnabled(true);

            }
        }

    }

    public void pointInsert(boolean flag){
        helper = new DatabaseHelper(this);
        String game_Id = "";
        String point_Id = "";

        if(gCount1 + gCount2 + 1 <= 9){
            game_Id = String.valueOf("0" + (gCount1 + gCount2 + 1));
        }else{
            game_Id = String.valueOf(gCount1 + gCount2 + 1);
        }

        if(count1 + count2 <= 9){
            point_Id = String.valueOf("0" + (count1 + count2));
        }else{
            point_Id = String.valueOf(count1 + count2);
        }

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "INSERT INTO POINT_TBL VALUES(1,1,?,?,?,?,?,?,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        //sql = "INSERT INTO POINT_TBL VALUES(1,0,"+ game_Id +","+ point_Id +","+ player_Id1 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";

        //Log.d("PointInsert", "game_Id: " + game_Id + ", point_Id: " + point_Id);
        //Log.d("PointInsert", "Player ID: " + (flag ? player_Id1 : player_Id2) + ", f_flag1: " + f_flag1 + ", w_flag1: " + w_flag1 + ", a_flag1: " + a_flag1);

        if(flag){
            if(sideFlag){
                db.execSQL(sql, new String[]{game_Id, point_Id,player_Id1,f_flag1,w_flag1,a_flag1});
            }else{
                db.execSQL(sql, new String[]{game_Id, point_Id,player_Id2,f_flag1,w_flag1,a_flag1});
            }
        }else{
            if(sideFlag){
                db.execSQL(sql, new String[]{game_Id, point_Id,player_Id2,f_flag1,w_flag1,a_flag2});
            }else{
                db.execSQL(sql, new String[]{game_Id, point_Id,player_Id1,f_flag1,w_flag1,a_flag2});
            }
        }

        try {

        } finally {
            db.close();
        }
    }

    public void gameInsert(boolean flag) {
        helper = new DatabaseHelper(this);
        String game_Id = "";
        if(gCount1 + gCount2 <= 9){
            game_Id = String.valueOf("0" + (gCount1 + gCount2));
        }else{
            game_Id = String.valueOf(gCount1 + gCount2);
        }
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "UPDATE GAME_TBL SET V_OPPONENTS_ID = ?, START_TIME = CURRENT_TIMESTAMP, END_TIME = CURRENT_TIMESTAMP WHERE GAME_ID = ?;";
        //sql = "INSERT INTO GAME_TBL VALUES(1,0," + "0" + game_Id + ","+ player_Id1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";

        if(flag){
            if(sideFlag){
                db.execSQL(sql, new String[]{player_Id1, game_Id});
            }else if(!sideFlag){
                db.execSQL(sql, new String[]{player_Id2, game_Id});
            }
        }else{
            if(sideFlag){
                db.execSQL(sql, new String[]{player_Id2, game_Id});
            }else if(!sideFlag){
                db.execSQL(sql, new String[]{player_Id1, game_Id});
            }
        }
        try {

        } finally {
            db.close();
        }
    }


    public void setInsert(boolean flag) {
        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "UPDATE SET_TBL SET V_OPPONENTS_ID = ?, START_TIME = CURRENT_TIMESTAMP, END_TIME = CURRENT_TIMESTAMP WHERE SET_ID = '1';";

        if(flag){
            if(sideFlag){
                db.execSQL(sql, new String[]{player_Id1});
            }else{
                db.execSQL(sql, new String[]{player_Id2});
            }

        }else{
            if(sideFlag){
                db.execSQL(sql, new String[]{player_Id2});
            }else{
                db.execSQL(sql, new String[]{player_Id1});
            }
        }

    }

    public void pointDelete(){
        helper = new DatabaseHelper(this);
        TextView name1 = findViewById(R.id.name1);
        TextView name2 = findViewById(R.id.name2);

        String point_Id = "";
        String game_Id = "";

        if(count1 + count2 <= 9){
            point_Id = String.valueOf("0" + (count1 + count2));
        }else{
            point_Id = String.valueOf(count1 + count2);
        }

        if(gCount1 + gCount2 + 1 <= 9){
            game_Id = String.valueOf("0" + (gCount1 + gCount2 + 1));
        }else{
            game_Id = String.valueOf(gCount1 + gCount2 + 1);
        }

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql1 = "SELECT V_OPPONENTS_ID FROM POINT_TBL WHERE POINT_ID = ? AND GAME_ID = ?;";
        String sql2 = "DELETE FROM POINT_TBL WHERE POINT_ID = ?  AND GAME_ID = ?";

        Cursor cursor = db.rawQuery(sql1, new String[]{point_Id,game_Id});

        cursor.moveToNext();
        if(tie_Flag){

            if((count1 + count2) % 6 == 0){
                sideChangeActivity();
            }

            if(count1 + count2 == 1 || (count1 + count2) % 2 == 1){
                serverActivity();
            }

            if(cursor.getString(0).equals(player_Id1)){
                if(name1.getText().equals(player_Last_Name1 + player_First_Name1)){
                    count1--;
                    pointDecrement(true);
                }else if(name2.getText().equals(player_Last_Name1 + player_First_Name1)){
                    count2--;
                    pointDecrement(false);
                }
            }else if(cursor.getString(0).equals(player_Id2)){
                if(name1.getText().equals(player_Last_Name2 + player_First_Name2)){
                    count1--;
                    pointDecrement(true);
                }else if(name2.getText().equals(player_Last_Name2 + player_First_Name2)){
                    count2--;
                    pointDecrement(false);
                }
            }

        }else{
            if(cursor.getString(0).equals(player_Id1)){
                if(name1.getText().equals(player_Last_Name1 + player_First_Name1)){
                    count1--;
                    pointDecrement(true);
                }else if(name2.getText().equals(player_Last_Name1 + player_First_Name1)){
                    count2--;
                    pointDecrement(false);
                }
            }else if(cursor.getString(0).equals(player_Id2)){
                if(name1.getText().equals(player_Last_Name2 + player_First_Name2)){
                    count1--;
                    pointDecrement(true);
                }else if(name2.getText().equals(player_Last_Name2 + player_First_Name2)){
                    count2--;
                    pointDecrement(false);
                }
            }
        }

        db.execSQL(sql2, new String[]{point_Id,game_Id});
    }

    public void pointDecrement(boolean flag){
        TextView point1 = (TextView) findViewById(R.id.point1);
        TextView point2 = (TextView) findViewById(R.id.point2);

        if(!tie_Flag){
            if(flag){
                if(count2 == count1 + 1 && count1 + count2 >= 7){
                    point2.setText("Adv");
                }else if(count1 == 3 && count1 + count2 <= 5 || count1 == count2 && count1 + count2 >= 6) {
                    point1.setText("40");
                }else if(count1 == 2){
                    point1.setText("30");
                }else if(count1 == 1){
                    point1.setText("15");
                }else if(count1 == 0){
                    point1.setText("0");
                }

            }else if(!flag){
                if(count1 == count2 + 1 && count1 + count2 >= 7){
                    point1.setText("Adv");
                }else if(count2 == 3 && count2 + count1 <= 5 || count2 == count1 && count2 + count1 >= 6) {
                    point2.setText("40");
                }else if(count2 == 2){
                    point2.setText("30");
                }else if(count2 == 1){
                    point2.setText("15");
                }else if(count2 == 0){
                    point2.setText("0");
                }

            }
        }else if(tie_Flag){

            if(flag){
                point1.setText(String.valueOf(count1));
            }else if(!flag){
                point2.setText(String.valueOf(count2));
            }

        }

        falseLiset();

    }

}