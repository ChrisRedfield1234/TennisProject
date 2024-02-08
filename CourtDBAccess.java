package com.example.tennisproject;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

public class CourtDBAccess extends AppCompatActivity {
    private DatabaseHelper helper;

    /*public StringBuilder SelectAll(){
        ControlDBAccess access = new ControlDBAccess();
        helper = access.DBConnect();
        StringBuilder builder = new StringBuilder();

        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select COURT_ID,COURT_NAME from COURT_TBL";


        try {
            Cursor cursor = db.rawQuery(sql, null);

            if (cursor.getCount() == 0) {

            }

            while (cursor.moveToNext()) {
                builder.append(cursor.getString(0) + " ");
                builder.append(cursor.getString(1) + "\n");

            }

        } finally {
            db.close();
        }


        return builder;
    }
     */
}
