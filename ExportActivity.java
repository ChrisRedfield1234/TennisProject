package com.example.tennisproject;
import static java.util.Objects.isNull;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.ShapeTypes;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSimpleShape;
import org.apache.poi.xssf.usermodel.XSSFTextBox;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineEndProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.STLineEndType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ExportActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    ArrayList<Point_DTO> pointList = new ArrayList<Point_DTO>();
    ArrayList<Server_DTO> serverList = new ArrayList<Server_DTO>();
    ArrayList<Side_DTO> sideList = new ArrayList<Side_DTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button startbtn = findViewById(R.id.export);
        startbtn.setOnClickListener((View v) -> {
            startActivity(new Intent(this, MainActivity.class));
        });

        Button dialogbtn = findViewById(R.id.dialog2);
        dialogbtn.setOnClickListener((View v) -> {
            showDialog(v);
        });


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

        try {
            Cursor cursor1 = db.rawQuery(sql1, null);
            Cursor cursor2 = db.rawQuery(sql2, null);
            Cursor cursor3 = db.rawQuery(sql3, null);

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

        } finally {
            db.close();
        }

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

            //以下左側のserviceを囲めたやつ
            /*
            HSSFPatriarch patriarch = (HSSFPatriarch) sheet.createDrawingPatriarch();
            HSSFClientAnchor a = new HSSFClientAnchor(5, 5, 1023, 255, (short) 13, 20, (short) 15, 20);
            HSSFSimpleShape shape = patriarch.createSimpleShape(a);
            // 図形の形状を楕円形に設定
            shape.setShapeType(HSSFSimpleShape.OBJECT_TYPE_OVAL);
            // 塗りつぶしをなしに設定
            shape.setNoFill(true);
*/
            FileOutputStream fos = new FileOutputStream(file);
            excel.write(fos);
            excel.close();
            fos.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showDialog(View view) {
        DialogFragment dialogFragment = new ExportDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog");
    }

}


