package com.example.tennisproject;
import static java.util.Objects.isNull;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;

 */

public class ExportActivity extends AppCompatActivity {

    private DatabaseHelper helper;
    ArrayList<Point_DTO> pointList = new ArrayList<Point_DTO>();
    ArrayList<Server_DTO> serverList = new ArrayList<Server_DTO>();
    ArrayList<Side_DTO> sideList = new ArrayList<Side_DTO>();

    String pointA ="";
    String pointB="";

    //String nameA = "";
    //String nameB = "";

    String setScoreA ="";
    String setScoreB ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Button startbtn = findViewById(R.id.export);
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //asyncTask a=new asyncTask();
                //a.execute("gmailより前", "アプリパスワード","試合結果","送信完了\n本文") ;
                //a.execute("Gmailのアカウント名（@gmail.comの前まで）","Gmailのパスワード","テストタイトル","送信完了\n本文をここに記述する") ;
            }
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
        String sql4 ="select COUNT(*) from GAME_TBL WHERE V_OPPONENTS_ID = 1";
        String sql5 ="select COUNT(*) from GAME_TBL WHERE V_OPPONENTS_ID = 2";
        //String sql6 ="select PLAYER_LAST_NAME || ' ' || PLAYER_FIRST_NAME from PLAYER_TBL where PLAYER_ID = 1";
        //String sql7 ="select PLAYER_LAST_NAME || ' ' || PLAYER_FIRST_NAME from PLAYER_TBL where PLAYER_ID = 2";

        try {
            Cursor cursor1 = db.rawQuery(sql1, null);
            Cursor cursor2 = db.rawQuery(sql2, null);
            Cursor cursor3 = db.rawQuery(sql3, null);
            Cursor cursor4 = db.rawQuery(sql4, null);
            Cursor cursor5 = db.rawQuery(sql5, null);
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

            /*
            if(pointA.equals("5")&&!(pointB.equals("6"))) {
                point1.setText("6");
            } else if(pointA.equals("6")){
                point1.setText("7");
            } else {
                point1.setText(pointA);
            }
             */
            //point1 = findViewById(R.id.GameScore1);



            TextView point2 = findViewById(R.id.GameScore2);
            point2.setText(pointB);

            /*
            if(pointB.equals("5")&&!(pointA.equals("6"))) {
                point2.setText("6");
            } else if(pointB.equals("6")) {
                point2.setText("7");
            }else {
                point2.setText(pointB);
            }
            */
            //point2 = findViewById(R.id.GameScore2);



           /* while (cursor6.moveToNext()) {
                nameA = cursor6.getString(0);
            }

            TextView name1 = findViewById(R.id.Name1);
            name1.setText(nameA);
            name1 = findViewById(R.id.Name1);

            while (cursor7.moveToNext()) {
                nameB = cursor7.getString(0);
            }

            TextView name2 = findViewById(R.id.Name2);
            name2.setText(nameB);
            name2 = findViewById(R.id.Name2);*/

            TextView setScore1 = findViewById(R.id.SetScore1);


           if(pointA.equals("5")&&(!(pointB.equals("6")))|| pointA.equals("6")) {
                setScore1.setText("1");
            }

            TextView setScore2 = findViewById(R.id.SetScore2);



           if(pointB.equals("5")&&(!(pointA.equals("6")))||pointB.equals("6")) {
                setScore2.setText("1");
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

            FileOutputStream fos = new FileOutputStream(file);
            excel.write(fos);
            excel.close();
            fos.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

/*
    private class asyncTask extends android.os.AsyncTask{
        protected String account;
        protected String password;
        protected String title;
        protected String text;

        @Override
        protected Object doInBackground(Object... obj){
            account=(String)obj[0];
            password=(String)obj[1];
            title=(String)obj[2];
            text=(String)obj[3];

            java.util.Properties properties = new java.util.Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.socketFactory.post", "465");
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            final javax.mail.Message msg = new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(account,password);
                }
            }));

            try {
                msg.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
                //自分自身にメールを送信
                msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(account + "@gmail.com"));
                msg.setSubject(title);
                msg.setText(text);

                javax.mail.Transport.send(msg);

            } catch (Exception e) {
                return (Object)e.toString();
            }

            return (Object)"送信が完了しました";

        }

        @Override
        protected void onPostExecute(Object obj) {
            //画面にメッセージを表示する
            Toast.makeText(ExportActivity.this,(String)obj,Toast.LENGTH_LONG).show();
        }
    }

 */

    public void showDialog(View view) {
        DialogFragment dialogFragment = new ExportDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "my_dialog");
    }

}


