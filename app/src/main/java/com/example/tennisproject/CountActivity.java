package com.example.tennisproject;

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
    boolean m_Flag1 = false;
    boolean m_Flag2 = false;
    boolean f_Flag1 = false;
    boolean f_Flag2 = false;
    boolean a_Flag1 = false;
    boolean a_Flag2 = false;
    boolean sideFlag = true;
    private DatabaseHelper helper;
    String playerId1 = "1";
    String playerId2 = "2";

    String f_flag1 = "0";
    String w_flag1 = "0";
    String f_flag2 = "0";
    String w_flag2 = "0";

    String a_flag1 = "0";
    String a_flag2 = "0";

    ArrayList<Server_DTO> serverList = new ArrayList<Server_DTO>();
    ArrayList<Side_DTO> sideList = new ArrayList<Side_DTO>();
    ArrayList<PLAYER_DTO> playerList = new ArrayList<PLAYER_DTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.count);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        TextView name1 = findViewById(R.id.name1);
        TextView name2 = findViewById(R.id.name2);
        TextView count1 = findViewById(R.id.point1);
        TextView count2 = findViewById(R.id.point2);
        Button minus1 = findViewById(R.id.m_btn1);
        Button minus2 = findViewById(R.id.m_btn2);
        Button fault1 = findViewById(R.id.fault1);
        Button fault2 = findViewById(R.id.fault2);
        Button ace1 = findViewById(R.id.ace1);
        Button ace2 = findViewById(R.id.ace2);

        helper = new DatabaseHelper(this);

        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 ="select * from SERVER_TBL";
        String sql2 ="select * from SIDE_TBL";
        String sql3 = "select PLAYER_LAST_NAME,PLAYER_FIRST_NAME from PLAYER_TBL";

        try {
            Cursor cursor1 = db.rawQuery(sql1, null);
            Cursor cursor2 = db.rawQuery(sql2, null);
            Cursor cursor3 = db.rawQuery(sql3, null);

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

            while(cursor3.moveToNext()){
                PLAYER_DTO dto = new PLAYER_DTO();
                dto.setLastName(cursor3.getString(0));
                dto.setFirstName(cursor3.getString(1));
                playerList.add(dto);
            }

        } finally {
            db.close();
        }

        System.out.println(sideList.get(0).getSide_Id());
        System.out.println(sideList.get(0).getPlayer_Id());
        System.out.println(sideList.get(0).getToss_Winner());

        if(sideList.get(0).getPlayer_Id().equals("1")){
            System.out.println("錦織左");
            name1.setText(playerList.get(0).getLastName() + " " + playerList.get(0).getFirstName());
            name2.setText(playerList.get(1).getLastName() + " " + playerList.get(1).getFirstName());

        }else if(sideList.get(0).getPlayer_Id().equals("2")){
            System.out.println("錦織右");
            name1.setText(playerList.get(1).getLastName() + " " + playerList.get(1).getFirstName());
            name2.setText(playerList.get(0).getLastName() + " " + playerList.get(0).getFirstName());

        }

        count1.setOnClickListener((View v) -> {
            countActivity1();
            historyActivity(c_Flag1);
            count1.setTextColor(Color.BLUE);
            c_Flag1 = true;

        });


        count2.setOnClickListener((View v) -> {
            countActivity2();
            historyActivity(c_Flag2);
            count2.setTextColor(Color.BLUE);
            c_Flag2 = true;
        });


        minus1.setOnClickListener((View v) -> {
            minusActivity1();
            historyActivity(m_Flag1);
            minus1.setTextColor(Color.BLUE);
            m_Flag1 = true;
        });


        minus2.setOnClickListener((View v) -> {
            minusActivity2();
            historyActivity(m_Flag2);
            minus2.setTextColor(Color.BLUE);
            m_Flag2 = true;
        });


        fault1.setOnClickListener((View v) -> {
            faultActivity1();
            historyActivity(f_Flag1);
            if(f_Flag1){
                count2.setTextColor(Color.BLUE);
            }
            fault1.setTextColor(Color.BLUE);
            f_Flag1 = true;
        });


        fault2.setOnClickListener((View v) -> {
            faultActivity2();
            historyActivity(f_Flag2);
            if(f_Flag2){
                count1.setTextColor(Color.BLUE);
            }
            fault2.setTextColor(Color.BLUE);
            f_Flag2 = true;
        });


        ace1.setOnClickListener((View v) -> {
            historyActivity(a_Flag1);
            count1.setTextColor(Color.BLUE);
            ace1.setTextColor(Color.BLUE);
            a_Flag1 = true;
            a_flag1 = "1";
            countActivity1();
            a_flag1 = "0";
        });


        ace2.setOnClickListener((View v) -> {
            historyActivity(a_Flag2);
            count2.setTextColor(Color.BLUE);
            ace2.setTextColor(Color.BLUE);
            a_Flag2 = true;
            a_flag2 = "1";
            countActivity2();
            a_flag2 = "0";
        });

    }
//上のインサート別クラスに移動させる？
    public void countActivity1(){
        TextView point1 = (TextView)findViewById(R.id.point1);
        //String view1 = point1.getText().toString();
        TextView point2 = (TextView)findViewById(R.id.point2);
        TextView gPoint1 = (TextView)findViewById(R.id.gamecount1);
        TextView gPoint2 = (TextView)findViewById(R.id.gamecount2);
        //0から15
        if(count1 == 0){
            point1.setText("15");
            count1++;
            pointInsert(true);
        }else if(count1 == 1) {
            point1.setText("30");
            count1++;
            pointInsert(true);
        }else if(count1 == 2){
            point1.setText("40");
            count1++;
            pointInsert(true);
        }else if(count1 == 3 && count2 < 3){
            point1.setText("0");
            point2.setText("0");
            count1++;
            pointInsert(true);
            checkGamecount1();
            count1 = 0;
            count2 = 0;
        }else if(count1 + count2 >= 6 && count1 == count2){
            point1.setText("Adv");
            count1++;
            pointInsert(true);
        }else if(count1 + count2 >= 6 && count1 > count2){
            point1.setText("0");
            point2.setText("0");
            count1++;
            pointInsert(true);
            checkGamecount1();
            count1 = 0;
            count2 = 0;
        }else if(count1 + count2 >= 6 && count1 < count2){
            point2.setText("40");
            count1++;
            pointInsert(true);
        }
        f_flag1 = "0";
        w_flag1 = "0";
        falseLiset();
    }

    public void countActivity2(){
        TextView point1 = (TextView)findViewById(R.id.point1);
        TextView point2 = (TextView)findViewById(R.id.point2);
        TextView gPoint2 = (TextView)findViewById(R.id.gamecount2);
        if(count2 == 0){
            point2.setText("15");
            count2++;
            pointInsert(false);
        }else if(count2 == 1) {
            point2.setText("30");
            count2++;
            pointInsert(false);
        }else if(count2 == 2){
            point2.setText("40");
            count2++;
            pointInsert(false);
        }else if(count2 == 3 && count1 < 3){
            point1.setText("0");
            point2.setText("0");
            count2++;
            pointInsert(false);
            checkGamecount2();
            count1 = 0;
            count2 = 0;
        }else if(count1 + count2 >= 6 && count1 == count2){
            point2.setText("Adv");
            count2++;
            pointInsert(false);
        }else if(count1 + count2 >= 6 && count2 > count1){
            point1.setText("0");
            point2.setText("0");
            count2++;
            pointInsert(false);
            checkGamecount2();
            count1 = 0;
            count2 = 0;
        }else if(count1 + count2 >= 6 && count2 < count1){
            point1.setText("40");
            count2++;
            pointInsert(false);
        }
        falseLiset();
        f_flag2 = "0";
        w_flag2 = "0";
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
            sCount1++;
            setInsert1();
            checkSetcount1();
        }else if(gCount1 == 6 && gCount2 == 5){
            sCount1++;
            setInsert1();
            checkSetcount1();
        }else if(gCount1 == 5 && gCount2 == 6){
            gCount1++;
            gPoint1.setText(Integer.toString(gCount1));
            callActivity(true);
            gameInsert(true);
            sideChengeActivity();
        }else if(gCount2 <= 5){
            gCount1++;
            gPoint1.setText(Integer.toString(gCount1));
            callActivity(true);
            gameInsert(true);
            sideChengeActivity();
        }
    }

    public void checkGamecount2(){
        TextView gPoint2 = (TextView)findViewById(R.id.gamecount2);
        if(gCount2 == 5 && gCount1 < 5){
            sCount2++;
            setInsert2();
            checkSetcount2();
        }else if(gCount2 == 6 && gCount1 == 5){
            sCount2++;
            setInsert2();
            checkSetcount2();
        }else if(gCount2 == 5 && gCount1 == 6){
            gCount2++;
            gPoint2.setText(Integer.toString(gCount2));
            callActivity(false);
            gameInsert(false);
            sideChengeActivity();
        }else if(gCount1 <= 5){
            gCount2++;
            gPoint2.setText(Integer.toString(gCount2));
            callActivity(false);
            gameInsert(false);
            sideChengeActivity();
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

    public void minusActivity1(){
        TextView point1 = (TextView)findViewById(R.id.point1);
        TextView point2 = (TextView)findViewById(R.id.point2);
        if(count1 >= 4 && count1 % 2 == 0 && count2 >= 3 && count2 % 2 == 1) {
            point1.setText("40");
            count1--;
        }else if(count2 >= 4 && count2 % 2 == 0 && count1 >= 3 && count1 % 2 == 1){
            point2.setText("40");
            count2--;
        }else if(count1 == 3){
            point1.setText("30");
            count1--;
        }else if(count1 == 2){
            point1.setText("15");
            count1--;
        }else if(count1 == 1){
            point1.setText("0");
            count1--;
        }
    }

    public void minusActivity2() {
        TextView point1 = (TextView) findViewById(R.id.point1);
        TextView point2 = (TextView) findViewById(R.id.point2);
        if(count2 >= 4 && count2 % 2 == 0 && count1 >= 3 && count1 % 2 == 1) {
            point2.setText("40");
            count2--;
        }else if(count1 >= 4 && count1 % 2 == 0 && count2 >= 3 && count2 % 2 == 1){
            point1.setText("40");
            count1--;
        }else if(count2 == 3){
            point2.setText("30");
            count2--;
        }else if(count2 == 2){
            point2.setText("15");
            count2--;
        }else if(count2 == 1){
            point2.setText("0");
            count2--;
        }
    }

    public void sideChengeActivity(){
        //ゲームカウント数の合計が奇数の場合、サイドチェンジ
        if((gCount1 + gCount2) % 2 == 1){
        TextView sPoint1 = (TextView)findViewById(R.id.setcount1);
        TextView sPoint2 = (TextView)findViewById(R.id.setcount2);
        TextView gPoint1 = (TextView)findViewById(R.id.gamecount1);
        TextView gPoint2 = (TextView)findViewById(R.id.gamecount2);
        TextView player1 = (TextView)findViewById(R.id.name1);
        TextView player2 = (TextView)findViewById(R.id.name2);

        //画面の値をStringに代入
        String setPoint1 = sPoint1.getText().toString();
        String setPoint2 = sPoint2.getText().toString();
        String gamePoint1 = gPoint1.getText().toString();
        String gamePoint2 = gPoint2.getText().toString();
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
        Button minus1 = findViewById(R.id.m_btn1);
        Button minus2 = findViewById(R.id.m_btn2);
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
        }else if(m_Flag1){
            count2.setTextColor(Color.BLACK);
            m_Flag1 = false;
        }else if(m_Flag2){
            count2.setTextColor(Color.BLACK);
            m_Flag2 = false;
        }else if(f_Flag1){
            fault1.setTextColor(Color.WHITE);
            count2.setTextColor(Color.BLACK);
            f_Flag1 = false;
        }else if(m_Flag2){
            fault1.setTextColor(Color.WHITE);
            count2.setTextColor(Color.BLACK);
            m_Flag2 = false;
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
    //ダイアログ出してラジオボタン実装で入力させる？
        //3回目以降は出す必要なし
    }


    public void pointInsert(boolean flag){
        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "";

        if(flag){
            if(sideFlag){
                sql = "INSERT INTO POINT_TBL VALUES(1,0,"+(gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId1 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
                System.out.println("1,0," + (gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId1 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag1);
            }else{
                sql = "INSERT INTO POINT_TBL VALUES(1,0,"+(gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId2 +"," + f_flag1 + "," + w_flag2 + ","+ a_flag1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
                System.out.println("1,0," + (gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId2 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag1);
            }
        }else{
            if(sideFlag){
                sql = "INSERT INTO POINT_TBL VALUES(1,0,"+(gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId2 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
                System.out.println("1,0," + (gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId2 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag2);
            }else{
                sql = "INSERT INTO POINT_TBL VALUES(1,0,"+(gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId1 +"," + f_flag1 + "," + w_flag2 + ","+ a_flag2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
                System.out.println("1,0," + (gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId1 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag2);
            }
        }



        try {
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.getCount() == 0) {
                // The query returned an empty result set.
            }

        } finally {
            db.close();
        }
    }

    public void pointInsert2(){
        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "";
        if(sideFlag){
            sql = "INSERT INTO POINT_TBL VALUES(1,0,"+(gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId2 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            System.out.println("1,0," + (gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId2 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag2);
        }else{
            sql = "INSERT INTO POINT_TBL VALUES(1,0,"+(gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId1 +"," + f_flag1 + "," + w_flag2 + ","+ a_flag2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            System.out.println("1,0," + (gCount1 + gCount2 + 1) +","+ (count1 + count2) +","+ playerId1 +"," + f_flag1 + "," + w_flag1 + ","+ a_flag2);
        }

        try {
            Cursor cursor = db.rawQuery(sql, null);


        } finally {
            db.close();
        }
    }


    public void gameInsert(boolean flag) {
        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "";

        if(flag){
            if(sideFlag){
                sql = "INSERT INTO GAME_TBL VALUES(1,0," + (gCount1 + gCount2 + 1) + ","+ playerId1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            }else if(!sideFlag){
                sql = "INSERT INTO GAME_TBL VALUES(1,0," + (gCount1 + gCount2 + 1) + ","+ playerId2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            }
        }else{
            if(sideFlag){
                sql = "INSERT INTO GAME_TBL VALUES(1,0," + (gCount1 + gCount2 + 1) + ","+ playerId2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            }else if(!sideFlag){
                sql = "INSERT INTO GAME_TBL VALUES(1,0," + (gCount1 + gCount2 + 1) + ","+ playerId1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
            }
        }




        try {
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.getCount() == 0) {
                // The query returned an empty result set.
            }

        } finally {
            db.close();
        }
    }


        public void gameInsert2(){
            helper = new DatabaseHelper(this);
            try {
                helper.createDatabase();
            } catch (
                    IOException e) {
                throw new Error("Unable to create database");
            }

            SQLiteDatabase db = helper.getWritableDatabase();
            String sql = "";


            try {
                Cursor cursor = db.rawQuery(sql, null);

                if (cursor.getCount() == 0) {
                    // The query returned an empty result set.
                }

            } finally {
                db.close();
            }

    }

    public void setInsert1() {
        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "";
        if(sideFlag){
            sql = "INSERT INTO SET_TBL VALUES(1,1,"+ playerId1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        }else if(!sideFlag){
            sql = "INSERT INTO SET_TBL VALUES(1,1,"+ playerId2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        }


        try {
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.getCount() == 0) {
                // The query returned an empty result set.
            }

        } finally {
            db.close();
        }
    }


    public void setInsert2() {
        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "";
        if(sideFlag){
            sql = "INSERT INTO SET_TBL VALUES(1,1,"+ playerId2 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        }else if(!sideFlag){
            sql = "INSERT INTO SET_TBL VALUES(1,1,"+ playerId1 +",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP)";
        }


        try {
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.getCount() == 0) {
                // The query returned an empty result set.
            }

        } finally {
            db.close();
        }
    }


}