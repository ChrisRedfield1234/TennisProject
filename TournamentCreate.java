package com.example.tennisproject;

import android.content.Context;
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

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Objects;

public class TournamentCreate extends AppCompatActivity {

    private DatabaseHelper helper;

    private String participants = "";

    private String block = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tournament_create);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        selectTournamentInfo();

        if(Objects.nonNull(participants) && Objects.nonNull(block)){
            MyView myView = new MyView(this);
            setContentView(myView);
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

            boolean flag = false;

            int divisor = 4;

            //座標保存リスト
            int[][] intList = new int[(int) par][2];

            // トーナメントの幅を計算（ここでは画面の幅を基準にします）
            //int stepWidth = (width - 2 * margin) / 7;
            int stepWidth = (int) ((width - 2 * margin) / (par - 1));

            int j = 0;

            // 1ラウンド目の線を描画
            for (int i = 0; i < par / 2; i++) {
                int x1 = margin + stepWidth * j;
                int x2 = margin + stepWidth * (j + 1);

                if (flag && i + 1 == (par / 2)) {

                    //次の試合の縦線
                    canvas.drawLine((x1 + x2) / 2, height - margin, (x1 + x2) / 2, height - margin - 300, paint);

                } else {
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
                }

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
        block = cursor.getString(1);



    }

}
