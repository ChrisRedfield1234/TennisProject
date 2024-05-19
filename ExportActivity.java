package com.example.tennisproject;
import static com.example.tennisproject.MainActivity.match_Id;
import static com.example.tennisproject.MainActivity.player_First_Name1;
import static com.example.tennisproject.MainActivity.player_First_Name2;
import static com.example.tennisproject.MainActivity.player_Id1;
import static com.example.tennisproject.MainActivity.player_Id2;
import static com.example.tennisproject.MainActivity.player_Last_Name1;
import static com.example.tennisproject.MainActivity.player_Last_Name2;
import static com.example.tennisproject.Umpire_Main.umpire_Id;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExportActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    ArrayList<Point_DTO> pointList = new ArrayList<Point_DTO>();
    ArrayList<Server_DTO> serverList = new ArrayList<Server_DTO>();
    ArrayList<Side_DTO> sideList = new ArrayList<Side_DTO>();

    String pointA ="";
    String pointB="";

    String setScoreA ="";
    String setScoreB ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Intent intent = new Intent(this, Umpire_Main.class);

        Button startbtn = findViewById(R.id.export);
        startbtn.setOnClickListener((View v) -> {

            intent.putExtra("EXTRA_DATA",umpire_Id);
            startActivity(intent);

        });

        Button dialogbtn = findViewById(R.id.dialog2);
        dialogbtn.setOnClickListener((View v) -> {
            showDialog(v);
        });

        setName();
        selectResult();

    }

    public void setName(){
        TextView player1 = (TextView)findViewById(R.id.player1);
        TextView player2 = (TextView)findViewById(R.id.player2);

        player1.setText(player_Last_Name1 + player_First_Name1);
        player2.setText(player_Last_Name2 + player_First_Name2);

    }


    public void selectResult(){
        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 ="select * from POINT_TBL";
        String sql2 ="select * from SERVER_TBL";
        String sql3 ="select * from SIDE_TBL";
        String sql4 ="select COUNT(*) from GAME_TBL WHERE V_OPPONENTS_ID = ?";
        String sql5 ="select COUNT(*) from GAME_TBL WHERE V_OPPONENTS_ID = ?";

        Cursor cursor1 = db.rawQuery(sql1, null);
        Cursor cursor2 = db.rawQuery(sql2, null);
        Cursor cursor3 = db.rawQuery(sql3, null);
        Cursor cursor4 = db.rawQuery(sql4, new String[]{player_Id1});
        Cursor cursor5 = db.rawQuery(sql5, new String[]{player_Id2});
        // Cursor cursor6 = db.rawQuery(sql6, null);
        //Cursor cursor7 = db.rawQuery(sql7, null);

        while (cursor1.moveToNext()) {
            Point_DTO dto = new Point_DTO();
            dto.setMatch_Id(cursor1.getString(0));
            dto.setSet_Id(cursor1.getString(1));
            dto.setGame_Id(cursor1.getString(2));
            dto.setPoint_Id(cursor1.getString(3));
            dto.setV_Id(cursor1.getString(4));
            dto.setFault_Flag(cursor1.getString(5));
            dto.setWfault_Flag(cursor1.getString(6));
            dto.setAce_Flag(cursor1.getString(7));
            dto.setStart_Time(cursor1.getString(8));
            dto.setEnd_Time(cursor1.getString(9));
            pointList.add(dto);
        }

        while (cursor2.moveToNext()) {
            Server_DTO dto = new Server_DTO();
            dto.setServer_Id(cursor2.getString(0));
            dto.setPlayer_Id(cursor2.getString(1));
            dto.setToss_Winner(cursor2.getString(2));
            serverList.add(dto);
        }

        while (cursor3.moveToNext()) {
            Side_DTO dto = new Side_DTO();
            dto.setSide_Id(cursor3.getString(0));
            dto.setPlayer_Id(cursor3.getString(1));
            dto.setToss_Winner(cursor3.getString(2));
            sideList.add(dto);
        }

        while (cursor4.moveToNext()) {
            pointA = cursor4.getString(0);
        }

        while (cursor5.moveToNext()) {
            pointB = cursor5.getString(0);
        }

        TextView point1 = findViewById(R.id.GameScore1);
        point1.setText(pointA);

        TextView point2 = findViewById(R.id.GameScore2);
        point2.setText(pointB);

        TextView setScore1 = findViewById(R.id.SetScore1);
        TextView setScore2 = findViewById(R.id.SetScore2);

        if(pointA.equals("6")){
            if(pointB.equals("7")){

                setScore1.setText("0");
                setScore2.setText("1");
                updateV(match_Id,player_Id2);

            }else{

                setScore1.setText("1");
                setScore2.setText("0");
                updateV(match_Id,player_Id1);

            }

        }else if(pointB.equals("6")){

            if(pointA.equals("7")){

                setScore1.setText("1");
                setScore2.setText("0");
                updateV(match_Id,player_Id1);

            }else{

                setScore1.setText("0");
                setScore2.setText("1");
                updateV(match_Id,player_Id2);

            }

        }
    }

    public void updateV(String match_Id,String player_Id){

        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "UPDATE MATCH_TBL SET V_OPPONENTS_ID = ? WHERE MATCH_ID = ?;";

        db.execSQL(sql,new String[]{player_Id,match_Id});

        db.close();
        //addMatch(match_Id);

    }

    public void addMatch(String match_Id){

        helper = new DatabaseHelper(this);
        try {
            helper.createDatabase();
        } catch (
                IOException e) {
            throw new Error("Unable to create database");
        }

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql1 = "SELECT * FROM TOURNAMENT_INFO_TBL;";

        Cursor cursor1 = db.rawQuery(sql1, null);

        cursor1.moveToNext();
        String participants = cursor1.getString(0);
        String block = cursor1.getString(1);

        int totalMatch = Integer.parseInt(participants) - 1;
        int firstRound = Integer.parseInt(participants) / 2;

        int nextMatch_Id = 0;
        System.out.println(nextMatch_Id);
        db.close();

    }

    /*
    private static void generateNextMatchMappings(int totalMatch,int firstRound) {
        int matchCounter = FIRST_ROUND_MATCHES + 1;

        // 1回戦の次の試合番号を設定
        for (int i = 1; i <= FIRST_ROUND_MATCHES; i += 2) {
            nextMatchMapping.put(i, matchCounter);
            nextMatchMapping.put(i + 1, matchCounter);
            matchCounter++;
        }

        // 2回戦以降の次の試合番号を設定
        int matchesInRound = FIRST_ROUND_MATCHES / 2;
        while (matchCounter < TOTAL_MATCHES) {
            for (int i = 0; i < matchesInRound; i += 2) {
                nextMatchMapping.put(matchCounter - matchesInRound * 2 + i, matchCounter);
                nextMatchMapping.put(matchCounter - matchesInRound * 2 + i + 1, matchCounter);
                matchCounter++;
            }
            matchesInRound /= 2;
        }
    }

    private static int getNextMatchNumber(int finishedMatch) {
        return nextMatchMapping.getOrDefault(finishedMatch, -1);
    }



     */


    public void showDialog(View view) {
        DialogFragment dialogFragment = new ExportDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog");
    }

    public void writeWork(){

        String fileName = "スコアカード.xls";
        String path = "";
        String file = "";

        try {

            AssetManager assetManager = getAssets();
            String player_Id = "";
            int r = 35;
            int c = 12;
            String game_Id = "1";

            String server = "1";
            InputStream inputStream = assetManager.open(fileName);
            Workbook excel = WorkbookFactory.create(inputStream);
            Sheet sheet = excel.getSheet("Sheet1");
            HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
            HSSFClientAnchor a = new HSSFClientAnchor(0, 0, 0, 0, (short) 0, 0, (short) 0, 0);;
            Row row;
            Cell cell;

            //以下トスの更新処理
            for(Server_DTO dto:serverList){
                if(dto.getToss_Winner().equals("1")){

                    if(dto.getPlayer_Id().equals("1")){

                        row = sheet.getRow(21);
                        cell = row.getCell(7);
                        cell.setCellValue("✓");
                        row = sheet.getRow(20);
                        cell = row.getCell(10);
                        cell.setCellValue("✓");

                        if(dto.getServer_Id().equals("1")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 13, 20, (short) 15, 20);

                        }else if(dto.getServer_Id().equals("2")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 16, 20, (short) 18, 20);

                        }
                    }else if(dto.getPlayer_Id().equals("2")){

                        row = sheet.getRow(21);
                        cell = row.getCell(24);
                        cell.setCellValue("✓");
                        row = sheet.getRow(20);
                        cell = row.getCell(27);
                        cell.setCellValue("✓");

                        if(dto.getServer_Id().equals("1")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 30, 20, (short) 32, 20);

                        }else if(dto.getServer_Id().equals("2")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 33, 20, (short) 35, 20);

                        }
                    }

                }else if(dto.getToss_Winner().equals("2")){

                    if(dto.getPlayer_Id().equals("1")){

                        row = sheet.getRow(20);
                        cell = row.getCell(10);
                        cell.setCellValue("✓");

                        if(dto.getServer_Id().equals("1")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 13, 20, (short) 15, 20);

                        }else if(dto.getServer_Id().equals("2")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 16, 20, (short) 18, 20);

                        }
                    }else if(dto.getPlayer_Id().equals("2")){

                        row = sheet.getRow(20);
                        cell = row.getCell(27);
                        cell.setCellValue("✓");

                        if(dto.getServer_Id().equals("1")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 30, 20, (short) 32, 20);

                        }else if(dto.getServer_Id().equals("2")){

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 33, 20, (short) 35, 20);

                        }
                    }




                }

            }

            HSSFSimpleShape shape1 = patriarch.createSimpleShape(a);
            shape1.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);
            shape1.setNoFill(true);

            for(Side_DTO dto:sideList) {
                if (dto.getToss_Winner().equals("1")) {
                    if (dto.getPlayer_Id().equals("1")) {

                        row = sheet.getRow(21);
                        cell = row.getCell(7);
                        cell.setCellValue("✓");
                        row = sheet.getRow(21);
                        cell = row.getCell(10);
                        cell.setCellValue("✓");

                        if (dto.getSide_Id().equals("1")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 15, 21, (short) 15, 21);

                        } else if (dto.getSide_Id().equals("2")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 17, 21, (short) 17, 21);

                        }

                    } else if (dto.getPlayer_Id().equals("2")) {

                        row = sheet.getRow(21);
                        cell = row.getCell(24);
                        cell.setCellValue("✓");
                        row = sheet.getRow(21);
                        cell = row.getCell(27);
                        cell.setCellValue("✓");

                        if (dto.getSide_Id().equals("1")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 32, 21, (short) 32, 21);

                        } else if (dto.getSide_Id().equals("2")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 34, 21, (short) 34, 21);

                        }

                    }

                } else if (dto.getToss_Winner().equals("2")) {

                    if (dto.getPlayer_Id().equals("1")) {

                        row = sheet.getRow(21);
                        cell = row.getCell(10);
                        cell.setCellValue("✓");

                        if (dto.getSide_Id().equals("1")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 15, 21, (short) 15, 21);

                        } else if (dto.getSide_Id().equals("2")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 17, 21, (short) 17, 21);

                        }

                    } else if (dto.getPlayer_Id().equals("2")) {

                        row = sheet.getRow(21);
                        cell = row.getCell(27);
                        cell.setCellValue("✓");

                        if (dto.getSide_Id().equals("1")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 32, 21, (short) 32, 21);

                        } else if (dto.getSide_Id().equals("2")) {

                            a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 34, 21, (short) 34, 21);

                        }

                    }

                }

            }

            HSSFSimpleShape shape2 = patriarch.createSimpleShape(a);
            shape2.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);
            shape2.setNoFill(true);


            //以下ポイントの更新処理
            for(Point_DTO dto:pointList){
                if(!game_Id.equals(dto.getGame_Id())){
                    r = r + 2;
                    c = 12;
                }
                player_Id = dto.getV_Id();
                game_Id = dto.getGame_Id();
                System.out.println("行：" + r + "　列：" + c);
                if(player_Id.equals(server)){
                    row = sheet.getRow(r); // 行を指定
                    cell = row.getCell(c);     // 列を指定

                    if(dto.getAce_Flag().equals("1")){
                        cell.setCellValue("A");
                        System.out.println("プレイヤー" + player_Id + "はサービスエース");
                    }else{
                        cell.setCellValue("／");
                        System.out.println("プレイヤー" + player_Id + "は得点");
                    }

                }else{
                    row = sheet.getRow(r + 1); // 行を指定
                    cell = row.getCell(c);     // 列を指定

                    if(dto.getWfault_Flag().equals("1")){
                        cell.setCellValue("F");
                        System.out.println("プレイヤー" + player_Id + "はダブルフォルト");
                    }else{
                        cell.setCellValue("／");
                        System.out.println("プレイヤー" + player_Id + "は失点");
                    }

                }
                c++;


            }

            path = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString();
            file = path + "/" + fileName;

            FileOutputStream fos = new FileOutputStream(file);
            excel.write(fos);
            excel.close();
            fos.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}


